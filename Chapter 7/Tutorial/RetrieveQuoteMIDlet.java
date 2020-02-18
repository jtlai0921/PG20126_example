import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

public class RetrieveQuoteMIDlet extends MIDlet {
	private Display displayMngr = null;
	private EntryForm entryForm = null;
	private ChartCanvas chartCanvas = null;

	public RetrieveQuoteMIDlet () {
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
					} else if (entryForm.getSymbolField().getString().length() > 0)
						displayChartCanvas();
				}
			}
		};
		entryForm.setItemStateListener(itemListener);
		entryForm.setCommandListener(commandListener);
	}

	private void initCanvasListener() {
		CommandListener commandListener = new CommandListener() {
		public void commandAction(Command c, Displayable d) {
			if (c == chartCanvas.getExitCommand())
			displayMngr.setCurrent(entryForm);
			}
		};
		chartCanvas.setCommandListener(commandListener);
	}

	private void displayEntryForm () {
		if (entryForm == null) {
			entryForm = new EntryForm("RetrieveQuote");
		}
		initListener();
		displayMngr.setCurrent(entryForm);
	}

	private void displayChartCanvas() {
		if (chartCanvas == null) {
			chartCanvas = new ChartCanvas();
		}
		String currentSymbol = entryForm.getSymbolField().getString();
		int[] prices = retrievePrices(currentSymbol);
		if (prices != null) {
			if (prices.length > 2) {
				initCanvasListener();
				displayMngr.setCurrent(chartCanvas);
				chartCanvas.displayChart(currentSymbol,prices[0],prices[2]);
			} else {
				Alert noDataAlert = new Alert("Recorded Price","Recorded price for " + currentSymbol + " is: $" + prices[0] + "." + prices[1] + ". No historical data exists.", null, AlertType.INFO);
				noDataAlert.setTimeout(Alert.FOREVER);
				displayMngr.setCurrent(noDataAlert, entryForm);
			}
		} else {
			Alert noDataAlert = new Alert("No prices", "No price exists data for "+ currentSymbol, null, AlertType.INFO);
			noDataAlert.setTimeout(Alert.FOREVER);
			displayMngr.setCurrent(noDataAlert, entryForm);
		}
	}

	private int[] retrievePrices(String symbol) {
		int[] dollars = null;
		try {
			RecordStore anRMS = RecordStore.openRecordStore("Quotes" , true);
			RecordFilter rf = new QuoteFilter(symbol);
			RecordEnumeration rEnum = anRMS.enumerateRecords(rf,null,false);
			if (rEnum.hasNextElement()) {
				byte[] rec = rEnum.nextRecord();
				dollars = parsePrices(rec);
			} else
				dollars = null;
			rEnum.destroy();
			anRMS.closeRecordStore();
		} catch (RecordStoreFullException fullStore) {
			//handle a full record store problem
		} catch (RecordStoreNotFoundException notFoundException) {
			//handle store not found which should not happen with the
			//createIfNecessary
		} catch (RecordStoreException recordStoreException) {
			//handling record store problems
		}
		return dollars;
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
			int currentDollars = Integer.parseInt(rec.substring(dollar1Pos +
			1,cent1Pos));
			int currentCents = Integer.parseInt(rec.substring(cent1Pos + 1));
			int[] returnPrices = {currentDollars, currentCents};
			return returnPrices;
		}
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
}