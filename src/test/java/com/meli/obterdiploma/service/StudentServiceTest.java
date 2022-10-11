package com.meli.obterdiploma.service;

import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.IStudentDAO;
import com.meli.obterdiploma.repository.IStudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private IStudentDAO studentDAO;
    @Mock
    private IStudentRepository repo;

    @InjectMocks
    private  StudentService service;

    private StudentDTO student;
    private Set<StudentDTO> students;

    @BeforeEach
    public void setup() {
        this.student = new StudentDTO(1L,
                "Teste",
                "Teste",
                7.5d,
                new ArrayList<>(){{
                    add(new SubjectDTO("Teste", 7.5));
                }});

        this.students = new HashSet<>();
        this.students.add(this.student);
    }

    @Test
    @DisplayName("read - Encontrou o estudante com sucesso")
    public void read_returnStudent_whenFoundById(){
        long studentId = 1;
        Mockito.when(this.studentDAO.findById(ArgumentMatchers.anyLong()))
                .thenReturn(this.student);

        StudentDTO response = this.service.read(studentId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(studentId);
        assertThat(response.getAverageScore()).isPositive();
        assertThat(response.getStudentName()).isNotNull();
        assertFalse(response.getSubjects().isEmpty());

    }

    @Test
    @DisplayName("read - Lanca uma excecao quando nao encontrou o estudante")
    public void read_throwsException_whenNotFoundById(){
        long nonExistentId = 999;

        BDDMockito.given(this.studentDAO.findById(ArgumentMatchers.anyLong()))
                .willThrow(new StudentNotFoundException(nonExistentId));

        assertThrows(StudentNotFoundException.class, () -> {
           this.service.read(nonExistentId);
        });
    }

    @Test
    @DisplayName("get all - Retorna um conjunto de estudantes com sucesso")
    public void getAll_returnStudentsSet_whenFoundAll(){
        Mockito.when(this.repo.findAll()).thenReturn(this.students);

        Set<StudentDTO> response = this.service.getAll();

        assertThat(response).isNotNull();
    }

}
