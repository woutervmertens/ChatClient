import java.io.IOException;
import java.util.HashMap;

public class ContentScan {
    String imageStart = "<img";
    String sourceStart = "src=\"";
    String imageNameBase = "img";
    HashMap<String, String> imageInfoList = new HashMap<>();
    int imageCount = 0;
    HtmlWriter writer = new HtmlWriter();

    public String Scan(String in)
    {
        imageInfoList.clear();
        if(in.contains(imageStart))
        {
            int lastIndex = 0;
            while(lastIndex != -1){

                lastIndex = in.indexOf(sourceStart,lastIndex);

                if(lastIndex != -1){
                    int srcstart = in.indexOf(sourceStart,lastIndex) + sourceStart.length();
                    int endsrcs = in.indexOf("\"",srcstart+1);
                    in = FilterSource(in,srcstart,endsrcs);
                    lastIndex += sourceStart.length();
                }
            }
            GetAllImages();
        }
        return in;
    }

    private String FilterSource(String str, int start, int end)
    {
        String source = str.substring(start,end);
        if(!source.startsWith("/")) source = "/" + source;
        String ext = ".jpg"; //Default value
        if(source.contains(".")){
            int extPos = source.lastIndexOf('.');
            ext = source.substring(extPos);
        }
        String local = imageNameBase + imageCount++ + ext;
        imageInfoList.put(local,source);
        return str.replace(source,local);
    }

    private void GetAllImages()
    {
        for (HashMap.Entry<String,String> entry : imageInfoList.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            Connection instance;
            instance = Connection.getInstance();
            try{
                HttpResponse httpResponse = instance.get(value);
                writer.CreateFileObject(key,"",httpResponse.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
