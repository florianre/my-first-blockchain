package transaction;

import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
  public String transactionId; // this is also the hash of the transaction.
  public PublicKey sender; // senders address/public key.
  public PublicKey recipient; // Recipients address/public key.
  public float value;
  public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

  public ArrayList<TransactionInput> inputs = new ArrayList<>();
  public ArrayList<TransactionOutput> outputs = new ArrayList<>();

  private static int sequence = 0; // a rough count of how many transactions have been generated.

  public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
    this.sender = from;
    this.recipient = to;
    this.value = value;
    this.inputs = inputs;
  }

  public void generateSignature(PrivateKey privateKey) {
    String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
    signature = StringUtil.applyECDSASig(privateKey, data);
  }

  public boolean verifySignature() {
    String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
    return StringUtil.verifyECDSASig(sender, data, signature);
  }

  private String calculateHash() {
    sequence++;
    return StringUtil.applySha256(StringUtil.getStringFromKey(sender) +
        StringUtil.getStringFromKey(recipient) + value + sequence);
  }
}
