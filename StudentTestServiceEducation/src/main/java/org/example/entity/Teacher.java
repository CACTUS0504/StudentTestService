package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Teacher {
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

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Test> tests;

    @ManyToMany(mappedBy = "teachers")
    @JsonIgnore
    private List<Student> students;

    @Column(name = "owner_id")
    @JsonIgnore
    private Long ownerId;
}
