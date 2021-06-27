package com.yapily.marvelcharacters;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;

public class MD5Utils {

    public static String createDigest(String ts, String privateKey, String publicKey) {
        // Marvel expect the md5hash from combining ts+prk+puk in UTF-8
        final String stringToDigest = ts.concat(privateKey).concat(publicKey);
        byte[] messageToDigest = stringToDigest.getBytes(StandardCharsets.UTF_8);
        // The digest is generated with 128 bits -> 16 bytes -> 2 hex chars per byte = 32 hex char array
        return DigestUtils.md5DigestAsHex(messageToDigest);
    }

    public static AbstractMap.SimpleEntry<Long, String> createTimestampsAndMd5Hash(String privateKey, String publicKey){
        final long currentEpochTime = System.currentTimeMillis(); // TS
        String md5Hash = createDigest(String.valueOf(currentEpochTime), privateKey, publicKey);
        return new AbstractMap.SimpleEntry<>(currentEpochTime, md5Hash);
    }
}