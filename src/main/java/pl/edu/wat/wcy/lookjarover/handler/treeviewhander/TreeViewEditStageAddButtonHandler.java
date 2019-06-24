package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeViewEditStageAddButtonHandler implements EventHandler<ActionEvent> {

    private final RadioButton addSuperclassButton;
    private final RadioButton addInterfaceButton;

    private final TextField textField;

    private final TreeView<CustomFile> treeView;

    public TreeViewEditStageAddButtonHandler(TreeView<CustomFile> treeView, TextField textField, RadioButton addSuperclassButton, RadioButton addInterfaceButton) {

        this.addSuperclassButton = addSuperclassButton;
        this.addInterfaceButton = addInterfaceButton;
        this.textField = textField;

        this.treeView = treeView;
    }


    @Override
    public void handle(ActionEvent event) {

        TreeItem<CustomFile> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
        CustomFile customFile = selectedTreeItem.getValue();

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(customFile.getFile());

            CtClass ctClass = customFile.getCtClass();
            ClassPool classPool = ctClass.getClassPool();

            if (addSuperclassButton.isSelected()) {
                CtClass ctSuperclass = classPool.getCtClass(textField.getText());
                ctClass.setSuperclass(ctSuperclass);
            } else if (addInterfaceButton.isSelected()) {
                List<CtClass> ctInterfaces = new ArrayList<>();
                String[] interfaceName = textField.getText().split("\\s*,\\s*");

                for (String s : interfaceName) {
                    s = s.replace(" ", "");
                    CtClass ctInterface = classPool.getCtClass(s);
                    ctInterfaces.add(ctInterface);
                }

                CtClass[] ctClasses = new CtClass[ctInterfaces.size()];

                for (int i = 0; i < ctInterfaces.size(); i++) {
                    ctClasses[i] = ctInterfaces.get(i);
                }

                ctClass.setInterfaces(ctClasses);
            }


            byte[] ctClassBytecode = ctClass.toBytecode();
            outputStream.write(ctClassBytecode);
            ctClass.defrost();

            Button button = (Button) event.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();

        } catch (CannotCompileException | NotFoundException | IOException e) {
            LookJarOver.errorMessage(e);
//            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LookJarOver.errorMessage(e);
//                    e.printStackTrace();
                }
            }
        }
    }
}
