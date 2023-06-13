package be.vdab.expo.console;

import be.vdab.expo.domain.Bestelling;
import be.vdab.expo.exceptions.OnvoldoendeTicketsException;
import be.vdab.expo.services.BestellingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {

    private final BestellingService bestellingService;
    //private final
    public MyRunner(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    @Override
    public void run(String... args) throws Exception {

        var scanner= new Scanner(System.in);
        System.out.print("Geef uw naam op: ");
        var naam = scanner.nextLine();
        System.out.print("Welk type ticket: (1, 2, 3): ");
        var type = scanner.nextInt();

        try {
            var bestelling = new Bestelling(naam, type);
            var nieuweId =bestellingService.bestel(bestelling);
            System.out.println("Id van deze bestelling: " + nieuweId);
            System.out.println("Bestelling successvol aangemaakt");
        }catch (IllegalArgumentException e){
            System.err.println(e.getMessage());
        }catch (OnvoldoendeTicketsException e){
            System.err.println("Onvoldoende tickets beschikbaar voor het gevraagde type");
        }
    }
}