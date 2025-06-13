package com.siven.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    private static final Logger logger = LoggerFactory.getLogger(AESUtil.class);
    private static final String SECRET_KEY = "0K31W7K7bmXQ0k1XwbuqW51VT6KJ9F+0srTDBh4HFAY="; // Must be Base64-encoded 256-bit key

    private static byte[] getDecodedKey() {
        if (SECRET_KEY == null) {
            throw new IllegalStateException("APP_AES_KEY environment variable not set.");
        }
        byte[] key = Base64.getDecoder().decode(SECRET_KEY);
        if (key.length != 32) {
            throw new IllegalArgumentException("AES key must be 256 bits (32 bytes after Base64 decode).");
        }
        return key;
    }

    public static String encrypt(String value) {
        try {
            byte[] key = getDecodedKey();
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            byte[] iv = new byte[12];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);
            return Base64.getEncoder().encodeToString(byteBuffer.array());

        } catch (Exception ex) {
            logger.error("Encryption failed", ex);
            throw new RuntimeException("AES encryption error", ex);
        }
    }

    public static String decrypt(String encrypted) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            if (decoded.length < 12) {
                throw new IllegalArgumentException("Invalid encrypted data: too short");
            }

            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[12];
            byteBuffer.get(iv);

            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            byte[] key = getDecodedKey();
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] original = cipher.doFinal(cipherText);
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception ex) {
            logger.error("Decryption failed", ex);
            throw new RuntimeException("AES decryption error", ex);
        }
    }
}
