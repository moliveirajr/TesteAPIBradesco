package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;

import static util.RSA.getPrivateKeyFromString;

public class Assinador {

    private static final String privateKeyHomolg = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDgtZ+AeFbb78N6\n" +
            "ZaxJO2A6yaM7bpRAo1qm0YwvA5OsUoBt9fK0JaJP7qyTs8mqg9RradhXINH+Z+xF\n" +
            "IUSBemzlICETeVs66WyOlet8I2Cv7VISmTex4gSB42atFbV4PPOfL8hERyC2IFWw\n" +
            "PGi6kjvNyvVrComonC9TnNRv4PFabSoe2Xgcvs6WE8yV1O/7H1bWvPyZPu28EIdH\n" +
            "viVxDuEf8kGc843pyAYsa5cO5HbEyEDs8PGqoer0t8jYL7TUzEAYrqYLqqPh60ga\n" +
            "orLfJjIBvcX30G53+pROxjNpARTsV69+r8eobDhPEmrt4PJ3tAJSxnYscuAzZfTX\n" +
            "e9PsOPNRAgMBAAECggEBAJCdMn5adAPtZQ9jWaJznPWYiP2Zp72Fo7SSTTaAhcwz\n" +
            "+EVMMJsxUSlCADy6SwH6/3z1TG6eR6cDuLGwvEbbxDMdH0xKIKVkTiHhaACnP/VY\n" +
            "tSYVBxvqyl3RPGYN/5DXIS6EcM1IPUwYuSzHY9sWe3hwssrlqDwj7+1OlAVAbfNz\n" +
            "8x1WaXOQodhl2IRtuL3FSCQDQfaGrcLCBXseElWOKiYL1XYSqg7Uk1zGAzDYXZe8\n" +
            "Zke/iaP6Lg73xBFYNlI8Z5t554S3bWHOmJtB9JgxF06FpubNLgDhL7UgHhHvRJTz\n" +
            "tTgYuRgfi4ichvwFVp8sw80dZEQWt+QszCAbFRJuWLUCgYEA8KM00YNJ5om49a1T\n" +
            "q4N3unHuHu/cbp2IUDbpRKpgLL9RWDrNjH3kWd58avU5at+MANrpuFuuwCnXKqZU\n" +
            "Rk/MryVHGR5WPXr0gYwAIEvS3c23DPM2RMl5sNLfGG+OeOAcYZn/Zus9iJGzCBPF\n" +
            "BdPyXnxhudsDiz7Y7rD2LUdgIVMCgYEA7w4Z0I8jQs7ElFvbfF8+D2G1ytESAto8\n" +
            "1BfSCOFvOU08cY6Hb1cHuDeQt2l03bRpO3/DP1UbCE9TEsMKrISz2q+Mdcq1bEOa\n" +
            "LjKODtnSB691hyDyb1+Ekb+B9ygzQeTQQ8nchwVxn/CAxx0E370QhXvdBZUAnxQe\n" +
            "/c4nFBneEEsCgYEAojcy/OWHJMzEjYD5PU6ToHD694n0S/EQGhraJzq/OIsD9/kz\n" +
            "5ThcfiSYNLX4rc3ioBTypx/O7qWF8a9MclWLAqqbghhMRIelWcsZrqvOi00Iz8cI\n" +
            "V1iGGygb0mopXdyd5UGdxTBhO7YUPSauk14sLXulmVdFzVFhhY5vdVh/OhcCgYBL\n" +
            "gJ2gpITThfDKltSUzJQSUZ8URsI0im8p4bghu+ngJEfR6d1WyhsTEOGxPCqAF4oh\n" +
            "E7I8H4ohLsSjKV0GrgcMK+PmFDcG0bcxniAGCr/uU3mAS2SmznlVezH2OQkvZ7Br\n" +
            "qKbxMkP36wMRYFX4wIMmVb+yEqSWmclMCj+HdsAPTwKBgQDSjZG66BZzWIfuwvUF\n" +
            "F6G9k20XIPqX/6m6ut4pFLwBZtTZImdwoIj+NUKswluFS74lm4y3mifMLIyEI6lq\n" +
            "unwRU2SQt+U/6V/C33tt2CrsmRbwSaPhRu+BYtyTt7zVXP+7mPOvUm63uqpTpF8I\n" +
            "I5+v7O0zlrOcNWTMhiFLlbYqYQ==\n" +
            "-----END PRIVATE KEY-----\n";

