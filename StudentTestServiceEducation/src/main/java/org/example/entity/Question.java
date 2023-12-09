package org.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "option_1")
    private String option_1;

    @Column(name = "option_2")
    private String option_2;

    @Column(name = "option_3")
    private String option_3;

    @Column(name = "option_4")
    private String option_4;

    @Column(name = "answer")
    @JsonIgnore
    private Integer answer;

    @ManyToOne
    @JoinColumn(name="test_id")
    @JsonBackReference
    private Test test;
}
