import block.Block;
import com.google.gson.GsonBuilder;
import transaction.TransactionOutput;
import transaction.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChain {

  public static List<Block> blockchain = new ArrayList<>();
  public static Map<String, TransactionOutput> UTXOs = new HashMap<>(); //list of all unspent transactions.
  public static Wallet walletA;
  public static Wallet walletB;

  public static final String FIRST_HASH = "0";
  public static final int DIFFICULTY = 5;

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
