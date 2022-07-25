import block.Block;

import java.util.List;

public class Validator {

  public Boolean validate(List<Block> blockChain) {
    Block current;
    Block previous;

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
    }
    return true;
  }
}
