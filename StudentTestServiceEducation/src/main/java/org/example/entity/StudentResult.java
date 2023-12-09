package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "marks")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class StudentResult {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="test_id")
    private Test completedTest;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @Column(name = "result")
    private Float mark;

    @Column(name = "completed")
    private Boolean completed;

    @OneToMany(mappedBy = "studentResult", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Answer> studentAnswers;
}
