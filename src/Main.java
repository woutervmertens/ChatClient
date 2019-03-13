import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws IOException {

        String domain = "tcpipguide.com";

        Connection con = new Connection(domain);
        HtmlWriter htmlWriter = new HtmlWriter();
        htmlWriter.CreateFolder(domain);
        HttpResponse httpResponse = con.get("/");
        htmlWriter.CreateFileBase(domain,domain,httpResponse.getContent());
        //HttpResponse httpResponse = con.get("/Jvh1OQm.jpg");
        //HttpResponse httpResponse = con.get("/images/branding/googlelogo/1x/googlelogo_white_background_color_272x92dp.png");



        //System.out.println(new String(httpResponse2.getContent()));

        //Path file = Paths.get("name.jpg");
        //Path file = Paths.get("img.png");
        //Files.write(file, httpResponse.getContent());


//        htmlWriter.CreateFolderAndFile("img","imgs",httpResponse2.getContent());


        con.close();
    }
}
