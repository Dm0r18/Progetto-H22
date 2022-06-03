package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SupportController implements Initializable {

    @FXML
    JFXComboBox<String> options;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        options.getItems().addAll("Rimborsi", "Tariffe", "Reclami", "Termini e Condizioni Generali di Trasporto", "Altro...");

    }

    @FXML
    private void send(){
        if(!options.getSelectionModel().isEmpty()){
            //send mail
        }
    }

    @FXML
    private void cancel(){
        Stage stage = (Stage) Stage.getWindows().get(1);
        stage.close();
    }
}
