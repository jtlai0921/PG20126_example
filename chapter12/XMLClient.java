package com.ctimn;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import nanoxml.*;
import nanoxml.sax.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class XMLClient extends MIDlet implements CommandListener {
  private Form outputForm = new Form("Order Information");
  private Form inputForm = new Form("Enter Customer");
  private TextField customerFld = new TextField("ID", "1", 15, TextField.NUMERIC);
  private Command okCmd = new Command("OK", Command.OK, 1);
  private Command exitCmd = new Command("Exit", Command.EXIT, 1);
  private Command getCmd = new Command("Get", Command.OK, 1);
  private Display display;
  private boolean initialized = false;
  private static final String CONNECTION_URL = "http://localhost:7001/XMLExample.jsp";
  protected void startApp() throws MIDletStateChangeException {
    init();
    display = Display.getDisplay(this);
    display.setCurrent(inputForm);
  }

  private void init(){
    if (!initialized){
	  inputForm.append(customerFld);
	  inputForm.addCommand(getCmd);
	  inputForm.addCommand(exitCmd);
	  outputForm.addCommand(okCmd);
	  outputForm.addCommand(exitCmd);
	  inputForm.setCommandListener(this);
	  outputForm.setCommandListener(this);
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
    } else if ((displayable == outputForm) && (cmd == okCmd)) {
      display.setCurrent(inputForm);
    } else if ((displayable == inputForm) && (cmd == getCmd)) {
      getCustomer();
    } else {
      display.setCurrent(inputForm);
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

  private void getCustomer(){
    removeStringItems();
    String id = customerFld.getString();
    display.setCurrent(outputForm);
    try {
      XMLHandler handler = new XMLHandler(this);
      Parser parser = ParserFactory.makeParser("nanoxml.sax.SAXParser");
      parser.setDocumentHandler(handler);
      InputSource source = getXml(id);
      parser.parse(source);
    } catch (Exception x){
      x.printStackTrace();
    }
  }

  private InputSource getXml(String id){
    InputSource source = null;
    String url = CONNECTION_URL + "?customerId="+id;
    try {
      DataInputStream is = Connector.openDataInputStream(url);
      source = new InputSource(is);
    } catch (IOException x){
      x.printStackTrace();
    }
    return source;
  }

  private void removeStringItems(){
    int count = outputForm.size();
    for(int i=count-1; i >= 0; i--){
      if (outputForm.get(i) instanceof StringItem){
        outputForm.delete(i);
      }
    }
  }

  public void append(String s){
    outputForm.append(s + "\n");
  }
}