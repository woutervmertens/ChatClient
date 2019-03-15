import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
    public HttpResponse MultipleResponses(Socket socket)
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
}
