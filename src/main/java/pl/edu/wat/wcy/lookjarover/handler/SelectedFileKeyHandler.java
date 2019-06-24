package pl.edu.wat.wcy.lookjarover.handler;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

import java.io.*;

public class SelectedFileKeyHandler implements EventHandler<KeyEvent> {

    private final SelectedFileHandler selectedFileHandler;

    private boolean firstShow;
    private final KeyCodeCombination kcc = new KeyCodeCombination(KeyCode.ENTER);

    public SelectedFileKeyHandler(SelectedFileHandler selectedFileHandler) {
        this.selectedFileHandler = selectedFileHandler;

        firstShow = true;
    }

    @Override
    public void handle(KeyEvent event) {

        if (kcc.match(event)) {

            TreeView source = (TreeView) event.getSource();
            TreeItem selectedTreeItem = (TreeItem) source.getSelectionModel().getSelectedItem();
            CustomFile selectedItemValue = (CustomFile) selectedTreeItem.getValue();
            File selectedItemValueFile = selectedItemValue.getFile();

            if (selectedItemValueFile.isDirectory()) {
                if (!firstShow) {
                    if (selectedTreeItem.isExpanded()) {
                        selectedTreeItem.setExpanded(false);
                    } else {
                        selectedTreeItem.setExpanded(true);
                    }
                } else {
                    firstShow = false;
                }
            } else {
                selectedFileHandler.handle(selectedItemValueFile);
            }
        }
    }
}
