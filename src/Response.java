import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Response {
    //Reads incoming data and gets it converted into a HttpResponse, then returns it
    public HttpResponse SingleResponse(Socket socket)
    {
        HttpResponse response = null;
        try{
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try
            {
                byte[] buffer = new byte[2048];
                int len;
                while ((len = input.read(buffer)) > 1 || socket.getInputStream().available() > 0) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            ResponseParser responseParser = new ResponseParser(baos);
            response = responseParser.parseAndClose();

            baos.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return response;
    }

    //Gets all the data read and sends back the returned HttpResponse list
    public ArrayList<HttpResponse> MultipleResponses(Socket socket)
    {
        ArrayList<HttpResponse> response = new ArrayList<>();
        try{
            InputStream input = socket.getInputStream();
            ResponseParser responseParser = new ResponseParser();
            response = responseParser.ReadObjects(input);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
