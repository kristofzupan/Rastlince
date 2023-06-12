package si.uni_lj.fe.tnuv.rastlince;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<RastlinaModel> rastlinaModeli;

    public RecyclerViewAdapter(Context context, ArrayList<RastlinaModel> rastlinaModeli) {
        this.context = context;
        this.rastlinaModeli = rastlinaModeli;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //izgled, inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //vsebina vseh k so na skrinu
        holder.tvIme.setText(rastlinaModeli.get(position).getRastlinaIme());
        holder.tvVrsta.setText(rastlinaModeli.get(position).getRastlinaVrsta());
        holder.tvVrstaLat.setText(rastlinaModeli.get(position).getRastlinaVrstaLat());
        holder.imageView.setImageResource(rastlinaModeli.get(position).getImage());

        //getColor je deprecated sicer
        holder.card.setCardBackgroundColor(holder.itemView.getResources().getColor(getRandomColor(), null));
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.rastline_1);
        colorCode.add(R.color.rastline_2);
        colorCode.add(R.color.rastline_3);
        colorCode.add(R.color.rastline_4);
        colorCode.add(R.color.rastline_5);
        colorCode.add(R.color.rastline_6);
        colorCode.add(R.color.rastline_7);
        colorCode.add(R.color.rastline_8);
        colorCode.add(R.color.rastline_9);
        colorCode.add(R.color.rastline_10);
        colorCode.add(R.color.rastline_11);
        colorCode.add(R.color.rastline_12);
        colorCode.add(R.color.rastline_13);
        colorCode.add(R.color.rastline_14);
        colorCode.add(R.color.rastline_15);


        Random r = new Random();
        int number = r.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @Override
    public int getItemCount() {
        //total st modelov
        return rastlinaModeli.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvIme, tvVrsta, tvVrstaLat;

        CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView =itemView.findViewById(R.id.rastlinaIkona);
            tvIme =itemView.findViewById(R.id.rastlinaIme);
            tvVrsta =itemView.findViewById(R.id.rastlinaVrsta);
            tvVrstaLat =itemView.findViewById(R.id.rastlinaVrstaLat);

            card = itemView.findViewById(R.id.seznamCard);
        }
    }
}
