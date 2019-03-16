import java.util.Scanner;

public class PostRequest extends Request {
    public PostRequest(Connection con) {
        super(con);
    }

    public void Request(String resource)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Post message: ");
        String toPost = sc.nextLine();
        sc.close();
        HttpResponse res = super.send("POST " + resource + " " + toPost);
        System.out.println();
        System.out.println("POST Response: " + res.getStatusCode() + " " + res.getStatusMessage());
        System.out.println();
    }
}
