//package ContentReading;

import java.util.Map;

public class ContentFactory {
    private static ContentFactory instance = new ContentFactory();
    private enum ReaderType{
        ContentLength,
        TransferEncoding,
        Unknown,
        NotSet
    }
    private static ReaderType type = ReaderType.NotSet;
    private static ReaderType lastValidType;

    private ContentFactory() {
    }
    public static ContentFactory get() {
        return instance;
    }

    //Set the type of reader
    public static void SetReader(Map<String,String> headers){
        if(headers.containsKey("Content-Length"))
        {
            type = ReaderType.ContentLength;
            lastValidType = type;
        }
        else if(headers.containsKey("Transfer-Encoding")) {
            type = ReaderType.TransferEncoding;
            lastValidType = type;
        }
        else type = ReaderType.Unknown;
    }

    //Link the correct reader
    public static ContentReader reader(){

        if(type == ReaderType.Unknown)
            type = lastValidType;
        if(type == ReaderType.ContentLength)
            return new ContentLengthReader();
        else if(type == ReaderType.TransferEncoding)
            return new TransferEncodingReader();
        else return null;
    }

}
