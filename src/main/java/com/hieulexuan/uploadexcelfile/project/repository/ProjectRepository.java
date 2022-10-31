package com.hieulexuan.uploadexcelfile.project.repository;

import com.hieulexuan.uploadexcelfile.project.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "select p from Project p left join fetch p.users left join fetch p.tasks where p.name = ?1")
    Optional<Project> findByName(String projectName);
}
