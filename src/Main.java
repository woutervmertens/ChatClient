import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException {

        String domain = "google.com";

        Connection con = new Connection(domain);
        HttpResponse httpResponse = con.get("/");


        System.out.println(new String(httpResponse.getContent()));

        con.close();
    }
}
