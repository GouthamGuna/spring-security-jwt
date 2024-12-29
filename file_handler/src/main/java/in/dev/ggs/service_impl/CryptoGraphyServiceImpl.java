package in.dev.ggs.service_impl;

import in.dev.ggs.service.CryptoGraphyService;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class CryptoGraphyServiceImpl implements CryptoGraphyService {

    private static final String ALGORITHM = "AES";

    @Override
    public String CaesarCipherEncrypt(String plainText, String keyword) {
        StringBuilder ciphertext = new StringBuilder();
        keyword = keyword.toUpperCase();
        plainText = plainText.toUpperCase();

        for (int i = 0, j = 0; i < plainText.length(); i++) {
            char letter = plainText.charAt(i);

            if (letter < 'A' || letter > 'Z') {
                ciphertext.append(letter);
                continue;
            }

            char shift = keyword.charAt(j % keyword.length());
            char encryptedLetter = (char) ((letter + shift - 2 * 'A') % 26 + 'A');
            ciphertext.append(encryptedLetter);
            j++;
        }

        return ciphertext.toString();
    }

    @Override
    public String CaesarCipherDecrypt(String cipherText, String keyword) {
        StringBuilder plaintext = new StringBuilder();
        keyword = keyword.toUpperCase();
        cipherText = cipherText.toUpperCase();

        for (int i = 0, j = 0; i < cipherText.length(); i++) {
            char letter = cipherText.charAt(i);

            if (letter < 'A' || letter > 'Z') {
                plaintext.append(letter);
                continue;
            }

            char shift = keyword.charAt(j % keyword.length());
            char decryptedLetter = (char) ((letter - shift + 26) % 26 + 'A');
            plaintext.append(decryptedLetter);
            j++;
        }
        return plaintext.toString();
    }

    @Override
    public SecretKey generateKey() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(256); // or 128
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyGen.generateKey();
    }

    @Override
    public byte[] encrypt(byte[] data, SecretKey secretKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, SecretKey secretKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void encryptFile(File inputFile, File outputFile, SecretKey key) {
        byte[] fileData = null;
        try {
            fileData = Files.readAllBytes(inputFile.toPath());
            byte[] encryptedData = encrypt(fileData, key);
            Files.write(outputFile.toPath(), encryptedData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File decryptFile(File inputFile, SecretKey key) throws IOException {
        byte[] encryptedData = Files.readAllBytes(inputFile.toPath());
        byte[] decryptedData = decrypt(encryptedData, key);

        // Create a new file for decrypted data
        File outputFile = new File(inputFile.getParent(), "decrypted_" + inputFile.getName());
        Files.write(outputFile.toPath(), decryptedData);

        return outputFile; // Return the new decrypted file
    }

    @Override
    public void decryptFile(File inputFile, File outputFile, SecretKey key) {
        try {
            byte[] encryptedData = null;
            encryptedData = Files.readAllBytes(inputFile.toPath());
            byte[] decryptedData = decrypt(encryptedData, key);
            Files.write(outputFile.toPath(), decryptedData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
