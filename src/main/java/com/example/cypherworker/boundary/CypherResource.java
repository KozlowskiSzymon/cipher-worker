package com.example.cypherworker.boundary;

import com.example.cypherworker.control.CypherService;
import com.example.cypherworker.control.CypherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/cypher")
@RestController
@RequiredArgsConstructor
public class CypherResource {

  private final CypherService cypherService;

  @GetMapping("/keys")
  public ResponseEntity<String> generateKeyPairForUser(@RequestParam("userId") long userId) throws NoSuchAlgorithmException {
    return ok(cypherService.generateKeys(userId));
  }

  @PostMapping("/encrypt")
  public ResponseEntity<String> encrypt(@RequestBody CypherDTO cypherDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
      BadPaddingException, InvalidKeyException, InvalidKeySpecException {
    return ok(cypherService.encrypt(cypherDTO.getValue(), cypherDTO.getPublicKey()));
  }

  @PostMapping("/decrypt")
  public ResponseEntity<String> decrypt(@RequestBody CypherDTO cypherDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
      BadPaddingException, InvalidKeyException, InvalidKeySpecException {
    return ok(cypherService.decrypt(cypherDTO.getValue(), cypherDTO.getUserId()));
  }

}
