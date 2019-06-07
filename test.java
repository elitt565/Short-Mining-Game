/**
 * Created by elitt on 5/23/2019.
 * Program Description:
 * Resources Used:
 * Worked With:
 */
public class test {
  public static MiningGameGUI display;
  public static void main(String[] args) {
    display = new MiningGameGUI(15, 15, 15, 15);
    int[][] arr = display.getIntArr();
//    for(int i = 0; i < arr.length; i++) {
//      for(int x = 0; x < arr[i].length; x++) {
//        System.out.print(arr[i][x] + " ");
//      }
//      System.out.println();
//    }
  }
  public static MiningGameGUI getObject() {
    return display;
  }
}