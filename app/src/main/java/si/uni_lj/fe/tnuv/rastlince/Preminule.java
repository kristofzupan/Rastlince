package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class Preminule extends AppCompatActivity {

    protected ListView lv;
    private ArrayList<HashMap<String, String>> rastlineModeliP;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.seznamRastlin);

        View ozadje = findViewById(R.id.ozadje);
        ozadje.setBackgroundResource(R.color.ozadje_preminule);

        TextView text = findViewById(R.id.seznamText);
        text.setText("Preminule rastline:");

        ImageButton plus = findViewById(R.id.dodajRastlino);
        plus.setVisibility(View.GONE);

        ImageButton main = findViewById(R.id.PreminuleIkona);
        main.setImageResource(R.drawable.kangla);
        main.setBackgroundResource(R.drawable.background);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        String path = "/storage/emulated/0/Android/data/si.uni_lj.fe.tnuv.rastlince/files/Pictures/";

        FileFilter filterJson = new FileFilter() {
            public boolean accept(File f) {
                System.out.println(f.getName());
                return (f.getName().startsWith("P") && f.getName().endsWith("json"));
            }
        };

        SimpleAdapter adapter = null;
        File directory = new File(path);
        File[] files = directory.listFiles(filterJson);
        System.out.print(files.length);
        String JsonString = "{\"rastlina\": [";

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File finalF = files[i];
                PrenosPodatkov pp = new PrenosPodatkov(finalF);
                String rezultat = pp.loadJson(finalF);
                if (i != 0) {
                    JsonString = JsonString + ",";
                }
                if (isUTF8(rezultat)) {
                    JsonString = JsonString + rezultat;
                } else {
                    break;
                }
            }
            JsonString += "]}";
            //System.out.println(JsonString);
            rastlineModeliP = new JSONParser().parseToArrayList(JsonString);

            adapter = new SimpleAdapter(
                    Preminule.this,
                    rastlineModeliP,
                    R.layout.list_view_row,
                    new String[]{"ime", "znanstveno ime", "sorta", "image"},
                    new int[]{R.id.rastlinaIme, R.id.rastlinaVrstaLat, R.id.rastlinaVrsta, R.id.rastlinaIkona}
            );
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Profil.class);
                    intent.putExtra("path", files[(int)id].getAbsolutePath());
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            Handler handler = new Handler(Looper.getMainLooper());
            SimpleAdapter finalAdapter = adapter;
            Thread thread = new Thread() {
                public void run() {
                    for (int i = 0; i < rastlineModeliP.size(); i++) {
                        HashMap<String, String> item = rastlineModeliP.get(i);
                        String imagePath = files[i].getAbsolutePath();
                        imagePath = imagePath.substring(0, imagePath.length() - 4) + "jpg";

                        File imgFile = new File(imagePath);
                        if (imgFile.exists()) {
                            item.put("image", imagePath);
                        } else {
                            int index = i % 9;
                            String drawablePath = "android.resource://" + getPackageName() + "/drawable/i"+index;
                            item.put("image", drawablePath);
                        }

                    }
                    runOnUiThread(() -> finalAdapter.notifyDataSetChanged());
                }
            };
            thread.start();
        }

    }

    public boolean isUTF8 (String input) {
        return Charset.forName("UTF-8").newEncoder().canEncode(input);
    };


}
