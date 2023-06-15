package si.uni_lj.fe.tnuv.rastlince;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();
    private ArrayList<HashMap<String, String>> seznamRastlin = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray seznam = jsonObj.getJSONArray("rastlina");

            // looping through All Files
            for (int i = 0; i < seznam.length(); i++) {

                JSONObject r = seznam.getJSONObject(i);

                String ime = r.getString("Ime");
                String sorta = r.getString("Sorta");
                String znanstvenoIme = r.getString("ZnanstvenoIme");
                String zalivanjeDni = r.getString("ZalivanjeDni");
                String zalivanjeDate = r.getString("ZalivanjeDate");

                // tmp hash map for single contact
                HashMap<String, String> rastlina = new HashMap<>();


                // adding each child node to HashMap key => value

                rastlina.put("ime", ime);
                rastlina.put("sorta", sorta);
                rastlina.put("znanstveno_ime", znanstvenoIme);
                rastlina.put("zalivanje_date", zalivanjeDate);
                rastlina.put("zalivanje_dni", zalivanjeDni);

                // adding contact to contact list
                seznamRastlin.add(rastlina);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return seznamRastlin;
    }
}
