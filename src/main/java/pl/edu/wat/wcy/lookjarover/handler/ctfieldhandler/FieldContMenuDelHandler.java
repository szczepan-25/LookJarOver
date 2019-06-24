package pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtFieldListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FieldContMenuDelHandler implements EventHandler<ActionEvent> {

    private final ListView<CtFieldListCell> ctFieldListView;
    private final CtClass ctClass;
    private final File changingClassFile;


    public FieldContMenuDelHandler(ListView<CtFieldListCell> ctFieldListView, CtClass ctClass, File changingClassFile) {
        this.ctFieldListView = ctFieldListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
    }

    @Override
    public void handle(ActionEvent event) {

        CtFieldListCell ctFieldListCell = ctFieldListView.getSelectionModel().getSelectedItem();
        CtField ctField = ctFieldListCell.getCtField();


        FileOutputStream outputStream = null;
        try {
            ctClass.removeField(ctField);

            outputStream = new FileOutputStream(changingClassFile);
            byte[] strToBytes = ctClass.toBytecode();
            outputStream.write(strToBytes);
            ctClass.defrost();

        } catch (CannotCompileException | IOException | NotFoundException e) {
            LookJarOver.errorMessage(e);
//            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LookJarOver.errorMessage(e);
//                    e.printStackTrace();
                }
            }
        }
        ctFieldListView.getItems().remove(ctFieldListCell);
    }
}
