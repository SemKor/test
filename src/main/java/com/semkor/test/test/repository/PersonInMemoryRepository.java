package com.semkor.test.test.repository;

import com.semkor.test.test.models.Person;
import com.semkor.test.test.models.ToDo;
import org.springframework.stereotype.Repository;

@Repository
public class PersonInMemoryRepository extends InMemoryRepository<Person> {

    @Override
    protected Long getId(Person entity) {
        return entity.getPersonId();
    }

    @Override
    protected void setId(Person entity, Long id) {
        entity.setPersonId(id);
    }
}
