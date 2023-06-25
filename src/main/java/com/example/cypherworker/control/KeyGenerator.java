package com.example.cypherworker.control;

import com.example.cypherworker.boundary.CypherDTO;
import com.example.cypherworker.model.CypherRepository;
import com.example.cypherworker.model.Key;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class KeyGenerator implements Function<Long, CypherDTO> {

    private CypherConfig config;

    @Override
    public CypherDTO apply(Long userId) {
        log.info("[Cypher] Generating keys for user with id : [{}], with: [{}] [{}].", userId, config.getAlgorithm(), config.getKeySize());
        KeyPair pair;
        try {
            pair = generateKeys(config.getKeySize());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        log.info("[Cypher] Finished generating keys for user with id : [{}].", userId);
        return CypherDTO.builder()
                        .userId(userId)
                        .privateKey(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()))
                        .publicKey(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()))
                        .build();
    }

    private KeyPair generateKeys(int n) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(config.getAlgorithm());
        keyGenerator.initialize(n);
        return keyGenerator.generateKeyPair();
    }
}
