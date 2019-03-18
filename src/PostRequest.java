import java.util.Scanner;

public class PostRequest extends Request {
    public PostRequest(Connection con) {
        super(con);
    }

    public void Request(String resource)
    {
        //Send POST request
        Scanner sc = new Scanner(System.in);
        System.out.println("Post message: ");
        String message = sc.nextLine();
        sc.close();
        HttpResponse res = super.sendMessage("POST " + resource, message);

        //Print response status
        System.out.println();
        System.out.println("POST Response: " + res.getStatusCode() + " " + res.getStatusMessage());
        System.out.println();
    }
}
