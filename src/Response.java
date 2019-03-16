import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Response {
    public HttpResponse SingleResponse(Socket socket)
    {
        HttpResponse response = null;
        try{
            InputStream input = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try
            {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) > -1 ) {
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
