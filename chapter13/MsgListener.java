package com.ctimn;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MsgListener extends MIDlet implements CommandListener {

    private Form outputForm;
    private Display display;
    private List menu;
    private Command okCmd = new Command("OK", Command.OK, 1);
    private Command exitCmd = new Command("Exit", Command.EXIT, 1);
    private SocketListener socketListener;
    private DatagramListener datagramListener;

    private static final String[] choices = {
        "1 Socket Listener",
        "2 Datagram Listener"
    };

    protected void startApp() throws MIDletStateChangeException {
        display = Display.getDisplay(this);
		outputForm = new Form("Messages");
		menu = new List("Select:", List.IMPLICIT, choices, null);
		outputForm.addCommand(okCmd);
		menu.addCommand(okCmd);
		outputForm.addCommand(exitCmd);
		menu.addCommand(exitCmd);
		outputForm.setCommandListener(this);
		menu.setCommandListener(this);
        display.setCurrent(menu);
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unconditional)
    				throws MIDletStateChangeException {
		System.out.println("Destroy App.");
		if (socketListener != null){
			socketListener.shutdown();
		}
    }

    public void commandAction(Command cmd, Displayable activeDisplay) {
		if (cmd == exitCmd) {
			handleExit();
		} else if ((activeDisplay == menu) && (cmd == okCmd)) {
			handleOK(((List)activeDisplay).getSelectedIndex());
			return;
		}
		display.setCurrent(menu);
   	 }

	 private void handleExit(){
		try {
		System.out.println("exit.");
			destroyApp(true);
			notifyDestroyed();
		} catch (MIDletStateChangeException x){
			x.printStackTrace();
		}
	 }

   	 private void handleOK(int idx){
		display.setCurrent(outputForm);
		switch (idx) {
			case 0:
			  socketListener();
			  break;
			case 1:
			  datagramListener();
			  break;
		}
	 }

	private void socketListener(){
		if (socketListener == null){
			socketListener = new SocketListener(outputForm);
			socketListener.start();
		}
	}

    private void datagramListener() {
		if (datagramListener == null){
			datagramListener = new DatagramListener(outputForm);
			datagramListener.start();
		}
    }
}