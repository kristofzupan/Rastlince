package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Profil extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> rastlina;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        TextView imePolje = findViewById(R.id.profilIme);
        Button vrstaPolje = findViewById(R.id.bubbleVrsta);
        Button vrstaLatPolje = findViewById(R.id.bubbleVrstaLat);
        ImageView slikaPolje = findViewById(R.id.profilSlika);

        String path = getIntent().getStringExtra("path");
        System.out.println(path);

        File file = new File(path);

        PrenosPodatkov pp = new PrenosPodatkov(file);
        String rezultat = pp.loadJson(file);

        System.out.print(rezultat);
        String JsonString = "{\"rastlina\": [" + rezultat + "]}";
        rastlina = new JSONParser().parseToArrayList(JsonString);

        Bitmap myBitmap = BitmapFactory.decodeFile(path.substring(0, path.length() - 4) + "jpg");
        try {
            ExifInterface exif = new ExifInterface(path.substring(0, path.length() - 4) + "jpg");
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
        }
        catch (Exception e) {

        }
        slikaPolje.setImageBitmap(myBitmap);



        String ime = rastlina.get(0).get("ime");
        String sorta = rastlina.get(0).get("sorta");
        String znanstvenoIme = rastlina.get(0).get("znanstvenoIme");


        ImageButton puscica = findViewById(R.id.profilPuscica);

        puscica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        imePolje.setText(ime);
        vrstaPolje.setText(sorta);
        vrstaLatPolje.setText(znanstvenoIme);


    }
}
