import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        response.setHttpVersion(splitFirstLine[0]);
        response.setStatusCode(Integer.parseInt(splitFirstLine[1]));
        response.setStatusMessage(splitFirstLine[2]);

        //Set response Headers
        HashMap<String, String> headers = new HashMap<>();
        response.setHeaders(headers);

        boolean loop = true;
        //read headers
        while (loop){
            String line = reader.readLine();
            if(line.equals("")) loop = false;
            else{
                String[] parts = line.split(": ",2);
                headers.put(parts[0],parts[1]);
            }
        }

        // TODO: 04/03/2019
        //Content generation

        String s;
        while ((s = reader.readLine()) != null){
            System.out.println(s);
        }
        return response;
    }
}
