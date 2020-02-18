//Listing 10.5
import com.sun.kjava.*;

public final class RetrieveQuoteSpotlet extends Spotlet implements DialogOwner{
	static int MAX_BAR_SIZE = 150;
	static int START_X_POSITION = 5;
	static int START_Y_CURRENT = 97;
	static int START_Y_HISTORIC = 122;
	static int BAR_HEIGHT = 5;
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

	private int[] parsePrices(byte[] quoteRec) {
		String rec = new String(quoteRec);
		int dollar1Pos = rec.indexOf(';');
		int cent1Pos = rec.indexOf(';',dollar1Pos+1);
		int dollar2Pos = rec.indexOf(';',cent1Pos + 1);
		System.out.println("=====> " + rec);
		if (dollar2Pos > 0) { //had a historical price
			int cent2Pos = rec.indexOf(';',dollar2Pos + 1);
			int currentDollars = Integer.parseInt(rec.substring(dollar1Pos + 1, cent1Pos));
			int currentCents = Integer.parseInt(rec.substring(cent1Pos + 1, dollar2Pos));
			int historicalDollars = Integer.parseInt(rec.substring(dollar2Pos + 1, cent2Pos));
			int historicalCents = Integer.parseInt(rec.substring(cent2Pos + 1));
			int[] returnPrices = {currentDollars, currentCents,
			historicalDollars, historicalCents};
			return returnPrices;
		} else { //no previous historical price
			int currentDollars = Integer.parseInt(rec.substring(dollar1Pos + 1, cent1Pos));
			int currentCents = Integer.parseInt(rec.substring(cent1Pos + 1));
			int[] returnPrices = {currentDollars, currentCents};
			return returnPrices;
		}
	}

	private int[] retrievePrices(String symbol) {
		int[] dollars = null;
		String dbName = "QuoteData";
		int dbType = 0x494E5653; //'INVS'
		//'CATT' Palm-registered database creator id
		//for Catapult Technologies. Assigned hex value
		int dbCreator = 0x43415454;
		com.sun.kjava.Database quoteDB = new Database (dbType, dbCreator, Database.READWRITE);
		if (!quoteDB.isOpen()) {
			Database.create(0, dbName, dbCreator, dbType, false);
			quoteDB = new Database (dbType, dbCreator, Database.READWRITE);
		}
		boolean found = false;
		for (int i = 0; i<quoteDB.getNumberOfRecords(); i++) {
			String raw = new String(quoteDB.getRecord(i));
			if (raw.startsWith(symbol + ';')) {
				found = true;
				byte[] rec = quoteDB.getRecord(i);
				dollars = parsePrices(rec);
				break;
			}
		}
		if (!found) {
			dollars = null;
		}
		quoteDB.close();
		return dollars;
	}

	private void displayChart(String currentSymbol) {
		int[] prices = retrievePrices(currentSymbol);
		if (prices != null) {
			if (prices.length > 2) {
				paintChart(currentSymbol,prices[0],prices[2]);
			} else {
				Graphics.drawRectangle(5,60,155,70,Graphics.ERASE,0);
				Graphics.drawString("Recorded price for " + currentSymbol + " is: $" + prices[0] + "." + prices[1], 5, 65, Graphics.PLAIN);
				Graphics.drawString("No historical data exists.", 5, 80, Graphics.INVERT);
			}
		} else {
			Graphics.playSound(Graphics.SOUND_ERROR);
			Dialog noDataAlert = new Dialog(this,"Alert", "No price exists for " + currentSymbol,"OK");
			noDataAlert.showDialog();
		}
	}

	public void paintChart(String sym, int currentPrice, int historicPrice) {
		Graphics.drawRectangle(5,60,155,70,Graphics.ERASE,0);
		Graphics.drawString(sym + " Performance",5,60,Graphics.PLAIN);
		Graphics.drawString("current vs. historic",5,73,Graphics.PLAIN);
		Graphics.drawString("$" + currentPrice, 5, 85, Graphics.PLAIN);
		Graphics.drawString("$" + historicPrice, 5, 110, Graphics.PLAIN);
		int[] prices = {currentPrice, historicPrice};
		int[] lengths = determineLengths(prices);
		Graphics.drawRectangle (START_X_POSITION, START_Y_CURRENT, lengths[0],
		BAR_HEIGHT, Graphics.PLAIN, 0);
		Graphics.drawRectangle (START_X_POSITION, START_Y_HISTORIC,
		lengths[1], BAR_HEIGHT, Graphics.PLAIN, 0);
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