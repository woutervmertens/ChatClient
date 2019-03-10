import java.io.*;
import java.util.ArrayList;

public class HtmlWriter {
    /*public boolean CreateFile(String folderName, String title, ArrayList<String> arrayList){
        Writer writer = null;
        File file = new File(folderName + "/" + title + ".html");
        try{
            FileOutputStream fop = new FileOutputStream(file);
            if(!file.exists()) {file.createNewFile();}

            writer = new BufferedWriter(
                new OutputStreamWriter(fop));

            for(int i = 0; i<arrayList.size();i++) {
                writer.write(arrayList.get(i));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }*/

    public boolean CreateFile(String folderName, String title, byte[] content){
        File file = new File(folderName + "/" + title + ".html");
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
        return new File(name).mkdir();
    }

    public boolean CreateFolderAndFile(String folderName, String fileName, byte[] content){
        if(!CreateFolder(folderName)) return false;
        return CreateFile(folderName,fileName,content);
    }
}
