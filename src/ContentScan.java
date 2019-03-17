import java.util.LinkedHashMap;

public class ContentScan {
    private String imageStart = "<img";
    private String sourceStart = "src=\"";
    private String imageNameBase = "img";
    private String blocked = "ads";
    public LinkedHashMap<String, String> imageInfoList = new LinkedHashMap<>();
    public int imageCount = 0;
    public int adCount = 0;
    //HtmlWriter writer = new HtmlWriter();

    public void ClearList()
    {
        imageInfoList.clear();
    }

    //Scans string for imageStart
    //  if found: loops scan for sourceStart, next loop starts at last sourceStart position
    //      if sourceStart found: Filter from start to end of source
    //  Download all images
    //returns the filtered string
    public String Scan(String in)
    {
        if(in.contains(imageStart))
        {
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
        String local;
        if(source.contains(blocked))
        {
            local = "../ad.jpg";
        }
        else
        {
            local = getLocal(imageCount++,ext);
            imageInfoList.put(local,source);
        }
        return str.replace(origSource,local);
    }

    public String getLocal(int i, String ext)
    {
        return imageNameBase + i + ext;
    }
}
