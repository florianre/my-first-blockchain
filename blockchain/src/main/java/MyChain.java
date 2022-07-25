import block.Block;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MyChain {

  public static final String FIRST_HASH = "0";
  public static int DIFFICULTY = 5;

  public static void main(String[] args) {
    List<String> data = new ArrayList<>();
    data.add("Hi im the first block");
    data.add("Yo im the second block");
    data.add("Hey im the third block");
    List<Block> blockchain = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      String prevHash = i == 0 ? FIRST_HASH : blockchain.get(i - 1).getHash();
      blockchain.add(new Block(data.get(i), prevHash));
      System.out.printf("Trying to Mine block %d... %n", i + 1);
      blockchain.get(i).mineBlock(DIFFICULTY);
    }

    System.out.println("\nBlockchain is Valid: " + Validator.validate(blockchain, DIFFICULTY));

    String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    System.out.println(blockchainJson);
  }
}
