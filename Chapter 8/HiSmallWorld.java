//Listing 8.1
import com.sun.kjava.*;

public class HiSmallWorld extends Spotlet {
  private Graphics g = Graphics.getGraphics();

  public static void main(String[] args) {
    HiSmallWorld app = new HiSmallWorld();
  }

  public HiSmallWorld() {
    g.clearScreen();
    g.drawString("Hi Small World!", 45, 80);
  }
}