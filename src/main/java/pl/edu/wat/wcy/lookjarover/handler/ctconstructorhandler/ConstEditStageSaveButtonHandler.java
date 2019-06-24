package pl.edu.wat.wcy.lookjarover.handler.ctconstructorhandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import pl.edu.wat.wcy.lookjarover.LookJarOver;
import pl.edu.wat.wcy.lookjarover.listview.CtConstructorListCell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConstEditStageSaveButtonHandler implements EventHandler<ActionEvent> {

    private final RadioButton overwriteBodyButton;

    private final TextArea textArea;

    private final ListView<CtConstructorListCell> ctConstListView;
    private final CtClass ctClass;
    private final File changingClassFile;

    public ConstEditStageSaveButtonHandler(ListView<CtConstructorListCell> ctConstListView, CtClass ctClass, File changingClassFile,
                                           TextArea textArea, RadioButton overwriteBodyButton) {

        this.ctConstListView = ctConstListView;
        this.ctClass = ctClass;
        this.changingClassFile = changingClassFile;
        this.textArea = textArea;
        this.overwriteBodyButton = overwriteBodyButton;
    }

    @Override

    public void handle(ActionEvent event) {

        CtConstructorListCell ctConstListCell = ctConstListView.getSelectionModel().getSelectedItem();
        CtConstructor ctConst = ctConstListCell.getCtConstructor();

        FileOutputStream outputStream = null;
        try {

            if (overwriteBodyButton.isSelected()) {
                ctConst.setBody(textArea.getText());
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
