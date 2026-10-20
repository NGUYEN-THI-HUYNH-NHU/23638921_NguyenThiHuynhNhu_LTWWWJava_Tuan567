/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatamongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("positions")
public class Position {
    @Id
    private Integer id;
    private String title;
}
