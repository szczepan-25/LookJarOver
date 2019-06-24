package pl.edu.wat.wcy.lookjarover.handler;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javassist.*;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.classviewer.FileTab;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstContMenuAddHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstContMenuDelHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstContMenuEditHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstContMenuReqHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler.FieldContMenuAddHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler.FieldContMenuDelHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler.FieldContMenuReqHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodContMenuAddHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodContMenuDelHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodContMenuEditHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodContMenuReqHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;
import pl.edu.wat.wcy.lookjarover.listview.CtFieldListCell;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;
import pl.edu.wat.wcy.lookjarover.view.constructor.CtConstAddStage;
import pl.edu.wat.wcy.lookjarover.view.constructor.CtConstEditStage;
import pl.edu.wat.wcy.lookjarover.view.field.CtFieldAddStage;
import pl.edu.wat.wcy.lookjarover.view.method.CtMethodAddStage;
import pl.edu.wat.wcy.lookjarover.view.method.CtMethodEditStage;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;

public class SelectedFileHandler {

    private final File tempJarFile;
    private final ClassPool pool;
    private final TabPane mainTabPane;

    public SelectedFileHandler(File tempJarFile, ClassPool pool, TabPane tabPane) {
        this.tempJarFile = tempJarFile;
        this.pool = pool;
        this.mainTabPane = tabPane;
    }

