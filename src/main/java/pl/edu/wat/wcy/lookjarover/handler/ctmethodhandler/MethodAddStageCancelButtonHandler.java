package pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class MethodAddStageCancelButtonHandler implements EventHandler<ActionEvent> {

    private final Stage stage;

    public MethodAddStageCancelButtonHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.close();
    }
}
