//package ContentReading;

import java.util.Map;

public class ContentFactory {
    private static ContentFactory instance = new ContentFactory();
    private ContentFactory() {
    }
    public static ContentFactory get() {
        return instance;
    }
    public static ContentReader reader(Map<String,String> headers){

        if(headers.containsKey("Content-Length"))
            return new ContentLengthReader(headers.get("Content-Length"));
        else if(headers.containsKey("Transfer-Encoding"))
            return new TransferEncodingReader();
        else return null;
    }

}
