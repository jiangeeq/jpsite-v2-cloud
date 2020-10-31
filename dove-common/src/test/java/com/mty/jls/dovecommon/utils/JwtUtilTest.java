package com.mty.jls.dovecommon.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JwtUtilTest {
    String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7IGV4cDogMTYwNDA3MjM5OSwgdXNlcl9uYW1lOiBqaWFuZ3BlbmcsIGF1dGhvcml0aWVzOiBbICBhZG1pbiBdLCBqdGk6IGFmYTk1NzBjLTMwNzQtNGNlNi04MDljLTlkYjRjMzMyYzI1MSwgY2xpZW50X2lkOiBqYXZhYm95LCBzY29wZTogWyAgYWxsIF0sIGlhdDogMTYwNDA2ODc5OX0iLCJleHAiOjE2MDQwNzI2NjF9.2P8mR90b9W__juiwIKNAD_L7In0kYIpFG1-w6iRv2CY";
    String signingKey = "javaboy";

    String subject = "{" +
            " exp: 1604072399," +
            " user_name: jiangpeng," +
            " authorities: [" +
            "  admin" +
            " ]," +
            " jti: afa9570c-3074-4ce6-809c-9db4c332c251," +
            " client_id: javaboy," +
            " scope: [" +
            "  all" +
            " ]," +
            " iat: 1604068799" +
            "}";
    @Test
    public void testDecode(){
        Claims claims = JwtUtil.decode(jwtToken, signingKey);
        System.out.println(claims);
    }

    @Test
    public void testEncode(){
        String jwtToken = JwtUtil.encode(subject, signingKey, null);
        System.out.println(jwtToken);
    }

    @Test
    public void testEncodeAndDecode(){
        String jwtToken = JwtUtil.encode(subject, signingKey, Map.of("author","蒋老湿"));
        System.out.println(jwtToken);
        Claims claims = JwtUtil.decode(jwtToken, signingKey);
        System.out.println(claims);

    }

}
