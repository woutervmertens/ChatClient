import java.util.ArrayList;

public class GetRequest extends Request {
    public String domain;
    public String host;
    public GetRequest(Connection con)
    {
        super(con);
        this.domain = super.domain;
    }

    public HttpResponse InitialRequest(String resource)
    {
        HttpResponse res = super.send("GET " + resource);
        return AnalyseInitialRequest(res);
    }
    HttpResponse AnalyseInitialRequest(HttpResponse res)
    {


        return res;
    }
    HttpResponse AdditionalRequests(ArrayList<String> resources)
    {
        for(String res : resources) PipelineSend(res);

        return super.response.MultipleResponses(super.socket);
    }

    public void PipelineSend(String cmd)
    {
        String total = cmd+" HTTP/1.1";
        System.out.println("Fire to ");
        System.out.println(total);
        System.out.println("Host: "+domain+":"+port);
        System.out.println("Connection: keep-alive");
        System.out.println();

        writer.println(total);
        writer.println("Host: "+domain+":"+port);
        writer.println("Connection: keep-alive");
        writer.println();
        writer.flush();
    }

}
