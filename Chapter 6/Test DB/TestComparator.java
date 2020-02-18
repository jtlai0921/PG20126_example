import javax.microedition.rms.*;

public class TestComparator implements RecordComparator {

    public int compare(byte[] rec1, byte[] rec2) {
        String r1 = new String(rec1);
        String r2 = new String(rec2);
        if (r1.compareTo(r2) > 0)
            return (RecordComparator.FOLLOWS);
        else if (r1.compareTo(r2) < 0)
            return (RecordComparator.PRECEDES);
        else return (RecordComparator.EQUIVALENT);
    }
}
