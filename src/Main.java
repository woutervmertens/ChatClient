import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws IOException {
        String domain = "example.com";

        Connection con = new Connection(domain);
        HttpResponse httpResponse = con.get("/");


        con.close();
    }
}
