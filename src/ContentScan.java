import java.io.IOException;
import java.util.HashMap;

public class ContentScan {
    String imageStart = "<img";
    String sourceStart = "src=\"";
    String imageNameBase = "img";
    HashMap<String, String> imageInfoList = new HashMap<>();
    int imageCount = 0;
    HtmlWriter writer = new HtmlWriter();

    //Scans string for imageStart
    //  if found: loops scan for sourceStart, next loop starts at last sourceStart position
    //      if sourceStart found: Filter from start to end of source
    //  Download all images
    //returns the filtered string
    public String Scan(String in)
    {
        if(in.contains(imageStart))
        {
            imageInfoList.clear();
            int lastIndex = 0;
            while(lastIndex != -1){

                lastIndex = in.indexOf(sourceStart,lastIndex);

                if(lastIndex != -1){
                    int srcstart = in.indexOf(sourceStart,lastIndex) + sourceStart.length();
                    int endsrcs = in.indexOf("\"",srcstart+1);
                    if(endsrcs != -1)
                    {
                        in = FilterSource(in,srcstart,endsrcs);
                    }
                    lastIndex += sourceStart.length();
                }
            }
            GetAllImages();
        }
        return in;
    }

    //Take the source substring, save it in map and replace it with the name of the local file
    private String FilterSource(String str, int start, int end)
    {
        String source = str.substring(start,end);
        String origSource = source;
        if(!source.startsWith("/")) source = "/" + source;
        String ext = ".jpg"; //Default value
        if(source.contains(".")){
            int extPos = source.lastIndexOf('.');
            ext = source.substring(extPos);
        }
        String local = imageNameBase + imageCount++ + ext;
        imageInfoList.put(local,source);
        return str.replace(origSource,local);
    }

    //GET request all the images and save them locally
    private void GetAllImages()
    {
        Connection instance;
        instance = Connection.getInstance();
        HttpResponse httpResponse = null;
        try {
            httpResponse = instance.get(imageInfoList.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (HashMap.Entry<String,String> entry : imageInfoList.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            writer.CreateFileObject(key,"",httpResponse.getContent());
        }
    }
}
