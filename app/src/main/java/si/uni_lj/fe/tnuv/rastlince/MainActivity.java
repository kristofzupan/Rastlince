package si.uni_lj.fe.tnuv.rastlince;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dodajRastlino = findViewById(R.id.dodajRastlino);
        dodajRastlino.setOnClickListener(v -> {
            Intent intent = new Intent(this, SlikajRastlino.class);
            startActivity(intent);
        });
    }
}