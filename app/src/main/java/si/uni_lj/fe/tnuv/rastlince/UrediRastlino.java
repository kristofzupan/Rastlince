package si.uni_lj.fe.tnuv.rastlince;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class UrediRastlino extends AppCompatActivity {
    private EditText sortaPolje;
    private EditText znanstvenoPolje;
    private EditText imePolje;
    private EditText zalivanjeDni;
    private EditText zalivanjeDatum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_rastlino);
        zalivanjeDni = findViewById(R.id.zalivanjeDni);
        zalivanjeDni.setInputType(InputType.TYPE_CLASS_NUMBER);
        zalivanjeDni.setText("7");

        zalivanjeDatum = findViewById(R.id.zalivanjeDatum);
        zalivanjeDatum.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);

        ImageButton zapriGumb = findViewById(R.id.zapriGumb);
        zapriGumb.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


        sortaPolje = findViewById(R.id.sortaPolje);
        znanstvenoPolje = findViewById(R.id.znanstvenoPolje);
        imePolje = findViewById(R.id.imePolje);

        String Sorta = getIntent().getStringExtra("CommonName");
        String ZnanstvenoIme = getIntent().getStringExtra("ScienName");
        String PotSlika = getIntent().getStringExtra("PathImgae");
        System.out.println("AAAAAAAAAAAAAAAARararar"+ PotSlika);
        String shraniPot = "";
        String shraniDatotekaIme = "";

        if (!Objects.equals(Sorta, "") && Sorta != null) {
            sortaPolje.setText(Sorta);
            imePolje.setText(Sorta);
        }
        if (!Objects.equals(ZnanstvenoIme, "") && ZnanstvenoIme != null) {
            znanstvenoPolje.setText(ZnanstvenoIme);
        }
        if (!Objects.equals(PotSlika, "") && PotSlika != null) {
            shraniDatotekaIme = PotSlika.substring(PotSlika.length()-23, PotSlika.length()-4)+".txt";
            System.out.println(shraniDatotekaIme);
            shraniPot = PotSlika.substring(0, PotSlika.length()-23);
        } else {
            File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (outputDir != null && !outputDir.exists()) {
                outputDir.mkdirs();
            }
            shraniPot = outputDir.getAbsolutePath();
        }
        System.out.println(shraniPot);

        File zaShranitiFile = new File(shraniPot, shraniDatotekaIme);
        ImageButton potrdiGumb = findViewById(R.id.potrdiGumb);
        potrdiGumb.setOnClickListener(v -> {
            try {
                FileOutputStream fos = new FileOutputStream(zaShranitiFile);
                fos.write((sortaPolje.getText().toString()+"\n").getBytes());
                fos.write((znanstvenoPolje.getText().toString()+"\n").getBytes());
                fos.write((imePolje.getText().toString()+"\n").getBytes());
                fos.write((zalivanjeDni.getText().toString()+"\n").getBytes());
                fos.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

}