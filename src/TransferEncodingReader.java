//package ContentReading;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TransferEncodingReader implements ContentReader {
    @Override
    public byte[] readBody(BufferedReader reader) throws IOException {
        ArrayList<byte[]> byteList = new ArrayList<>();
        ContentScan contentScanner = new ContentScan();

        int size=0;
        do {
            String hexSize = reader.readLine();
            if(hexSize.equals("")) { size = 0; continue; }
            size = readHexString(hexSize);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            String s = "";
            while(!(s= reader.readLine()).equals("0")){
                s = contentScanner.Scan(s);
                s += '\n';
                byte[] b = s.getBytes();
                byteArray.writeBytes(b);
            }
            byteList.add(byteArray.toByteArray());
        }while(size!=0);

        //reuse size:0
        ByteArrayOutputStream returnArray = new ByteArrayOutputStream();
        for (byte[] byteArray : byteList) {
            returnArray.writeBytes(byteArray);
        }
        return returnArray.toByteArray();
    }

    private int readHexString(String hex){
        return Integer.parseInt(hex,16);
    }
}


