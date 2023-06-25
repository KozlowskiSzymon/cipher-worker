package com.example.cypherworker.boundary;

import com.example.cypherworker.control.CypherConfig;
import com.example.cypherworker.control.Decrypter;
import com.example.cypherworker.control.Encrypter;
import com.example.cypherworker.control.KeyGenerator;
import com.example.cypherworker.model.CypherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CypherResource {

  private final CypherConfig config;

  @PostMapping("/keys")
  public ResponseEntity<CypherDTO> generateKeyPairForUser(@RequestBody long userId) {
    log.info("CypherResource.generateKeyPairForUser POST /keys");
    return ok(new KeyGenerator(config).apply(userId));
  }

  @PostMapping("/encrypt")
  public ResponseEntity<String> encrypt(@RequestBody CypherDTO cypherDTO) {
    log.info("CypherResource.encrypt POST /encrypt");
    return ok(new Encrypter(config).apply(cypherDTO));
  }

  @PostMapping("/decrypt")
  public ResponseEntity<String> decrypt(@RequestBody CypherDTO cypherDTO) {
    log.info("CypherResource.decrypt POST /decrypt");
    return ok(new Decrypter(config).apply(cypherDTO));
  }

}
