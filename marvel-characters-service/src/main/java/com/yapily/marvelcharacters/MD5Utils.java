package com.yapily.marvelcharacters;

import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Utils {

    public static String digest(String ts, String privateKey, String publicKey) {
        // md5(ts+prk+puk)
        final String stringToDigest = ts.concat(privateKey).concat(publicKey);
        byte[] messageToDigest = stringToDigest.getBytes(StandardCharsets.UTF_8);
        byte[] digest = DigestUtils.md5Digest(messageToDigest);
        return HexUtils.toHexString(digest);
    }
}