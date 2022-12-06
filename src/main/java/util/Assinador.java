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
    private static final String privateKeyProd = "-----BEGIN PRIVATE KEY-----\n"+
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJlJkpvbWxh7bD\n"+
            "cCmAyIZdzjH2/GEyEy/K2nrpuMPrhCfuYFmGdrdhNJCtjN+ex4wsi64tIZw//buJ\n"+
            "mngoJO9nS+GzlJ7xb96n9XxVBfLNiVdeWna5+uDz9mc9vzOmdAy9fnw0l5RtbnAu\n"+
            "HIDLoG+O7xM74pmHTMsqAEPVwiGHuq7xPLQEjKsHQou9YV52iv7c/NpAQXPTWVJ8\n"+
            "JOhV9LJK7mH1/Li2IqsdPtoCBovGqO6/gTict+w/KGFpnmNwi4a123PbT083uPUf\n"+
            "Msx4MtvZDzBR9nkQxXwojYgU5lpFW/nNCOFMNRDQPRi5DZvCcGMscPTYbWFQpcwQ\n"+
            "DBbaVwY7AgMBAAECggEAAYtwKdMe7rXuhZlF1PHOB2JkHNNneN3CxcteZvOxtKVP\n"+
            "m2w4Y2cxFb9nkgNJV2S9Qz4fkildLCePZbxXfV9DFZSN4M8CJMsWdrNsrEe3/TE3\n"+
            "O5OpfNiNgfEUCEB/bqAaO0pNcCdVNApFNWU/uDYjhLUT5A3JeJe/2s0jt+8D5Y/A\n"+
            "j/mwG5cozz4HlieUvLycNIlGaiH77oSynzoBBPhvWS4H6iWeqPQFkk9BdQmdSOeg\n"+
            "BK0XELXAk9QEt8QWnMtEL1MvD/hKjZuH9lmRGk/LC6JQMdOUWIPTKuxWNikPt16H\n"+
            "EcHkWo649wYjQhv2PV8uQzuYFmqkdKZnd+HPY2dXkQKBgQDPK0A1BLarQsFzE2GV\n"+
            "aPap7xUh6v1Z69u18cCxDqGpQlSKanwltQUoiaCx/BgVvDPvXauA88FrbCV5TQ5f\n"+
            "hf5uUYbJkXg+N7XfFAgaFo+zyKCtqIyOQ2O/o51sfeGVkx+pN13sxGuvE4QXoQjV\n"+
            "bcEYxSdsBEJ3ggJRgXjLfwDSWQKBgQD5GCIs/Y8KeLwCclmo4Nl8AQf2Nq5nVcl+\n"+
            "ZswUuujX4q+CbUGQkkCepfSGsEF9N/kqdL4YNT02gvtJr+ET4W/yrKFW0FZxZU8O\n"+
            "Ju8HRFQQZck7h8hwt+KPlc8BvyZ5pPWkjJ0illOq3RdEdbOlvXr41eAoRn2pOY4c\n"+
            "T1NVv7FCswKBgBOe9grPcnmP7gB3InPjKBF6Klc3CV293+ZAik56o0cDmDwvz6hG\n"+
            "l8F6H3Ub+i/8zjJqfbbN31DMQlRVvvm/GBTctdZbZ7cE0b3aCNZnsp6Fbupjn0RO\n"+
            "D6Fz3pIK7hWbhJEjREsLY2OGhqkyQELujS5KZexuDtmAv7V2fLlRQwOpAoGBAJPd\n"+
            "96uteVRGB5WMPB08HN633foSy8Ub79jRPSSxL7e4IWfTY7BmoWWMLnQl/TpQF+FU\n"+
            "G+RS9lt9tZ4wgfRDRylPwRa/5hC6iwtjoVE3BpAVXDeyixIelqeVzqTCzBw6CHf9\n"+
            "4Om/QzuSs1U4acRhjrZYgBBoiAAE4o97p4SnnppxAoGAJsEQart/rCRNKvf9mNUb\n"+
            "vGFc8bJeirKBLS7Y+SElP0UQCnGVJ44GMWGXLSyeYN0qBnKk9IxaqFrsngK3pTA+\n"+
            "FFmSmTtHRK0SeW2tMbNp+A6Nk+pgV2lm+qqg5c3/jAYlJVezhES9SYTSyEMz04Y1\n"+
            "oZqYiY0/o39tdO5KbDxrP8E=\n"+
            "-----END PRIVATE KEY-----\n";

    public static PrivateKey getPrivateKey(Boolean isProducao) {
        // TODO: 15/12/2021 colocar chaves no objeto ambiente

        PrivateKey keyBradesco = null;
        String privateKey = (isProducao) ? privateKeyProd : privateKeyHomolg;
        try {
            keyBradesco = getPrivateKeyFromString(privateKey);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        return keyBradesco;
    }

    public static String Sign(Boolean isProducao, String message) {

        String signed = "";
        try {
            signed = RSA.sign(getPrivateKey(isProducao), message);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signed;
    }

}
