package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        ImageButton puscica = findViewById(R.id.profilPuscica);
        ImageButton grob = findViewById(R.id.grob);

        String path = getIntent().getStringExtra("path");
        long id = getIntent().getLongExtra("id",0);

        puscica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        File file = new File(path);

        PrenosPodatkov pp = new PrenosPodatkov(file);
        String rezultat = pp.loadJson(file);

        String JsonString = "{\"rastlina\": [" + rezultat + "]}";
        rastlina = new JSONParser().parseToArrayList(JsonString);


        File imgFile = new File(path.substring(0, path.length() - 4) + "jpg");
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(path.substring(0, path.length() - 4) + "jpg");

            try {
                ExifInterface exif = new ExifInterface(path.substring(0, path.length() - 4) + "jpg");
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
            } catch (Exception e) {

            }
            slikaPolje.setImageBitmap(myBitmap);
        } else {
            long index = id % 9;
            String drawableName = "i" + index;
            int drawableResourceId = getResources().getIdentifier(drawableName, "drawable", getPackageName());

            slikaPolje.setImageResource(drawableResourceId);
        }


        String ime = rastlina.get(0).get("ime");
        String vrsta = rastlina.get(0).get("sorta");
        String vrstaLat = rastlina.get(0).get("znanstveno_ime");
        String datum = rastlina.get(0).get("zalivanje_date");
        String dni = rastlina.get(0).get("zalivanje_dni");

        imePolje.setText(ime);
        vrstaPolje.setText(vrsta);
        vrstaLatPolje.setText(vrstaLat);

        MaterialCalendarView calendarView = findViewById(R.id.koledar);

        LocalDate localDate = LocalDate.of(Integer.parseInt(datum.substring(0,4)), Integer.parseInt(datum.substring(5,7)), Integer.parseInt(datum.substring(8,10)));
        int naDni = Integer.parseInt(dni);

        List<CalendarDay> highlightedDates = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            highlightedDates.add(CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth())); // Dates are zero-based (0 = January, 1 = February, etc.)
            localDate = localDate.plusDays(naDni);
        }

        // Apply a decorator to highlight the dates
        calendarView.addDecorator(new HighlightDecorator(Color.RED, highlightedDates));

        // Add a listener to handle date selection events
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(
                    MaterialCalendarView widget,
                    CalendarDay date,
                    boolean selected
            ) {
                // Handle the selected date change event here
            }
        });

        grob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPot = path.substring(0, path.length() - 4) + "jpg"; // Replace with the actual file path
                String newImgIme = "P_" + imgPot.substring(imgPot.length() - 23, imgPot.length()); // Replace with the new file name

                String filePot = imgPot.substring(0, imgPot.length() - 3) + "json";
                String newFileIme = "P_" + filePot.substring(filePot.length() - 24, filePot.length());;

                File img = new File(imgPot);
                File file = new File(filePot);
                String parentPath = img.getParent(); // Get the parent directory path

                File newImg = new File(parentPath, newImgIme);
                File newFile = new File(parentPath, newFileIme);
                boolean renamedImg = img.renameTo(newImg);
                boolean renamedFile = file.renameTo(newFile);

                if (renamedImg && renamedFile) {
                    System.out.println("Files renamed successfully.");
                } else {
                    System.out.println("Failed to rename the files.");
                }
                Intent intent = new Intent(getApplicationContext(), Preminule.class);
                startActivity(intent);
            }
        });

        ImageButton svincnik = findViewById(R.id.svincnik);

        svincnik.setOnClickListener(v -> {
            Intent intent = new Intent(this, UrediRastlino.class);
            intent.putExtra(getString(R.string.commonName), vrsta);
            intent.putExtra(getString(R.string.scienceName), vrstaLat);
            intent.putExtra(getString(R.string.pathImage), path.substring(0, path.length() - 4)+"jpg");
            intent.putExtra("Days", dni);
            intent.putExtra("Date", datum);
            startActivity(intent);
        });
    }

    // Custom decorator class to highlight dates
    private static class HighlightDecorator implements DayViewDecorator {
        private final int color;
        private final List<CalendarDay> dates;

        public HighlightDecorator(int color, List<CalendarDay> dates) {
            this.color = color;
            this.dates = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    public static Bitmap rotateBitmap(Bitmap sourceBitmap, float angleDegrees) {
        // Create a matrix for the rotation transformation
        Matrix matrix = new Matrix();
        matrix.postRotate(angleDegrees);

        // Create a new rotated Bitmap
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }
}
