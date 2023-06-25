package com.example.cypherworker.control;

import com.example.cypherworker.boundary.CypherDTO;
import com.example.cypherworker.model.CypherRepository;
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
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class Decrypter implements Function<CypherDTO, String> {

    private CypherConfig config;

    @Override
    public String apply(CypherDTO cypherDTO) {
        log.info("[Cypher] Decryption of: [{}], for user: [{}] started.",cypherDTO.getValue(), cypherDTO.getUserId());

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(config.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        EncodedKeySpec privateKey = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(cypherDTO.getPrivateKey()));
        String decrypt;
        try {
            decrypt = decrypt(config.getAlgorithm(), cypherDTO.getValue(), keyFactory.generatePrivate(privateKey));
        } catch (NoSuchPaddingException | InvalidKeySpecException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        log.info("[Cypher] Decryption of: [{}], for user: [{}] ended.",cypherDTO.getValue(), cypherDTO.getUserId());

        return decrypt;
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
}
