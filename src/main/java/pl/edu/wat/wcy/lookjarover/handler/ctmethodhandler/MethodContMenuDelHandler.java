package pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MethodContMenuDelHandler implements EventHandler<ActionEvent> {

    private final ListView<CtMethodListCell> ctMethodListView;
    private final CtClass ctClass;
    private final File changingClassFile;

    public MethodContMenuDelHandler(ListView<CtMethodListCell> ctMethodListView, CtClass ctClass, File changingClassFile) {
        this.ctMethodListView = ctMethodListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
    }

    @Override
    public void handle(ActionEvent event) {

        CtMethodListCell ctMethodListCell = ctMethodListView.getSelectionModel().getSelectedItem();
        CtMethod ctMethod = ctMethodListCell.getCtMethod();


        FileOutputStream outputStream = null;
        try {
            ctClass.removeMethod(ctMethod);

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
        ctMethodListView.getItems().remove(ctMethodListCell);
    }
}
