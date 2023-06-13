package be.vdab.expo.services;

import be.vdab.expo.domain.Bestelling;
import be.vdab.expo.domain.Tickets;
import be.vdab.expo.repositories.BestellingRepository;
import be.vdab.expo.repositories.TicketsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private final BestellingRepository bestellingRepo;
    private final TicketsRepository ticketsRepo;

    public BestellingService(BestellingRepository bestellingRepo, TicketsRepository ticketsRepo) {
        this.bestellingRepo = bestellingRepo;
        this.ticketsRepo = ticketsRepo;
    }

    @Transactional
    public long bestel(Bestelling bestelling){
        var tickets = ticketsRepo.findAndLockTickets();
        tickets.bestel(bestelling.getType());
        var bestellingId = bestellingRepo.bestel(bestelling);
        ticketsRepo.update(tickets);

        return bestellingId;
    }
}
