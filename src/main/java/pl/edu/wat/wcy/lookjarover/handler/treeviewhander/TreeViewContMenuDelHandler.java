package pl.edu.wat.wcy.lookjarover.handler.treeviewhander;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.apache.commons.io.FileUtils;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;


import java.io.File;
import java.io.IOException;

public class TreeViewContMenuDelHandler implements EventHandler<ActionEvent> {

    private final TreeView<CustomFile> treeView;


    public TreeViewContMenuDelHandler(TreeView<CustomFile> treeView) {
        this.treeView = treeView;
    }

    @Override
    public void handle(ActionEvent event) {

        TreeItem<CustomFile> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
        CustomFile selectedTreeItemValue = selectedTreeItem.getValue();
        File valueFile = selectedTreeItemValue.getFile();

        if (valueFile != null) {
            try {
                if (valueFile.isDirectory()) {
                    FileUtils.deleteDirectory(valueFile);
                } else if (valueFile.isFile()) {
                    FileUtils.forceDelete(valueFile);
                }
            } catch (IOException e) {
                LookJarOver.errorMessage(e);
                e.printStackTrace();
            }
        }

        selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
    }
}
