//package ContentReading;

import java.io.BufferedReader;
import java.io.IOException;

public interface ContentReader {
    byte[] readBody(BufferedReader reader) throws IOException;
}
