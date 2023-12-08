package com.example.vaadincrowd.repositories;

import com.example.vaadincrowd.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("FROM Employee e WHERE CONCAT(e.lastName, ' ', e.firstName, ' ', e.patronymic) " +
            "LIKE CONCAT('%', :name, '%')")
    List<Employee> findByName(@Param("name") String name);
}
