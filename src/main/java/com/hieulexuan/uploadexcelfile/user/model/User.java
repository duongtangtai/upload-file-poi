package com.hieulexuan.uploadexcelfile.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hieulexuan.uploadexcelfile.project.model.Project;
import com.hieulexuan.uploadexcelfile.task.model.Task;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<Project> projects;

    @ManyToMany(mappedBy = "users",  cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<Task> tasks;
}
