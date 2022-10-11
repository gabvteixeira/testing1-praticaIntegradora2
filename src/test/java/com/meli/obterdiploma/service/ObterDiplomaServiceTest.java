package com.meli.obterdiploma.service;

import com.meli.obterdiploma.exception.StudentNotFoundException;
import com.meli.obterdiploma.model.StudentDTO;
import com.meli.obterdiploma.model.SubjectDTO;
import com.meli.obterdiploma.repository.IStudentDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ObterDiplomaServiceTest {

    @InjectMocks
    private ObterDiplomaService service;

    @Mock
    private IStudentDAO dao;

    private StudentDTO student;

    @BeforeEach
    public void setup(){
        // Preparar (Arrange)
        this.student = new StudentDTO(1L,
                "Teste",
                "Teste",
                7.5d,
                new ArrayList<>(){{
                 add(new SubjectDTO("Teste", 7.5));
                }});
    }

    @Test
    @DisplayName("analyzeScores - Encontrou estudante e retornou diploma")
    // NOME DO METODO - O QUE RETORNA - QUANDO
    public void analyzeScores_returnStudent_whenFoundStudent(){
        long studentId = 1;
        Mockito.when(dao.findById(ArgumentMatchers.anyLong()))
                .thenReturn(this.student);

        // Executar (Act)
        StudentDTO response = this.service.analyzeScores(studentId);

        // Avaliar (Arrange)
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(studentId);
        assertThat(response.getAverageScore()).isPositive();
        assertThat(response.getStudentName()).isNotNull();
        assertFalse(response.getSubjects().isEmpty());
    }

    @Test
    @DisplayName("analyzeScores - Lanca excecao quando nao encontra o estudante")
    public void analyzeScores_throwException_whenNotFoundStudent(){
        long nonExistentId = 999;

        BDDMockito.given(dao.findById(ArgumentMatchers.anyLong()))
        .willThrow(new StudentNotFoundException(nonExistentId));

        assertThrows(StudentNotFoundException.class, () -> {
           service.analyzeScores(nonExistentId);
        });
    }

}
