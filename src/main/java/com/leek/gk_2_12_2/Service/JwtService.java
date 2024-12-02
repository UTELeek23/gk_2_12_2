package com.leek.gk_2_12_2.Service;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
@Service
public class JwtService {

    private static final String SECRET = "9B64AF7E42CF989FFDE716FC5D1AF"; // Đảm bảo đủ 256-bit

    public String generateToken(String subject) throws Exception {
        // Header
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();

        // Claims (Payload)
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer("spring-boot-app")
                .expirationTime(new Date(new Date().getTime() + 3600000)) // Token hết hạn sau 1 giờ
                .build();

        // Tạo JWS Object
        JWSObject jwsObject = new JWSObject(header, new com.nimbusds.jose.Payload(claims.toJSONObject()));

        // Ký JWS Object
        jwsObject.sign(new MACSigner(SECRET));

        return jwsObject.serialize(); // Trả về token dạng chuỗi
    }
    public boolean validateToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            return jwsObject.verify(new MACVerifier(SECRET));
        } catch (Exception e) {
            return false; // Token không hợp lệ
        }
    }

    public String getSubjectFromToken(String token) throws Exception {
        JWSObject jwsObject = JWSObject.parse(token);
        return jwsObject.getPayload().toJSONObject().get("sub").toString();
    }
}
