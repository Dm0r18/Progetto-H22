package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Factory;
import model.Session;
import model.booking.ticket.TicketMail;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * Classe Controller, relativa al Pattern MVC, che si occupa di gestire la logica dell'applicativo e le richieste del cliente.
 * Classe contenente l'interazione con JavaFX.
 *
 * @author GruppoNoSuchMethod
 */
public class SupportController implements Initializable {
    private final Session session = Factory.getInstance().getSession();

    @FXML private JFXComboBox<String> options;
    @FXML private TextArea text;

    /**
     * Metodo che si occupa di reperire le informazioni necessarie all'apertura di una richiesta di supporto.
     *
     * @param location URL della risorsa.
     * @param resources Risorse necessarie per l'apertura della richiesta di supporto.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        options.getItems().addAll("Accesso", "Rimborsi", "Tariffe", "Reclami",
                "Termini e Condizioni Generali di Trasporto", "Altro...");
    }

    /**
     * Metodo che si occupa di gestire le operazioni dell'interfaccia grafica di richiesta del supporto.
     */
    @FXML
    private void execute(){
        Stage stage = (Stage) Stage.getWindows().get(1);
        if(!options.getSelectionModel().isEmpty() && !text.getText().isEmpty()) {
            try {
                    TicketMail mail = new TicketMail();
                    mail.setSubject("Support Request: "
                            + options.getSelectionModel().getSelectedItem());
                    if(session.isLogged()) {
                            mail.setText(session.getUser().getEmail()
                                    + "\n" + new Date(System.currentTimeMillis())
                                    + "\n----------------------------------------\n"
                                    + text.getText());
                    } else {
                            mail.setText("Not Logged User - Assistenza da pagina di LOGIN\n"
                                    + new Date(System.currentTimeMillis())
                                    + "\n----------------------------------------\n"
                                    + text.getText());
                    }
                    mail.send("h22aerospin@gmail.com", null);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Support");
                    alert.setHeaderText("La richiesta è stata inoltrata con successo");
                    alert.setContentText(
                            "Verrà contattato dal nostro supporto al più presto possibile");
                    alert.showAndWait();
                    stage.close();
            } catch (RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("NetworkError");
                    alert.setHeaderText("La richiesta non è stata inoltrata, riprovare");
            }
        }
    }

    /**
     * Metodo che si occupa della chiusura dell'interfaccia grafica di supporto.
     */
    @FXML
    private void cancel(){
        Stage stage = (Stage) Stage.getWindows().get(1);
        stage.close();
    }
}