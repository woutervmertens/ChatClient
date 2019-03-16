//import ContentReading.ContentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResponseParser {

    private BufferedReader reader;
    private ByteArrayOutputStream input;

    public ResponseParser(ByteArrayOutputStream input) {
        this.input = input;
    }

    public HttpResponse parseAndClose() throws IOException {
        //Duplicate InputStream
        InputStream is = new ByteArrayInputStream(input.toByteArray());

        //Create Response
        HttpResponse response = new HttpResponse();
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
        response = ReadHeader(response);
        response = ReadBody(response);

        reader.close();
        is.close();
        return response;
    }

    private HttpResponse ReadHeader(HttpResponse response)
    {
        try {
            //Read and split first line
            String firstLine = reader.readLine();
            if (firstLine != null) {
                String[] splitFirstLine = firstLine.split(" ", 3);

                //Set first line properties
                //ex. HTTP/1.1 200 OK
                if (splitFirstLine.length >= 3) {
                    if (splitFirstLine[0].contains("HTTP")) {
                        response.setHasHeader(true);
                        response.setHttpVersion(splitFirstLine[0]);
                        response.setStatusCode(Integer.parseInt(splitFirstLine[1]));
                        response.setStatusMessage(splitFirstLine[2]);
                    } else {
                        response.setHasHeader(false);
                    }
                }else {
                    response.setHasHeader(false);
                }
            }else {
                response.setHasHeader(false);
            }
            //Set response Headers
            HashMap<String, String> headers = new HashMap<>();

            boolean loop = true;
            //readBody headers
            while (loop) {
                String line = reader.readLine();
                if (line == null || line.equals("")) loop = false;
                else {
                    System.out.println(line);
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) headers.put(parts[0], parts[1]);
                }
            }
            response.setHeaders(headers);
        }catch(IOException e) {e.printStackTrace();}

        return response;
    }

    private HttpResponse ReadBody(HttpResponse response)
    {
        Map<String, String> headers = response.getHeaders();
        ContentFactory.get().SetReader(headers);
        try{
            byte[] content = ContentFactory.get().reader().readBody(reader);
            response.setContent(content);
        }catch (IOException e) {e.printStackTrace();}

        return response;
    }

    public String[] ReadObjects(InputStream input)
    {
        BufferedInputStream sis = new BufferedInputStream(input);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[2048];
            for (int n; (n = sis.read(buffer)) != -1; )
                out.write(buffer, 0, n);
            sis.close();
            out.close();
        }catch (IOException e) {e.printStackTrace();}

        String output = new String(out.toByteArray());
        return output.split("\\r?\\n");
    }
}
