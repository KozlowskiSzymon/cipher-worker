package com.example.cypherworker.control;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CypherConfig {

  @Value("${app.cypher.key.size}")
  private int keySize;

  @Value("${app.cypher.algorithm}")
  private String algorithm;

}
