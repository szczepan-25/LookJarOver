package pl.edu.wat.wcy.lookjarover;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javassist.ClassPool;
import org.apache.commons.io.FileUtils;
import pl.edu.wat.wcy.lookjarover.classviewer.FileTab;
import pl.edu.wat.wcy.lookjarover.handler.SelectedFileHandler;
import pl.edu.wat.wcy.lookjarover.handler.SelectedFileKeyHandler;
import pl.edu.wat.wcy.lookjarover.handler.treeviewhander.*;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;
import pl.edu.wat.wcy.lookjarover.tree.FileCell;
import pl.edu.wat.wcy.lookjarover.tree.FileTreeItem;
import pl.edu.wat.wcy.lookjarover.view.treeview.TreeViewEditStage;

import java.io.*;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class LookJarOver extends Application {

    private TreeView<CustomFile> treeView;
    private ContextMenu treeViewContextMenu;
    private TreeItem<CustomFile> editTreeItem;
    private File tempDirInstance;
    private ClassPool pool;
    private TabPane mainTabPane;
    private MenuBar menuBar;
    private File jar;
    private Manifest manifest;
    private File jarDestination;

    //addTreeViewStage
    private Stage addTreeViewStage;
    private Scene addTreeViewScene;
    private RadioButton packageButton;
    private RadioButton classButton;
    private RadioButton interfaceButton;
    private Label hiddenFileNameLabel;
    private TextField fileNameTextField;
    private Button addTreeViewAddButton;
    private Button addTreeViewCancelButton;




    @Override
    public void init() {

        pool = ClassPool.getDefault();
        ClassPool.doPruning = false;

        jar = null;

        treeView = new TreeView<>();
        treeView.setPrefWidth(400);
        treeViewContextMenu = new ContextMenu();
        MenuItem treeViewAdd = new MenuItem("Add");
        MenuItem treeViewEdit = new MenuItem("Edit");
        MenuItem treeViewDelete = new MenuItem("Delete");
        treeViewContextMenu.getItems().addAll(treeViewAdd, treeViewEdit, treeViewDelete);
        treeView.setContextMenu(treeViewContextMenu);

        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        mainTabPane.setPrefWidth(600);
        mainTabPane.setFocusTraversable(false);

        //addTreeViewStage
        Label fileTypeLabel = new Label("Type: ");

        ToggleGroup radioButtonGroup = new ToggleGroup();

        packageButton = new RadioButton("package");
        packageButton.setSelected(true);
        packageButton.setOnAction((event) -> hiddenFileNameLabel.setText("package"));

        classButton = new RadioButton("class");
        classButton.setOnAction(((event) -> hiddenFileNameLabel.setText("public class")));

        interfaceButton = new RadioButton("interface");
        interfaceButton.setOnAction(((event) -> hiddenFileNameLabel.setText("public interface")));

        packageButton.setToggleGroup(radioButtonGroup);
        classButton.setToggleGroup(radioButtonGroup);
        interfaceButton.setToggleGroup(radioButtonGroup);

        Label fileNameLabel = new Label("Name: ");

        hiddenFileNameLabel = new Label();
        hiddenFileNameLabel.setTextAlignment(TextAlignment.RIGHT);

        fileNameTextField = new TextField();
        fileNameTextField.setPromptText("Example");

        addTreeViewAddButton = new Button("Add");
        addTreeViewCancelButton = new Button("Cancel");

        HBox buttonRow = new HBox();
        buttonRow.setSpacing(10.0);
        buttonRow.setAlignment(Pos.BOTTOM_CENTER);
        buttonRow.getChildren().addAll(addTreeViewAddButton, addTreeViewCancelButton);

        HBox nameRow = new HBox();
        nameRow.setSpacing(10.0);
        nameRow.getChildren().addAll(fileNameLabel, hiddenFileNameLabel, fileNameTextField);

        HBox typeRow = new HBox();
        typeRow.setSpacing(10.0);
        typeRow.getChildren().addAll(fileTypeLabel, packageButton, classButton, interfaceButton);

        VBox addTreeViewMainPane = new VBox();
        addTreeViewMainPane.setSpacing(15.0);
        addTreeViewMainPane.getChildren().addAll(typeRow, nameRow, buttonRow);

        addTreeViewScene = new Scene(addTreeViewMainPane, 600, 600);
    }

    @Override
    public void start(Stage primaryStage) {

        mainTabPane.getTabs().addListener((ListChangeListener<Tab>) c -> {
            if (c.getList().isEmpty()) {
                treeView.requestFocus();
                mainTabPane.setFocusTraversable(false);
            } else if (c.getList().size() == 1){
                mainTabPane.setFocusTraversable(true);
            }
        });
        mainTabPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            private final KeyCodeCombination kcc = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);

            @Override
            public void handle(KeyEvent event) {

                if (kcc.match(event)) {
                    Tab selectedTab = mainTabPane.getSelectionModel().getSelectedItem();
                    mainTabPane.getTabs().remove(selectedTab);
                }
            }
        });

        treeView.setOnContextMenuRequested(new TreeViewContMenuReqHandler());

        addTreeViewStage = new Stage();
        addTreeViewStage.setTitle("Add package/class/interface");
        addTreeViewStage.setScene(addTreeViewScene);
        addTreeViewStage.setOnShowing(event -> {
            fileNameTextField.clear();
            packageButton.setSelected(true);
            fileNameTextField.requestFocus();

            editTreeItem = treeView.getSelectionModel().getSelectedItem();
            addTreeViewAddButton.setOnAction(new TreeViewAddStageAddButtonHandler(editTreeItem, packageButton, classButton, interfaceButton, fileNameTextField, pool, tempDirInstance));

            TreeViewAddStageCancelButtonHandler cancelButtonHandler = new TreeViewAddStageCancelButtonHandler(addTreeViewStage);
            addTreeViewCancelButton.setOnAction(cancelButtonHandler);
        });
        treeViewContextMenu.getItems().get(0).setOnAction(new TreeViewContMenuAddHandler(addTreeViewStage));
        TreeViewEditStage treeViewEditStage = new TreeViewEditStage(treeView);
        treeViewContextMenu.getItems().get(1).setOnAction(new TreeViewContMenuEditHandler(treeViewEditStage.getTreeViewEditStage()));
        treeViewContextMenu.getItems().get(2).setOnAction(new TreeViewContMenuDelHandler(treeView));


        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Jar file", "*.jar")
        );

        HBox hBox = new HBox();
        VBox mainPane = new VBox();
        hBox.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
        hBox.getChildren().addAll(treeView, mainTabPane);

        menuBar = new MenuBar();
        Menu fileBar = new Menu("_File");

        MenuItem openFileItem = new MenuItem("Open");
        openFileItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openFileItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                File toRemove = tempDirInstance;
                jar = fileChooser.showOpenDialog(primaryStage);

                if (jar != null) {
                    try {
                        if (toRemove != null) {
                            FileUtils.deleteDirectory(toRemove);
                        }
                    } catch (IOException e) {
                        LookJarOver.errorMessage(e);
//                        e.printStackTrace();
                    }

                    try {
                        JarFile jarFile = new JarFile(jar);
                        manifest = jarFile.getManifest();
                    } catch (IOException e) {
                        LookJarOver.errorMessage(e);
//                        e.printStackTrace();
                    }

                    String property = "java.io.tmpdir";
                    String tempDir = System.getProperty(property);

                    String tempDirName = jar.getName().substring(0, jar.getName().indexOf(".jar"));
                    tempDirInstance = new File(new File(tempDir), tempDirName);

                    if (!tempDirInstance.mkdirs()) {
                        try {
                            FileUtils.deleteDirectory(tempDirInstance);
                            tempDirInstance.mkdirs();
                        } catch (IOException e) {
                            LookJarOver.errorMessage(e);
//                            e.printStackTrace();
                        }
                    }


                    String pathToJarExe = getClass().getResource("/externalProgram/jar.exe").getPath();
                    ProcessBuilder pb = new ProcessBuilder(pathToJarExe, "xf", jar.getAbsolutePath());
                    pb.redirectErrorStream(true);
                    pb.directory(tempDirInstance);

                    Process pr;
                    try {
                        pr = pb.start();
                        pr.waitFor();
                    } catch (IOException | InterruptedException e) {
                        LookJarOver.errorMessage(e);
//                        e.printStackTrace();
                    }

                    mainTabPane.getTabs().clear();

                    FileTreeItem root = new FileTreeItem(new CustomFile(tempDirInstance, null, false), null, pool);
                    treeView.setRoot(root);
                    treeView.getRoot().setExpanded(false);
                    treeView.getSelectionModel().select(treeView.getRoot());

                    SelectedFileHandler selectedFileHandler = new SelectedFileHandler(tempDirInstance, pool, mainTabPane);

                    SelectedFileKeyHandler releasedHandler = new SelectedFileKeyHandler(selectedFileHandler);
                    treeView.setOnKeyReleased(releasedHandler);

                    treeView.setCellFactory(param -> new FileCell(selectedFileHandler));
                }
            }
        });

        MenuItem saveFileItem = new MenuItem("Save");
        saveFileItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveFileItem.setOnAction(event -> {
            jarDestination = fileChooser.showSaveDialog(primaryStage);

            if (jarDestination != null) {
                JarOutputStream target;
                try {
                    if (jar != null) {
                        target = new JarOutputStream(new FileOutputStream(jarDestination), manifest);
                        CreateJar createJar = new CreateJar();
                        createJar.createJar(tempDirInstance, target, tempDirInstance);
                        target.close();
                    }
                } catch (IOException e) {
                    LookJarOver.errorMessage(e);
//                        e.printStackTrace();
                }
            }
        });

        MenuItem quitAppItem = new MenuItem("Quit");
        quitAppItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        quitAppItem.setOnAction(event -> Platform.exit());

        fileBar.getItems().addAll(openFileItem, saveFileItem, quitAppItem);
        menuBar.getMenus().addAll(fileBar);

        mainPane.getChildren().addAll(menuBar, hBox);
        Scene scene = new Scene(mainPane, 800, 600);

        primaryStage.setResizable(false);
        primaryStage.setTitle("LookJarOver");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {

        pool = null;
        treeView = null;

        ObservableList<Tab> tabs = mainTabPane.getTabs();
        for (Tab t : tabs) {
            ((FileTab) t).setFile(null);
        }
        mainTabPane = null;
        menuBar = null;
        if (tempDirInstance != null)
            FileUtils.deleteDirectory(tempDirInstance);
    }

    public static void main(String... args) {
        Application.launch(args);
    }

    public static void errorMessage(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Exception Dialog");
        alert.setHeaderText(null);
        alert.setContentText(exception.getMessage());

        Label label = new Label("The exception stacktrace was: ");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
