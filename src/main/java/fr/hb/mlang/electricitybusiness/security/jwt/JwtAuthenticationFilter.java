package fr.hb.mlang.electricitybusiness.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hb.mlang.electricitybusiness.security.ApplicationConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final ApplicationConfig appConfig;
  private final ObjectMapper objectMapper;

  public JwtAuthenticationFilter(
      JwtService jwtService, ApplicationConfig appConfig,
      ObjectMapper objectMapper
  ) {
    this.jwtService = jwtService;
    this.appConfig = appConfig;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ") || "OPTIONS".equalsIgnoreCase(
        request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = authHeader.substring("Bearer ".length());
    String userEmail;

    try {
      userEmail = jwtService.extractUserEmail(accessToken);

      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = appConfig.userDetailsService().loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(accessToken, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      throwJwtException(response, "Access token expired", "TOKEN_EXPIRED");
    } catch (Exception e) {
      throw new AuthenticationServiceException("Failed to authenticate user: " + e.getMessage());
    }
  }

  /**
   * Exception handler for JWT
   *
   * @param response Response to be sent back to the client.
   * @param message  Error message of the error
   * @param code     Code of the error
   * @throws IOException formatted for better readability.
   */
  private void throwJwtException(HttpServletResponse response, String message, String code)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), Map.of("error", message, "code", code));
  }
}
