package com.example.cypherworker.boundary;

import com.example.cypherworker.control.CypherConfig;
import com.example.cypherworker.control.Decrypter;
import com.example.cypherworker.control.Encrypter;
import com.example.cypherworker.control.KeyGenerator;
import com.example.cypherworker.model.CypherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/cypher")
@RestController
@RequiredArgsConstructor
public class CypherResource {

  private final CypherConfig config;

  private final CypherRepository cypherRepository;

  @GetMapping("/keys")
  public ResponseEntity<String> generateKeyPairForUser(@RequestParam("userId") long userId) {
    return ok(new KeyGenerator(config, cypherRepository).apply(userId));
  }

  @PostMapping("/encrypt")
  public ResponseEntity<String> encrypt(@RequestBody CypherDTO cypherDTO) {
    return ok(new Encrypter(config).apply(cypherDTO));
  }

  @PostMapping("/decrypt")
  public ResponseEntity<String> decrypt(@RequestBody CypherDTO cypherDTO) {
    return ok(new Decrypter(config, cypherRepository).apply(cypherDTO));
  }

}
