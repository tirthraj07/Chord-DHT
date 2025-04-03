package com.tirthraj.dht.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final int M = 160; // The bit size for Chord ring (SHA-1)
    private static final BigInteger TWO_POWER_M = BigInteger.valueOf(2).pow(M);


    public static BigInteger hash(String input){
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger hashValue = new BigInteger(1, digest);
            return hashValue.mod(TWO_POWER_M);   // Ensuring it's within [0, 2^m - 1]
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Hashing algorithm not found: " + HASH_ALGORITHM, e);
        }
    }

    public static BigInteger hashNode(String ip, int port){
        return hash(ip + ":" + port);
    }

    public static BigInteger hashKey(String key){
        return hash(key);
    }

}
