package pl.edu.wat.wcy.lookjarover.view.treeview;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import pl.edu.wat.wcy.lookjarover.handler.treeviewhander.TreeViewEditStageAddButtonHandler;
import pl.edu.wat.wcy.lookjarover.handler.treeviewhander.TreeViewEditStageCancelButtonHandler;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

public class TreeViewEditStage {

    private final Stage treeViewEditStage;

    private final RadioButton addSuperclass;
    private final TextField textField;
    private Label hiddenTextFieldLabel;


    public TreeViewEditStage(TreeView<CustomFile> treeView) {

        VBox treeViewEditMainPane = new VBox();
        treeViewEditMainPane.setSpacing(15.0);

        this.addSuperclass = new RadioButton("add superclass");
        addSuperclass.setSelected(true);
        addSuperclass.setOnAction((event) -> hiddenTextFieldLabel.setText("extends"));

        RadioButton addInterface = new RadioButton("add interface");
        addInterface.setOnAction((event) -> hiddenTextFieldLabel.setText("implements"));

        ToggleGroup radioButtonToggleGroup = new ToggleGroup();
        radioButtonToggleGroup.getToggles().addAll(addSuperclass, addInterface);

        this.textField = new TextField();
        textField.setPromptText("Example");

        this.hiddenTextFieldLabel = new Label("extends");
        hiddenTextFieldLabel.setTextAlignment(TextAlignment.LEFT);


        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        HBox radioButtonPane = new HBox();
        radioButtonPane.setSpacing(10.0);
        radioButtonPane.getChildren().addAll(addSuperclass, addInterface);

        HBox textFieldPane = new HBox();
        textFieldPane.setSpacing(5.0);
        textFieldPane.getChildren().addAll(hiddenTextFieldLabel, textField);

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(addButton, cancelButton);



        treeViewEditMainPane.getChildren().addAll(radioButtonPane, textFieldPane, buttonPane);

        Scene treeViewEditScene = new Scene(treeViewEditMainPane, 600, 600);

        this.treeViewEditStage = new Stage();
        treeViewEditStage.setTitle("Add Superclass/Interface");
        treeViewEditStage.setScene(treeViewEditScene);


        TreeViewEditStageAddButtonHandler addButtonHandler = new TreeViewEditStageAddButtonHandler(treeView, textField, addSuperclass, addInterface);
        addButton.addEventHandler(ActionEvent.ACTION, addButtonHandler);

        TreeViewEditStageCancelButtonHandler cancelButtonHandler = new TreeViewEditStageCancelButtonHandler(treeViewEditStage);
        cancelButton.addEventHandler(ActionEvent.ACTION, cancelButtonHandler);

        treeViewEditStage.setOnShowing(((event) -> {
            addSuperclass.setSelected(true);
            textField.clear();
            textField.requestFocus();
        }));
    }

    public Stage getTreeViewEditStage() {
        return treeViewEditStage;
    }
}
