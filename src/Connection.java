import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private String domain;
    private int port;
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private PrintWriter writer;
    private BufferedReader reader;


    public Connection(String domain) throws IOException {
        this(domain,80);
    }

    public Connection(String domain, int port) throws IOException {

        //Add www in front of the address if it is not present
        String[] parts = domain.split("\\.",2);//takes a regex and '.' has a special meaning

        if(parts[0].equals("www")) this.domain = domain;
        else                       this.domain = "www."+domain;

        this.port = port;
        socket = new Socket(domain, port);

        output = socket.getOutputStream();
        input = socket.getInputStream();

        writer = new PrintWriter(output);
        reader = new BufferedReader(new InputStreamReader(input));
    }

    HttpResponse get(String resource) throws IOException {
        return send("GET "+resource);
    }
    HttpResponse send(String cmd) throws IOException {
        writer.println(cmd+" HTTP/1.1");
        writer.println("Host: "+domain);
        writer.println();
        writer.flush();

        return read();
    }
    private HttpResponse read() throws IOException {
        String string;
        ArrayList<String> arrayList = new ArrayList<>();

        ResponseParser responseParser = new ResponseParser(reader);
        HttpResponse response = responseParser.parseAndClose();

        return response;
    }

    void close() throws IOException {
        socket.close();
    }

}
