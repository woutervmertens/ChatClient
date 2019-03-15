import java.io.IOException;

public class HeadRequest extends Request{
    public HeadRequest(Connection con){
        super(con);
    }
    public HttpResponse Request(String resource) throws IOException
    {
        return super.send("HEAD " + resource);
    }
}
