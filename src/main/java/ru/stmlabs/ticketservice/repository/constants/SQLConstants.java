package ru.stmlabs.ticketservice.repository.constants;

public class SQLConstants {
    public static final String SQL_GET_TICKETS_BY_USER_ID = """            
            SELECT t.*, r.departure, r.arrival, r.carrier, r.duration
            FROM ticket t
            JOIN route r ON t.route_id = r.id
            WHERE t.user_id = ? AND t.status = 'sold'
            """;

    public static final String SQL_GET_STATUS_BY_TICKET_ID = "SELECT status FROM ticket WHERE id = ?";

    public static final String SQL_GET_TICKET_BY_TICKET_ID = """            
            SELECT t.id, t.seat, t.price, t.status, t.dataTime, t.route_id,
            r.departure, r.arrival, r.carrier 
            FROM ticket t 
            JOIN route r ON t.route_id = r.id 
            WHERE t.id = ?
            """;

    public static final String SQL_UPDATE_TICKET_STATUS = "UPDATE ticket SET status = 'sold', user_id = ? WHERE id = ?";
    public static final String SQL_GET_AVAILABLE_TICKET = """ 
            SELECT t.id, t.route_id, t.dataTime, t.seat, t.price, t.status, t.user_id, 
            r.departure, r.arrival, r.carrier 
            FROM ticket t 
            JOIN route r ON t.route_id = r.id 
            WHERE t.status = 'available'
            """;

    public static final String SQL_FIND_LOGIN_USER = "SELECT * FROM app_user WHERE login = ?";
    public static final String SQL_ADD_USER = "INSERT INTO app_user (login, password, fullName) VALUES (?, ?, ?)";

    public static final String SQL_FIND_ID_BY_LOGIN = "SELECT id FROM app_user WHERE login = ?";

}
