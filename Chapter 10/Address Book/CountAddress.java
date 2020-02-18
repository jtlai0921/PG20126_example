//Listing 10.6
import com.sun.kjava.*;

public class CountAddress extends Spotlet {
	Button exitButton;
	ScrollTextBox results;
	Database addressDB;
	int dbType = 0x44415441; //'addr'
	int dbCreator = 0x61646472; //'DATA'

	public static void main(String[] args) {
		new CountAddress().count();
	}

	public void count() {
		register(NO_EVENT_OPTIONS);
		exitButton = new Button("Exit",10,130);
		results = new ScrollTextBox("",5,10,150,100);
		Graphics.clearScreen();
		addressDB = new Database(dbType, dbCreator, Database.READWRITE);
		int numRec = addressDB.getNumberOfRecords();
		addressDB.close();
		results.setText("The # of recs in the Address Book Database is: " + numRec);
		exitButton.paint();
		results.paint();
	}

	public void penDown(int x, int y){
		if (exitButton.pressed(x,y))
			System.exit(0);
	}
}
