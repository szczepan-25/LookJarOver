package pl.edu.wat.wcy.lookjarover.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import pl.edu.wat.wcy.lookjarover.handler.BranchCollapsedHandler;
import pl.edu.wat.wcy.lookjarover.handler.BranchExpendedHandler;

import java.io.*;

public class FileTreeItem extends TreeItem<CustomFile> {


    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    private final FileTreeItem parentFileTreeItem;
    private final ClassPool classPool;

    public FileTreeItem(CustomFile file, FileTreeItem parentFileTreeItem, ClassPool classPool) {
        super(file);

        this.parentFileTreeItem = parentFileTreeItem;
        this.classPool = classPool;

        super.setExpanded(true);

        BranchCollapsedHandler branchCollapsedHandler = new BranchCollapsedHandler();
        this.addEventHandler(TreeItem.branchCollapsedEvent(), branchCollapsedHandler);

        BranchExpendedHandler branchExpendedHandler = new BranchExpendedHandler();
        this.addEventHandler(TreeItem.branchExpandedEvent(), branchExpendedHandler);

    }


    @Override
    public ObservableList<TreeItem<CustomFile>> getChildren() {

        if (this.isFirstTimeChildren) {

            this.isFirstTimeChildren = false;

            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {

        if (this.isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            File f = getValue().getFile();
            isLeaf = f.isFile();
        }

        return isLeaf;
    }

    private ObservableList<TreeItem<CustomFile>> buildChildren(FileTreeItem fileTreeItem) {

        File f = fileTreeItem.getValue().getFile();

        if (f != null && f.isDirectory()) {

            File[] files = f.listFiles();

            if (files != null) {
                ObservableList<TreeItem<CustomFile>> children = FXCollections.observableArrayList();

                for (File childFile : files) {
                    CtClass ctClass = null;
                    if (childFile != null && childFile.getAbsolutePath().endsWith(".class")) {
                        try {
                            InputStream ins = new FileInputStream(childFile);
                            classPool.makeClass(ins);
                            ins.close();

                            StringBuilder packageSb = new StringBuilder();
                            FileTreeItem parent = fileTreeItem;

                            while (parent != null) {
                                packageSb.append(parent.getValue().getFile().getName());
                                packageSb.append(".");

                                parent = parent.getParentFileTreeItem();
                            }
                            String[] reversePackage = packageSb.toString().split("\\.");
                            StringBuilder reversePackageSb = new StringBuilder();

                            for (int i = reversePackage.length - 2; i > -1; i--) {
                                reversePackageSb.append(reversePackage[i]);
                                reversePackageSb.append(".");
                            }

                            String ctClassName = childFile.getName().substring(0, childFile.getName().indexOf(".class"));
                            ctClass = classPool.getCtClass(reversePackageSb.toString() + ctClassName);
                        } catch (IOException | NotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    children.add(new FileTreeItem(new CustomFile(childFile, ctClass, false), fileTreeItem, classPool));
                }

                return children;
            }
        }

        return FXCollections.emptyObservableList();
    }

    private FileTreeItem getParentFileTreeItem() {
        return parentFileTreeItem;
    }
}
