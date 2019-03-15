
public class PutRequest extends Request {
    public PutRequest(Connection con) {
        super(con);
    }

    public HttpResponse Request(String resource, String toPut)
    {
        return super.send("PUT " + resource + " " + toPut);
    }
}
