/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatamongodb.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveEmployeeRequest {
    private Integer id;

    @NotBlank(message = "Tên không được để trống")
    @Size
    private String name;

    @Size(max = 255)
    private String role;

    @NotNull(message = "Lương không được để trống")
    @Min(value = 18, message = "Tuổi tối thiểu là 18")
    @Max(value = 65, message = "Tuổi tối đa là 65")
    private Integer age;

    @NotNull(message = "Lương không được để trống")
    @PositiveOrZero
    private Double salary;

    @NotNull(message = "Vui lòng chọn phòng ban")
    private Integer departmentId;

    @NotNull(message = "Vui lòng chọn chức vụ")
    private Integer positionId;
}
