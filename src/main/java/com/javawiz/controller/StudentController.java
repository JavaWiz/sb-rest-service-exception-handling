package com.javawiz.controller;

import com.javawiz.exception.StudentNotFoundException;
import com.javawiz.model.Student;
import com.javawiz.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping("/students")
    public List<Student> retrieveAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> retrieveStudent(@PathVariable long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isPresent())
            throw new StudentNotFoundException("id-" + id);
        return ResponseEntity.ok(student.get());
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentRepository.deleteById(id);
    }

    @PostMapping("/students")
    public ResponseEntity<Object> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (!studentOptional.isPresent())
            return ResponseEntity.notFound().build();
        student.setId(id);
        studentRepository.save(student);
        return ResponseEntity.noContent().build();
    }
}
