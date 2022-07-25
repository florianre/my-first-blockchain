import block.Block;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MyChain {


  public static final String FIRST_HASH = "0";

  public static void main(String[] args) {
    List<String> data = new ArrayList<>();
    data.add("Hi im the first block");
    data.add("Yo im the second block");
    data.add("Hey im the third block");
    List<Block> blockChain = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      String prevHash = i == 0 ? FIRST_HASH : blockChain.get(i - 1).getHash();
      blockChain.add(new Block(data.get(i), prevHash));
    }

    String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
    System.out.println(blockchainJson);
  }
}
