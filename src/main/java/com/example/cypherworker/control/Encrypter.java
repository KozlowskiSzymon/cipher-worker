package com.example.cypherworker.control;

import com.example.cypherworker.boundary.CypherDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class Encrypter implements Function<CypherDTO, String> {

    private CypherConfig config;

    @Override
    public String apply(CypherDTO cypherDTO) {
        log.info("[Cypher] Encryption of:[{}] started.", cypherDTO.getValue());

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cypherDTO.getPublicKey()));
        String encrypt;
        try {
            encrypt = encrypt(config.getAlgorithm(), cypherDTO.getValue(), keyFactory.generatePublic(publicKeySpec));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        log.info("[Cypher] Encryption of: [{}] ended.", cypherDTO.getValue());

        return encrypt;
    }

    private String encrypt(String algorithm, String input, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                     .encodeToString(cipherText);
    }

}
