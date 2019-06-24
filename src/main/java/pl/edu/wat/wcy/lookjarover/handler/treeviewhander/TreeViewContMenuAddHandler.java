package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class TreeViewContMenuAddHandler implements EventHandler<ActionEvent> {

    private final Stage addTreeViewStage;

    public TreeViewContMenuAddHandler(Stage stage) {
        this.addTreeViewStage = stage;
    }


    @Override
    public void handle(ActionEvent event) {
        addTreeViewStage.show();
    }
}
