package com.purcell.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("persons")
public class Person {
    @Id
    String id;
    String firstname;
    String lastname;
    Address address;
}
