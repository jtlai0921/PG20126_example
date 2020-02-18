package com.ctimn;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class SocketListener extends Thread{

	private Form outputForm;
	private boolean shutdownFlag = false;
	private StreamConnectionNotifier notifier;

	public SocketListener(Form outputForm){
		this.outputForm = outputForm;
	}

	private StreamConnectionNotifier createNotifier() throws IOException {
		return (StreamConnectionNotifier)
				  Connector.open("serversocket://:4444", Connector.READ_WRITE, true);
	}

	public void run(){
		String dataIn = null;
		String dataOut = null;
		int counter = 1;
		StringItem item = new StringItem("Listening to Socket", "");
		outputForm.append(item);
		StreamConnection connection = null;
		try {
			notifier = createNotifier();
			while (true) {
				try {
					connection = notifier.acceptAndOpen();
				} catch (InterruptedIOException x){
					if (shutdownFlag){
						return;
					} else {
						notifier = createNotifier();
					}
				}
				DataInputStream is = connection.openDataInputStream();
				DataOutputStream os = connection.openDataOutputStream();
				connection.close();
				try {
					dataIn = is.readUTF();
					System.out.println(dataIn);
					item = new StringItem("Msg: ", "'"+dataIn+"'");
					outputForm.append(item);
					dataOut = "Message " + counter + " from the server.";
					counter++;
					os.writeUTF(dataOut);
					os.flush();
				} finally {
					os.close();
					is.close();
				}
			}
		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
	}

	public void shutdown(){
		shutdownFlag = true;
	}
}