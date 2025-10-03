package fr.hb.mlang.electricitybusiness.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import fr.hb.mlang.electricitybusiness.config.AppProperties;
import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.springframework.stereotype.Service;

@Service
public class JwtKeyManager {

  private final AppProperties.Jwt jwtProps;
  private Algorithm algorithm;

  public JwtKeyManager(AppProperties appProps) {
    this.jwtProps = appProps.jwt();
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  @PostConstruct
  public void initialize() throws Exception {
    Path directory = Paths.get(jwtProps.keysLocation());
    Files.createDirectories(directory);
    KeyPair keyPair;

    Path publicFile = directory.resolve("public.key");
    Path privateFile = directory.resolve("private.key");

    if (Files.notExists(publicFile) || Files.notExists(privateFile)) {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048); // Good default value
      keyPair = generator.generateKeyPair();

      Files.write(publicFile, keyPair.getPublic().getEncoded());
      Files.write(privateFile, keyPair.getPrivate().getEncoded());
    } else {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      keyPair = new KeyPair(
          keyFactory.generatePublic(new X509EncodedKeySpec(Files.readAllBytes(publicFile))),
          keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privateFile)))
      );
    }

    this.algorithm = Algorithm.RSA256(
        (RSAPublicKey) keyPair.getPublic(),
        (RSAPrivateKey) keyPair.getPrivate()
    );
  }
}
