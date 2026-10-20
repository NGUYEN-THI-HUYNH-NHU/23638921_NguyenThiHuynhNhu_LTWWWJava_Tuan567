package iuh.fit.demospringdatamongodb.repository;

import iuh.fit.demospringdatamongodb.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringdatamongodb.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringdatamongodb.entity.Department;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DepartmentRepository extends MongoRepository<Department, Integer> {
    @Override
    @Query(value = "{}", fields = "{ 'employees': 0 }", sort = "{ '_id': 1 }")
    List<Department> findAll();

    @Query(value = "{ 'employees.0': { $exists: false } }", fields = "{ 'employees': 0 }", sort = "{ '_id': 1 }")
    List<Department> findWithoutEmployees();

    @Aggregation(pipeline = {
            "{ $project: { _id: 0, departmentId: '$_id', departmentName: '$name', " +
                    "employeeCount: { $size: { $ifNull: ['$employees', []] } } } }",
            "{ $sort: { departmentId: 1 } }"
    })
    List<DepartmentEmployeeCountResponse> countEmployeesByDepartment();

    @Aggregation(pipeline = {
            "{ $project: { _id: 0, departmentId: '$_id', departmentName: '$name', " +
                    "avgSalary:  { $ifNull: [ { $avg: '$employees.salary' }, 0 ] }, " +
                    "totalSalary: { $sum: '$employees.salary' } } }",
            "{ $sort: { departmentId: 1 } }"
    })
    List<DepartmentSalaryResponse> salaryStatsByDepartment();
}
