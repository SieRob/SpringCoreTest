package be.vdab.expo.repositories;

import be.vdab.expo.domain.Tickets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public class TicketsRepository {
    private final JdbcTemplate template;

    private final RowMapper<Tickets> mapper =
            (rs, rowNum) -> new Tickets(
                    rs.getInt("juniorDag"),
                    rs.getInt("seniorDag")
            );

    public TicketsRepository(JdbcTemplate template) {
        this.template = template;
    }

    public void update(Tickets tickets) {
        var sql = """
                UPDATE tickets
                SET juniorDag = ?, seniorDag = ?
                """;

        template.update(sql, tickets.getJuniorDag(), tickets.getSeniorDag());
    }

    public Tickets findAndLockTickets(){
        var sql = """
                SELECT juniorDag, seniorDag
                FROM tickets
                FOR UPDATE
                """;

        return template.queryForObject(sql, mapper);
    }
}
