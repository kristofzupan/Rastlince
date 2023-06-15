package si.uni_lj.fe.tnuv.rastlince;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UrediRastlino extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    private EditText sortaPolje;
    private EditText znanstvenoPolje;
    private EditText imePolje;
    private EditText zalivanjeDni;
    private EditText zalivanjeDatum;
    private LocalDate localDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_rastlino);
        zalivanjeDni = findViewById(R.id.zalivanjeDni);
        zalivanjeDni.setInputType(InputType.TYPE_CLASS_NUMBER);
        zalivanjeDni.setText("7");

        zalivanjeDatum = findViewById(R.id.zalivanjeDatum);
        zalivanjeDatum.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);

        sortaPolje = findViewById(R.id.sortaPolje);
        znanstvenoPolje = findViewById(R.id.znanstvenoPolje);
        imePolje = findViewById(R.id.imePolje);

        String Sorta = getIntent().getStringExtra(getString(R.string.commonName));
        String ZnanstvenoIme = getIntent().getStringExtra(getString(R.string.scienceName));
        String PotSlika = getIntent().getStringExtra(getString(R.string.pathImage));
        String shraniPot = "";
        String shraniDatotekaIme = "";

        ImageButton zapriGumb = findViewById(R.id.zapriGumb);
        zapriGumb.setOnClickListener(v -> {
            if (!Objects.equals(PotSlika, "") && PotSlika != null) {
                File zaZbrisatSlika = new File(PotSlika);
                zaZbrisatSlika.delete();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        zalivanjeDatum.setOnClickListener(v -> {
            prikaziIzbiranjeDatuma();
        });

        if (!Objects.equals(Sorta, "") && Sorta != null) {
            sortaPolje.setText(Sorta);
            imePolje.setText(Sorta);
        }
        if (!Objects.equals(ZnanstvenoIme, "") && ZnanstvenoIme != null) {
            znanstvenoPolje.setText(ZnanstvenoIme);
        }
        if (!Objects.equals(PotSlika, "") && PotSlika != null) {
            shraniDatotekaIme = PotSlika.substring(PotSlika.length()-23, PotSlika.length()-4)+".json";
            shraniPot = PotSlika.substring(0, PotSlika.length()-23);
        } else {
            File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (outputDir != null && !outputDir.exists()) {
                outputDir.mkdirs();
            }
            shraniPot = outputDir.getAbsolutePath();
            shraniDatotekaIme = getOutputFile().getName();
        }

        File zaShranitiFile = new File(shraniPot, shraniDatotekaIme);
        ImageButton potrdiGumb = findViewById(R.id.potrdiGumb);
        potrdiGumb.setOnClickListener(v -> {
            if (sortaPolje.getText().toString().equals("") || znanstvenoPolje.getText().toString().equals("") || imePolje.getText().toString().equals("") || zalivanjeDni.getText().toString().equals("") || zalivanjeDatum.getText().toString().equals("")) {
                Toast.makeText(this, "Prosimo izpolnite vse podatke!",  Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(getString(R.string.sorta), sortaPolje.getText());
                jsonObject.put(getString(R.string.znanstvenoImeJson), znanstvenoPolje.getText());
                jsonObject.put(getString(R.string.ime), imePolje.getText());
                jsonObject.put(getString(R.string.zalivanjeDniJson), zalivanjeDni.getText());
                jsonObject.put(getString(R.string.zalivanjeDateJson), localDate);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                /*JSONObject rastlina = new JSONObject();
                rastlina.put("rastlina", jsonArray);*/
                FileWriter fileWriter = new FileWriter(zaShranitiFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void prikaziIzbiranjeDatuma () {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        localDate = LocalDate.of(year, month+1, dayOfMonth);
        zalivanjeDatum.setText(localDate.toString());
    }

    private File getOutputFile() {
        // Create a directory for storing the captured images
        File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Create a file to save the captured image
        String timeStamp = new SimpleDateFormat(getString(R.string.datumSimpleFormat), Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + ".json";
        Log.d(TAG, fileName + " ---- " + outputDir);
        return new File(outputDir, fileName);
    }
}