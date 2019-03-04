import java.io.*;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException {
        String domain = "www.google.com";

        Connection con = new Connection(domain);
        ArrayList<String> stringlist = con.get("/");

        for (String s : stringlist) {
            System.out.println(s);
        }

        con.close();
        //String domain = "example.com";

        //Socket socket = new Socket("google.com", 80);
        //Socket socket = new Socket(domain, 80);




        /*InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String t;
        while ((t = reader.readLine()) != null){
            System.out.println(t);
        }
        reader.close();
        socket.close();*/
    }
}
