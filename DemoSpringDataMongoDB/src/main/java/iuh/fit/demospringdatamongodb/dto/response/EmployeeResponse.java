/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatamongodb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Integer id;
    private String name;
    private String role;
    private Integer age;
    private Double salary;
    private Integer departmentId;
    private String departmentName;
    private Integer positionId;
    private String positionTitle;
}
