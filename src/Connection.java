import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    String domain;
    int port;
    Socket socket;
    OutputStream output;
    InputStream input;
    PrintWriter writer;
    BufferedReader reader;


    public Connection(String domain) throws IOException {
        this(domain,80);
    }

    public Connection(String domain, int port) throws IOException {
        this.domain = domain;
        this.port = port;
        socket = new Socket(domain, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        writer = new PrintWriter(output);
        reader = new BufferedReader(new InputStreamReader(input));
    }

    ArrayList<String> get(String resource) throws IOException {
        return send("GET "+resource);
    }
    ArrayList<String> send(String cmd) throws IOException {
        writer.println("GET /"+" HTTP/1.1");
        writer.println("Host: "+domain);
        writer.println();
        writer.flush();

        return read();
    }
    private ArrayList<String> read() throws IOException {
        String string;
        ArrayList<String> arrayList = new ArrayList<>();
        //String[] stringArray = (String[])reader.lines().toArray();
        boolean loop = true;
        int countdown = 10;
        while (loop){
            string = reader.readLine();
            arrayList.add(string);
            loop = (countdown-->0)?true:false;
        }
        reader.close();

        return arrayList;
    }

    void close() throws IOException {
        socket.close();
    }

}
