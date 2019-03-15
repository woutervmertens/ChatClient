public class PostRequest extends Request {
    public PostRequest(Connection con) {
        super(con);
    }

    public HttpResponse Request(String resource, String toPost)
    {
        return super.send("POST " + resource + " " + toPost);
    }
}
