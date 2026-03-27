package com.vpapro.library_api.security;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class MyArgon2PasswordEncoder implements PasswordEncoder {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 4;
    private static final int MEMORY = 1 << 15;
    private static final int ITERATIONS = 3;

    private final BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(SALT_LENGTH);

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = saltGenerator.generateKey();
        byte[] hash = hash(rawPassword, salt);

        byte[] combined = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hash, 0, combined, salt.length, hash.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    private byte[] hash(CharSequence rawPassword, byte[] salt) {
        byte[] hash = new byte[HASH_LENGTH];

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withMemoryAsKB(MEMORY)
                .withIterations(ITERATIONS)
                .withParallelism(PARALLELISM)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);
        generator.generateBytes(rawPassword.toString().getBytes(StandardCharsets.UTF_8), hash, 0, hash.length);

        return hash;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {
        byte[] decoded = Base64.getDecoder().decode(storedPassword);

        byte[] salt = Arrays.copyOfRange(decoded, 0, SALT_LENGTH);
        byte[] hash = Arrays.copyOfRange(decoded, SALT_LENGTH, decoded.length);

        byte[] candidate = hash(rawPassword, salt);

        return Arrays.equals(hash, candidate);
    }
}