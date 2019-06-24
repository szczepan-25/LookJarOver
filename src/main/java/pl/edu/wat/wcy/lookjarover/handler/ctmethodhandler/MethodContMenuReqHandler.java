package pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

public class MethodContMenuReqHandler implements EventHandler<ContextMenuEvent> {

    @Override
    public void handle(ContextMenuEvent event) {

        ListView source = (ListView) event.getSource();
        CtMethodListCell selectedItem = (CtMethodListCell) source.getSelectionModel().getSelectedItem();
        ContextMenu sourceContextMenu = source.getContextMenu();
        MenuItem ctMethodAdd = sourceContextMenu.getItems().get(0);
        MenuItem ctMethodDelete = sourceContextMenu.getItems().get(1);
        MenuItem ctMethodEdit = sourceContextMenu.getItems().get(2);

        if (selectedItem == null) {
            ctMethodAdd.setDisable(false);
            ctMethodDelete.setDisable(true);
            ctMethodEdit.setDisable(true);
        } else {
            ctMethodAdd.setDisable(false);
            ctMethodDelete.setDisable(false);
            ctMethodEdit.setDisable(false);
        }
    }
}
