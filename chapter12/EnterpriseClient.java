package com.ctimn;

import javax.microedition.io.*;
import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class EnterpriseClient extends MIDlet implements CommandListener {

    private Form outputForm = new Form("Server Messages");
    private Form inputForm = new Form("Input Message");
    private TextField msgFld = new TextField("Msg", "Test", 15, TextField.ANY);
    private Command okCmd = new Command("OK", Command.OK, 1);
    private Command exitCmd = new Command("Exit", Command.EXIT, 1);
    private Command sendCmd = new Command("Send", Command.SCREEN, 1);
    private Display display;
    private String sessionId;
    private boolean initialized = false;
    private int methodType = 0;
    private static final int GET = 0;
    private String url = "http://localhost:7001/EnterpriseServletExample";

    private static final String[] choices = {
        "1 GET Message",
        "2 POST Message"
    };

    private List menu = new List("Select:", List.IMPLICIT, choices, null);

    protected void startApp() throws MIDletStateChangeException {
		init();
        display = Display.getDisplay(this);
        display.setCurrent(menu);
    }

    private void init(){

		if (!initialized){
			inputForm.append(msgFld);
			menu.addCommand(okCmd);
			menu.addCommand(exitCmd);
			outputForm.addCommand(okCmd);
			outputForm.addCommand(exitCmd);
			inputForm.addCommand(sendCmd);
			menu.setCommandListener(this);
			outputForm.setCommandListener(this);
			inputForm.setCommandListener(this);
			initialized = true;
		}
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
		} else if ((displayable == inputForm) && (cmd == sendCmd)) {
			sendMsg();
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
	   display.setCurrent(inputForm);
	   methodType = idx;
	}

	private void sendMsg(){
		display.setCurrent(outputForm);
		processInit();
		if (methodType == GET){
			doGet();
		} else {
			doPost();
		}
	}

/*NOTE: content-length is offset by two due to cr-lf appended (in MIDP HTTP code)
		after length is set.  This is a bug in the MIDP 1.0 reference implementation.
		Other SDKs are unlikely	to have this problem.
*/
	private void doGet(){
		String data = msgFld.getString();
		try {
			String paramString = "?msg="+msgFld.getString();
			HttpConnection connection = getConnection(paramString);
			connection.setRequestMethod(HttpConnection.GET); //optional -- this is the default
 			connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
			setCookie(connection);
			sendData(connection, data);
			receiveData(connection);
			processHeaderInfo(connection);
			System.out.println("Done.");
		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
	}

	private void doPost(){
		String data = msgFld.getString();
		try {
			HttpConnection connection = getConnection(null);
			connection.setRequestMethod(HttpConnection.POST);
 			connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
			setCookie(connection);
			sendData(connection, data);
			receiveData(connection);
			processHeaderInfo(connection);
			System.out.println("Done.");
		} catch (IOException x) {
			System.out.println("Problems sending or receiving data.");
			x.printStackTrace();
		}
	}

	private void processInit(){
		System.out.println("\n");
		System.out.println("Initiating Write-Read with Servlet.");
		StringItem item = new StringItem("Contacting Servlet", "");
		outputForm.append(item);
	}

	private HttpConnection getConnection(String paramString) throws IOException {
		if (paramString != null)
			url += paramString;
			HttpConnection connection = (HttpConnection)Connector.open(url, Connector.READ_WRITE);
		return connection;
	}

	private void setCookie(HttpConnection connection) throws IOException {
		if (sessionId != null){
			connection.setRequestProperty("Cookie", sessionId);
			System.out.println("Cookie Set...................:"+sessionId);
		}
	}

	private void sendData(HttpConnection connection, String data) throws IOException {
		byte[] dataOut = data.getBytes();
		System.out.println("Data To Send.................:"+data);
		System.out.println("Length of Data To Send.......:"+dataOut.length);
		DataOutputStream os = connection.openDataOutputStream();
		try {
			os.write(dataOut);
			os.flush();
			System.out.println("Output Stream Flushed.");
		} finally {
			os.close();
		}
	}

	private void receiveData(HttpConnection connection) throws IOException {
		StringBuffer sb = new StringBuffer("");
		DataInputStream is = connection.openDataInputStream();
		try {
			System.out.println("Input Stream Opened.");
			System.out.println("Data Length to Receive.......:"+connection.getLength());
			long len = connection.getLength();
			int c = 0;
			for (int ccnt=0; ccnt < len; ccnt++){
				c = is.read();
				sb.append((char)c);
			}
			String dataIn = sb.toString();
			StringItem item = new StringItem("Msg: ", dataIn);
			outputForm.append(item);
			System.out.println("Data Received................:"+dataIn);
		} finally {
			is.close();
		}
	}

	private void processHeaderInfo(HttpConnection connection) throws IOException {
		if (sessionId == null){
			sessionId = connection.getHeaderField("set-cookie"); //get the session, if we don't have it.
		}
		System.out.println("Get Header Info:");
		for (int ccnt=0; ; ccnt++){
			String name = connection.getHeaderFieldKey(ccnt);
    	    if (name == null){
    	      break;
    	    }
			System.out.println("  Key=Value..................:"+name+"="+connection.getHeaderField(ccnt));
		}
	}
}