package be.vdab.expo.repositories;

import be.vdab.expo.domain.Bestelling;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class BestellingRepository {
    private final JdbcTemplate template;

    public BestellingRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Bestelling> mapper =
            (rs, rowNum) -> new Bestelling(
                    rs.getString("naam"),
                    rs.getInt("type")
            );

    public long bestel(Bestelling bestelling){
        var sql = """
                INSERT INTO bestellingen(naam, ticketType)
                VALUES (?,?)
                """;

        var keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, bestelling.getNaam());
            statement.setInt(2, bestelling.getType());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Bestelling> findAndLockById(long id){
        try {
            var sql = """
                    SELECT id, naam, ticketType
                    FROM bestellingen
                    WHERE id = ?
                    """;
            return Optional.of(template.queryForObject(sql, mapper, id));
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        }
    }
}
