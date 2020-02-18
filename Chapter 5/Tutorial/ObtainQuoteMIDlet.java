//Listing 5.16
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

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
					} else {
						if (entryForm.getSymbolField().getString().length() > 0) {
							String sym = entryForm.getSymbolField().getString();
							displayPrice("The price of " + sym + " is $111.19");
						}
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
			resultsAlert = new Alert("Quote Price", null, null,
			AlertType.CONFIRMATION);
			resultsAlert.setTicker(adTicker);
			resultsAlert.setTimeout(Alert.FOREVER);
		}
		resultsAlert.setString(quoteString);
		displayMngr.setCurrent(resultsAlert, entryForm);
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
