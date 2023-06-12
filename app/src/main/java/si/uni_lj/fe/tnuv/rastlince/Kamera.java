package si.uni_lj.fe.tnuv.rastlince;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import android.util.Rational;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;


public class Kamera extends AppCompatActivity implements NetworkTask.NetworkCallback {
    private PreviewView predPogled;
    private ImageView zajetaSlika;
    private ImageCapture slikaZajemanje;
    private LinearLayout rezultatOverlay;
    private Button preskociGumb;
    private ImageButton slikajGumb;
    private ImageButton zapriOverlay;
    private ImageButton potrdiSliko;
    private TextView sortaIme;
    private GifImageView nalaganje;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private File savedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera);
        predPogled = findViewById(R.id.predPogled);
        zajetaSlika = findViewById(R.id.zajetaSlika);
        rezultatOverlay = findViewById(R.id.RezultatOverlay);
        nalaganje = findViewById(R.id.nalaganje);
        sortaIme = findViewById(R.id.sortaIme);

        slikajGumb = findViewById(R.id.slikajGumb);
        slikajGumb.setOnClickListener(v -> {
            slikajGumb.setVisibility(View.GONE);
            zajemiSliko();
        });

        preskociGumb = findViewById(R.id.preskociGumb);
        preskociGumb.setOnClickListener(v -> {
            if (savedFile != null) {
                savedFile.delete();
            }
            Intent intent = new Intent(this, UrediRastlino.class);
            startActivity(intent);
        });

        zapriOverlay = findViewById(R.id.zapriOverlay);
        zapriOverlay.setOnClickListener(v -> {
            zajetaSlika.setVisibility(View.GONE);
            rezultatOverlay.setVisibility(View.GONE);
            predPogled.setVisibility(View.VISIBLE);
            preskociGumb.setVisibility(View.VISIBLE);
            slikajGumb.setVisibility(View.VISIBLE);
            savedFile.delete();
        });

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    slikaZajemanje = new ImageCapture.Builder().build();
                    bindImage(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindImage(@NonNull ProcessCameraProvider cameraProvider) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Preview preview = new Preview.Builder().build();
        slikaZajemanje = new ImageCapture.Builder().setTargetRotation(Surface.ROTATION_0).build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(predPogled.createSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, slikaZajemanje, preview);
    }

    private void zajemiSliko() {
        File datotekaShranjenaSlika = getOutputFile();
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(datotekaShranjenaSlika).build();
        slikaZajemanje.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d(TAG, datotekaShranjenaSlika.toString());
                        // Get the saved image file
                        savedFile = !datotekaShranjenaSlika.toString().equals("") ? new File(datotekaShranjenaSlika.toString()) : null;

                        if (savedFile != null) {
                            // Display the captured image
                            Bitmap bitmap = BitmapFactory.decodeFile(savedFile.getAbsolutePath());

                            // Rotate the bitmap based on the image's orientation
                            try {
                                ExifInterface exifInterface = new ExifInterface(savedFile.getAbsolutePath());
                                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                Matrix matrix = new Matrix();
                                switch (orientation) {
                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        matrix.setRotate(90);
                                        break;
                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        matrix.setRotate(180);
                                        break;
                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        matrix.setRotate(270);
                                        break;
                                }
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            zajetaSlika.setImageBitmap(bitmap);
                            zajetaSlika.setVisibility(View.VISIBLE);
                            predPogled.setVisibility(View.GONE);
                            nalaganje.setVisibility(View.VISIBLE);
                            preskociGumb.setVisibility(View.VISIBLE);

                            String apiEndpoint = getString(R.string.api_url);
                            String imagePath = savedFile.getAbsolutePath();

                            NetworkTask networkTask = new NetworkTask(Kamera.this);
                            networkTask.execute(apiEndpoint, imagePath);
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        // Handle capture errors
                        exception.printStackTrace();
                    }
                });

    }

    @Override
    public void onResultReceived(String result) {
        if (Objects.equals(result, "404")) {
            onRequestFailed(result);
            return;
        }
        if (!isJSONValid(result)) {
            onRequestFailed(result);
            return;
        }
        // Process the result here
        potrdiSliko = findViewById(R.id.potrdiSliko);
        Gson gson = new Gson();
        ResponseData responseData = gson.fromJson(result, ResponseData.class);
        //System.out.println(responseData.getFirstRespnseDataResultsSpeciesName());
        //System.out.println(responseData.getFirstResponseDataResultsSpeciesCommonName());
        nalaganje.setVisibility(View.GONE);
        rezultatOverlay.setVisibility(View.VISIBLE);
        String tekstSortaIme = "";
        if (!Objects.equals(responseData.getFirstResponseDataResultsSpeciesCommonName(), "")) {
            tekstSortaIme += responseData.getFirstResponseDataResultsSpeciesCommonName();
        }
        if (!Objects.equals(responseData.getFirstRespnseDataResultsSpeciesName(), "")) {
            tekstSortaIme += "\n" + responseData.getFirstRespnseDataResultsSpeciesName();
        }
        sortaIme.setText(tekstSortaIme);

        potrdiSliko.setOnClickListener(v -> {
            Intent intent = new Intent(this, UrediRastlino.class);
            if (!Objects.equals(responseData.getFirstResponseDataResultsSpeciesCommonName(), "")) {
                intent.putExtra(getString(R.string.commonName), responseData.getFirstResponseDataResultsSpeciesCommonName());
            }
            if (!Objects.equals(responseData.getFirstRespnseDataResultsSpeciesName(), "")) {
                intent.putExtra(getString(R.string.scienceName), responseData.getFirstRespnseDataResultsSpeciesName());
            }
            intent.putExtra(getString(R.string.pathImage), savedFile.getAbsolutePath());
            startActivity(intent);
        });

    }

    @Override
    public void onRequestFailed(String result) {
        // Handle the request failure here
        Log.e(TAG, "Request failed");
        if (Objects.equals(result, "404")) {
            Toast.makeText(this, getString(R.string.toast_notFound),  Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.toast_error),  Toast.LENGTH_SHORT).show();
        }
        zajetaSlika.setVisibility(View.GONE);
        rezultatOverlay.setVisibility(View.GONE);
        predPogled.setVisibility(View.VISIBLE);
        preskociGumb.setVisibility(View.VISIBLE);
        slikajGumb.setVisibility(View.VISIBLE);
        nalaganje.setVisibility(View.GONE);
        savedFile.delete();

    }

    private File getOutputFile() {
        // Create a directory for storing the captured images
        File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (outputDir != null && !outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Create a file to save the captured image
        String timeStamp = new SimpleDateFormat(getString(R.string.datumSimpleFormat), Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";
        Log.d(TAG, fileName + " ---- " + outputDir);
        return new File(outputDir, fileName);
    }

    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}