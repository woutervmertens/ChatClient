
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Connection {
    private String domain;
    private int port;
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private PrintWriter writer;
    private static Connection instance = null;

    public static Connection getInstance(){ return instance; }

    public Connection(String domain) throws IOException {
        this(domain,80);
    }

    public Connection(String domain, int port) throws IOException {
        instance = this;

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

    HttpResponse get(Collection<String> resourceList) throws IOException {
        return send(resourceList, "GET ");
    }

    HttpResponse get(String resource) throws IOException {
        return send("GET "+resource);
    }

    HttpResponse head(String resource){
        return null;
    }

    HttpResponse put(String resource, String toPut){
        return null;
    }

    HttpResponse post(String resource, String toPost){
        return null;
    }

    HttpResponse send(String cmd) throws IOException {
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

    HttpResponse send(Collection<String> cmdList, String cmd) throws IOException {
        for(String c : cmdList)
        {
            String total = cmd+" HTTP/1.1";
            System.out.println("Fire to ");
            System.out.println(total);
            System.out.println("Host: "+domain+":"+port);
            System.out.println();

            writer.println(total);
            writer.println("Host: "+domain+":"+port);
            writer.println("Connection: keep-alive");
            writer.println();
            writer.flush();
        }


        return read();
    }

    private HttpResponse read() throws IOException {
        input = socket.getInputStream();

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
        HttpResponse response = responseParser.parseAndClose();

        baos.close();

        return response;
    }

    void close() throws IOException {
        socket.close();
        instance = null;
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
