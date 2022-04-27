package model.persistence.daoInterface;

import model.persistence.entity.Flight;


import java.sql.Date;
import java.util.List;

public interface FlightDaoInterface {
    List<Flight> findAll();
    List<Flight> findById(int id);
    List<Flight> findByDate(Date date);
    List<Flight> findFlightsByDate(String dep, String arr, String scheduledDate);
    public void persist(Flight entity);
    public void update(Flight entity);
    public void delete(Flight entity);
    public void deleteAll();
}