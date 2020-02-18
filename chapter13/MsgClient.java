package com.ctimn;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MsgClient extends MIDlet implements CommandListener {

    private Form outputForm;
    private Display display;
    private List menu;
    private Command okCmd = new Command("OK", Command.OK, 1);
    private Command exitCmd = new Command("Exit", Command.EXIT, 1);

    private static final String[] choices = {
        "1 HTTP Example",
        "2 Socket Message",
        "3 Datagram Message"
    };

    protected void startApp() throws MIDletStateChangeException {
        display = Display.getDisplay(this);
		outputForm = new Form("Server Messages");
		menu = new List("Select:", List.IMPLICIT, choices, null);
		menu.addCommand(okCmd);
		outputForm.addCommand(okCmd);
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
    }

    public void commandAction(Command cmd, Displayable displayable) {
        if (cmd == exitCmd){
			handleExit();
		} else if ((displayable == menu) && (cmd == okCmd)) {
			handleOK(((List)displayable).getSelectedIndex());
		} else {
		      display.setCurrent(menu);
        }
   	 }

	private void handleExit(){
		try {
			notifyDestroyed();
			destroyApp(true);
		} catch (MIDletStateChangeException x) {
			x.printStackTrace();
		}
	}

	private void handleOK(int idx){
	   display.setCurrent(outputForm);
	   switch (idx) {
		case 0:
		  getHttpMessage();
		  break;
		case 1:
		  socketMessage();
		  break;
		case 2:
		  datagramMessage();
		  break;
	  }
	}

	private void getHttpMessage(){
		int c = 0;
		String dataIn = null;
		StringItem item = new StringItem("Reading from URL", "");
		outputForm.append(item);
		try {
			ContentConnection connection = (ContentConnection)
				Connector.open("http://www.catapult-technologies.com/ctimain.htm", Connector.READ);
			DataInputStream is = connection.openDataInputStream();
			try {
				System.out.println("encoding: "+ connection.getEncoding());
				System.out.println("length: "+ connection.getLength());
				System.out.println("type: "+ connection.getType());
				StringBuffer sb = new StringBuffer("");
				for (int ccnt=0; ccnt < connection.getLength(); ccnt++){
					c = is.read();
					sb.append((char)c);
				}
				dataIn = sb.toString();
		    	item = new StringItem("Title: ", getTitle(dataIn));
		        outputForm.append(item);
			} finally {
				is.close();
			}

		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
	}

	private String getTitle(String data){
		String titleTag = "<TITLE>";
		int idx1 = data.indexOf(titleTag);
		int idx2 = data.indexOf("</TITLE>");
		return data.substring(idx1 + titleTag.length(), idx2);
	}

	private void socketMessage(){
		StringBuffer sb = new StringBuffer("");
		String dataIn = null;
		String dataOut = null;
		int c = 0;
		try {
			StreamConnection connection = (StreamConnection)
				Connector.open("socket://localhost:4444", Connector.READ_WRITE);
			DataOutputStream os = connection.openDataOutputStream();
			DataInputStream is = connection.openDataInputStream();
			connection.close();
			try {
				dataOut = "Message from the client.";
				os.writeUTF(dataOut);
				os.flush();
				dataIn = is.readUTF();
				System.out.println(dataIn);
				StringItem si = new StringItem("Msg: ", "'"+dataIn+"'");
        		outputForm.append(si);
			} finally {
				is.close();
				os.close();
			}
		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
	}

    private void datagramMessage() {
		String msg = null;
		try {
			DatagramConnection connection =
					(DatagramConnection)Connector.open("datagram://localhost:5555", Connector.READ_WRITE);
			Datagram datagram = null;
			try {
				datagram = connection.newDatagram(100);
				sendDatagram(connection, datagram, "Message from the Client");
 				datagram = connection.newDatagram(100);
				msg = receiveDatagram(connection, datagram);
			} finally {
				connection.close();
			}
		} catch (IOException x) {
			x.printStackTrace();
		}
    	StringItem item = new StringItem("Msg: ", msg);
        outputForm.append(item);
    }

	private String receiveDatagram(DatagramConnection connection, Datagram datagram)
			throws IOException{
		connection.receive(datagram);
		System.out.println("Address="+datagram.getAddress());
		System.out.println("Length="+datagram.getLength());
		System.out.println("Offset="+datagram.getOffset());
		byte[] byteData = datagram.getData();
		byte b = 0;
		StringBuffer sb = new StringBuffer("");
		for (int ccnt=0; ccnt < byteData.length; ccnt++){
			if (byteData[ccnt] > 0){
				sb.append((char)byteData[ccnt]);
			} else {
				break;
			}
		}
		String data = sb.toString();
		System.out.println("Data="+data);
		return data;
	}

	private void sendDatagram(DatagramConnection connection, Datagram datagram, String msg)
  			throws IOException{
		byte[] data = msg.getBytes();
		datagram.setData(data, 0, data.length);
		connection.send(datagram);
	}
}