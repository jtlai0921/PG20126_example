//Listing 5.15
import javax.microedition.lcdui.*;
import java.util.*;

class ChartCanvas extends Canvas {
	static final int MAX_BAR_SIZE = 65;
	static final int START_X_POSITION = 30;
	static final int START_Y_CURRENT = 27;
	static final int START_Y_HISTORIC = 39;
	static final int BAR_HEIGHT = 10;
	private int currentPrice;
	private int historicPrice;
	private String symbol = null;
	private Command exitCommand;

	public void displayChart(String sym, int amtCur, int amtHist) {
		symbol = sym;
		currentPrice = amtCur;
		historicPrice = amtHist;
		serviceRepaints();
	}

	public ChartCanvas() {
		exitCommand = new Command("Exit", Command.EXIT, 1);
		addCommand(exitCommand);
	}

	public Command getExitCommand() {
		return exitCommand;
	}

	protected void paint(Graphics g){
		int currentColor = g.getColor();
		g.setColor(255,255,255);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(currentColor);
		g.drawString(symbol + " Performance",1,1,Graphics.TOP|Graphics.LEFT);
		g.drawString("current vs. historic ", 1, 12, Graphics.TOP|Graphics.LEFT);
		g.drawString("$" + currentPrice, 1, 24, Graphics.TOP|Graphics.LEFT);
		g.drawString("$" + historicPrice, 1, 36, Graphics.TOP|Graphics.LEFT);
		int[] prices = {currentPrice, historicPrice};
		int[] lengths = determineLengths(prices);
		g.fillRect (START_X_POSITION, START_Y_CURRENT, lengths[0], BAR_HEIGHT);
		g.fillRect (START_X_POSITION, START_Y_HISTORIC, lengths[1], BAR_HEIGHT);
		g.drawLine(30,26,30,50);
		g.drawLine(50,26,50,50);
		g.drawLine(70,26,70,50);
		g.drawLine(90,26,90,50);
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
}