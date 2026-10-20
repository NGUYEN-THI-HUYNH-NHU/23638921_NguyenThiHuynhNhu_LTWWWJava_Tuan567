/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatamongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class EmployeeProjection {
    @Id
    private Integer departmentId;

    @Field("name")
    private String departmentName;

    @Field("employees")
    private Employee employee;
}
