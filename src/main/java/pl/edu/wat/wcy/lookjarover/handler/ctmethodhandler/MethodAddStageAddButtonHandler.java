package pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MethodAddStageAddButtonHandler implements EventHandler<ActionEvent> {

    private final TextArea textArea;

    private final ListView<CtMethodListCell> ctMethodListView;
    private final CtClass ctClass;
    private final File changingClassFile;

    public MethodAddStageAddButtonHandler(ListView<CtMethodListCell> ctMethodListView, CtClass ctClass, File changingClassFile,
                                          TextArea textArea) {
        this.ctMethodListView = ctMethodListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;

        this.textArea = textArea;
    }

    @Override
    public void handle(ActionEvent event) {

        try {
            CtMethod newCtMethod = CtNewMethod.make(textArea.getText(), ctClass);
            ctClass.addMethod(newCtMethod);

            ctMethodListView.getItems().add(new CtMethodListCell(newCtMethod.getName(), newCtMethod));
        } catch (CannotCompileException e) {
            LookJarOver.errorMessage(e);
//            e.printStackTrace();
        }

        FileOutputStream outputStream = null;
        try {

            outputStream = new FileOutputStream(changingClassFile);
            byte[] strToBytes = ctClass.toBytecode();
            outputStream.write(strToBytes);
            ctClass.defrost();

            Button button = (Button) event.getSource();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();

        } catch (CannotCompileException | IOException e) {
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
    }
}
