package com.socialnetwork.weconnect.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.exception.ErrorCode;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	String secretKey;
	@Value("${application.security.jwt.expiration}")
	long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	long refreshExpiration;

	// Hàm để trích xuất username từ JWT token
	public String extractUsername(String token, HttpServletResponse response) {
		return extractClaim(token, Claims::getSubject, response);
	}

	// Hàm để trích xuất thông tin từ JWT token sử dụng claimsResolver
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, HttpServletResponse response) {
		final Claims claims = extractAllClaims(token, response);
		return claimsResolver.apply(claims);
	}

	// Hàm để tạo ra JWT token từ UserDetails
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	// Hàm để tạo ra JWT token từ UserDetails và thêm các extra claims
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	// Hàm để tạo ra Refresh token từ UserDetails
	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	// Hàm để kiểm tra tính hợp lệ của JWT token
	public boolean isTokenValid(String token, UserDetails userDetails, HttpServletResponse response) {
		final String username = extractUsername(token, response);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, response);
	}

	// Hàm để kiểm tra xem JWT token đã hết hạn hay chưa
	private boolean isTokenExpired(String token, HttpServletResponse response) {
		return extractExpiration(token, response).before(new Date());
	}

	// Hàm để trích xuất ngày hết hạn từ JWT token
	private Date extractExpiration(String token, HttpServletResponse response) {
		return extractClaim(token, Claims::getExpiration, response);
	}

	// Hàm để trích xuất toàn bộ claims từ JWT token
	private Claims extractAllClaims(String token, HttpServletResponse response) {
		Claims claims = Jwts.claims();
		try {

			return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			response.setStatus(ErrorCode.INVALID_SIGNATURE.getCode());
		}
		return claims;
	}



	// Hàm để lấy key để ký JWT token
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}