package si.uni_lj.fe.tnuv.rastlince;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
        ImageButton grob = findViewById(R.id.grob);

        puscica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        String ime, vrsta, vrstaLat;
        String image = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ime = extras.getString("ime");
            vrsta = extras.getString("sorta");
            vrstaLat = extras.getString("znanstveno ime");
            image = extras.getString("image");

        } else {
            ime = "????";
            vrsta = "????";
            vrstaLat = "????";
        }

        imePolje.setText(ime);
        vrstaPolje.setText(vrsta);
        vrstaLatPolje.setText(vrstaLat);

        File imgFile = new File(image);

        if (image != null && imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(image);
            bitmap = rotateBitmap(bitmap, 90);
            slikaPolje.setImageBitmap(bitmap);
        } else {
            slikaPolje.setImageResource(R.drawable.i8);
        }

        String finalImage = image;
        grob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgPot = finalImage; // Replace with the actual file path
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
