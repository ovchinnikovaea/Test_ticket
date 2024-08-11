package ru.stmlabs.ticketservice.mapper;
import org.springframework.stereotype.Component;
import ru.stmlabs.ticketservice.dto.TicketDto;
import ru.stmlabs.ticketservice.entity.Route;
import ru.stmlabs.ticketservice.entity.Ticket;
import ru.stmlabs.ticketservice.repository.RouteRepository;


@Component
public class TicketMapper {

    private final RouteRepository routeRepository;

    public TicketMapper(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public TicketDto toDto(Ticket ticket) {
        Route route = routeRepository.getRouteById(ticket.getRouteId());

        TicketDto ticketDto = new TicketDto();
        ticketDto.setDeparture(route.getDeparture());
        ticketDto.setArrival(route.getArrival());
        ticketDto.setDateTime(ticket.getDateTime());
        ticketDto.setPrice(ticket.getPrice());

        return ticketDto;
    }
}
