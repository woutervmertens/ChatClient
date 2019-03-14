//import ContentReading.ContentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ResponseParser {

    private BufferedReader reader;
    private ByteArrayOutputStream input;

    private enum ResponseType{
        HTML,
        IMAGE
    }
    private ResponseType type;

    public ResponseParser(ByteArrayOutputStream input) {
        this.input = input;
    }

    public HttpResponse parseAndClose() throws IOException {
        //Duplicate InputStream
        InputStream is1 = new ByteArrayInputStream(input.toByteArray());
        InputStream is2 = new ByteArrayInputStream(input.toByteArray());

        //Create Response
        HttpResponse response = new HttpResponse();
        reader = new BufferedReader(new InputStreamReader(is1, StandardCharsets.ISO_8859_1));
        //Read and split first line
        String firstLine = reader.readLine();
        if(firstLine != null) {
            String[] splitFirstLine = firstLine.split(" ", 3);

            //Set first line properties
            //ex. HTTP/1.1 200 OK
            if (splitFirstLine.length >= 3) {
                if (splitFirstLine[0].contains("HTTP")) {
                    response.setHttpVersion(splitFirstLine[0]);
                    response.setStatusCode(Integer.parseInt(splitFirstLine[1]));
                    response.setStatusMessage(splitFirstLine[2]);
                } else {
                    //default values (for images)
                    response.setHttpVersion("HTTP/1.1");
                    response.setStatusCode(200);
                    response.setStatusMessage("OK");
                }
            }
        }
        //Set response Headers
        HashMap<String, String> headers = new HashMap<>();
        response.setHeaders(headers);
        long headerByteCounter = 0;
        if(firstLine != null)headerByteCounter += firstLine.length() + 4; // +4 because /r/n (+2) for first and last line
        boolean loop = true;
        //readBody headers
        while (loop){
            String line = reader.readLine();
            if(line == null || line.equals("")) loop = false;
            else{
                System.out.println(line);
                headerByteCounter += line.length() + 2;
                String[] parts = line.split(": ",2);
                if(parts.length == 2)headers.put(parts[0],parts[1]);
            }
        }

        //  -------------|--Factory-Singleton|--new-Instance---|--methods--|
        ContentFactory.get().SetReader(headers);
        String imageExt = "";
        if(headers.containsKey("Content-Type"))
        {
            String tempType = headers.get("Content-Type");
            switch (tempType)
            {
                case "image/jpg":
                case "image/png":
                    type = ResponseType.IMAGE;
                    imageExt = tempType.substring(6);
                    break;
                default:
                    type = ResponseType.HTML;
                    break;
            }
        }
        else type = ResponseType.IMAGE;
        byte[] content;
        switch (type)
        {
            case HTML:
                content = ContentFactory.get().reader().readBody(reader);
                response.setContent(content);
                break;
            case IMAGE:
                is2.skip(headerByteCounter);
                BufferedInputStream sis = new BufferedInputStream(is2);
                content = ContentFactory.get().reader().readImage(sis, imageExt);
                response.setContent(content);
                break;
        }
        reader.close();
        is1.close();
        is2.close();
        return response;
    }
}
