//Listing 7.3
import javax.microedition.io.*;
import java.io.*;

public class QuoteService {

	public static int[] getPrice(String symbolString, int type) {
		String quotePage = getQuotePage(symbolString, type);
		if (quotePage.length() > 0)
			return parseQuote(quotePage, type);
		else
			return null;
	}

	private static int[] parseQuote(String aQuotePage, int type){
		String skip;
		String dollarsEnd;
		String quoteEnd;
		String quoteDollars = null;
		String quoteCents = null;
		int[] dollarsCents = new int[2];
		if (type == 0) {
			skip = "$&nbsp;";
			dollarsEnd = ".";
			quoteEnd = "</b>";
		} else {
			skip = "$";
			dollarsEnd = ".";
			quoteEnd = "</b>";
		}
		try {
			int generalPos = aQuotePage.indexOf(skip);
			int dollarStop = aQuotePage.indexOf(dollarsEnd, generalPos);
			int quoteStop = aQuotePage.indexOf(quoteEnd, dollarStop);
			quoteDollars = aQuotePage.substring(generalPos + (skip.length()), dollarStop);
			dollarsCents[0] = Integer.parseInt(quoteDollars);
			quoteCents = aQuotePage.substring(dollarStop + 1, quoteStop);
			dollarsCents[1] = Integer.parseInt(quoteCents);
			return dollarsCents;
		} catch (Exception e){
			System.out.println("Error attempting to parse quote from " + "source page. Improper Symbol?");
			return null;
		}
	}

	private static String getQuotePage(String symbolString, int type) {
		char marker = '$';
		int readLength = 20;
		StringBuffer quotePage = new StringBuffer();
		try {
			String protocol = "http://";
			String stockURL = "quotes.nasdaq.com/Quote.dll?" + "page=multi&page=++&mode=stock&symbol=";
			String fundURL = "www.nasdaq.com/asp/quotes_mutual.asp?" + "page=++&mode=fund&symbol=";
			InputStream in;
			if (type == 0) {
				in = Connector.openInputStream(protocol + stockURL +
				symbolString);
			} else {
				in = Connector.openInputStream(protocol + fundURL +
				symbolString);
			}
			int ch;
			while ((ch = in.read()) > 0 ) {
				if (((char) ch) == marker) {
					int cnt = 0;
					while (cnt < readLength) {
						ch = in.read();
						quotePage.append((char)ch);
						cnt++;
					}
					break;
				}
			}
			in.close();
		} catch (IOException ex) {
			System.out.println("Exception reading quote from HTTP Connection " +
			ex.getMessage());
		}
		return quotePage.toString();
	}
}
