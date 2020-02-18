//Listing 5.17
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

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
		initCanvasListener();
		displayMngr.setCurrent(chartCanvas);
		String currentSymbol = entryForm.getSymbolField().getString();
		chartCanvas.displayChart(currentSymbol,75,110);
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
