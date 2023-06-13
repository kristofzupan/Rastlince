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
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    ArrayList<RastlinaModel> rastlineModeli = new ArrayList<>();
    int[] rastlineSlike = {R.drawable.e, R.drawable.b, R.drawable.c, R.drawable.d};
    private RecyclerViewAdapter.ClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.seznamRastlin);
        pripraviModeleRastlin();

        RVsetOnClickListener();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, rastlineModeli, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button dodajRastlino = findViewById(R.id.dodajRastlino);

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
    }

    private void RVsetOnClickListener() {
        listener = new RecyclerViewAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), Profil.class);
                intent.putExtra("ime", rastlineModeli.get(position).getRastlinaIme());
                intent.putExtra("vrsta", rastlineModeli.get(position).getRastlinaVrsta());
                intent.putExtra("vrstaLat", rastlineModeli.get(position).getRastlinaVrstaLat());
                //intent.putExtra("img", rastlineModeli.get(position).);
                startActivity(intent);
            }
        };
    }

    private void pripraviModeleRastlin() {
        String[] rastlineImena = getResources().getStringArray(R.array.imena_rastlin);
        String[] rastlineVrste = getResources().getStringArray(R.array.vrste_rastlin);
        String[] rastlineVrsteLat = getResources().getStringArray(R.array.vrste_rastlin_lat);

        for (int i = 0; i < rastlineImena.length; i++) {
            rastlineModeli.add(new RastlinaModel(rastlineImena[i], rastlineVrste[i], rastlineVrsteLat[i], rastlineSlike[i]));
        }
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