package si.uni_lj.fe.tnuv.rastlince;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private ListView lv;
    private ArrayList<HashMap<String, String>> rastlineModeli;
    private File[] file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAlarm();

        ImageButton dodajRastlino = findViewById(R.id.dodajRastlino);

        ImageButton preminule = findViewById(R.id.PreminuleIkona);
        //preminule.setImageResource(R.drawable.grave);

        lv = findViewById(R.id.seznamRastlin);

        lv.setOnItemClickListener(((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), Profil.class);
            intent.putExtra("path", file[(int)id].getAbsolutePath());
            intent.putExtra("id", id);
            startActivity(intent);
        }));
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

        FileFilter filterJson = new FileFilter() {

            public boolean accept(File f)
            {
                return (f.getName().endsWith("json") && f.getName().startsWith("I"));
            }
        };

        File directory = new File(path);
        file = directory.listFiles(filterJson);
        String JsonString = "{\"rastlina\": [";

        for (int i = 0; i < file.length; i++) {
            File finalF = file[i];
            PrenosPodatkov pp = new PrenosPodatkov(finalF);
            String rezultat = pp.loadJson(finalF);
            if (i != 0) {
                JsonString = JsonString + ",";
            }
            JsonString = JsonString + rezultat;

        }
        JsonString += "]}";
        rastlineModeli = new JSONParser().parseToArrayList(JsonString);



        SimpleAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                rastlineModeli,
                R.layout.list_view_row,
                new String[] {"ime", "znanstveno ime", "sorta", "image"},
                new int[] {R.id.rastlinaIme, R.id.rastlinaVrstaLat, R.id.rastlinaVrsta, R.id.rastlinaIkona}
        )
        {
                public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CardView cardView = view.findViewById(R.id.seznamCard);

                List<Integer> colorCode = new ArrayList<>();

                colorCode.add(0xFF527853);
                colorCode.add(0xFFa1b089);
                colorCode.add(0xFF4b804c);
                colorCode.add(0xFFa4bb83);
                colorCode.add(0xFF7e9871);
                colorCode.add(0xFF6c8d60);
                colorCode.add(0xFF588159);
                colorCode.add(0xFF91a57b);
                colorCode.add(0xFF88a972);
                colorCode.add(0xFF588159);
                colorCode.add(0xFF496e4c);
                colorCode.add(0xFF709d62);
                colorCode.add(0xFF5c9256);

                cardView.setCardBackgroundColor(colorCode.get(position % 13));

                return view;
                }
        };

        lv.setAdapter(adapter);

        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread() {
            public void run() {
                for (int i = 0; i < rastlineModeli.size(); i++) {
                    HashMap<String, String> item = rastlineModeli.get(i);
                    String imagePath = file[i].getAbsolutePath();
                    imagePath = imagePath.substring(0, imagePath.length() - 4) + "jpg";
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        item.put("image", imagePath);
                    } else {
                        System.out.println("NO IMAGE");
                        int index = i % 9;
                        String drawablePath = "android.resource://" + getPackageName() + "/drawable/i"+index;
                        item.put("image", drawablePath);
                        //ImageView
                    }
                     // Add the image path to the HashMap

                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        };
        thread.start();


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
        if (hasCameraPermission()) {
            enableCamera();
        }
    }

    private void enableCamera() {
        Intent intent = new Intent(this, Kamera.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == 0) {
            enableCamera();
        } else if (grantResults[0] == -1) {
            Toast.makeText(this, "Prosimo dovolite uporabo kamere!",  Toast.LENGTH_SHORT).show();
        }
    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }

    }

}