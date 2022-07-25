import block.Block;

import java.util.List;

public class Validator {

  public static Boolean validate(List<Block> blockChain, int difficulty) {
    Block current;
    Block previous;
    String hashTarget = new String(new char[difficulty]).replace('\0', '0');

    for (int i = 1; i < blockChain.size(); i++) {
      current = blockChain.get(i);
      previous = blockChain.get(i - 1);
      if (!current.getHash().equals(current.calculateHash())) {
        System.out.println("Current Hashes not equal");
        return false;
      }

      if (!current.getPreviousHash().equals(previous.getHash())) {
        System.out.println("Previous Hashes not equal");
        return false;
      }

      //check if hash is solved
      if(!current.getHash().substring(0, difficulty).equals(hashTarget)) {
        System.out.println("This block hasn't been mined");
        return false;
      }
    }
    return true;
  }
}
