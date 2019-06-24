package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class TreeViewContMenuEditHandler implements EventHandler<ActionEvent> {

    private final Stage stage;

    public TreeViewContMenuEditHandler(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void handle(ActionEvent event) {
        stage.show();
    }
}
