package si.uni_lj.fe.tnuv.rastlince;


import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;

public class NetworkTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "NetworkTask";
    private NetworkCallback callback;

    public NetworkTask(NetworkCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String apiEndpoint = params[0];
        String imagePath = params[1];

        OkHttpClient client = new OkHttpClient();

        // Prepare the request body
        MediaType mediaType = MediaType.parse("image/jpeg");
        File imageFile = new File(imagePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("organs", "flower")
                .addFormDataPart("images", "image.jpg",
                        RequestBody.create(mediaType, imageFile))
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(apiEndpoint)
                .post(requestBody)
                .build();

        // Execute the request
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            return e.toString();
        }

        // Handle the response
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                Log.e(TAG, "Exception: " + e.getMessage());
                return e.toString();
            }
        } else {
            Log.e(TAG, "Request failed: " + response.code());
            return String.valueOf(response.code());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            callback.onResultReceived(result);
        } else {
            callback.onRequestFailed(result);
        }
    }

    public interface NetworkCallback {
        void onResultReceived(String result);

        void onRequestFailed(String result);
    }
}