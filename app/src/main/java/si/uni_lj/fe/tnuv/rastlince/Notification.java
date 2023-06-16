package si.uni_lj.fe.tnuv.rastlince;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class Notification {
    static class NotificationHelper {

        private Context mContext;
        private static final String NOTIFICATION_CHANNEL_ID = "10001";

        NotificationHelper(Context context) {
            mContext = context;
        }

        void createNotification()
        {

            Intent intent = new Intent(mContext , Notification.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                    0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);

            String podatki = notificationPodatki();

            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Rastlince")
                    .setContentText(podatki)
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);

            // Create an expandable style for the notification
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.bigText(podatki);
            mBuilder.setStyle(bigTextStyle);

            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
        }
    }

    public static String notificationPodatki() {
        String path = "/storage/emulated/0/Android/data/si.uni_lj.fe.tnuv.rastlince/files/Pictures/";

        FileFilter filterJson = new FileFilter() {

            public boolean accept(File f)
            {
                return (f.getName().endsWith("json") && f.getName().startsWith("I"));
            }
        };

        File directory = new File(path);
        File[] file = directory.listFiles(filterJson);
        String JsonString = "{\"rastlina\": [";

        for (int i = 0; i < file.length; i++) {
            File finalF = file[i];
            PrenosPodatkov pp = new PrenosPodatkov(finalF);
            String rezultat = pp.loadJson(finalF);
            if (i != 0) {
                JsonString = JsonString + ",";
            }
            JsonString = JsonString + rezultat;

        }
        JsonString += "]}";
        ArrayList<HashMap<String, String>> rastlineModeli = new JSONParser().parseToArrayList(JsonString);

        int zaZaliti = 0;
        String notification = "Danes morate zaliti sledeče rastline:\n";
        for (int i = 0; i < rastlineModeli.size(); i++) {
            String ime = rastlineModeli.get(i).get("ime");
            String datum = rastlineModeli.get(i).get("zalivanje_date");
            String dni = rastlineModeli.get(i).get("zalivanje_dni");


            LocalDate localDate = LocalDate.of(Integer.parseInt(datum.substring(0,4)), Integer.parseInt(datum.substring(5,7)), Integer.parseInt(datum.substring(8,10)));
            long naDni = Long.parseLong(dni);

            LocalDate today = LocalDate.now();
            long dayDifference = ChronoUnit.DAYS.between(localDate, today);
            if (dayDifference % naDni == 0) {
                zaZaliti++;
                notification += ime+"\n";
            }
        }
        if (zaZaliti == 0) {
            notification = "Danes vam ni treba zaliti rastlin!";
        }
        return notification;
    }
}
