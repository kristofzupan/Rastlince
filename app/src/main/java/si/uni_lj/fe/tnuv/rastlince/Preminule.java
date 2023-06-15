package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        File directory = new File(path);
        File[] files = directory.listFiles(filterJson);
        System.out.print(files.length);
        String JsonString = "{\"rastlina\": [";

        for (int i = 0; i < files.length; i++) {
            File finalF = files[i];
            PrenosPodatkov pp = new PrenosPodatkov(finalF);
            String rezultat = pp.loadJson(finalF);
            System.out.println("main 99: " + rezultat);
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

        SimpleAdapter adapter = new SimpleAdapter(
                Preminule.this,
                rastlineModeliP,
                R.layout.list_view_row,
                new String[]{"ime", "znanstveno ime", "sorta", "image"},
                new int[]{R.id.rastlinaIme, R.id.rastlinaVrstaLat, R.id.rastlinaVrsta, R.id.rastlinaIkona}
        );
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> clickedItem = (Map<String, String>) parent.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), Profil.class);

                intent.putExtra("ime", clickedItem.get("ime"));
                intent.putExtra("znanstveno ime", clickedItem.get("znanstveno ime"));
                intent.putExtra("sorta", clickedItem.get("sorta"));
                intent.putExtra("image", clickedItem.get("image"));
                startActivity(intent);
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread() {
            public void run() {
                for (int i = 0; i < rastlineModeliP.size(); i++) {
                    HashMap<String, String> item = rastlineModeliP.get(i);
                    String imagePath = files[i].getAbsolutePath();
                    imagePath = imagePath.substring(0, imagePath.length() - 4) + "jpg";

                    ImageView imageView = new ImageView(Preminule.this);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(myBitmap);

                    item.put("image", imagePath); // Add the image path to the HashMap
                }
                System.out.println("149:" + rastlineModeliP);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        };
        thread.start();


    }

    public boolean isUTF8 (String input) {
        return Charset.forName("UTF-8").newEncoder().canEncode(input);
    };


}
