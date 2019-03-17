import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GetRequest extends Request {
    public String domain;
    private HtmlWriter htmlWriter;
    private Connection connection;
    private ContentScan scanner;
    public GetRequest(Connection con)
    {
        super(con);
        this.connection = con;
        this.domain = super.domain;
        htmlWriter = new HtmlWriter();
    }

    public void InitialRequest(String resource)
    {
        HttpResponse res = super.send("GET " + resource);
        if(res.getStatusCode() != 200) return;
        htmlWriter.CreateFolder(domain);
        AnalyseInitialRequest(res);
    }
    void AnalyseInitialRequest(HttpResponse res)
    {
        scanner = new ContentScan();
        String ext = res.getHeaders().get("Content-Type");
        if(ext != null && ext.contains("image"))
        {
            ext = "." + ext.substring(ext.indexOf('/') + 1);
            htmlWriter.CreateObjectFile(scanner.getLocal(0,ext),"",res.getContent());
            return;
        }

        String content = new String(res.getContent());
        String[] contentLines = content.split("\\r?\\n");

        int nLines = contentLines.length;
        scanner.ClearList();
        for (int i = 0; i < nLines; i++) {
            contentLines[i] = scanner.Scan(contentLines[i]);
        }

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        for(String str : contentLines)
        {
            byte[] b = str.getBytes();
            byteArray.writeBytes(b);
        }
        htmlWriter.CreateFileBase(domain,domain,byteArray.toByteArray());

        if(scanner.imageCount > 0)
        {
            AdditionalRequests();
        }
    }
    void AdditionalRequests()
    {
        System.out.println();
        System.out.println("Loading objects");
        System.out.println();
        //Reset socket
        super.socket = connection.ResetSocket();
        try {output = socket.getOutputStream();}
            catch(IOException e){e.printStackTrace();}
        writer = new PrintWriter(output);
        Collection<String> resources = scanner.imageInfoList.values();
        ArrayList<HttpResponse> response = new ArrayList<>();
        for(String res : resources)
        {

            PipelineSend("GET " + res);
        }

        response = super.response.MultipleResponses(super.socket);

        //Possible problem here, responses out of order -> wrong image wrong name
        int i = 0;
        for (HttpResponse res : response)
        {
            if(res.getStatusCode() == 200)
            {
                String ext= res.getHeaders().get("Content-Type");
                if(ext != null)
                {
                    ext = "." + ext.substring(ext.indexOf('/') + 1);
                    htmlWriter.CreateObjectFile(scanner.getLocal(i++,ext),"",res.getContent());
                }
                else {
                    String key = "";
                    for (Map.Entry<String, String> e : scanner.imageInfoList.entrySet()) {
                        if (e.getKey().startsWith(scanner.getLocal(i,""))) {
                            key = e.getKey();
                        }
                    }
                    if(key == "") key = "img" + i + ".jpg"; //default value
                    i++;
                    htmlWriter.CreateObjectFile(key,"",res.getContent());
                }
            }
        }
    }

    public void PipelineSend(String cmd)
    {
        String total = cmd + " HTTP/1.1";
        System.out.println("Fire to ");
        System.out.println(total);
        System.out.println("Host: " + domain + ":" + port);
        System.out.println("Connection: keep-alive");
        System.out.println();

        writer.println(total);
        writer.println("Host: "+domain+":"+port);
        writer.println("Connection: keep-alive");
        writer.println();
        writer.flush();
    }

}
