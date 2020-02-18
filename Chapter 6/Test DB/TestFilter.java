import javax.microedition.rms.*;

public class TestFilter implements RecordFilter {
    public boolean matches(byte[] rec) {
        String r = new String(rec);
        return ((r.charAt(0) == 'a') || (r.charAt(0) == 'A'));
    }
}