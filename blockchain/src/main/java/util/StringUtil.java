package util;

import transaction.Transaction;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class StringUtil {

  public static String applySha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      //Applies sha256 to our input,
      byte[] hash = digest.digest(input.getBytes("UTF-8"));
      StringBuilder hexString = new StringBuilder(); // This will contain hash as hexidecimal
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
    Signature dsa;
    byte[] output;
    try {
      dsa = Signature.getInstance("ECDSA", "BC");
      dsa.initSign(privateKey);
      byte[] strByte = input.getBytes();
      dsa.update(strByte);
      output = dsa.sign();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return output;
  }

  public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
    try {
      Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
      ecdsaVerify.initVerify(publicKey);
      ecdsaVerify.update(data.getBytes());
      return ecdsaVerify.verify(signature);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getStringFromKey(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  //Tacks in array of transactions and returns a merkle root.
  public static String getMerkleRoot(List<Transaction> transactions) {
    int count = transactions.size();
    List<String> previousTreeLayer = new ArrayList<>();
    for (Transaction transaction : transactions) {
      previousTreeLayer.add(transaction.transactionId);
    }
    List<String> treeLayer = previousTreeLayer;
    while (count > 1) {
      treeLayer = new ArrayList<>();
      for (int i = 1; i < previousTreeLayer.size(); i++) {
        treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
      }
      count = treeLayer.size();
      previousTreeLayer = treeLayer;
    }
    return treeLayer.size() == 1 ? treeLayer.get(0) : "";
  }
}
