import java.util.Random;

public class QuoteService {

	public static int[] getPrice(String symbolString, int type) {
		Random generator = new Random();
		int dollars = Math.abs(generator.nextInt()) %100;
		int cents = Math.abs(generator.nextInt()) %100;
		int[] priceParts = {dollars, cents};
		return priceParts;
	}
}
