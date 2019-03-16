import java.io.*;
import java.net.Socket;

public class Request {

    protected String domain;
    protected int port;
    protected Socket socket;
    protected Response response;
    protected PrintWriter writer;
    private OutputStream output;
    public Request(Connection con)
    {
        this.domain = con.GetDomain();
        this.port = con.GetPort();
        this.socket = con.GetSocket();
        response = new Response();
        try {
            output = socket.getOutputStream();
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        writer = new PrintWriter(output);
    }

    public HttpResponse send(String cmd)
    {
        String total = cmd+" HTTP/1.1";
        System.out.println("Fire to ");
        System.out.println(total);
        System.out.println("Host: "+domain+":"+port);
        System.out.println();

        writer.println(total);
        writer.println("Host: "+domain+":"+port);
        writer.println();
        writer.flush();

        return read();
    }

    public HttpResponse read()
    {
        return response.SingleResponse(socket);
    }
}