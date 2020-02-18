import javax.microedition.rms.*;

public class QuoteFilter implements RecordFilter {
	private String symbol;

	public QuoteFilter(String sym) {
		symbol = sym;
	}

	public boolean matches(byte[] rec) {
		String r = new String(rec);
		return (r.startsWith(symbol + ';'));
	}
}
