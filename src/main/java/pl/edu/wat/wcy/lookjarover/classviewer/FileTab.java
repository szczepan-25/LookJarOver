package pl.edu.wat.wcy.lookjarover.classviewer;

import javafx.scene.control.Tab;

import java.io.File;

public class FileTab extends Tab {

    private File file;

    public FileTab(String tabName, File file) {
        super(tabName);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
