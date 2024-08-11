package ru.stmlabs.ticketservice.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RouteRowMapper implements RowMapper<Route> {
    @Override
    public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
        Route route = new Route();
        route.setId(rs.getInt("id"));
        route.setDeparture(rs.getString("departure"));
        route.setArrival(rs.getString("arrival"));
        return route;
    }
}


