package com.ctimn;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EnterpriseServletExample extends HttpServlet {

    public void service (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
		System.out.println("Accepting data from a J2ME client.  IP="+request.getRemoteAddr());
		System.out.println("Request Method...:"+request.getMethod());
		printParameterInfo(request);
		printHeaderInfo(request);
		printCookieInfo(request);
		processCounter(request);
		String data = processDataInput(request);
		String responseString = "Echo \"" + data + "\" send via " + request.getMethod();
		processResponse(response, responseString);
     }

	 private void printParameterInfo(HttpServletRequest request){
		System.out.println("Parameter Info:");
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()){
			String name = (String)e.nextElement();
			System.out.println("  Parameter........:"+name+"="+request.getParameter(name));
		}
	 }

	 private void printHeaderInfo(HttpServletRequest request){
		System.out.println("Header Info:");
		Enumeration e = request.getHeaderNames();
		while (e.hasMoreElements()){
			String name = (String)e.nextElement();
			System.out.println("  Header...........:"+name+"="+request.getHeader(name));
		}
 	 }

     private void printCookieInfo(HttpServletRequest request){
		System.out.println("Cookies:");
		Cookie[] cookies = request.getCookies();
		for (int ccnt=0; ccnt < cookies.length; ccnt++){
			System.out.println("  cookie...........:"+cookies[ccnt]);
			System.out.println("  cookie name......:"+cookies[ccnt].getName());
			System.out.println("  cookie value.....:"+cookies[ccnt].getValue());
			System.out.println("  cookie max age...:"+cookies[ccnt].getMaxAge());
		}
	 }

	 private void processCounter(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		Integer sessionCounter = null;
		int tempCounter = 0;
		Object obj = session.getAttribute("hit_counter");
		if (obj == null){
			sessionCounter = new Integer(1);
		} else{
			sessionCounter = (Integer)obj;
		}
		tempCounter = sessionCounter.intValue();
		System.out.println("Hit Counter........:"+tempCounter);
		tempCounter++;
		sessionCounter = new Integer(tempCounter);
		session.setAttribute("hit_counter", sessionCounter);
	}

	private String processDataInput(HttpServletRequest request) throws IOException {
		//Due to a bug in HttpConnection (cr-lf append) this needs to be exapanded by 2
		int len = request.getContentLength()+2;
		String s = "";
		System.out.println("Request Content Length = "+len);
		if (len > 0) {
			System.out.println("Reading data from request:");
			BufferedReader reader = request.getReader();
			char[] buffer = new char[len];
			int i  = reader.read(buffer, 0, buffer.length);
			s = new String(buffer);
			s = s.substring(0, s.length()-2);
			System.out.print("  Data.............:");
			System.out.println(s);
			System.out.println("  Data Length .....:"+i);
		}
		return s;
	}

	private void processResponse(HttpServletResponse response, String data) throws IOException {
		System.out.println("Responding...");
		response.setContentType("text/plain");
		response.setContentLength(data.length()+2);
		PrintWriter writer = response.getWriter();
		writer.println(data);
		System.out.println("Reponse Sent");
	}
  }