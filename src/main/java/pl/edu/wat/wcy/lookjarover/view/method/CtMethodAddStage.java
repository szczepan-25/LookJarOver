package pl.edu.wat.wcy.lookjarover.view.method;

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
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodAddStageAddButtonHandler;
import pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler.MethodAddStageCancelButtonHandler;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

import java.io.File;

public class CtMethodAddStage {

    private final Stage ctMethodAddStage;

    private final TextArea textArea;

    public CtMethodAddStage(ListView<CtMethodListCell> ctMethodListView, CtClass ctClass, File changingClassFile) {

        VBox ctMethodAddMainPane = new VBox();
        ctMethodAddMainPane.setSpacing(15.0);

        this.textArea = new TextArea();

        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5.0);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(addButton, cancelButton);

        ctMethodAddMainPane.getChildren().addAll(textArea, buttonPane);

        Scene ctMethodAddScene = new Scene(ctMethodAddMainPane, 600, 600);

        this.ctMethodAddStage = new Stage();
        ctMethodAddStage.setTitle("Add method");
        ctMethodAddStage.setScene(ctMethodAddScene);

        MethodAddStageAddButtonHandler addButtonHandler = new MethodAddStageAddButtonHandler(ctMethodListView, ctClass, changingClassFile, textArea);
        addButton.addEventHandler(ActionEvent.ACTION, addButtonHandler);

        MethodAddStageCancelButtonHandler cancelButtonHandler = new MethodAddStageCancelButtonHandler(ctMethodAddStage);
        cancelButton.addEventHandler(ActionEvent.ACTION, cancelButtonHandler);


        ctMethodAddStage.setOnShowing((event) -> {
            textArea.clear();
            textArea.requestFocus();
        });
    }

    public Stage getCtMethodAddStage() {
        return ctMethodAddStage;
    }
}
