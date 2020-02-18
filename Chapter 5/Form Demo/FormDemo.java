//Listing 5.4 and 5.5

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class FormDemo extends MIDlet{

    Display d;
    Form aForm;
    ChoiceGroup aChoiceGroup;
    DateField aDateField;
    Gauge aGauge;
    StringItem aStringItem;
    TextField aTextField;
    ImageItem anImageItem;
    Image anImage;

    public FormDemo () {
        aForm = new Form("Demo Form");
        String choices[] = {"This", "That"};
        aStringItem = new StringItem(null,"Demo Items");
        aChoiceGroup = new ChoiceGroup("Choose",Choice.EXCLUSIVE,choices,null);
        aDateField = new DateField(null,DateField.TIME);
        aGauge = new Gauge("Score",true,10,1);
        aTextField = new TextField("Comments","Your comments here",20,0);
        try {
            anImage = Image.createImage("/star.png");
        } catch (java.io.IOException ioE) {
            System.out.println("Problem reading image");
        }
        anImageItem = new ImageItem("Demo Image",
        anImage,ImageItem.LAYOUT_CENTER,"No image");
        aForm.append(aStringItem);
        aForm.append(aChoiceGroup);
        aForm.append(aDateField);
        aForm.append(aGauge);
        aForm.append(aTextField);
        aForm.append(anImageItem);
    }

    protected void startApp() {
        d = Display.getDisplay(this);
        d.setCurrent(aForm);
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unconditional) {
    }
}
