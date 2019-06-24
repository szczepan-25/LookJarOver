package pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class FieldContMenuAddHandler implements EventHandler<ActionEvent> {

    private final Stage stage;

    public FieldContMenuAddHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.show();
    }
}
