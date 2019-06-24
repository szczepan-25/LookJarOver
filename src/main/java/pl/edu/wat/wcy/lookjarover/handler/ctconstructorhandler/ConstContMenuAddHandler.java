package pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ConstContMenuAddHandler implements EventHandler<ActionEvent> {

    private final Stage stage;

    public ConstContMenuAddHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        stage.show();
    }
}
