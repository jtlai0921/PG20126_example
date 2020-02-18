//Listing 9.1
import com.sun.kjava.*;

public class Widgets extends Spotlet {

	Graphics g = Graphics.getGraphics();
	private Button exitButton;
	private CheckBox employedBox;
	private boolean cbState = false;
	private RadioGroup genderGroup;
	private RadioButton mButton;
	private RadioButton fButton;
	private ScrollTextBox performanceBox;
	private TextField ageField;
	private Slider salSlider;
	private ValueSelector kidsValSelect;

	public static void main (String args[]) {
		new Widgets().drawWidgets();
	}

	private void drawWidgets() {
		register(NO_EVENT_OPTIONS);
		g.clearScreen();
		g.drawString(" Simple Widgets Example ", 5, 10, g.INVERT);
		//example check box
		employedBox = new CheckBox(10, 25, "Employed");
		employedBox.paint();
		//example radio button and radio button group
		mButton = new RadioButton(10, 40, "Male");
		fButton = new RadioButton(50, 40, "Female");
		genderGroup = new RadioGroup(2);
		genderGroup.add(mButton);
		genderGroup.add(fButton);
		genderGroup.setSelected(mButton);
		mButton.paint();
		fButton.paint();
		//example scroll text box
		performanceBox = new ScrollTextBox("No record of missed work. Meets or exceeds on all performance reviews.", 10, 55, 140, 25);
		performanceBox.paint();
		//example text field
		ageField = new TextField("Age", 10, 85, 50, 20);
		ageField.paint();
		//example slider
		g.drawString("Salary Level: ",10,105);
		salSlider = new Slider(90, 105, 50, 1, 5, 1);
		salSlider.paint();
		//example value selector
		kidsValSelect = new ValueSelector("Kids: ", 1, 5, 1, 10, 125);
		kidsValSelect.paint();
		exitButton = new Button("Exit", 10, 140);
		exitButton.paint();
	}

	public void penDown(int x, int y){
		if (exitButton.pressed(x,y)){
			System.exit(0);
		} else if (employedBox.pressed(x,y)) {
			employedBox.handlePenDown(x,y);
		} else if (mButton.pressed(x, y)) {
			genderGroup.setSelected(mButton);
		} else if (fButton.pressed(x, y)) {
			genderGroup.setSelected(fButton);
		} else if (performanceBox.contains(x,y)) {
			performanceBox.handlePenDown(x,y);
		} else if (ageField.pressed(x,y)) {
			ageField.setFocus();
		} else if (salSlider.contains(x,y)) {
			salSlider.handlePenDown(x,y);
		} else if (kidsValSelect.pressed(x,y)) {
		}
	}

	public void keyDown(int keyCode) {
		if (ageField.hasFocus()) {
			ageField.handleKeyDown(keyCode);
		}
	}
}
