//package ContentReading;

import java.io.*;
import java.util.ArrayList;

public class ContentLengthReader implements ContentReader {
    /**
     * Reads the Content-Length body and scans it
     * @param reader
     * @return body as byte array
     * @throws IOException
     */
    @Override
    public byte[] readBody(BufferedReader reader) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ContentScan contentScanner = new ContentScan();
        ArrayList<String> strList = new ArrayList<>();
        String s = reader.readLine();

        //Read all the lines and add them to strList
        while (s  != null) {
            strList.add(s);
            s = reader.readLine();
        }

        //Scan all the strings and convert to bytes
        for(String str : strList)
        {
            str = contentScanner.Scan(str) + '\n';
            byte[] b = str.getBytes();
            baos.writeBytes(b);
        }
        return baos.toByteArray();
    }

    @Override
    public byte[] readImage(BufferedInputStream input, String ext) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            try {
                byte[] buffer = new byte[2048];
                for (int n; (n = input.read(buffer)) != -1; )
                    out.write(buffer, 0, n);
            } finally {
                input.close();
            }
        } finally {
            out.close();
        }
        return out.toByteArray();
    }
}
