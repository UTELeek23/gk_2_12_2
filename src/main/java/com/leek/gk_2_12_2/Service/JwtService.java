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

    private static final String SECRET = "4118550d15b6a3df81ebe709a033fa6f2cd15a010bfa40b8db78e539986d60cc44358bb5c0513008839be2d5c5951914aefd9e8a3ee3d148eb3d03cb2c29abad2e3a60aa727b3416d8e5f55384f6a28f0ac31a4db51ffe8e509d9a84aeab9e5442c16ac9c6fd5654fb43d0471c7989bf5f52e0fa83dc0e4ece3b74258e425d61600582f3a64599c8b8041b77207412eec3761c8168374d6d96f1b3db3d2419d90515e8fcc1078c198f2a73b05dbaed091fb003f174019df0f0f12f0def7b4e0dd3e2e2634bc8b584b0debefd2656df882a97eb4aa88a08fe241f2d982cd7e6f8634deb0a8a592401e72db298edd16313032e25e304bff0dc677b6ac878c313bd"; // Đảm bảo đủ 256-bit

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
