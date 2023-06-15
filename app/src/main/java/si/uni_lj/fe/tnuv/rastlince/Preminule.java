package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Preminule extends AppCompatActivity {
    ArrayList<RastlinaModel> rastlineModeli = new ArrayList<>();
    //private RecyclerViewAdapter.ClickListener listener;
    int[] rastlineSlike = {R.drawable.e, R.drawable.b, R.drawable.c, R.drawable.d};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*RecyclerView recyclerView = findViewById(R.id.seznamRastlin);
        pripraviModeleRastlin();

        RVsetOnClickListener();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, rastlineModeli, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        View ozadje = findViewById(R.id.ozadje);
        ozadje.setBackgroundResource(R.color.ozadje_preminule);

        TextView text = findViewById(R.id.seznamText);
        text.setText("Preminule rastline:");

        ImageButton plus = findViewById(R.id.dodajRastlino);
        plus.setVisibility(View.GONE);

        ImageButton main = (ImageButton)findViewById(R.id.PreminuleIkona);
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

    private void pripraviModeleRastlin() {
        String[] rastlineImena = getResources().getStringArray(R.array.imena_rastlin);
        String[] rastlineVrste = getResources().getStringArray(R.array.vrste_rastlin);
        String[] rastlineVrsteLat = getResources().getStringArray(R.array.vrste_rastlin_lat);

        for (int i = 0; i < rastlineImena.length; i++) {
            rastlineModeli.add(new RastlinaModel(rastlineImena[i], rastlineVrste[i], rastlineVrsteLat[i], rastlineSlike[i]));
        }
    };

    /*private void RVsetOnClickListener() {
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
    }*/

}
