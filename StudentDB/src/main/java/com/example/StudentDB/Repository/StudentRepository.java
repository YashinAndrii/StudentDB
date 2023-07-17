package com.example.StudentDB.Repository;

import com.example.StudentDB.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByDepartment(String department);
    List<Student> findByFaculty(String faculty);
    Student findByLname(String lname);

}
