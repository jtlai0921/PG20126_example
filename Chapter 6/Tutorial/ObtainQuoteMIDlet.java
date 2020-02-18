//Listing 6.9
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

public class ObtainQuoteMIDlet extends MIDlet {
	private Display displayMngr = null;
	private EntryForm entryForm = null;
	private Alert resultsAlert = null;
	private Ticker adTicker =
	new Ticker("Track your investments with Investment Tracker");

	public ObtainQuoteMIDlet () {
	}

	private void initListener () {
		ItemStateListener itemListener = new ItemStateListener () {
			public void itemStateChanged (Item item) {
				if ((item == entryForm.getInvestmentChoice()) && (entryForm.getInvestmentChoice().getSelectedIndex() == 1) && !(entryForm.getSymbolField().getString().toUpperCase().endsWith("X"))) {
					Alert symbolAlert = new Alert("Check Symbol","Mutual Funds end in 'X'", null, AlertType.WARNING);
					symbolAlert.setTimeout(Alert.FOREVER);
					displayMngr.setCurrent(symbolAlert, entryForm);
				}
			}
		};
		CommandListener commandListener = new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == entryForm.getExitCommand()) {
					destroyApp(true);
				} else if (c == entryForm.getGetCommand()) {
					if ((entryForm.getInvestmentChoice().getSelectedIndex() == 1) && !(entryForm.getSymbolField().getString().toUpperCase().endsWith("X"))){
						Alert symbolAlert = new Alert("Check Symbol","Mutual Funds end in 'X'", null, AlertType.WARNING);
						symbolAlert.setTimeout(Alert.FOREVER);
						displayMngr.setCurrent(symbolAlert, entryForm);
					} else if (entryForm.getSymbolField().getString().length() > 0) {
						String sym = entryForm.getSymbolField().getString();
						int type = entryForm.getInvestmentChoice().getSelectedIndex();
						int[] price = QuoteService.getPrice(sym, type);
						storePrice(sym, price);
						displayPrice("The price of " + sym + " is $" + price[0] + "." + price[1]);
					}
				}
			}
		};
		entryForm.setItemStateListener(itemListener);
		entryForm.setCommandListener(commandListener);
	}

	private void displayEntryForm () {
		if (entryForm == null) {
			entryForm = new EntryForm("ObtainQuote");
		}
		initListener();
		displayMngr.setCurrent(entryForm);
	}

	private void displayPrice(String quoteString) {
		if (resultsAlert == null) {
			resultsAlert = new Alert("Quote Price", null, null,AlertType.CONFIRMATION);
			resultsAlert.setTicker(adTicker);
			resultsAlert.setTimeout(Alert.FOREVER);
		}
		resultsAlert.setString(quoteString);
		displayMngr.setCurrent(resultsAlert, entryForm);
	}

	private void storePrice(String symbol, int[] price) {
		String newRecord = symbol + ";" + price[0] + ";" + price[1];
		byte[] byteRec;
		try {
			RecordStore anRMS = RecordStore.openRecordStore("Quotes" , true);
			RecordFilter rf = new QuoteFilter(symbol);
			RecordEnumeration rEnum = anRMS.enumerateRecords(rf,null,false);
			if (rEnum.hasNextElement()) {
				int recId = rEnum.nextRecordId();
				newRecord += ';' + getLastPrice(anRMS.getRecord(recId));
				byteRec = newRecord.getBytes();
				anRMS.setRecord(recId,byteRec,0,byteRec.length);
			} else {
				byteRec = newRecord.getBytes();
				anRMS.addRecord(byteRec,0,byteRec.length);
			}
			rEnum.destroy();
			anRMS.closeRecordStore();
		} catch (RecordStoreFullException fullStore) {
			//handle a full record store problem
		} catch (RecordStoreNotFoundException notFoundException) {
			//handle store not found which should not happen with the
		} catch (RecordStoreException recordStoreException) {
			//handling record store problems
		}
	}

	private int[] parsePrices(byte[] quoteRec) {
		String rec = new String(quoteRec);
		int dollar1Pos = rec.indexOf(';');
		int cent1Pos = rec.indexOf(';',dollar1Pos+1);
		int dollar2Pos = rec.indexOf(';',cent1Pos + 1);
		if (dollar2Pos > 0) { //had a historical price
			int cent2Pos = rec.indexOf(';',dollar2Pos + 1);
			int currentDollars = Integer.parseInt(rec.substring(dollar1Pos + 1,cent1Pos));
			int currentCents = Integer.parseInt(rec.substring(cent1Pos + 1,dollar2Pos));
			int historicalDollars = Integer.parseInt(rec.substring(dollar2Pos + 1,cent2Pos));
			int historicalCents = Integer.parseInt(rec.substring(cent2Pos + 1));
			int[] returnPrices = {currentDollars, currentCents, historicalDollars,
			historicalCents};
			return returnPrices;
		} else { //no previous historical price
			int currentDollars = Integer.parseInt(rec.substring(dollar1Pos + 1, cent1Pos));
			int currentCents = Integer.parseInt(rec.substring(cent1Pos + 1));
			int[] returnPrices = {currentDollars, currentCents};
			return returnPrices;
		}
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

	protected void startApp() {
		displayMngr = Display.getDisplay(this);
		displayEntryForm();
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean unconditional) {
		notifyDestroyed();
	}

	public void commandAction(Command c, Displayable s) {
	}
}
