package block;

import transaction.Transaction;
import transaction.TransactionOutput;
import util.StringUtil;

import java.util.*;

public class Block {

  private String hash;
  private final String previousHash;
  public String merkleRoot;
  public List<Transaction> transactions = new ArrayList<>(); //our data will be a simple message.
  private final long timeStamp;
  private int nonce;

  public Block(String previousHash) {
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = calculateHash(); //Making sure we do this after we set the other values.
  }

  public String calculateHash() {
    return StringUtil.applySha256(previousHash + timeStamp + nonce + merkleRoot);
  }

  public void mineBlock(int difficulty) {
    merkleRoot = StringUtil.getMerkleRoot(transactions);
    String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
    while(!hash.substring( 0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }
    System.out.println("Block Mined!!! : " + hash);
  }

  public boolean addTransaction(Transaction transaction, Map<String, TransactionOutput> UTXOs) {
    //process transaction and check if valid, unless block is genesis block then ignore.
    if (transaction == null) return false;
    if (!Objects.equals(previousHash, "0") && !transaction.processTransaction(UTXOs, 1)) {
      System.out.println("Transaction failed to process. Discarded.");
      return false;
    }

    transactions.add(transaction);
    System.out.println("Transaction Successfully added to Block");
    return true;
  }

  public String getHash() {
    return hash;
  }

  public String getPreviousHash() {
    return previousHash;
  }
}
