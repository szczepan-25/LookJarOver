package pl.edu.wat.wcy.lookjarover.handler;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;


import java.io.File;

public class BranchExpendedHandler implements EventHandler<TreeItem.TreeModificationEvent<CustomFile>> {

    public BranchExpendedHandler() {
    }

    @Override
    public void handle(TreeItem.TreeModificationEvent<CustomFile> event) {

        TreeItem source = event.getTreeItem();

        if (event.wasExpanded()) {

            for (Object o : source.getChildren()) {
                TreeItem child = (TreeItem) o;
                CustomFile childValue = (CustomFile) child.getValue();
                File childValueFile = childValue.getFile();

                if (childValueFile.isDirectory() && child.isExpanded()) {
                    child.setExpanded(false);
                }
            }
        }
    }
}
