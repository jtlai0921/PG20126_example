//Listing 9.8
import com.sun.kjava.*;

public class ObtainQuoteSpotlet extends Spotlet implements DialogOwner {
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
			Dialog symbolAlert = new Dialog(this,"Alert", "Check Symbol\n\nMutual Funds end in 'X'","OK");
			symbolAlert.showDialog();
			return false;
		}
		return true;
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
				//later on, get the price from a quote service here
				int[] price = {75, 55};
				//later on, store the price in the database here
				resultsBox.setText("The price of " + sym + " is $" + price[0] + "." + price[1]);
				resultsBox.paint();
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