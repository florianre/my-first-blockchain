package block;

import util.StringUtil;

import java.util.Date;

public class Block {

  private final String hash;
  private final String previousHash;
  private final String data; // data will be a simple message.
  private final long timeStamp; //as number of milliseconds since 1/1/1970.

  //block.Block Constructor.
  public Block(String data,String previousHash ) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = calculateHash();
  }

  public String calculateHash() {
    return StringUtil.applySha256(data + previousHash + timeStamp);
  }

  public String getHash() {
    return hash;
  }

  public String getPreviousHash() {
    return previousHash;
  }
}
