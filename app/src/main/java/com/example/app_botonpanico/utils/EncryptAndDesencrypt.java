package com.example.app_botonpanico.utils;

import java.io.Serializable;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptAndDesencrypt implements Serializable {

    //private static final long serialVersionUID = 548685543976730876L;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String key = "08wR?!5!S6_WO&-v$f#0RUdrEfRoc1Th";
    private static final String salt = "huw1zOQ@a*&t8tr83e$16hiy#k+v1!0cr";
    private static SecretKey secretKeyTemp;

    public EncryptAndDesencrypt() {
        SecretKeyFactory secretKeyFactory;
        KeySpec keySpec;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            keySpec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
            secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing secret key", e);
        }
    }

    public String encrypt(String input) {
        if (secretKeyTemp == null) {
            throw new IllegalStateException("Secret key is not initialized.");
        }
        byte[] iv = new byte[16]; // consider using a secure random IV in real applications
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar", e);
        }
    }

    public String decrypt(String input) {
        if (secretKeyTemp == null) {
            throw new IllegalStateException("Secret key is not initialized.");
        }
        byte[] iv = new byte[16]; // consider using a secure random IV in real applications
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error al descifrar", e);
        }
    }

}


