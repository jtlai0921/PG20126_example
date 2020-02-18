import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

public class TestDB extends MIDlet {

    public void startApp() {
        testDatabase();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    private void testDatabase() {
        try {
            RecordStore anRMS = RecordStore.openRecordStore("TestRMS" , true);
            anRMS.addRecordListener(new TestListener());

/*          String test = "A test";
            byte[] c = test.getBytes();
            anRMS.addRecord(c, 0, c.length);
            RecordFilter rf = new TestFilter();
            if (rf.matches(anRMS.getRecord(1)))
                System.out.println("The first record starts with 'A'");
            else
                System.out.println("The first record does not start with 'A'");
*/

			String test = "This is a test";
			byte[] b = test.getBytes();
			anRMS.addRecord(b, 8, 6);
			anRMS.addRecord(b, 5, 2);
			RecordComparator rc = new TestComparator();
			byte[] r1 = anRMS.getRecord(1);
			byte[] r2 = anRMS.getRecord(2);
			System.out.println("Comparator found --> " + rc.compare(r1,r2));

/*			byte[] george = "George".getBytes();
			byte[] bob = "Bob".getBytes();
			byte[] andy = "Andy".getBytes();
			byte[] harry = "Harry".getBytes();
			byte[] adam = "Adam".getBytes();
			byte[] amos = "Amos".getBytes();
			byte[] fred = "Fred".getBytes();
			anRMS.addRecord(george, 0, george.length);
			anRMS.addRecord(bob, 0, bob.length);
			anRMS.addRecord(andy, 0, andy.length);
			anRMS.addRecord(harry, 0, harry.length);
			anRMS.addRecord(adam, 0, adam.length);
			anRMS.addRecord(amos, 0, amos.length);
			anRMS.addRecord(fred, 0, fred.length);
			RecordComparator rc = new TestComparator();
			RecordFilter rf = new TestFilter();
			RecordEnumeration rEnum = anRMS.enumerateRecords(rf,rc,false);
			while (rEnum.hasNextElement()) {
				byte[] nextRec = rEnum.nextRecord();
				String nextName = new String(nextRec);
				System.out.println(nextName);
			}
			rEnum.destroy();
*/
		} catch (Exception e) {
		}
    }
}