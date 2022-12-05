import java.util.ArrayList;

public class Bot {
  public static Integer computeNextMove(ArrayList<Integer[]> record) {
    if(!record.isEmpty()) {
      Integer[] roundResult = record.get(record.size()-1);
      return (roundResult[3] + 1)%5;
    }
    else {
      return 0;
    }
  }
}
