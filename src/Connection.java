import java.io.*;
import java.net.Socket;

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
//        String[] parts = domain.split("\\.",2);//takes a regex and '.' has a special meaning

//        if(parts[0].equals("www")) this.domain = domain;
//        else                       this.domain = "www."+domain;
        this.domain = domain;

        this.port = port;
        socket = new Socket(domain, port);

        output = socket.getOutputStream();
        input = socket.getInputStream();

        writer = new PrintWriter(output);
        reader = new BufferedReader(new InputStreamReader(input, "Cp1252"));
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
//        while (true){
//            String line = reader.readLine();
//            if(line.equals("")) return null;
//            System.out.println(line);
//            if(false) break;
//        }


        ResponseParser responseParser = new ResponseParser(reader);
        HttpResponse response = responseParser.parseAndClose();

        return response;
    }

    void close() throws IOException {
        socket.close();
    }

}
