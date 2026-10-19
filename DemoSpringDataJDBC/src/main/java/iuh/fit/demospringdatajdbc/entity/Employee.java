/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("employees")
public class Employee {
    @Id
    private Integer id;
    private String name;
    private String role;
    private Integer age;
    private Double salary;
    @Column("department_id")
    private Integer departmentId;
    @Column("position_id")
    private Integer positionId;
}
