package transaction;

import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Transaction {
  public String transactionId; // this is also the hash of the transaction.
  public PublicKey sender; // senders address/public key.
  public PublicKey recipient; // Recipients address/public key.
  public float value;
  public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

  public List<TransactionInput> inputs;
  public List<TransactionOutput> outputs = new ArrayList<>();

  private static int sequence = 0; // a rough count of how many transactions have been generated.

  public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
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

  //Returns true if new transaction could be created.
  public boolean processTransaction(Map<String, TransactionOutput> UTXOs, float minimumTransaction) {
    if (!verifySignature()) {
      System.out.println("#Transaction Signature failed to verify");
      return false;
    }

    //gather transaction inputs (Make sure they are unspent):
    for (TransactionInput i : inputs) {
      i.UTXO = UTXOs.get(i.transactionOutputId);
    }

    // check if transaction is valid:
    if (getInputsValue() < minimumTransaction) {
      System.out.println("#Transaction Inputs to small: " + getInputsValue());
      return false;
    }

    //generate transaction outputs:
    float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
    transactionId = calculateHash();
    outputs.add(new TransactionOutput(this.recipient, value, transactionId)); //send value to recipient
    outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender

    //add outputs to Unspent list
    for (TransactionOutput o : outputs) {
      UTXOs.put(o.id, o);
    }

    //remove transaction inputs from UTXO lists as spent:
    for (TransactionInput i : inputs) {
      if (i.UTXO == null) continue; //if Transaction can't be found skip it
      UTXOs.remove(i.UTXO.id);
    }

    return true;
  }

  public float getInputsValue() {
    float total = 0;
    for (TransactionInput i : inputs) {
      if (i.UTXO == null) continue; //if Transaction can't be found skip it
      total += i.UTXO.value;
    }
    return total;
  }

  public float getOutputsValue() {
    float total = 0;
    for (TransactionOutput o : outputs) {
      total += o.value;
    }
    return total;
  }

}
