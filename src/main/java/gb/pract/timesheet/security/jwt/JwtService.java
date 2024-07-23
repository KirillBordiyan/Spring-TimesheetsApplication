package gb.pract.timesheet.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    //секрет, нам потом понадобится, сгенерен тестом - HS256
    private static final String SECRET = "2361FECB0791BDC153E59305AD2389A3D6BB19EA3257EF0E0404079578DD1ECC";

    //время жизни аксесс токена, 30мин
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    /**
     * Метод геенрации самого токена, такого как мы знаем
     * @param userDetails
     * @return Строчное значение токена
     */
    public String generatedToken(UserDetails userDetails) { //метод генерации токена
        Map<String, String> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().toString());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    /**
     * Генерит сигнатуру токена
     * @return SecretKey - сгенерированный ключ
     */
    private SecretKey generateKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Из токена достает логин
     * @param jwt
     * @return login
     */
    public String extractUsername(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Из токена достает полезную нагрузку
     * @param jwt
     * @return claims
     */
    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * Метод проверки валидности токена
     * @param jwt
     * @return true / false
     */
    public boolean isTokenValid(String jwt){
        Claims claims = getClaims(jwt);
        return  claims.getExpiration().after(Date.from(Instant.now()));
    }
}






















