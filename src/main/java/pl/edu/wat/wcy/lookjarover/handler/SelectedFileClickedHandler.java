package pl.edu.wat.wcy.lookjarover.handler;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import pl.edu.wat.wcy.lookjarover.tree.CustomFile;

import java.io.*;

public class SelectedFileClickedHandler implements EventHandler<MouseEvent> {


    private final SelectedFileHandler selectedFileHandler;

    public SelectedFileClickedHandler(SelectedFileHandler selectedFileHandler) {
        this.selectedFileHandler = selectedFileHandler;
    }

    @Override
    public void handle(MouseEvent event) {

        if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {

            TreeCell source = (TreeCell) event.getSource();
            TreeItem selectedTreeItem = source.getTreeItem();
            CustomFile selectedItemValue;
            if (selectedTreeItem != null) {

                selectedItemValue = (CustomFile) selectedTreeItem.getValue();
                File selectedItemValueFile = selectedItemValue.getFile();
                selectedFileHandler.handle(selectedItemValueFile);
            }
        }
    }
}
