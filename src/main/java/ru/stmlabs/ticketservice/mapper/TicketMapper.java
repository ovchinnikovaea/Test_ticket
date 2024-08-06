package ru.stmlabs.ticketservice.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
//import ru.stmlabs.ticketservice.dto.CreateOrUpdateTicketDto;
import ru.stmlabs.ticketservice.dto.TicketDto;
import ru.stmlabs.ticketservice.dto.TicketsDto;
import ru.stmlabs.ticketservice.entity.Ticket;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class TicketMapper {

    public abstract TicketDto ticketToTicketDto(Ticket ticket);

    public abstract Ticket ticketDtoToTicket(TicketDto ticketDto);

    public abstract List<TicketDto> ticketsToTicketDtos(List<Ticket> tickets);
    public abstract List<Ticket> ticketDtosToTickets(List<TicketDto> ticketDtos);
    @Mapping(target = "results", source = "tickets")
    @Mapping(expression = "java(tickets.size())", target = "count")
    public TicketsDto ticketsToTicketsDto(List<Ticket> tickets) {
        TicketsDto ticketsDto = new TicketsDto();
        ticketsDto.setResults(ticketsToTicketDtos(tickets));
        ticketsDto.setCount(tickets.size());
        return ticketsDto;
    }
}
