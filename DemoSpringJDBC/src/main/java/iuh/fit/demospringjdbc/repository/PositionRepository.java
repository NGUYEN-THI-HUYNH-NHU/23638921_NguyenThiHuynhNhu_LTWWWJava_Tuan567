/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringjdbc.repository;

import iuh.fit.demospringjdbc.entity.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PositionRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Position> rowMapper = (rs, i) -> Position.builder()
            .id(rs.getInt("id")).title(rs.getString("title"))
            .build();

    public List<Position> findAll() {
        return jdbcTemplate.query("SELECT id, title FROM positions ORDER BY id", rowMapper);
    }
}
