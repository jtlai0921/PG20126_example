//Listing 5.14
import javax.microedition.lcdui.*;

public class EntryForm extends Form {
	private TextField symbolField = null;
	private Command exitCommand = null;
	private Command getCommand = null;
	private ChoiceGroup investmentChoice = null;

	public EntryForm(String title) {
		super(title);
		symbolField = new TextField("Investment Symbol", "", 6, TextField.ANY);
		String choices[] = {"Stock", "Fund"};
		investmentChoice = new ChoiceGroup("Type", Choice.EXCLUSIVE, choices, null);
		exitCommand = new Command("Exit", Command.EXIT, 1);
		getCommand = new Command("Get", Command.SCREEN, 2);
		append(symbolField);
		append(investmentChoice);
		addCommand(exitCommand);
		addCommand(getCommand);
	}

	public TextField getSymbolField() {
		return symbolField;
	}

	public ChoiceGroup getInvestmentChoice() {
		return investmentChoice;
	}

	public Command getExitCommand() {
		return exitCommand;
	}

	public Command getGetCommand() {
		return getCommand;
	}
}
