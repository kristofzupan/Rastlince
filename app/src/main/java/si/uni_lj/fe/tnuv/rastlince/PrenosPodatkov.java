package si.uni_lj.fe.tnuv.rastlince;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PrenosPodatkov extends AppCompatActivity{

    private final File file;

    String path = "/storage/emulated/0/Android/data/si.uni_lj.fe.tnuv.rastlince/files/Pictures/";


    public PrenosPodatkov(File file) {
        this.file = file;
    }

    public String loadJson(File file) {
        //dobi file
        File directory = new File(path);



        String s = "";
        try {
            InputStream is = new FileInputStream(file);
            s += convertStreamToString(is);
        } catch (Exception e){
            Log.e("TAG", "load Json: error " + e);
        }
        return s;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }




}
