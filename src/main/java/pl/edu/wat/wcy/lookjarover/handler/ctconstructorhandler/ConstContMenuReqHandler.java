package pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;

public class ConstContMenuReqHandler implements EventHandler<ContextMenuEvent> {

    @Override
    public void handle(ContextMenuEvent event) {

        ListView source = (ListView) event.getSource();
        CtConstructorListCell selectedItem = (CtConstructorListCell) source.getSelectionModel().getSelectedItem();
        ContextMenu sourceContextMenu = source.getContextMenu();
        MenuItem ctConstAdd = sourceContextMenu.getItems().get(0);
        MenuItem ctConstDelete = sourceContextMenu.getItems().get(1);
        MenuItem ctConstEdit = sourceContextMenu.getItems().get(2);

        if (selectedItem == null) {
            ctConstAdd.setDisable(false);
            ctConstDelete.setDisable(true);
            ctConstEdit.setDisable(true);
        } else {
            ctConstAdd.setDisable(false);
            ctConstDelete.setDisable(false);
            ctConstEdit.setDisable(false);
        }
    }
}
