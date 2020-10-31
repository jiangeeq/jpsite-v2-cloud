package com.mty.jls.dovecommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    public static String encode(String subject, String signingKey, Map<String, Object> claims) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                // 默认过期时间10分钟
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(signingKey));

        return jwtBuilder.compact();
    }

    public static Claims decode(String jwtToken, String signingKey) {
        // 提取出来的token字符串转换为一个Claims对象，再从Claims对象中提取出当前用户名和用户角色
        Claims claims = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(signingKey))
                .parseClaimsJws(jwtToken.replace("Bearer", "")).getBody();
        return claims;
    }
}
