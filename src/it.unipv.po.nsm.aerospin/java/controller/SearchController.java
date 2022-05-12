package controller;

import com.jfoenix.controls.JFXToggleButton;
import controller.util.IControlledScreen;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.Factory;
import model.Session;
import controller.util.manager.SearchManager;
import org.controlsfx.control.SearchableComboBox;
import view.ScreenContainer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SearchController implements Initializable, IControlledScreen {

    ScreenContainer myContainer;
    Session session = Factory.getInstance().getSession();
    SearchManager methods = new SearchManager();

    @FXML private SearchableComboBox<String> scbDep;
    @FXML private SearchableComboBox<String> scbRet;

    @FXML private DatePicker date1;
    @FXML private DatePicker date2;

    @FXML private JFXToggleButton select;

    @FXML private Label errLabel;

    private SimpleBooleanProperty error = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Se seleziono Solo Andata non posso inserire Data Ritorno
        date1.setDayCellFactory(methods.bookingRange(LocalDate.now()));
        date2.disableProperty().bind(select.selectedProperty().not().or(date1.valueProperty().isNull()));

        scbDep.setItems(methods.getDepartures());
        errLabel.visibleProperty().bind(error);

        select.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (select.isSelected()){
                select.setText("Andata e Ritorno");
            } else {
                select.setText("Solo Andata");
                date2.setValue(null);
            }
        });

    }

    public void setScreenParent(ScreenContainer screenParent) {
        myContainer = screenParent;
    }

    @FXML
    private void findArrivals (ActionEvent event){
        //controlla che venga resettato la combobox
        scbRet.getSelectionModel().clearSelection();
        scbRet.setItems(methods.getArrivals(scbDep.getValue()));
        select.setSelected(false);
        select.setDisable(true);
        error.set(false);
    }

    @FXML
    private void checkRoute(ActionEvent event){
        select.setSelected(false);
        if (methods.checkRoute(scbRet.getValue(), scbDep.getValue())){
            error.set(false);
            select.setDisable(false);
        } else {
            select.setDisable(true);
            error.set(true);
        }
    }

    //Gestisco date2>>date1
    @FXML
    private void returnDate (ActionEvent event){
        date2.setValue(null);
        date2.setDayCellFactory(methods.bookingRange(date1.getValue()));
    }

    @FXML
    private void goToResult(ActionEvent event) throws IOException {
        if (validateFields()) {
            session.setOneway(!select.isSelected());
            session.clear();
            session.setDep(scbDep.getSelectionModel().getSelectedItem());
            session.setRet(scbRet.getSelectionModel().getSelectedItem());
            session.setDateDep(date1.getValue());
            if (!(session.isOneway())) {
                session.setDateRet(date2.getValue());
            }
            myContainer.setScreen(Factory.getResult());
        }
    }

    public boolean validateFields(){
        if( scbRet.getSelectionModel().isEmpty() |
            date1.getValue() == null |
           (date2.getValue() == null & select.isSelected())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validate Fields");
                alert.setHeaderText(null);
                alert.setContentText("Inserire tutti i campi prima di procedere!");
                alert.showAndWait();
                return false;
        }
        return true;
    }

}
