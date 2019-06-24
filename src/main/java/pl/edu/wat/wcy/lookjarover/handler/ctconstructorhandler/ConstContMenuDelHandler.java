package pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javassist.*;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConstContMenuDelHandler implements EventHandler<ActionEvent> {

    private final ListView<CtConstructorListCell> ctConstListView;
    private final CtClass ctClass;
    private final File changingClassFile;

    public ConstContMenuDelHandler(ListView<CtConstructorListCell> ctConstListView, CtClass ctClass, File changingClassFile) {

        this.ctConstListView = ctConstListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
    }


    @Override
    public void handle(ActionEvent event) {

        CtConstructorListCell ctConstructorListCell = ctConstListView.getSelectionModel().getSelectedItem();
        CtConstructor ctConstructor = ctConstructorListCell.getCtConstructor();


        FileOutputStream outputStream = null;
        try {
            ctClass.removeConstructor(ctConstructor);

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
        ctConstListView.getItems().remove(ctConstructorListCell);
    }
}
