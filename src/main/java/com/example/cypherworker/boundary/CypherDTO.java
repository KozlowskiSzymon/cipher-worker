package com.example.cypherworker.boundary;

import lombok.Data;

@Data
public class CypherDTO {

  private String value;
  private String publicKey;
  private String privateKey;
  private long userId;

}
