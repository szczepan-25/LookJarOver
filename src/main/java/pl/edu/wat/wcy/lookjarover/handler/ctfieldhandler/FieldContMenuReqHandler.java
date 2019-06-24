package pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import pl.edu.wat.wcy.lookjarover.listview.CtFieldListCell;

public class FieldContMenuReqHandler implements EventHandler<ContextMenuEvent> {

    @Override
    public void handle(ContextMenuEvent event) {

        ListView source = (ListView) event.getSource();
        CtFieldListCell selectedItem = (CtFieldListCell) source.getSelectionModel().getSelectedItem();
        ContextMenu sourceContextMenu = source.getContextMenu();
        MenuItem ctFieldAdd = sourceContextMenu.getItems().get(0);
        MenuItem ctFieldDelete = sourceContextMenu.getItems().get(1);

        if (selectedItem == null) {
            ctFieldAdd.setDisable(false);
            ctFieldDelete.setDisable(true);
        } else {
            ctFieldAdd.setDisable(false);
            ctFieldDelete.setDisable(false);
        }
    }
}
