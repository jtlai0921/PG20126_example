package com.ctimn;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class DatagramListener extends Thread {

	private Form outputForm;
	private boolean shutdownFlag = false;

	public DatagramListener(Form outputForm){
		this.outputForm = outputForm;
	}

	public void run(){
		Datagram datagram = null;
		String msg = null;
		StringItem item = new StringItem("Listening for Datagrams", "");
		outputForm.append(item);
		try {
			DatagramConnection connection =	(DatagramConnection)
				Connector.open("datagram://:5555", Connector.READ_WRITE);
			try {
				while (true) {
					datagram = connection.newDatagram(100);
					try {
						msg = receiveDatagram(connection, datagram);
					} catch (InterruptedIOException x){
						if (shutdownFlag){
							return;
						}
					}
					item = new StringItem("Msg: ", msg);
					outputForm.append(item);
					sendDatagram(connection, datagram, "Message from the server");
				}
			} finally {
				connection.close();
			}
		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
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

	public void shutdown(){
		shutdownFlag = true;
	}
}