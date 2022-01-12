package util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSA {

    private static String getKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader (new FileReader (filename));
        String line;
        while ((line = br.readLine ( )) != null) {
            strKeyPEM += line + "\n";
        }
        br.close ( );
        return strKeyPEM;
    }

    public static RSAPrivateKey getPrivateKeyFile(String filename) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getKey (filename);
        return getPrivateKeyFromString (privateKeyPEM);
    }

    public static RSAPrivateKey getPrivateKeyFromString(String key) throws IOException, GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace ("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace ("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64 (privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance ("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec (encoded);
        return (RSAPrivateKey) kf.generatePrivate (keySpec);
    }

    public static RSAPublicKey getPublicKey(String filename) throws IOException, GeneralSecurityException {
        String publicKeyPEM = getKey (filename);
        return getPublicKeyFromString (publicKeyPEM);
    }

    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace ("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace ("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64 (publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance ("RSA");
        return (RSAPublicKey) kf.generatePublic (new PKCS8EncodedKeySpec (encoded));
    }

    public static String sign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance ("SHA256withRSA");
//        Signature sign = Signature.getInstance ("SHA512withRSA");
        sign.initSign (privateKey);
        sign.update (message.getBytes (StandardCharsets.UTF_8));
        return new String (Base64.encodeBase64 (sign.sign ( )), StandardCharsets.UTF_8);
    }


    public static boolean verify(PublicKey publicKey, String message, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance ("SHA256withRSA");
        sign.initVerify (publicKey);
        sign.update (message.getBytes (StandardCharsets.UTF_8));
        return sign.verify (Base64.decodeBase64 (signature.getBytes (StandardCharsets.UTF_8)));
    }

    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA");
        cipher.init (Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String (cipher.doFinal (rawText.getBytes (StandardCharsets.UTF_8)));
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance ("RSA");
        cipher.init (Cipher.DECRYPT_MODE, privateKey);
        return new String (cipher.doFinal (Base64.decodeBase64 (cipherText)), StandardCharsets.UTF_8);
    }

}
