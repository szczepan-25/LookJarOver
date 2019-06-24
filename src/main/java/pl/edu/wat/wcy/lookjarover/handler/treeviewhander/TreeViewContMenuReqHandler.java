package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

public class TreeViewContMenuReqHandler implements EventHandler<ContextMenuEvent> {

    @Override
    public void handle(ContextMenuEvent event) {

        TreeView source = (TreeView) event.getSource();
        TreeItem selectedTreeItem = (TreeItem) source.getSelectionModel().getSelectedItem();
        ContextMenu sourceContextMenu = source.getContextMenu();
        MenuItem treeViewAdd = sourceContextMenu.getItems().get(0);
        MenuItem treeViewEdit = sourceContextMenu.getItems().get(1);
        MenuItem treeViewDelete = sourceContextMenu.getItems().get(2);

        if (selectedTreeItem != null) {
            CustomFile selectedTreeItemValue = (CustomFile) selectedTreeItem.getValue();

            if (selectedTreeItemValue.getFile().isDirectory() && selectedTreeItemValue.isAdd()) {
                treeViewAdd.setDisable(false);
                treeViewEdit.setDisable(true);
                treeViewDelete.setDisable(false);
            } else if (selectedTreeItemValue.getFile().isDirectory() && !selectedTreeItemValue.isAdd()){
                treeViewAdd.setDisable(false);
                treeViewEdit.setDisable(true);
                treeViewDelete.setDisable(true);
            } else if (selectedTreeItemValue.getFile().isFile() && selectedTreeItemValue.isAdd()) {
                treeViewAdd.setDisable(true);
                if (selectedTreeItemValue.getFile().getName().endsWith(".class")) {
                    treeViewEdit.setDisable(false);
                } else {
                    treeViewEdit.setDisable(true);
                }
                treeViewDelete.setDisable(false);
            } else if (selectedTreeItemValue.getFile().isFile() && !selectedTreeItemValue.isAdd()) {
                treeViewAdd.setDisable(true);
                if (selectedTreeItemValue.getFile().getName().endsWith(".class")) {
                    treeViewEdit.setDisable(false);
                } else {
                    treeViewEdit.setDisable(true);
                }
                treeViewDelete.setDisable(true);
            }
        } else {
            sourceContextMenu.hide();
        }
    }
}
