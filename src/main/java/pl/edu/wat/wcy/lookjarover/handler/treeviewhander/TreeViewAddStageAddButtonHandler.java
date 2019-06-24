package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeViewAddStageAddButtonHandler implements EventHandler<ActionEvent> {

    private final TreeItem<CustomFile> editingTreeItem;
    private final RadioButton packageButton;
    private final RadioButton classButton;
    private final RadioButton interfaceButton;
    private final TextField textField;
    private final ClassPool pool;
    private final File tempDir;

    public TreeViewAddStageAddButtonHandler(TreeItem<CustomFile> editingTreeItem, RadioButton packageButton, RadioButton classButton, RadioButton interfaceButton, TextField textField, ClassPool pool, File tempDir) {

        this.editingTreeItem = editingTreeItem;
        this.packageButton = packageButton;
        this.classButton = classButton;
        this.interfaceButton = interfaceButton;
        this.textField = textField;
        this.pool = pool;
        this.tempDir = tempDir;
    }



    @Override
    public void handle(ActionEvent event) {

        CustomFile editingCustomFile = editingTreeItem.getValue();
        File editingFile = editingCustomFile.getFile();

        if (!textField.getText().equals("")) {
            String textFieldText = textField.getText();
            String regex = "\\s*(^[a-zA-Z_][a-zA-Z0-9_]*)\\s*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(textFieldText);
            if (matcher.find()) {

                String newFileName = null;

                if (packageButton.isSelected()) {
                    newFileName = matcher.group(1);
                } else if (classButton.isSelected() || interfaceButton.isSelected()) {
                    String fileExtension = ".class";
                    newFileName = (matcher.group(1) + fileExtension);
                }

                if (newFileName != null) {
                    File newFile = new File(editingFile, newFileName);

                    if (!newFile.exists()) {
                        CtClass ctClass = null;
                        if (packageButton.isSelected()) {
                            newFile.mkdirs();
                        } else if (classButton.isSelected() || interfaceButton.isSelected()) {
                            String fileExtension = ".java";
                            newFileName = (matcher.group(1) + fileExtension);
                            newFile = new File(editingFile, newFileName);
                            try {
                                newFile.createNewFile();

                                if (newFile.exists()) {

                                    StringBuilder sourceSb = new StringBuilder();

                                    String packageName = null;
                                    if (!editingFile.getAbsolutePath().equals(tempDir.getAbsolutePath())) {
                                        packageName = editingFile.toPath().subpath(tempDir.toPath().getNameCount(), editingFile.toPath().getNameCount()).toString();
                                        packageName = packageName.replace("\\", ".");
                                    }

                                    if (packageName != null) {
                                        sourceSb.append("package ");
                                        sourceSb.append(packageName);
                                        sourceSb.append(";\n");
                                    }
                                    sourceSb.append("public class ");
                                    sourceSb.append(matcher.group(1));
                                    sourceSb.append("{\n");
                                    sourceSb.append("public ");
                                    sourceSb.append(matcher.group(1));
                                    sourceSb.append("(){}\n");
                                    sourceSb.append("}\n");


                                    try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
                                        byte[] strToBytes = sourceSb.toString().getBytes();
                                        outputStream.write(strToBytes);
                                    }

                                    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                                    compiler.run(null, null, null, newFile.getPath());

                                    newFile.delete();
                                    newFile = null;

                                    File classFile = new File(editingFile, matcher.group(1) + ".class");
                                    newFile = classFile;

                                    if (classFile.exists()) {
                                        InputStream ins = new FileInputStream(classFile);
                                        pool.makeClass(ins);
                                        ins.close();

                                        String ctClassName = matcher.group(1);
                                        ctClass = pool.getCtClass(packageName + "." + ctClassName);
                                    } else {
                                        System.out.println(matcher.group(1) + ".class file doesn't exist");
                                    }
                                } else {
                                    System.out.println("newFile doesn't exist");
                                }

                            } catch (IOException | NotFoundException e) {
                                LookJarOver.errorMessage(e);
//                                e.printStackTrace();
                            }
                        }

                        CustomFile customNewFile = new CustomFile(newFile, ctClass, true);
                        TreeItem<CustomFile> newFileTreeItem = new TreeItem<>(customNewFile);
                        editingTreeItem.getChildren().add(newFileTreeItem);

                        Button button = (Button) event.getSource();
                        Stage stage = (Stage) button.getScene().getWindow();
                        stage.close();

                    } else {
                        if (newFile.isDirectory()) {
                            if (!newFile.mkdirs()) {
                                String exceptionText = "A directory whit name '" +
                                        newFileName +
                                        "' already exists";
                                LookJarOver.errorMessage(new Exception(exceptionText));
                            }
                        } else if (newFile.isFile()) {
                            String sb = "Cannot create file " +
                                    newFileName +
                                    ". File already exists.";

                            LookJarOver.errorMessage(new Exception(sb));
                        }
                    }
                }
            } else {
                LookJarOver.errorMessage(new Exception("Not a valid Java qualified name"));
            }
        } else {
            LookJarOver.errorMessage(new Exception("Incorrect name"));
        }
    }
}