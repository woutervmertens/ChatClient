//package ContentReading;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface ContentReader {
    byte[] readBody(BufferedReader reader) throws IOException;
}
