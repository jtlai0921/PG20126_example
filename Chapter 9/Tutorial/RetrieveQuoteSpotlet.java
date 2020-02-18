//Listing 9.9
import com.sun.kjava.*;

public class RetrieveQuoteSpotlet extends Spotlet implements DialogOwner{
	static final int MAX_BAR_SIZE = 150;
	static final int START_X_POSITION = 5;
	static final int START_Y_CURRENT = 97;
	static final int START_Y_HISTORIC = 122;
	static final int BAR_HEIGHT = 5;
	private TextField symbolField = null;
	private RadioButton stockButton = null;
	private RadioButton fundButton = null;
	private RadioGroup investmentChoice = null;
	private Button exitButton = null;
	private Button getButton = null;

	public RetrieveQuoteSpotlet() {
		String tfLabel = "Symbol";
		symbolField = new TextField(tfLabel,5,25,Graphics.getWidth(tfLabel) + 40, Graphics.getHeight(tfLabel));
		stockButton = new RadioButton(50,45,"Stock");
		fundButton = new RadioButton(100,45,"Fund");
		investmentChoice = new RadioGroup(2);
		investmentChoice.add(stockButton);
		investmentChoice.add(fundButton);
		investmentChoice.setSelected(stockButton);
		exitButton = new Button("Exit",5,140);
		getButton = new Button("Get Quote", 105,140);
	}

	public static void main (String args[]) {
		RetrieveQuoteSpotlet quoteSpotlet = new RetrieveQuoteSpotlet();
		quoteSpotlet.displayForm();
	}

	private void displayForm() {
		register(NO_EVENT_OPTIONS);
		Graphics.clearScreen();
		Graphics.drawString("Retrieve Investment Quote",5,10,Graphics.INVERT);
		Graphics.drawString("Type:",5,45, Graphics.PLAIN);
		symbolField.paint();
		stockButton.paint();
		fundButton.paint();
		exitButton.paint();
		getButton.paint();
	}

	private boolean checkSymbol() {
		if ((investmentChoice.getSelected().equals(fundButton)) && !(symbolField.getText().toUpperCase().endsWith("X"))){
			Graphics.playSound(Graphics.SOUND_ERROR);
			Dialog symbolAlert = new Dialog(this,"Alert","Check Symbol\n\nMutual Funds end in 'X'","OK");
			symbolAlert.showDialog();
			return false;
		}
		return true;
	}

	private void displayChart(String currentSymbol) {
		//later on, get prices from a database here
		int[] prices = {75,55,110,45};
		if (prices != null) {
			if (prices.length > 2) {
				paintChart(currentSymbol,prices[0],prices[2]);
			} else {
				Graphics.drawRectangle(5,60,155,70,Graphics.ERASE,0);
				Graphics.drawString("Recorded price for" + currentSymbol + " is: $" + prices[0] + "." + prices[1], 5, 65, Graphics.PLAIN);
				Graphics.drawString("No historical data exists.", 5, 80,
				Graphics.INVERT);
			}
		} else {
			Graphics.playSound(Graphics.SOUND_ERROR);
			Dialog noDataAlert = new Dialog(this,"Alert", "No price exists for " + currentSymbol,"OK");
			noDataAlert.showDialog();
		}
	}

	public void paintChart(String sym, int currentPrice, int historicPrice)	{
		Graphics.drawRectangle(5,60,155,70,Graphics.ERASE,0);
		Graphics.drawString(sym + " Performance",5,60,Graphics.PLAIN);
		Graphics.drawString("current vs. historic",5,73,Graphics.PLAIN);
		Graphics.drawString("$" + currentPrice, 5, 85, Graphics.PLAIN);
		Graphics.drawString("$" + historicPrice, 5, 110, Graphics.PLAIN);
		int[] prices = {currentPrice, historicPrice};
		int[] lengths = determineLengths(prices);
		Graphics.drawRectangle (START_X_POSITION, START_Y_CURRENT, lengths[0], BAR_HEIGHT, Graphics.PLAIN, 0);
		Graphics.drawRectangle (START_X_POSITION, START_Y_HISTORIC, lengths[1], BAR_HEIGHT, Graphics.PLAIN, 0);
		for (int i = 30; i < MAX_BAR_SIZE; i = i + 30) {
			Graphics.drawLine (i, START_Y_CURRENT - 2, i, START_Y_HISTORIC +
			BAR_HEIGHT + 2, Graphics.PLAIN);
		}
	}

	private int[] determineLengths (int[] prices) {
		int ratio, higherPrice, lowerPrice;
		boolean currentHigher;
		if (prices[0] < prices[1]) {
			higherPrice = prices[1];
			lowerPrice = prices[0];
			currentHigher=false;
		} else {
			higherPrice = prices[0];
			lowerPrice = prices[1];
			currentHigher=true;
		}
		ratio = higherPrice/MAX_BAR_SIZE + 1;
		while (ratio > 1) {
			higherPrice = higherPrice/ratio;
			lowerPrice = lowerPrice/ratio;
			ratio = higherPrice/MAX_BAR_SIZE + 1;
		}
		if (currentHigher) {
			int[] ends = {higherPrice, lowerPrice};
			return ends;
		} else {
			int [] ends = {lowerPrice, higherPrice};
			return ends;
		}
	}

	public void penDown(int x, int y) {
		if (exitButton.pressed(x,y)){
			Graphics.playSound(Graphics.SOUND_CONFIRMATION);
			System.exit(0);
		} else if (getButton.pressed(x,y)) {
				symbolField.loseFocus();
				if ((symbolField.getText().length() > 0) && (checkSymbol())) {
				Graphics.playSound(Graphics.SOUND_STARTUP);
				String sym = symbolField.getText().toUpperCase();
				displayChart(sym);
			}
		} else if (symbolField.pressed(x,y)) {
			symbolField.setFocus();
		} else if (stockButton.pressed(x,y)) {
			symbolField.loseFocus();
			stockButton.handlePenDown(x,y);
		} else if (fundButton.pressed(x,y)) {
			symbolField.loseFocus();
			fundButton.handlePenDown(x,y);
		}
	}

	public void keyDown (int keyCode) {
		if (symbolField.hasFocus()) {
		symbolField.handleKeyDown(keyCode);
		}
	}

	public void dialogDismissed(java.lang.String title) {
		this.displayForm();
	}
}