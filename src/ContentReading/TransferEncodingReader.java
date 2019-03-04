package ContentReading;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class TransferEncodingReader implements ContentReader {
    @Override
    public byte[] readBody(BufferedReader reader) throws IOException {
        int byteCount = 0;
        ArrayList<byte[]> byteList = new ArrayList<>();

        int size=0;
        do {
            String hexSize = reader.readLine();
            if(hexSize.equals("")) continue;
            size = readHexString(hexSize);
            byteCount += size;
            byte[] byteArray = new byte[size];
            for (int i = 0; i < size; i++) {
                byteArray[i] = (byte) reader.read();
            }
            byteList.add(byteArray);
        }while(size!=0);

        //reuse size:0
        byte[] returnArray = new byte[byteCount];
        for (byte[] byteArray : byteList) {
            for (byte b : byteArray) {
                returnArray[size++] = b;
            }
        }
        return returnArray;
    }

    private int readHexString(String hex){
        return Integer.parseInt(hex,16);
    }
}


