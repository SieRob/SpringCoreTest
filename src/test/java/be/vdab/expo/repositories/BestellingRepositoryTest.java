package be.vdab.expo.repositories;

import be.vdab.expo.domain.Bestelling;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(BestellingRepository.class)
@Sql("/bestelling.sql")
class BestellingRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String BESTELLINGEN = "bestellingen";
    private static final String TICKETS = "tickets";

    private final BestellingRepository bestellingRepo;

    public BestellingRepositoryTest(BestellingRepository bestellingRepo) {
        this.bestellingRepo = bestellingRepo;
    }

    private long idVanTestBestelling1() {
        return jdbcTemplate.queryForObject("select id from bestellingen where naam = 'test1'", Long.class);
    }

    @Test
    void bestel() {
        var bestelId = idVanTestBestelling1();

        bestellingRepo.bestel(new Bestelling("test1", 1));
        assertThat(countRowsInTableWhere(BESTELLINGEN, "naam = 'test1' AND id = " + bestelId)).isOne();
    }

    @Test
    void bestellingZonderNaamMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingRepo.bestel(new Bestelling("", 1))
        );
    }

    @Test
    void bestelVerkeerdTypeMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingRepo.bestel(new Bestelling("test", 0))
        );

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingRepo.bestel(new Bestelling("test", 4))
        );
    }

    @Test
    void bestellingNegatiefTypeMislukt() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> bestellingRepo.bestel(new Bestelling("test", -2))
        );
    }
}