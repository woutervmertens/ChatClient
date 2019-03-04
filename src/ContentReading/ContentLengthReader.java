package ContentReading;

import java.io.BufferedReader;
import java.io.IOException;

public class ContentLengthReader implements ContentReader {

    int contentLength;
    public ContentLengthReader(String contentLength) {
        this.contentLength = Integer.parseInt(contentLength);
    }

    @Override
    public byte[] readBody(BufferedReader reader) throws IOException {

        byte[] bytes = new byte[contentLength];
        for (int i = 0; i < contentLength; i++) {
            //if there is some unexplainable error check for byte overflow. (should not happen)
            byte b = (byte)reader.read();
            bytes[i] = b;
        }
        return bytes;
    }
}
