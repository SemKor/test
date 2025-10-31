package com.semkor.test.test.repository;

import com.semkor.test.test.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true)
    List<Task> findByTitleContainingIgnoreCase(@Param("title") String title);

    // 🔹 Найти все задачи, где title содержит подстроку (без учёта регистра)
    //SELECT * FROM task WHERE LOWER(title) LIKE LOWER('%дел%');
    //List<Task> findByTitleContainingIgnoreCase(String title);

    // 🔹 Найти все задачи по статусу (true = выполненные, false = нет)
    //SELECT * FROM task WHERE completed = true;
    List<Task> findByCompleted(Boolean completed);

    // 🔹 Найти задачи по названию и статусу одновременно
    //SELECT * FROM task WHERE LOWER(title) LIKE LOWER('%дел%') AND completed = false;
    List<Task> findByTitleContainingIgnoreCaseAndCompleted(String title, Boolean completed);
}
