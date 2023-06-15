package si.uni_lj.fe.tnuv.rastlince;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private ListView lv;
    private ArrayList<HashMap<String, String>> rastlineModeli;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton dodajRastlino = findViewById(R.id.dodajRastlino);

        ImageButton preminule = findViewById(R.id.PreminuleIkona);
        //preminule.setImageResource(R.drawable.grave);

        lv = findViewById(R.id.seznamRastlin);

        dodajRastlino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    enableCamera();
                } else {
                    requestPermission();
                }
            }
        });

        preminule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Preminule.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        String path = "/storage/emulated/0/Android/data/si.uni_lj.fe.tnuv.rastlince/files/Pictures/";



        String s = "";

        FileFilter filterJson = new FileFilter() {

            public boolean accept(File f)
            {
                return f.getName().endsWith("json");
            }
        };

        File directory = new File(path);
        File[] file = directory.listFiles(filterJson);

        for (int i = 0; i < file.length; i++) {
            File finalF = file[i];
            PrenosPodatkov pp = new PrenosPodatkov(finalF);
            new Thread() {
                public void run() {
                    String rezultat = pp.loadJson(finalF);
                    runOnUiThread(() -> prikaziPodatke(rezultat));
                }
            }.start();
        };


      /*  }*/
    }

    private void prikaziPodatke(String rezultat) {
        System.out.println("main 97:" + rezultat.getClass());
        rastlineModeli = new JSONParser().parseToArrayList(rezultat);
        System.out.println("main 99000:" + rastlineModeli);
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                rastlineModeli,
                R.layout.list_view_row,
                new String[] {"ime", "znanstveno ime", "sorta"},
                new int[] {R.id.rastlinaIme, R.id.rastlinaVrstaLat, R.id.rastlinaVrsta}
        );

        // vstavi v activity_main (xml)
        lv.setAdapter(adapter);

    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
        System.out.println("NNNNNNNNNNNNNNNNNNNNOW");
        if (hasCameraPermission()) {
            enableCamera();
        }
    }

    private void enableCamera() {
        Intent intent = new Intent(this, Kamera.class);
        startActivity(intent);
    }
}