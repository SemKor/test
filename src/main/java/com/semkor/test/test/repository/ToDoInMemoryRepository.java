package com.semkor.test.test.repository;

import com.semkor.test.test.models.ToDo;
import org.springframework.stereotype.Repository;

@Repository
public class ToDoInMemoryRepository extends InMemoryRepository<ToDo> {

    @Override
    protected Long getId(ToDo entity) {
        return entity.getId();
    }

    @Override
    protected void setId(ToDo entity, Long id) {
        entity.setId(id);
    }
}
