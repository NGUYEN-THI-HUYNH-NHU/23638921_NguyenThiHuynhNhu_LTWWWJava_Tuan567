/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatamongodb.repository;

import com.mongodb.client.result.UpdateResult;
import iuh.fit.demospringdatamongodb.entity.Department;
import iuh.fit.demospringdatamongodb.entity.Employee;
import iuh.fit.demospringdatamongodb.entity.EmployeeProjection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private static final String DEPARTMENTS = "departments";
    private final MongoTemplate mongoTemplate;

    public List<EmployeeProjection> findAll() {
        return unwindAndFilter(null);
    }

    public Optional<EmployeeProjection> findById(Integer employeeId) {
        return unwindAndFilter(Criteria.where("employees.employeeId").is(employeeId))
                .stream().findFirst();
    }

    /**
     * Cập nhật nhân viên. Nếu đổi phòng ban thì xóa khỏi phòng cũ rồi thêm vào phòng mới.
     *
     * @return false nếu không tìm thấy nhân viên
     */
    public boolean update(Integer departmentId, Employee e) {
        Query sameDepartment = Query.query(Criteria.where("_id").is(departmentId)
                .and("employees.employeeId").is(e.getEmployeeId()));
        Update update = new Update()
                .set("employees.$.name", e.getName())
                .set("employees.$.role", e.getRole())
                .set("employees.$.age", e.getAge())
                .set("employees.$.salary", e.getSalary())
                .set("employees.$.positionId", e.getPositionId());

        UpdateResult result = mongoTemplate.updateFirst(sameDepartment, update, Department.class);
        if (result.getMatchedCount() > 0) return true;
        if (!deleteById(e.getEmployeeId())) return false;
        insert(departmentId, e);
        return true;
    }

    public boolean deleteById(Integer employeeId) {
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("employees.employeeId").is(employeeId)),
                new Update().pull("employees", new Document("employeeId", employeeId)),
                Department.class
        );
        return result.getModifiedCount() > 0;
    }

    public void insert(Integer departmentId, Employee employee) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(departmentId)),
                new Update().push("employees", employee),
                Department.class
        );
    }

    public Integer nextId() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("employees"),
                Aggregation.group().max("employees.employeeId").as("maxId")
        );
        Document result = mongoTemplate.aggregate(aggregation, DEPARTMENTS, Document.class)
                .getUniqueMappedResult();
        int max = (result == null || result.get("maxId") == null)
                ? 0 : ((Number) result.get("maxId")).intValue();
        return max + 1;
    }

    public List<EmployeeProjection> findByNameContainingIgnoreCase(String name) {
        return unwindAndFilter(Criteria.where("employees.name")
                .regex(Pattern.quote(name.trim()), "i"));
    }

    public List<EmployeeProjection> findByAgeBetween(int from, int to) {
        return unwindAndFilter(Criteria.where("employees.age").gte(from).lte(to));
    }

    public List<EmployeeProjection> findBySalaryGreaterThan(double salary) {
        return unwindAndFilter(Criteria.where("employees.salary").gt(salary));
    }

    public List<EmployeeProjection> search(Integer id, String name, Integer minAge, Integer maxAge,
                                           Double minSalary, Integer positionId) {
        List<Criteria> conditions = new ArrayList<>();
        if (id != null)
            conditions.add(Criteria.where("employees.employeeId").is(id));
        if (name != null && !name.isBlank())
            conditions.add(Criteria.where("employees.name").regex(Pattern.quote(name.trim()), "i"));
        if (minAge != null)
            conditions.add(Criteria.where("employees.age").gte(minAge));
        if (maxAge != null)
            conditions.add(Criteria.where("employees.age").lte(maxAge));
        if (minSalary != null)
            conditions.add(Criteria.where("employees.salary").gt(minSalary));
        if (positionId != null)
            conditions.add(Criteria.where("employees.positionId").is(positionId));
        return unwindAndFilter(conditions.isEmpty() ? null : new Criteria().andOperator(conditions));
    }

    public List<EmployeeProjection> findByPositionId(Integer positionId) {
        return unwindAndFilter(Criteria.where("employees.positionId").is(positionId));
    }

    public List<EmployeeProjection> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId) {
        return run(List.of(
                Aggregation.match(Criteria.where("_id").is(departmentId)),
                Aggregation.unwind("employees"),
                Aggregation.match(Criteria.where("employees.positionId").is(positionId)),
                Aggregation.sort(Sort.by("employees.employeeId"))
        ));
    }

    public List<EmployeeProjection> findTopByDepartmentIdOrderBySalaryDesc(Integer departmentId, int n) {
        return run(List.of(
                Aggregation.match(Criteria.where("_id").is(departmentId)),
                Aggregation.unwind("employees"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "employees.salary")),
                Aggregation.limit(n)
        ));
    }

    private List<EmployeeProjection> unwindAndFilter(Criteria employeeCriteria) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.unwind("employees"));
        if (employeeCriteria != null)
            operations.add(Aggregation.match(employeeCriteria));
        operations.add(Aggregation.sort(Sort.by("employees.employeeId")));
        return run(operations);
    }

    private List<EmployeeProjection> run(List<AggregationOperation> operations) {
        return mongoTemplate
                .aggregate(Aggregation.newAggregation(operations), DEPARTMENTS, EmployeeProjection.class)
                .getMappedResults();
    }
}
