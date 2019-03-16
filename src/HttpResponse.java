import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String httpVersion;
    private int statusCode;
    private String statusMessage;
    private Map<String,String> headers;
    private Map<String, String> objectInfos;

    private byte[] content;

    public HttpResponse() {
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getObjectInfos() {
        return objectInfos;
    }

    public void setObjectInfos(Map<String, String> objectInfos) {
        this.objectInfos = objectInfos;
    }

    public boolean hasObjects() {return this.objectInfos != null && !this.objectInfos.isEmpty();}

    public byte[] getContent() {
        return content;
    }

    public void addToContent(byte[] toAdd)
    {
        int contentLength = this.content.length;
        int addLength = toAdd.length;
        byte[] dest = new byte[contentLength + addLength];
        System.arraycopy(this.content,0,dest,0,contentLength);
        System.arraycopy(toAdd,0,dest,contentLength,addLength);
        this.content = dest;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean HasHeader() {return this.headers != null && !this.headers.isEmpty(); }
}
