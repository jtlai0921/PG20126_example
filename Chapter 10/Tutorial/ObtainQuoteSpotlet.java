//Listing 10.4
import com.sun.kjava.*;
public final class ObtainQuoteSpotlet extends Spotlet implements DialogOwner {
	private TextField symbolField = null;
	private RadioButton stockButton = null;
	private RadioButton fundButton = null;
	private RadioGroup investmentChoice = null;
	private ScrollTextBox resultsBox = null;
	private Button exitButton = null;
	private Button getButton = null;

	public ObtainQuoteSpotlet() {
		String tfLabel = "Symbol";
		symbolField = new TextField(tfLabel,5,25,Graphics.getWidth(tfLabel) + 40, Graphics.getHeight(tfLabel));
		stockButton = new RadioButton(50,45,"Stock");
		fundButton = new RadioButton(100,45,"Fund");
		investmentChoice = new RadioGroup(2);
		investmentChoice.add(stockButton);
		investmentChoice.add(fundButton);
		investmentChoice.setSelected(stockButton);
		resultsBox = new ScrollTextBox("",8,65,137,45);
		exitButton = new Button("Exit",5,140);
		getButton = new Button("Get Quote", 105,140);
	}

	public static void main (String args[]) {
		ObtainQuoteSpotlet quoteSpotlet = new ObtainQuoteSpotlet();
		quoteSpotlet.displayForm();
	}

	private void displayForm() {
		register(NO_EVENT_OPTIONS);
		Graphics.clearScreen();
		Graphics.drawString("Obtain Investment Quote",5,10,Graphics.INVERT);
		Graphics.drawString("Type:",5,45, Graphics.PLAIN);
		symbolField.paint();
		stockButton.paint();
		fundButton.paint();
		resultsBox.paint();
		Graphics.drawBorder(5,60, 150, 55, Graphics.PLAIN, Graphics.SIMPLE);
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

	private String getLastPrice(byte[] rec) {
		String recString = new String(rec);
		int dollarPos = recString.indexOf(';');
		int centPos = recString.indexOf(';',dollarPos+1);
		int centEnd = recString.indexOf(';',centPos + 1);
		if (centEnd > 0) //had a historical price
			return recString.substring(dollarPos+1,centEnd);
		else //no previous historical price
			return recString.substring(dollarPos+1);
	}

	private void storePrice(String symbol, int[] price) {
		String newRecord = symbol + ";" + price[0] + ";" + price[1];
		byte[] byteRec;
		String dbName = "QuoteData";
		int dbType = 0x494E5653; //'INVS'
		//'CATT' Palm-registered database creator id
		//for Catapult Technologies. Assigned hex value
		int dbCreator = 0x43415454;
		com.sun.kjava.Database quoteDB = new Database (dbType, dbCreator,Database.READWRITE);
		if (!quoteDB.isOpen()) {
			Database.create(0, dbName, dbCreator, dbType, false);
			quoteDB = new Database (dbType, dbCreator, Database.READWRITE);
		}
		boolean found = false;
		int n = quoteDB.getNumberOfRecords();
		for (int i = 0; i<n; i++) {
			byte[] raw = quoteDB.getRecord(i);
			if ((new String(raw)).startsWith(symbol + ';')) {
				found = true;
				newRecord += ';' + getLastPrice(raw);
				byteRec = newRecord.getBytes();
				quoteDB.setRecord(i, byteRec);
				break;
			}
		}
		if (!found) {
			byteRec = newRecord.getBytes();
			quoteDB.addRecord(byteRec);
		}
		quoteDB.close();
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
				int type;
				if (investmentChoice.getSelected().equals(fundButton))
					type = 1;
				else
					type = 0;
				//later on, get the price from a quote service here
				int[] price = QuoteService.getPrice(sym,type);
				if (price != null) {
					storePrice(sym, price);
					resultsBox.setText("The price of " + sym + " is $" + price[0] + "." + price[1]);
					resultsBox.paint();
				} else {
					Graphics.playSound(Graphics.SOUND_ERROR);
					Dialog symbolAlert = new Dialog(this,"Alert", "Check Symbol and Type.\n\nNo quote found.","OK");
					symbolAlert.showDialog();
				}
			}
		} else if (symbolField.pressed(x,y)) {
			symbolField.setFocus();
		} else if (stockButton.pressed(x,y)) {
			symbolField.loseFocus();
			stockButton.handlePenDown(x,y);
		} else if (fundButton.pressed(x,y)) {
			symbolField.loseFocus();
			fundButton.handlePenDown(x,y);
		} else if (resultsBox.contains(x,y)) {
			resultsBox.handlePenDown(x,y);
		}
	}

	public void keyDown (int keyCode) {
		if ((keyCode == 11) || (keyCode == 12)){
			resultsBox.handleKeyDown(keyCode);
		} else if (symbolField.hasFocus()) {
			symbolField.handleKeyDown(keyCode);
		}
	}

	public void penMove (int x, int y) {
		if (resultsBox.contains(x,y)) {
			resultsBox.handlePenMove(x,y);
		}
	}

	public void dialogDismissed(java.lang.String title) {
		this.displayForm();
	}
}