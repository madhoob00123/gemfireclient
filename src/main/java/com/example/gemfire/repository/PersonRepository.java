package com.example.gemfire.repository;

import com.example.gemfire.hello.Person;
import org.springframework.data.gemfire.repository.query.annotation.Trace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends CrudRepository<Person, String> {

    @Trace
    Person findByName(String name);

    @Trace
    Iterable<Person> findByAgeGreaterThan(int age);

    @Trace
    Iterable<Person> findByAgeLessThan(int age);

    @Trace
    Iterable<Person> findByAgeGreaterThanAndAgeLessThan(int greaterThanAge, int lessThanAge);

}
