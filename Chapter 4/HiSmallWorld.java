//Listing 4.1

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;

public class HiSmallWorld extends MIDlet {

    private TextBox textbox;

    public HiSmallWorld() {
        textbox = new TextBox("", "Hi Small World!", 20, 0);
    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(textbox);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
