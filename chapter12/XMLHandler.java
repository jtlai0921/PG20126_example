package com.ctimn;

import java.util.*;
import org.xml.sax.*;

public class XMLHandler implements DocumentHandler {
  private XMLClient app;
  private String customerId;

  public XMLHandler(XMLClient client){
    app = client;
  }

  public void characters(char[] ch, int start, int length) {
    String data = new String(ch);
    System.out.println("characters: "+new String(ch));
    app.append(data);
  }

  public void endDocument() {
    System.out.println("Finished parsing document.");
  }

  public void endElement(java.lang.String name) {
    System.out.println("endElement: "+name);
  }

  public void ignorableWhitespace(char[] ch, int start, int length) {
    System.out.println("ignorableWhitespace: "+new String(ch));
  }

  public void processingInstruction(java.lang.String target, java.lang.String data) {
    System.out.println("processingInstruction target="+target+" data="+data);
  }

  public void setDocumentLocator(Locator locator) {
    System.out.println("setDocumentLocator: "+locator);
  }

  public void startDocument() {
    System.out.println("Starting to parse document.");
  }

  public void startElement(java.lang.String name, AttributeList atts) {
    System.out.println("startElement: "+name);
    for (int i = 0; i < atts.getLength(); i++) {
      if (atts.getName(i).toLowerCase().equals("id")){
        customerId = atts.getValue(i);
      }
      System.out.println(" Attribute Name = "+atts.getName(i));
      System.out.println(" Attribute Type = "+atts.getType(i));
      System.out.println(" Attribute Value = "+atts.getValue(i));
    }
    if (name.toLowerCase().equals("customer")){
      app.append("Customer "+customerId);
    }
  }
}