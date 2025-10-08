package com.semkor.test.test.repository;

import com.semkor.test.test.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);
    Optional<Person> findByUsername(String username);
}