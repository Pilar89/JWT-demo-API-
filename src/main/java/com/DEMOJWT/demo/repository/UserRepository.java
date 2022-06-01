package com.DEMOJWT.demo.repository;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
