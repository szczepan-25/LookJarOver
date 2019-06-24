package pl.edu.wat.wcy.lookjarover.tree;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import pl.edu.wat.wcy.lookjarover.handler.SelectedFileClickedHandler;
import pl.edu.wat.wcy.lookjarover.handler.SelectedFileHandler;

public class FileCell extends TreeCell<CustomFile> {

    private static final Image folderCollapseImage;
    private static final Image folderExpendImage;
    private static final Image fileImage;


    static {
        folderCollapseImage = new Image("/image/Windows10/icons8-folder-32.png", 20, 20, true, true);
        folderExpendImage = new Image("/image/Windows10/icons8-opened-folder-32.png", 20, 20, true, true);
        fileImage = new Image("/image/Windows10/icons8-file-32.png", 20, 20, true, true);
    }

    public FileCell(SelectedFileHandler selectedFileHandler) {

        SelectedFileClickedHandler doubleClickHandler = new SelectedFileClickedHandler(selectedFileHandler);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, doubleClickHandler);

    }

    @Override
    protected void updateItem(CustomFile item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item.getFile().isDirectory()) {
                if (getTreeItem().isExpanded()) {
                    setGraphic(new ImageView(folderExpendImage));
                } else {
                    setGraphic(new ImageView(folderCollapseImage));
                }
            } else if (item.getFile().isFile()) {
                setGraphic(new ImageView(fileImage));
            } else {
                setText(null);
                setGraphic(null);
            }

            setText(item.getFile().getName());
        }
    }
}
