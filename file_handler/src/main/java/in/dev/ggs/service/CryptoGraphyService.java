package in.dev.ggs.service;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;

public interface CryptoGraphyService {

    String CaesarCipherEncrypt(String plainText, String keyword);

    String CaesarCipherDecrypt(String cipherText, String keyword);

    SecretKey generateKey();

    byte[] encrypt(byte[] data, SecretKey secretKey);

    byte[] decrypt(byte[] data, SecretKey secretKey);

    void encryptFile(File inputFile, File outputFile, SecretKey key);

    File decryptFile(File inputFile, SecretKey key) throws IOException;

    void decryptFile(File inputFile, File outputFile, SecretKey key);
}
