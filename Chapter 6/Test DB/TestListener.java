import javax.microedition.rms.*;

public class TestListener implements RecordListener {
    public void recordAdded(RecordStore rs, int id) {
        try {
            System.out.println(rs.getName() + " added record " + id);
        } catch (RecordStoreNotOpenException e) {
            //exception handling procedures
        }
    }

    public void recordChanged(RecordStore rs, int id) {
        try {
            System.out.println(rs.getName() + " changed record " + id);
        } catch (RecordStoreNotOpenException e) {
            //exception handling procedures
        }
    }

    public void recordDeleted(RecordStore rs, int id) {
        try {
            System.out.println(rs.getName() + " removed record " + id);
        } catch (RecordStoreNotOpenException e) {
            //exception handling procedures
        }
    }
}
