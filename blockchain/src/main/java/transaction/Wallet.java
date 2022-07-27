package transaction;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
  private PrivateKey privateKey;
  private PublicKey publicKey;

  public Wallet() {
    KeyPair keyPair = generateKeyPair();
    privateKey = keyPair.getPrivate();
    publicKey = keyPair.getPublic();
  }

  public KeyPair generateKeyPair() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
      // Initialize the key generator and generate a KeyPair
      keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
      return keyGen.generateKeyPair();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }
}
