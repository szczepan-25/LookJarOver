package pl.edu.wat.wcy.lookjarover.view.constructor;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javassist.CtClass;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstAddStageAddButtonHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler.ConstAddStageCancelButtonHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;

import java.io.File;

public class CtConstAddStage {

    private final Stage ctConstAddStage;

    private final TextArea textArea;

    public CtConstAddStage(ListView<CtConstructorListCell> ctConstListView, CtClass ctClass, File changingClassFile) {

        VBox ctConstAddMainPane = new VBox();
        ctConstAddMainPane.setSpacing(15.0);

        this.textArea = new TextArea();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(addButton, cancelButton);

        ctConstAddMainPane.getChildren().addAll(textArea, buttonPane);

        Scene ctConstAddScene = new Scene(ctConstAddMainPane, 600, 600);

        this.ctConstAddStage = new Stage();
        ctConstAddStage.setTitle("Add Constructor");
        ctConstAddStage.setScene(ctConstAddScene);


        ConstAddStageAddButtonHandler addButtonHandler = new ConstAddStageAddButtonHandler(ctConstListView, ctClass, changingClassFile, textArea);
        addButton.addEventHandler(ActionEvent.ACTION, addButtonHandler);

        ConstAddStageCancelButtonHandler cancelButtonHandler = new ConstAddStageCancelButtonHandler(ctConstAddStage);
        cancelButton.addEventHandler(ActionEvent.ACTION, cancelButtonHandler);

        ctConstAddStage.setOnShowing((event) -> {
            textArea.clear();
            textArea.requestFocus();
        });
    }


    public Stage getCtConstAddStage() {
        return ctConstAddStage;
    }
}
