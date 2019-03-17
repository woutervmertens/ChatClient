import java.io.IOException;

public class HeadRequest extends Request{
    public HeadRequest(Connection con){
        super(con);
    }
    public HttpResponse Request(String resource)
    {
        return super.send("HEAD " + resource);
    }
}
