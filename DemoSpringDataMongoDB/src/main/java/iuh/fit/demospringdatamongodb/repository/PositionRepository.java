package iuh.fit.demospringdatamongodb.repository;

import iuh.fit.demospringdatamongodb.entity.Position;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PositionRepository extends MongoRepository<Position, Integer> {
}