    private static final String privateKeyProd = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDAl3ru5P4qw68p\n" +
            "XLsmtn45s6TWBbM6TeSS8+Rr797pAelTruuJZMaswVMOUWWtd0wiT8aFCIQ3eIqG\n" +
            "v7aQOheSqjxVLuNiVsHW6DBlkf5tqRNZx1VFFQaPUjhQLnF8KB9YFJh5dYe0SccU\n" +
            "lfUEo3DB2m9Rlchn1zNlK2iguEycyDwbTJ21WK+OlTiFxLmdETu5tTfHcurSULlh\n" +
            "ThgnK5GDfz2S7lRVm4jptQn0XRdnfuvU3ITN/FHahfTu7nPhXPbKeGcicUCMEDlc\n" +
            "rmMgPWiDKk098Hv3umYhpRX8zEzaeJq0Jm++Mt0mqo9BUqe2nnEMcfhURmktuqUZ\n" +
            "UBztziOvAgMBAAECggEATvEIa8mjQQYq0yp2b/zXqnEvPKbjex+YdH/R/kg5N4sY\n" +
            "B7woQY30PbWUhMbqhXrj0yCd/8Oo5k/bgDYu79lP9kcfKc9pVtK2648K32dDQdHO\n" +
            "47roAcQHh1GtUXhn/fs4NdNjWE3LjSO8nXyGDRutSVhuzqM1nRLBt1NetJo2lQBs\n" +
            "UoHQQ4oFxYctnKvbxD3/2Al98tbHQXztQLzkVrBrp9ZowV8CmhidAgLL+Tp1JFCc\n" +
            "2SI7REYafo7gWgTOM2iAGG6WmZvs+zY5XPJ8aQ4AxC+84hM0R6vfFLpCbBv8pSTO\n" +
            "hI7dJQhbso88OOuGKkEYEVqlU+uaOH4s4g2C5ZISeQKBgQDfIMgNyYaSGkv8668U\n" +
            "ojf2XVqbGI1JzexBzTk6Gfchg9H0ZbWrdN+e2/IuuXC6jWuLueLfsEZICc7N/CEm\n" +
            "gKEpSGcMnNUatlgfxafWGJ4wia2arDxca07WSoCbb/NGxIyBWlU1Ff9GZqN/tkog\n" +
            "cBeUQCo7l9wyMZoNoMQbx00exQKBgQDc9wdSwVzUEbqeEzud3qISxDnZPf12Uino\n" +
            "olPF6p+UAnCfbuwY4/tORemuWpsbl/0VZNSldr6ssWWYvsbvKfMNTSuxmorADgxM\n" +
            "1IrIk5oOIJLc/23nbbHMk+9R4gMSXkwMdBjR8bW7G3KseC0aZjOWdDe9XAzDjI7I\n" +
            "FwY5ogof4wKBgE23klbTCc9b33nR8aKjOhDSxVteOuApHJBy1er53k1LJ9cTkiqN\n" +
            "Q9KGU52Ys74Wwg3iCjbVpDU1TVYL6tQJW5kQgewvuD5couc2JlZ5LqhDXDeSpFeg\n" +
            "fM5BFviNDtRoY3QurZ0W81pnJ621Ja6UvHHDw7IMHANTqY/znjr6uU7tAoGBALw5\n" +
            "KWHTGNIFMCSLWZNHHGREBssJRSlPICfsbtP+4mRgF7OX90HUojOw1jgRMmM0+v8I\n" +
            "XyN7nTzZ+CM/D5KW91VzUS7+6AZlcRDZVEc7hFVYZgIfC23CQVx0/72OevNgpANP\n" +
            "ufzMmvOBlBNaY4FDYdpdsUV6bdz9lHrZMhJR97HlAoGBAN5UlTu2k+XO0EHdBvSa\n" +
            "sxYImzTBuSBdlAFC+6fEOu/hU1QXvBV5X1Mv7HgUi5BctJfeh1QfStk09wHMII4E\n" +
            "4E5xl3F28aDpw+4OykLade/B58ZQzjZywKsxQkxPnwRG0Wu9TvT69/xoA1KmAk88\n" +
            "YsdAeALvKLEsI1stndR2cckg\n" +
            "-----END PRIVATE KEY-----\n";

    public static PrivateKey getPrivateKey(Boolean isProducao) {
        // TODO: 15/12/2021 colocar chaves no objeto ambiente

        PrivateKey keyBradesco = null;
        String privateKey=  (isProducao) ? privateKeyProd : privateKeyHomolg;
        try {
            keyBradesco = getPrivateKeyFromString (privateKey);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace ( );
        }

        return keyBradesco;
    }

    public static String Sign(Boolean isProducao, String message) {

        String signed = "";
        try {
            signed = RSA.sign (getPrivateKey (isProducao), message);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace ( );
        }
        return signed;
    }

}
