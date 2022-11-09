package com.example.cypherworker.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CypherRepository extends JpaRepository<Key, Long> {

  Key findByUserId(long userId);
}
