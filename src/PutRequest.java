import java.util.Scanner;

public class PutRequest extends Request {
    public PutRequest(Connection con) {
        super(con);
    }

    public void Request(String resource)
    {
        //Send PUT request
        Scanner sc = new Scanner(System.in);
        System.out.println("Put message: ");
        String message = sc.nextLine();
        sc.close();
        HttpResponse res = super.sendMessage("PUT " + resource, message);

        //Print response status
        System.out.println();
        System.out.println("PUT Response: " + res.getStatusCode() + " " + res.getStatusMessage());
        System.out.println();
    }
}
