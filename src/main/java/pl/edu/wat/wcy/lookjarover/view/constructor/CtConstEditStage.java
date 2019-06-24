package pl.edu.wat.wcy.lookjarover.view.constructor;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javassist.CtClass;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstEditStageCancelButtonHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstEditStageSaveButtonHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;

import java.io.File;

public class CtConstEditStage {

    private final Stage ctConstEditStage;

    private final TextArea textArea;
    private final RadioButton overwriteBodyButton;

    public CtConstEditStage(ListView<CtConstructorListCell> ctConstListView, CtClass ctClass, File changingClassFile) {

        VBox ctConstEditMainPane = new VBox();
        ctConstEditMainPane.setSpacing(15.0);

        this.textArea = new TextArea();
        ToggleGroup radioButtonGroup = new ToggleGroup();
        this.overwriteBodyButton = new RadioButton("Overwrite Body");
        overwriteBodyButton.setSelected(true);

        overwriteBodyButton.setToggleGroup(radioButtonGroup);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox radioButtonPane = new HBox();
        radioButtonPane.setSpacing(10.0);
        radioButtonPane.getChildren().addAll(overwriteBodyButton);

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(saveButton, cancelButton);

        ctConstEditMainPane.getChildren().addAll(radioButtonPane, textArea, buttonPane);

        Scene ctConstEditScene = new Scene(ctConstEditMainPane, 600, 600);
        this.ctConstEditStage = new Stage();
        ctConstEditStage.setTitle("Edit Constructor");
        ctConstEditStage.setScene(ctConstEditScene);

        ConstEditStageSaveButtonHandler saveButtonHandler = new ConstEditStageSaveButtonHandler(ctConstListView, ctClass, changingClassFile,
                textArea, overwriteBodyButton);
        saveButton.addEventHandler(ActionEvent.ACTION, saveButtonHandler);

        ConstEditStageCancelButtonHandler cancelButtonHandler = new ConstEditStageCancelButtonHandler(ctConstEditStage);
        cancelButton.addEventHandler(ActionEvent.ACTION, cancelButtonHandler);

        ctConstEditStage.setOnShowing((event) -> {
            overwriteBodyButton.setSelected(true);
            textArea.clear();
            textArea.requestFocus();
        });
    }


    public Stage getCtConstEditStage() {
        return ctConstEditStage;
    }
}