    public void handle(File selectedItemValue) {

        if (selectedItemValue != null) {

            if (selectedItemValue.isFile() && selectedItemValue.getName().contains(".class")) {

                FileTab newTab = new FileTab(selectedItemValue.getName(), selectedItemValue);

                newTab.setOnClosed(event -> newTab.setFile(null));

                TabPane subTabPane = new TabPane();
                subTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                Tab methodTab = new Tab("Method");
                Tab fieldTab = new Tab("Filed");
                Tab constructorTab = new Tab("Constructor");

                ListView<CtMethodListCell> ctMethodListView = new ListView<>();
                ListView<CtFieldListCell> ctFieldListView = new ListView<>();
                ListView<CtConstructorListCell> ctConstructorListView = new ListView<>();

                ContextMenu ctMethodContextMenu = new ContextMenu();
                MenuItem ctMethodAdd = new MenuItem("Add");
                MenuItem ctMethodDelete = new MenuItem("Delete");
                MenuItem ctMethodEdit = new MenuItem("Edit");
                ctMethodContextMenu.getItems().addAll(ctMethodAdd, ctMethodDelete, ctMethodEdit);

                ContextMenu ctFieldContextMenu = new ContextMenu();
                MenuItem ctFieldAdd = new MenuItem("Add");
                MenuItem ctFieldDelete = new MenuItem("Delete");
                ctFieldContextMenu.getItems().addAll(ctFieldAdd, ctFieldDelete);

                ContextMenu ctConstContextMenu = new ContextMenu();
                MenuItem ctConstAdd = new MenuItem("Add");
                MenuItem ctConstDelete = new MenuItem("Delete");
                MenuItem ctConstEdit = new MenuItem("Edit");
                ctConstContextMenu.getItems().addAll(ctConstAdd, ctConstDelete, ctConstEdit);

                ctMethodListView.setContextMenu(ctMethodContextMenu);
                ctFieldListView.setContextMenu(ctFieldContextMenu);
                ctConstructorListView.setContextMenu(ctConstContextMenu);

                methodTab.setContent(ctMethodListView);
                fieldTab.setContent(ctFieldListView);
                constructorTab.setContent(ctConstructorListView);

                subTabPane.getTabs().add(methodTab);
                subTabPane.getTabs().add(fieldTab);
                subTabPane.getTabs().add(constructorTab);

                newTab.setContent(subTabPane);

                boolean alreadyExist = false;
                boolean theSameFile = false;
                Iterator<Tab> it = mainTabPane.getTabs().iterator();
                FileTab tab = null;

                while (!theSameFile && !alreadyExist && it.hasNext()) {
                    tab = (FileTab) it.next();

                    if (tab.getFile().getAbsolutePath().equals(newTab.getFile().getAbsolutePath())) {
                        theSameFile = true;
                    }

                    if (tab.getText().equals(newTab.getText())) {
                        alreadyExist = true;
                    }
                }

                if (!theSameFile) {
                    if (alreadyExist) {

                        this.renameTabs(tab, newTab);

                    } else {
                        mainTabPane.getTabs().add(newTab);
                        mainTabPane.getSelectionModel().select(newTab);
                    }
                } else {
                    mainTabPane.getSelectionModel().select(tab);
                }

                String filePath = selectedItemValue.getAbsolutePath();
                String className = filePath.substring(filePath.indexOf(tempJarFile.getName()) + (tempJarFile.getName().length() + 1), filePath.indexOf(".class"));
                className = className.replace("\\", ".");
                CtClass cc = null;
                try {
                    cc = pool.get(className);
                } catch (NotFoundException e) {
                    LookJarOver.errorMessage(e);
//                    e.printStackTrace();
                }

                if (cc != null) {

                    CtMethod[] methods = cc.getDeclaredMethods();
                    CtField[] fields = cc.getDeclaredFields();
                    CtConstructor[] constructors = cc.getConstructors();


                    for (CtMethod m : methods) {
                        ctMethodListView.getItems().add(new CtMethodListCell(m.getName(), m));
                    }

                    for (CtField f : fields) {
                        ctFieldListView.getItems().add(new CtFieldListCell(f.getName(), f));
                    }

                    for (CtConstructor c : constructors) {
                        ctConstructorListView.getItems().add(new CtConstructorListCell(c.getName(), c));
                    }


                    MethodContMenuReqHandler methodContMenuReqHandler = new MethodContMenuReqHandler();
                    ctMethodListView.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, methodContMenuReqHandler);

                    CtMethodAddStage ctMethodAddStage = new CtMethodAddStage(ctMethodListView, cc, selectedItemValue);
                    CtMethodEditStage ctMethodEditStage = new CtMethodEditStage(ctMethodListView, cc, selectedItemValue);
                    MethodContMenuAddHandler methodContMenuAddHandler = new MethodContMenuAddHandler(ctMethodAddStage.getCtMethodAddStage());
                    MethodContMenuDelHandler methodContMenuDelHandler = new MethodContMenuDelHandler(ctMethodListView, cc, selectedItemValue);
                    MethodContMenuEditHandler methodContMenuEditHandler = new MethodContMenuEditHandler(ctMethodEditStage.getCtMethodEditStage());

                    ctMethodAdd.addEventHandler(ActionEvent.ACTION, methodContMenuAddHandler);
                    ctMethodDelete.addEventHandler(ActionEvent.ACTION, methodContMenuDelHandler);
                    ctMethodEdit.addEventHandler(ActionEvent.ACTION, methodContMenuEditHandler);

                    FieldContMenuReqHandler fieldContMenuReqHandler = new FieldContMenuReqHandler();
                    ctFieldListView.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, fieldContMenuReqHandler);

                    CtFieldAddStage ctFieldAddStage = new CtFieldAddStage(ctFieldListView, cc, selectedItemValue);
                    FieldContMenuAddHandler fieldContMenuAddHandler = new FieldContMenuAddHandler(ctFieldAddStage.getCtFieldAddStage());
                    FieldContMenuDelHandler fieldContMenuDelHandler = new FieldContMenuDelHandler(ctFieldListView, cc, selectedItemValue);

                    ctFieldAdd.addEventHandler(ActionEvent.ACTION, fieldContMenuAddHandler);
                    ctFieldDelete.addEventHandler(ActionEvent.ACTION, fieldContMenuDelHandler);

                    ConstContMenuReqHandler constContMenuReqHandler = new ConstContMenuReqHandler();
                    ctConstructorListView.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, constContMenuReqHandler);

                    CtConstAddStage ctConstAddStage = new CtConstAddStage(ctConstructorListView, cc, selectedItemValue);
                    CtConstEditStage ctConstEditStage = new CtConstEditStage(ctConstructorListView, cc, selectedItemValue);
                    ConstContMenuAddHandler constContMenuAddHandler = new ConstContMenuAddHandler(ctConstAddStage.getCtConstAddStage());
                    ConstContMenuDelHandler constContMenuDelHandler = new ConstContMenuDelHandler(ctConstructorListView, cc, selectedItemValue);
                    ConstContMenuEditHandler constContMenuEditHandler = new ConstContMenuEditHandler(ctConstEditStage.getCtConstEditStage());

                    ctConstAdd.addEventHandler(ActionEvent.ACTION, constContMenuAddHandler);
                    ctConstDelete.addEventHandler(ActionEvent.ACTION, constContMenuDelHandler);
                    ctConstEdit.addEventHandler(ActionEvent.ACTION, constContMenuEditHandler);
                }
            }
        }
    }

    private void renameTabs(FileTab tab, FileTab newTab) {

        StringBuilder sbExistingTab = new StringBuilder();
        StringBuilder sbNewTab = new StringBuilder();

        Path fileExistingTabPath = tab.getFile().toPath();
        Path fileNewTabPath = newTab.getFile().toPath();

        int fileExistingTabPathNameCount = fileExistingTabPath.getNameCount();
        fileExistingTabPath = fileExistingTabPath.subpath(tempJarFile.toPath().getNameCount(), fileExistingTabPathNameCount);
        fileExistingTabPathNameCount = fileExistingTabPath.getNameCount();

        int fileNewTabPathNameCount = fileNewTabPath.getNameCount();
        fileNewTabPath = fileNewTabPath.subpath(tempJarFile.toPath().getNameCount(), fileNewTabPathNameCount);
        fileNewTabPathNameCount = fileNewTabPath.getNameCount();

        int count = 0;

        while (count < Math.min(fileNewTabPathNameCount, fileExistingTabPathNameCount) &&
                fileNewTabPath.getName((fileNewTabPathNameCount - 1) - count).equals(fileExistingTabPath.getName((fileExistingTabPathNameCount - 1) - count))) {
            count++;
        }


        if (count != fileExistingTabPathNameCount) {
            while (count > -1) {
                sbExistingTab.append(fileExistingTabPath.getName((fileExistingTabPathNameCount - 1) - count));
                sbNewTab.append(fileNewTabPath.getName((fileNewTabPathNameCount - 1) - count));
                count--;

                if (count > -1) {
                    sbExistingTab.append("\\");
                    sbNewTab.append("\\");
                }
            }

            tab.setText(sbExistingTab.toString());
            newTab.setText(sbNewTab.toString());

            mainTabPane.getTabs().add(newTab);
            mainTabPane.getSelectionModel().select(newTab);

        } else {
            mainTabPane.getSelectionModel().select(tab);
        }
    }
}
