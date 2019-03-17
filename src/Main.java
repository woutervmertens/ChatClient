import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {

        //String domain = "tcpipguide.com";
        String command = args[0];
        String domain = args[1];
        int port = 80;
        port = Integer.parseInt(args[2]);

        Connection con = new Connection(domain,port);
        String resource = con.GetResource();
        switch (command)
        {
            case "GET":
                con.get(resource);
                break;
            case "POST":
                con.post(resource);
                break;
            case "PUT":
                con.put(resource);
                break;
            case "HEAD":
                con.put(resource);
                break;
        }

        con.close();
    }
}
