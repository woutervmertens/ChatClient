import java.io.IOException;
import java.util.Map;

public class HeadRequest extends Request{
    public HeadRequest(Connection con){
        super(con);
    }
    public HttpResponse Request(String resource)
    {
        //Send HEAD request
        HttpResponse res = super.send("HEAD " + resource);

        //Print response
        System.out.println("HEAD response: ");
        System.out.println(res.getHttpVersion() + " " + res.getStatusMessage() + " " + res.getStatusMessage());
        if(res.HasHeader())
        {
            for (Map.Entry<String,String> entry : res.getHeaders().entrySet())
            {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
        System.out.println(new String(res.getContent()));
        System.out.println();

        return res;
    }
}
