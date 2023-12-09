package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Answer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_num")
    private Long questionNum;

    @Column(name = "answer")
    private Integer answer;

    @ManyToOne
    @JoinColumn(name="student_result_id")
    @JsonIgnore
    private StudentResult studentResult;
}
