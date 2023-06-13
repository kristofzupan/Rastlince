package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Profil extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        TextView imePolje = findViewById(R.id.profilIme);
        Button vrstaPolje = findViewById(R.id.bubbleVrsta);
        Button vrstaLatPolje = findViewById(R.id.bubbleVrstaLat);
        ImageView slikaPolje = findViewById(R.id.profilSlika);

        ImageButton puscica = findViewById(R.id.profilPuscica);

        puscica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        String ime, vrsta, vrstaLat;
        Image img;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ime = extras.getString("ime");
            vrsta = extras.getString("vrsta");
            vrstaLat = extras.getString("vrstaLat");


        } else {
            ime = "????";
            vrsta = "????";
            vrstaLat = "????";
        }

        imePolje.setText(ime);
        vrstaPolje.setText(vrsta);
        vrstaLatPolje.setText(vrstaLat);
        slikaPolje.setImageResource(R.drawable.f);

    }
}
