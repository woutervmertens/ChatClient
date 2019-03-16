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
        InputStream is2 = new ByteArrayInputStream(input.toByteArray());

        //Create Response
        response = new HttpResponse();
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
        ReadHeader();
        if(response.getHeaders().containsKey("Content-Type"))
        {
            String tempType = response.getHeaders().get("Content-Type");
            switch (tempType) {
                case "image/jpg":
                case "image/jpeg":
                case "image/png":
                    ReadObject(is2);
                    break;
                default:
                    ReadBody();
                    break;
            }
        }

        reader.close();
        is.close();
        is2.close();
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

    //HTML
    private void ReadBody()
    {
        Map<String, String> headers = response.getHeaders();
        ContentFactory.get().SetReader(headers);
        try{
            byte[] content = ContentFactory.get().reader().readBody(reader);
            response.setContent(content);
        }catch (IOException e) {e.printStackTrace();}
    }

    //Single object
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

        byte[] object = out.toByteArray();
        response = ConvertToResponse(object);

    }

    //Splits up header and content, reads header as strings, keeps content as byte[], fills them into HttpResponse
    private HttpResponse ConvertToResponse(byte[] input)
    {
        InputStream headerStream = new ByteArrayInputStream(input);
        InputStream contentStream = new ByteArrayInputStream(input);

        int contentStart = 0;
        int totalLength = input.length;
        HttpResponse res = new HttpResponse();

        //Header
        BufferedReader hedRed = new BufferedReader(new InputStreamReader(headerStream));
        String line;
        try {
            line = hedRed.readLine();
            if(line != null && line.contains("HTTP")) {
                contentStart += line.getBytes().length;
                String[] splitFirstLine = line.split(" ", 3);
                res.setHttpVersion(splitFirstLine[0]);
                res.setStatusCode(Integer.parseInt(splitFirstLine[1]));
                res.setStatusMessage(splitFirstLine[2]);
                //Set response Headers
                HashMap<String, String> headers = new HashMap<>();
                boolean loop = true;
                //readBody headers
                while (loop) {
                    line = hedRed.readLine();
                    contentStart += line.getBytes().length + 2;
                    if (line == null || line.equals("")) {
                        loop = false;
                        contentStart += 2;
                    }
                    else {
                        String[] parts = line.split(": ", 2);
                        if (parts.length == 2) headers.put(parts[0], parts[1]);
                    }
                }
                res.setHeaders(headers);
            }
            hedRed.close();
        }catch(IOException e) {e.printStackTrace();}

        //Content
        int contentLength = totalLength - contentStart;
        byte[] result = new byte[contentLength];
        try {
            contentStream.skip(contentStart);
            contentStream.readNBytes(result,0,contentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }

        res.setContent(result);

        return res;
    }

    //Multiple objects
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

        /*String output = new String(out.toByteArray());
        output = output.replaceAll("HTTP","\nHTTP");
        return ConvertToResponses(output.split("\\r?\\n"));*/

        return ConvertToResponses(SplitByteArrays(out.toByteArray()));
    }

    private ArrayList<byte[]> SplitByteArrays(byte[] input)
    {
        InputStream countStream = new ByteArrayInputStream(input);
        InputStream divideStream = new ByteArrayInputStream(input);
        ArrayList<byte[]> result = new ArrayList<>();
        String count = "";
        try {
            count = new String(countStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] counts = count.split("(?=HTTP)");

        int start = 0;
        int len;
        byte[] check = "HTTP".getBytes();
        int inputLen = input.length;
        for (String str : counts)
        {
            if(str.length() == 0) continue;
            len = 0;
            int i = start + 1;
            while (input[i] != check[0]
                    || input[i+1] != check[1]
                    || input[i+2] != check[2]
                    || input[i+3] != check[3]){
                len++;
                i++;
                if(i + 3 >= inputLen) {
                    len = inputLen - start - 1;
                    break;
                }
            }
            len++;
            try {
                byte[] b = divideStream.readNBytes(len);
                result.add(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            start += len;
        }

        return result;
    }

    private ArrayList<HttpResponse> ConvertToResponses(ArrayList<byte[]> in)
    {
        ArrayList<HttpResponse> result = new ArrayList<>();

        for(byte[] ar : in)
        {
            result.add(ConvertToResponse(ar));
        }

        return result;
    }
}
