/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatamongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer employeeId;
    private String name;
    private String role;
    private Integer age;
    private Double salary;
    private Integer positionId;
}
