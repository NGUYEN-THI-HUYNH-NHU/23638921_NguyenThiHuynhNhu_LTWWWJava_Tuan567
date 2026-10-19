/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatajdbc.mapper;

import iuh.fit.demospringdatajdbc.dto.response.DepartmentSalaryResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentSalaryRowMapper implements RowMapper<DepartmentSalaryResponse> {
    @Override
    public DepartmentSalaryResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DepartmentSalaryResponse(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("avg_salary"),
                rs.getDouble("total_salary")
        );
    }
}
