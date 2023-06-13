package be.vdab.expo.services;

import be.vdab.expo.domain.Bestelling;
import be.vdab.expo.exceptions.OnvoldoendeTicketsException;
import be.vdab.expo.repositories.BestellingRepository;
import be.vdab.expo.repositories.TicketsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@JdbcTest
@Sql("/bestelling.sql")
@Import({BestellingService.class, TicketsRepository.class, BestellingRepository.class})
class BestellingServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String BESTELLINGEN = "bestellingen";
    private static final String TICKETS = "tickets";
    private final BestellingService bestellingService;
    public BestellingServiceTest(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    private long idVanTestBestelling1() {
        return jdbcTemplate.queryForObject("select id from bestellingen where naam = 'test1'", Long.class);
    }
    private long idVanTestBestelling2() {
        return jdbcTemplate.queryForObject("select id from bestellingen where naam = 'test2'", Long.class);
    }
    private long idVanTestBestelling3() {
        return jdbcTemplate.queryForObject("select id from bestellingen where naam = 'test3'", Long.class);
    }

    @Test
    void bestelType1() {
        var bestelId = idVanTestBestelling1();
        bestellingService.bestel(new Bestelling("test1", 1));

        assertThat(countRowsInTableWhere(BESTELLINGEN, "naam = 'test1' AND id = " + bestelId)).isOne();
        assertThat(countRowsInTableWhere(TICKETS, "juniorDag = 99 AND seniorDag = 200")).isOne();
    }
    @Test
    void bestelType2() {
        var bestelId = idVanTestBestelling2();
        bestellingService.bestel(new Bestelling("test2", 2));

        assertThat(countRowsInTableWhere(BESTELLINGEN, "naam = 'test2' AND id = " + bestelId)).isOne();
        assertThat(countRowsInTableWhere(TICKETS, "juniorDag = 100 AND seniorDag = 199")).isOne();
    }
    @Test
    void bestelType3() {
        var bestelId = idVanTestBestelling3();
        bestellingService.bestel(new Bestelling("test3", 3));

        assertThat(countRowsInTableWhere(BESTELLINGEN, "naam = 'test3' AND id = " + bestelId)).isOne();
        assertThat(countRowsInTableWhere(TICKETS, "juniorDag = 99 AND seniorDag = 199")).isOne();
    }

    /*
    @Test
    void bestelZonderNaamMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingService.bestel(new Bestelling("", 1))
        );
    }

    @Test
    void bestelVerkeerdTypeMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingService.bestel(new Bestelling("test", 0))
        );

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingService.bestel(new Bestelling("test", 4))
        );
    }
    */

    @Test
    void bestellingNegatiefTypeMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingService.bestel(new Bestelling("test", -2))
        );
    }

    @Test
    void bestellingOnvoldoendeTicketsType1Mislukt() {
        for(var i = 0; i<100; i++){
            bestellingService.bestel(new Bestelling("test"+i, 1));
        }
        assertThatExceptionOfType(OnvoldoendeTicketsException.class).isThrownBy(
                ()-> bestellingService.bestel(new Bestelling("testOnvoldoendeTickets", 1))
        );
    }

    @Test
    void bestellingOnvoldoendeTicketsType3Mislukt() {
        for(var i = 0; i<100; i++){
            bestellingService.bestel(new Bestelling("test"+i, 3));
        }
        assertThatExceptionOfType(OnvoldoendeTicketsException.class).isThrownBy(
                ()-> bestellingService.bestel(new Bestelling("testOnvoldoendeTickets", 3))
        );
    }
}