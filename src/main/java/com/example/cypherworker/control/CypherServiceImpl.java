package com.example.cypherworker.control;

import com.example.cypherworker.model.Key;
import com.example.cypherworker.model.CypherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class CypherServiceImpl implements CypherService{

  private final CypherConfig config;
  private final CypherRepository cypherRepository;

  public String encrypt(String input, String publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

    log.info("[Cypher] Encryption of:[{}] started.", input);

    KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
    String encrypt = encrypt(config.getAlgorithm(), input, keyFactory.generatePublic(publicKeySpec));

    log.info("[Cypher] Encryption of: [{}] ended.", input);

    return encrypt;
  }

  public String decrypt(String cypherText, long userId) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

    log.info("[Cypher] Decryption of: [{}], for user: [{}] started.",cypherText, userId);

    KeyFactory keyFactory = KeyFactory.getInstance(config.getAlgorithm());
    EncodedKeySpec privateKey = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(cypherRepository.findByUserId(userId).getKey()));
    String decrypt = decrypt(config.getAlgorithm(), cypherText, keyFactory.generatePrivate(privateKey));

    log.info("[Cypher] Decryption of: [{}], for user: [{}] ended.",cypherText, userId);

    return decrypt;
  }

  private String encrypt(String algorithm, String input, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] cipherText = cipher.doFinal(input.getBytes());
    return Base64.getEncoder()
        .encodeToString(cipherText);
  }

  private String decrypt(String algorithm, String cypherText, PrivateKey privateKey) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] plainText = cipher.doFinal(Base64.getDecoder()
        .decode(cypherText));
    return new String(plainText);
  }

  public String generateKeys(long userId) throws NoSuchAlgorithmException {
    KeyPair pair = generateKeys(config.getKeySize());
    cypherRepository.save(Key.builder()
        .userId(userId)
        .key(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()))
        .build());
    return Base64.getEncoder()
        .encodeToString(pair.getPublic().getEncoded());
  }

  private KeyPair generateKeys(int n) throws NoSuchAlgorithmException {
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(config.getAlgorithm());
    keyGenerator.initialize(n);
    return keyGenerator.generateKeyPair();
  }

}
