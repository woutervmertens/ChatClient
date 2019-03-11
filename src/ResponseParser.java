//import ContentReading.ContentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class ResponseParser {

    private BufferedReader reader;

    public ResponseParser(BufferedReader reader) {
        this.reader = reader;
    }

    public HttpResponse parseAndClose() throws IOException {
        //Create Response
        HttpResponse response = new HttpResponse();


        //Read and split first line
        String firstLine = reader.readLine();
        String[] splitFirstLine = firstLine.split(" ",3);

        //Set first line properties
        //ex. HTTP/1.1 200 OK
        if(splitFirstLine.length >= 3){
            response.setHttpVersion(splitFirstLine[0]);
            response.setStatusCode(Integer.parseInt(splitFirstLine[1]));
            response.setStatusMessage(splitFirstLine[2]);
        }
        /*else
        {
            //default values
            response.setHttpVersion("HTTP/1.1");
            response.setStatusCode(200);
            response.setStatusMessage("OK");
        }*/

        //Set response Headers
        HashMap<String, String> headers = new HashMap<>();
        response.setHeaders(headers);



        boolean loop = true;
        //readBody headers
        while (loop){
            String line = reader.readLine();
            if(line.equals("")) loop = false;
            else{
                System.out.println(line);
                String[] parts = line.split(": ",2);
                if(parts.length == 2)headers.put(parts[0],parts[1]);
            }
        }

        //  -------------|--Factory-Singleton|--new-Instance---|--methods--|
        byte[] content = ContentFactory.get().reader(headers).readBody(reader);
        response.setContent(content);

        //reader.close();
        return response;
    }
}
