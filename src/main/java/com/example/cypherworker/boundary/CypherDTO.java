package com.example.cypherworker.boundary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CypherDTO {

  private String value;
  private String publicKey;
  private String privateKey;
  private long userId;

}
