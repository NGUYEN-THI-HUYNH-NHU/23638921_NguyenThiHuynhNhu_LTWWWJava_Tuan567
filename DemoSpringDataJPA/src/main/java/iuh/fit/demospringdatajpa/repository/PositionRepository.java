package iuh.fit.demospringdatajpa.repository;

import iuh.fit.demospringdatajpa.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {
}
