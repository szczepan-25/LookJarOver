package pl.edu.wat.wcy.lookjarover.view.method;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javassist.CtClass;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodEditStageCancelButtonHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodEditStageSaveButtonHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

import java.io.File;

public class CtMethodEditStage {

    private final Stage ctMethodEditStage;

    private final TextArea editingTextArea;
    private final RadioButton insertBeforeButton;


    public CtMethodEditStage(ListView<CtMethodListCell> ctMethodListView, CtClass ctClass, File changingClassFile) {

        VBox ctMethodEditMainPane = new VBox();
        ctMethodEditMainPane.setSpacing(15.0);

        this.editingTextArea = new TextArea();
        ToggleGroup radioButtonGroup = new ToggleGroup();
        this.insertBeforeButton = new RadioButton("Insert Before");
        insertBeforeButton.setSelected(true);
        RadioButton insertAfterButton = new RadioButton("Insert After");
        RadioButton overwriteBodyButton = new RadioButton("Overwrite Body");

        insertBeforeButton.setToggleGroup(radioButtonGroup);
        insertAfterButton.setToggleGroup(radioButtonGroup);
        overwriteBodyButton.setToggleGroup(radioButtonGroup);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox radioButtonPane = new HBox();
        radioButtonPane.setSpacing(10.0);
        radioButtonPane.getChildren().addAll(insertBeforeButton, insertAfterButton, overwriteBodyButton);

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(saveButton, cancelButton);

        ctMethodEditMainPane.getChildren().addAll(radioButtonPane, editingTextArea, buttonPane);

        Scene ctMethodEditScene = new Scene(ctMethodEditMainPane, 600, 600);
        this.ctMethodEditStage = new Stage();
        ctMethodEditStage.setTitle("Edit Method");
        ctMethodEditStage.setScene(ctMethodEditScene);

        MethodEditStageSaveButtonHandler saveButtonHandler = new MethodEditStageSaveButtonHandler(ctMethodListView, ctClass, changingClassFile,
                editingTextArea, insertBeforeButton, insertAfterButton, overwriteBodyButton);
        saveButton.addEventHandler(ActionEvent.ACTION, saveButtonHandler);

        MethodEditStageCancelButtonHandler cancelButtonHandler = new MethodEditStageCancelButtonHandler(ctMethodEditStage);
        cancelButton.addEventHandler(ActionEvent.ACTION, cancelButtonHandler);

        ctMethodEditStage.setOnShowing((event) -> {
            insertBeforeButton.setSelected(true);
            editingTextArea.clear();
            editingTextArea.requestFocus();
        });
    }

    public Stage getCtMethodEditStage() {
        return ctMethodEditStage;
    }

}
