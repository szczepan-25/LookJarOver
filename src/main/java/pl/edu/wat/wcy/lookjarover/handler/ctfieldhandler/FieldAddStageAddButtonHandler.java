package pl.edu.wat.wcy.lookjarover.handler.ctfieldhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtFieldListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FieldAddStageAddButtonHandler implements EventHandler<ActionEvent> {

    private final ListView<CtFieldListCell> ctFieldListView;
    private final CtClass ctClass;
    private final File changingClassFile;

    private final TextField textField;

    public FieldAddStageAddButtonHandler(ListView<CtFieldListCell> ctFieldListView, CtClass ctClass, File changingClassFile, TextField textField) {
        this.ctFieldListView = ctFieldListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
        this.textField = textField;
    }

    @Override
    public void handle(ActionEvent event) {

        try {
            CtField newCtField = CtField.make(textField.getText(), ctClass);
            ctClass.addField(newCtField);

            ctFieldListView.getItems().add(new CtFieldListCell(newCtField.getName(), newCtField));
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
