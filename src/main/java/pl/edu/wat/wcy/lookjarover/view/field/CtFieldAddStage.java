package pl.edu.wat.wcy.lookjarover.view.field;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javassist.CtClass;
import pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler.FieldAddStageAddButtonHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtFieldListCell;

import java.io.File;

public class CtFieldAddStage {


    private final Stage ctFieldAddStage;

    private final TextField textField;


    public CtFieldAddStage(ListView<CtFieldListCell> ctFieldListView, CtClass ctClass, File changingClassFile) {

        VBox ctFieldAddMainPane = new VBox();
        ctFieldAddMainPane.setSpacing(15.0);

        this.textField = new TextField();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.getChildren().addAll(addButton, cancelButton);

        ctFieldAddMainPane.getChildren().addAll(textField, buttonPane);

        Scene ctFieldAddScene = new Scene(ctFieldAddMainPane, 600, 600);

        this.ctFieldAddStage = new Stage();
        ctFieldAddStage.setTitle("Add Field");
        ctFieldAddStage.setScene(ctFieldAddScene);

        FieldAddStageAddButtonHandler addButtonHandler = new FieldAddStageAddButtonHandler(ctFieldListView, ctClass, changingClassFile, textField);
        addButton.addEventHandler(ActionEvent.ACTION, addButtonHandler);

        ctFieldAddStage.setOnShowing((event) -> {
            textField.clear();
            textField.requestFocus();
        });

    }

    public Stage getCtFieldAddStage() {
        return ctFieldAddStage;
    }
}
