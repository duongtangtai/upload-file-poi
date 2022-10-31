package com.hieulexuan.uploadexcelfile.task.repository;

import com.hieulexuan.uploadexcelfile.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t left join fetch t.users left join fetch t.project where t.name = ?1")
    Optional<Task> findByName(String taskName);
}
