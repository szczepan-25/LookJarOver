package pl.edu.wat.wcy.lookjarover.handler.ctmethodhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtMethodListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MethodEditStageSaveButtonHandler implements EventHandler<ActionEvent> {

    private final RadioButton insertBeforeButton;
    private final RadioButton insertAfterButton;
    private final RadioButton overwriteBodyButton;

    private final TextArea textArea;

    private final ListView<CtMethodListCell> ctMethodListView;
    private final CtClass ctClass;
    private final File changingClassFile;


    public MethodEditStageSaveButtonHandler(ListView<CtMethodListCell> ctMethodListView, CtClass ctClass, File changingClassFile,
                                            TextArea textArea, RadioButton insertBeforeButton, RadioButton insertAfterButton,
                                            RadioButton overwriteBodyButton) {

        this.ctMethodListView = ctMethodListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
        this.textArea = textArea;
        this.insertBeforeButton = insertBeforeButton;
        this.insertAfterButton = insertAfterButton;
        this.overwriteBodyButton = overwriteBodyButton;

    }
    @Override
    public void handle(ActionEvent event) {

        CtMethodListCell ctMethodListCell = ctMethodListView.getSelectionModel().getSelectedItem();
        CtMethod ctMethod = ctMethodListCell.getCtMethod();


        FileOutputStream outputStream = null;
        try {

            if (insertBeforeButton.isSelected()) {
                ctMethod.insertBefore(textArea.getText());
            } else if (insertAfterButton.isSelected()) {
                ctMethod.insertAfter(textArea.getText());
            } else if (overwriteBodyButton.isSelected()) {
                ctMethod.setBody(textArea.getText());
            }


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
