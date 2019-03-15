import java.io.*;

public class HtmlWriter {
    private static String lastFolderName;

    public boolean CreateFileBase(String folderName, String title, byte[] content)
    {
        lastFolderName = folderName;
        return CreateFile(folderName, title,".html",content);
    }

    public boolean CreateObjectFile(String title, String extention, byte[] content)
    {
        return CreateFile(lastFolderName,title,extention,content);
    }

    private boolean CreateFile(String folderName, String title, String extention, byte[] content){
        File file = new File(folderName + "/" + title + extention);
        try{
            FileOutputStream fop = new FileOutputStream(file);
            if(!file.exists()) {file.createNewFile();}

            fop.write(content);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean CreateFolder(String name){
        lastFolderName = name;
        return new File(name).mkdir();
    }
}
