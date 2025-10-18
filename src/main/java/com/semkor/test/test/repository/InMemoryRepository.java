package com.semkor.test.test.repository;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public abstract class InMemoryRepository<T> {

    private final Map<Long, T> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    protected abstract Long getId(T entity);
    protected abstract void setId(T entity, Long id);

    public T save(T entity) {
        Long id = getId(entity);
        if (id == null) {
            id = idGenerator.getAndIncrement();
            setId(entity, id);
        }
        storage.put(id, entity);
        return entity;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public void clear() {
        storage.clear();
    }
}
