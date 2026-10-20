/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatamongodb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeRequest {
    private Integer id;
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private Double minSalary;
    private Integer positionId;

    public boolean hasAny() {
        return id != null
                || (name != null && !name.isBlank())
                || minAge != null || maxAge != null
                || minSalary != null
                || positionId != null;
    }
}
