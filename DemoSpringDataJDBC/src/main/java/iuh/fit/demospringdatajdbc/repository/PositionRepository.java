/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajdbc.repository;

import iuh.fit.demospringdatajdbc.entity.Position;
import org.springframework.data.repository.ListCrudRepository;

public interface PositionRepository extends ListCrudRepository<Position, Integer> {
}
