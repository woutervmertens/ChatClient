//import ContentReading.ContentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResponseParser {

    private BufferedReader reader;
    private ByteArrayOutputStream input;
    private HttpResponse response;

    public ResponseParser(){}
    public ResponseParser(ByteArrayOutputStream input) {
        this.input = input;
    }

    public HttpResponse parseAndClose() throws IOException {
        //Duplicate InputStream
        InputStream is = new ByteArrayInputStream(input.toByteArray());

        //Create Response
        response = new HttpResponse();
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
        ReadHeader();
        if(response.getHeaders().containsKey("Content-Type"))
        {
            String tempType = response.getHeaders().get("Content-Type");
            switch (tempType) {
                case "image/jpg":
                case "image/png":
                    ReadObject(is);
                    break;
                default:
                    ReadBody();
                    break;
            }
        }

        reader.close();
        is.close();
        return response;
    }

    private void ReadHeader()
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
                        response.setHttpVersion(splitFirstLine[0]);
                        response.setStatusCode(Integer.parseInt(splitFirstLine[1]));
                        response.setStatusMessage(splitFirstLine[2]);
                    } else {
                    }
                }else {
                }
            }else {
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
    }

    private void ReadBody()
    {
        Map<String, String> headers = response.getHeaders();
        ContentFactory.get().SetReader(headers);
        try{
            byte[] content = ContentFactory.get().reader().readBody(reader);
            response.setContent(content);
        }catch (IOException e) {e.printStackTrace();}
    }

    private void ReadObject(InputStream is)
    {
        BufferedInputStream sis = new BufferedInputStream(is);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            byte[] buffer = new byte[2048];
            for (int n; (n = sis.read(buffer)) != -1; )
                out.write(buffer, 0, n);
            sis.close();
            out.close();
        }catch (IOException e) {e.printStackTrace();}
    }

    public ArrayList<HttpResponse> ReadObjects(InputStream input)
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
        output = output.replaceAll("HTTP","\nHTTP");
        return ConvertToResponses(output.split("\\r?\\n"));
    }

    private ArrayList<HttpResponse> ConvertToResponses(String[] in)
    {
        ArrayList<HttpResponse> result = new ArrayList<>();
        int inLength = in.length;
        int next = 1;
        HttpResponse res = new HttpResponse();
        for (int i = 1; i < inLength; i += next)
        {
            next = 1;
            if(in[i].contains("HTTP"))
            {
                if(i != 1){
                    result.add(res);
                    res = new HttpResponse();
                }
                String[] splitFirstLine = in[i].split(" ", 3);
                res.setHttpVersion(splitFirstLine[0]);
                res.setStatusCode(Integer.parseInt(splitFirstLine[1]));
                res.setStatusMessage(splitFirstLine[2]);
                //Set response Headers
                HashMap<String, String> headers = new HashMap<>();

                boolean loop = true;
                //readBody headers
                while (loop) {
                    String line = in[i + next++];
                    if (line == null || line.equals("")) loop = false;
                    else {
                        String[] parts = line.split(": ", 2);
                        if (parts.length == 2) headers.put(parts[0], parts[1]);
                    }
                }
                res.setHeaders(headers);
                res.setContent(new byte[0]);
                continue;
            }
            else{
                res.addToContent(in[i].getBytes());
            }
        }

        return result;
    }
}
