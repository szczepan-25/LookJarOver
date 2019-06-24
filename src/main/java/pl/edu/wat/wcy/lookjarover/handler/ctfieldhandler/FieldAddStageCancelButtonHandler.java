package pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class FieldAddStageCancelButtonHandler implements EventHandler<ActionEvent> {

    private Stage stage;

    public FieldAddStageCancelButtonHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.close();
    }
}
