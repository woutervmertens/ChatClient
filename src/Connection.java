
import java.io.*;
import java.net.Socket;

public class Connection {
    private String domain;
    private int port;
    private Socket socket;
    private OutputStream output;
    private PrintWriter writer;

    public Connection(String domain) throws IOException {
        this(domain,80);
    }

    public Connection(String domain, int port) throws IOException {
        //Remove unaccepted parts
        if(domain.startsWith("https://"))domain = domain.replaceFirst("https://","");
        if(domain.startsWith("http://"))domain = domain.replaceFirst("http://","");
        if(domain.startsWith("www."))domain = domain.replaceFirst("www.","");
        if(domain.endsWith("/"))domain = domain.substring(0, domain.length() - 1);

        this.domain = domain;

        this.port = port;
        socket = new Socket(domain, port);

        output = socket.getOutputStream();

        writer = new PrintWriter(output);
    }

    void get(String resource) {
        GetRequest getRequest = new GetRequest(this);
        getRequest.InitialRequest(resource);
    }

    HttpResponse head(String resource){
        return null;
    }

    HttpResponse put(String resource, String toPut){
        return null;
    }

    void post(String resource){
        PostRequest postRequest = new PostRequest(this);
        postRequest.Request(resource);
    }

    public Socket ResetSocket()
    {
        try{
            socket.close();
            socket = new Socket(domain, port);
        }catch (IOException e)
        {e.printStackTrace();}
        return socket;
    }

    void close() throws IOException {
        socket.close();
    }

    public String GetDomain()
    {
        return domain;
    }
    public int GetPort()
    {
        return port;
    }
    public Socket GetSocket()
    {
        return socket;
    }

}
