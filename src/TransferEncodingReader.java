//package ContentReading;

import java.io.*;
import java.util.ArrayList;

public class TransferEncodingReader implements ContentReader {
    /**
     * Reads the Transfer-Encoding body and scans it
     * @param reader
     * @return body as byte array
     * @throws IOException
     */
    @Override
    public byte[] readBody(BufferedReader reader) throws IOException {
        ArrayList<byte[]> byteList = new ArrayList<>();
        ArrayList<String> strList = new ArrayList<>();

        int size=0;
        do {
            //Read the hex size and convert
            String hexSize = reader.readLine();
            if(hexSize.equals("")) { size = 0; continue; }
            size = readHexString(hexSize);

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            //Seperate reading and scanning to avoid not reading whole line
            String s = "";
            while(!(s = reader.readLine()).equals("0")){
                strList.add(s);
            }
            for(String str : strList)
            {
                str += '\n';
                byte[] b = str.getBytes();
                byteArray.writeBytes(b);
            }
            //Add the read lines to byteList
            byteList.add(byteArray.toByteArray());
        }while(size!=0);


        //Convert the byteList from List of byteArrays to 1 byteArray
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


