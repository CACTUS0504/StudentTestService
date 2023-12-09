package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "middle_name")
    private String middle_name;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<StudentResult> studentResults;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "student_teacher",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Teacher> teachers;

    @Column(name = "owner_id")
    @JsonIgnore
    private Long ownerId;
}