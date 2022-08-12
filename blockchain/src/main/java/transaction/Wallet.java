package transaction;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {
  private PrivateKey privateKey;
  private PublicKey publicKey;
  public Map<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.

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

  //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
  public float getBalance(Map<String, TransactionOutput> UTXOs) {
    float total = 0;
    for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
      TransactionOutput UTXO = item.getValue();
      if (UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
        this.UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
        total += UTXO.value;
      }
    }
    return total;
  }

  //Generates and returns a new transaction from this wallet.
  public Transaction sendFunds(PublicKey _recipient, float value) {
    if (getBalance(UTXOs) < value) { //gather balance and check funds.
      System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
      return null;
    }
    List<TransactionInput> inputs = new ArrayList<>();

    float total = 0;
    for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
      TransactionOutput UTXO = item.getValue();
      total += UTXO.value;
      inputs.add(new TransactionInput(UTXO.id));
      if (total > value) break;
    }

    Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
    newTransaction.generateSignature(privateKey);

    for (TransactionInput input : inputs) {
      UTXOs.remove(input.transactionOutputId);
    }
    return newTransaction;
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }
}
