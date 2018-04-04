package com.purcell.repository;

import com.purcell.entity.Person;
import org.springframework.data.repository.CrudRepository;


public interface PersonRepository extends CrudRepository<Person, String> {
}
