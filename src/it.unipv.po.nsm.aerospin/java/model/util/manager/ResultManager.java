package model.util.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import model.Factory;
import model.persistence.CachedFlights;
import model.persistence.entity.Flight;
import model.persistence.service.FlightService;
import model.util.Session;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ResultManager {

    Session session = Factory.getInstance().getSession();
    CachedFlights cachedFlights = CachedFlights.getInstance();
    List<Flight> results = cachedFlights.findAll();


//    private List<Flight> flights = new ArrayList<>();
//    private FlightService flightService = new FlightService();
//    private Flight flight = new Flight();
//    private String date;
//
//    public List<Flight> getFlights(){
//        flights = flightService.findAll();
//        return flights;
//    }
//
//    public List<Flight> getFlightsByDepArr(String dep, String arr, String scheduledDate) {
//        List<Flight> flights = flightService.findAll();
//        List<Flight> results = new ArrayList<>();
//        for (Flight f : flights) {
//            date = f.getScheduledDate().toString().substring(0,10);
//            if (f.getRouteByFlightRouteId().getAirportByDeparture().getAirportName().equals(dep)
//                    && f.getRouteByFlightRouteId().getAirportByArrival().getAirportName().equals(arr) &&
//                    date.equals(scheduledDate)) {
//                results.add(f);
//            }
//
//        }
//
//        return results;
//    }


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
        return new Callback<DatePicker, DateCell>() {
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


    public ObservableList<Flight> getFlights(String dep, String ret, Date date) {

        List<Flight> departures;
        departures = results.stream()
                            .filter(o -> o.getRouteByFlightRouteId().getAirportByDeparture().equalsString(dep))
                            .filter(o -> o.getRouteByFlightRouteId().getAirportByArrival().equalsString(ret))
//                            .filter(o -> o.getScheduledDate().equals(date))
                .distinct()
                .collect(Collectors.toList());

//        .filter(o -> o.getScheduledDate().equals(date))
//        System.out.println(departures);
        return FXCollections.observableArrayList(departures);
    }

}
