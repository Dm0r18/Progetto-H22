package controller.util.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import model.exception.NoMatchException;
import model.persistence.CachedFlights;
import model.persistence.entity.Flight;
import model.persistence.service.FlightService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ResultManager {

    CachedFlights cachedFlights = CachedFlights.getInstance();
    FlightService flightService = new FlightService();
    List<Flight> results = cachedFlights.findAll();

    private static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[a-zA-z]{2,15}$");

    public ObservableList<Flight> getFlights(String dep, String ret, Date date) throws NoMatchException {

        List<Flight> departures;
        departures = results.stream()
                .filter(o -> o.getRouteById().getAirportDep().equalsString(dep))
                .filter(o -> o.getRouteById().getAirportArr().equalsString(ret))
                .filter(o -> o.getScheduledDate().equals(date))
                .filter(o -> o.getSeats() > 0)
                .distinct()
                .collect(Collectors.toList());
        if(departures.size() == 0){
            throw new NoMatchException("Not Matched!\n");
        } else {
            return FXCollections.observableArrayList(departures);
        }
    }

    public void bookSeat(Flight flight) {
        flight.setSeats(flight.getSeats()-1);
        flightService.update(flight);
    }

    /*  Controllo se l'età selezionata è > 16
     *  e permetto di selezionare solo date passate
     *  come data di nascita
     */
    public boolean isMinor(LocalDate birthDate){
        LocalDate today = LocalDate.now();
        Period age = Period.between(birthDate, today);
        long years = age.getYears();
        return years < 16;
    }

    public Callback<DatePicker, DateCell> ageRange() {
        return new Callback<>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now().minusDays(1);
                        setDisable(empty || date.isAfter(today));
                    }
                };
            }
        };
    }

    public boolean dataCheck(String name, String surname) {
        Matcher matcher1 = VALID_NAME_REGEX.matcher(name);
        Matcher matcher2 = VALID_NAME_REGEX.matcher(surname);

        return matcher1.find() && matcher2.find();
    }
}
