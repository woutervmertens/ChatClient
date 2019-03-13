
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    private String domain;
    private int port;
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private PrintWriter writer;
    private BufferedReader reader;
    private static Connection instance = null;

    public static Connection getInstance(){ return instance; }

    public Connection(String domain) throws IOException {
        this(domain,80);
    }

    public Connection(String domain, int port) throws IOException {
        instance = this;
        this.domain = domain;

        this.port = port;
        socket = new Socket(domain, port);

        output = socket.getOutputStream();

        writer = new PrintWriter(output);
    }

    HttpResponse get(String resource) throws IOException {
        return send("GET "+resource);
    }

    HttpResponse send(String cmd) throws IOException {
        return send(cmd,false);
    }

    HttpResponse send(String cmd, boolean bMultiple) throws IOException {
        String total = cmd+" HTTP/1.1";
        System.out.println("Fire to ");
        System.out.println(total);
        System.out.println("Host: "+domain+":"+port);
        System.out.println();

        writer.println(total);
        writer.println("Host: "+domain+":"+port);
        if(bMultiple) writer.println("Connection: keep-alive");
        writer.println();
        writer.flush();

        return read();
    }

    private HttpResponse read() throws IOException {
        input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
        ResponseParser responseParser = new ResponseParser(reader);
        HttpResponse response = responseParser.parseAndClose();

        return response;
    }

    void close() throws IOException {
        reader.close();
        socket.close();
        instance = null;
    }

}
