/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatajdbc.mapper;

import iuh.fit.demospringdatajdbc.dto.response.DepartmentEmployeeCountResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentEmployeeCountRowMapper implements RowMapper<DepartmentEmployeeCountResponse> {
    @Override
    public DepartmentEmployeeCountResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DepartmentEmployeeCountResponse(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getLong("employee_count")
        );
    }
}
