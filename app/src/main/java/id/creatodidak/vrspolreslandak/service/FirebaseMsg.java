package id.creatodidak.vrspolreslandak.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.Dashboard;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.LaporanCekEmbung;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.LaporanCekHotspot;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.LaporanKampanyeCegahKarhutla;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.PetaHotspot;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.chat.ChatLaporanCekEmbung;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.chat.ChatLaporankampanyeCegahKarhutla;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.CekEmbungPimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.CekHotspotPimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.KampanyePimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.CekEmbungVerifikator;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.CekHotspotVerifikator;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.KampanyeVerifikator;
import id.creatodidak.vrspolreslandak.database.DBHelper;

public class FirebaseMsg extends FirebaseMessagingService {
    public static MediaPlayer BG;
    public static Vibrator vibrator;
    Intent intent;
    private static final String TAG = "MyFirebaseMsgService";
    private static final String SHARED_PREF_NAME = "MySharedPref";
    private static final String FCM_TOKEN_KEY = "FCM_TOKEN";
    public static final String BROADCAST_ACTION = "id.creatodidak.vrspolreslandak";
    long[] sosPattern = {0, 100, 100, 100, 1000, 1000, 1000, 100, 100, 100, 1000};
    RemoteMessage remoteMessage2;
    DBHelper dbHelper;
    SharedPreferences sh;
    String jabatan, satfung;
    boolean isVerifikator;
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        dbHelper = new DBHelper(getApplicationContext());
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        jabatan = sh.getString("jabatan", "");
        satfung = sh.getString("satfung", "");

        isVerifikator = jabatan.contains("KABAG") || jabatan.contains("KAPOLRES") || jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR");
        //broadcast to intent

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_TOKEN_KEY, token);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleIntent(Intent intent) { //handle on background

        try {
            if (intent.getExtras() != null) {
                /**
                 *
                 * @Ambil parameter jika perlu
                 */
                String title = intent.getExtras().getString("gcm.notification.title");
                String body = intent.getExtras().getString("gcm.notification.body");
                String notification_id = intent.getExtras().getString("gcm.notification.notification_id");
                String from = intent.getExtras().getString("gcm.notification.from");
                String channel_id = intent.getExtras().getString("gcm.notification.channel_id"); //notifikasiVRSPolresLandak
                String topic = intent.getExtras().getString("topic"); //NotifikasiKarhutlaVRS

                //if(channel_id.equals("notifikasiVRSPolresLandak")) { //pakai ini jika ingin deteksi "notifikasiVRSPolresLandak"

                /**
                 *
                 * Build ulang dan kirim ke fungsi onMessageReceived
                 */

                RemoteMessage.Builder builder = new RemoteMessage.Builder("FirebaseMsg");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
                //}

            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        remoteMessage2 = remoteMessage;
        if (remoteMessage.getNotification() != null && !remoteMessage.getData().isEmpty()) {
            int notificationId = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("notification_id")));
            String channelId = "NOTIFIKASI_VRS";
            CharSequence channelName = "NOTIFIKASI VRS";
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.notifnew);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setSound(sound, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 200, 100, 200, 100, 200});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationChannel alarm = new NotificationChannel("ALARM_KARHUTLA", "ALARM KARHUTLA", NotificationManager.IMPORTANCE_HIGH);
            Uri sound2 = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alarm);
            alarm.setSound(sound2, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            alarm.enableVibration(true);
            alarm.setVibrationPattern(sosPattern);
            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(alarm);

            if (Objects.equals(remoteMessage.getData().get("topic"), "UpdateVRS")) {

                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect vibrationEffect = VibrationEffect.createWaveform(sosPattern, -1); // -1 means don't repeat
                    vibrator.vibrate(vibrationEffect);
                } else {
                    // For devices below Android O, vibrate for 2 seconds
                    vibrator.vibrate(1000);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=id.creatodidak.vrspolreslandak"));
                intent.setPackage("com.android.vending");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notificon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icps))
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {return;}
                notificationManagerCompat.notify(notificationId, builder.build());
            }

            if (Objects.equals(remoteMessage.getData().get("topic"), "atensiPimpinan")) {
                Intent intent = new Intent(this, Dashboard.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notificon)
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {return;}
                notificationManagerCompat.notify(notificationId, builder.build());
            }

            //NOTIF MENU KARHUTLA
            if (Objects.equals(remoteMessage.getNotification().getChannelId(), "ALARM_KARHUTLA")) {
                Intent intent = new Intent(this, PetaHotspot.class); // Ganti dengan class activity yang sesuai
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ALARM_KARHUTLA")
                        .setSmallIcon(R.drawable.notificon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.karhutla))
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSilent(false)
                        .setVibrate(sosPattern)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(98070129, builder.build());
                playSound();
            }

            if (Objects.equals(remoteMessage.getData().get("topic"), "laporanCekHotspot")) {
                Intent intent;
                if (jabatan.contains("KAPOLRES")) {
                    intent = new Intent(this, CekHotspotPimpinan.class);
                } else if (jabatan.contains("KABAG") || jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")) {
                    intent = new Intent(this, CekHotspotVerifikator.class);
                }else{
                    intent = new Intent(this, LaporanCekHotspot.class);
                    String chatid = dbHelper.getChatId(remoteMessage.getData().get("idlaporan"), "LaporanCekHotspot");
                    if (!dbHelper.isChatExist(chatid, "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"))) {
                        dbHelper.newSingleChatData(chatid, "bot", "SERVER", "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"));
                    }
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notificon)
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {return;}
                notificationManagerCompat.notify(notificationId, builder.build());
            }

            if (Objects.equals(remoteMessage.getData().get("topic"), "laporanCekEmbungAir")) {
                Intent intent;
                if (jabatan.contains("KAPOLRES")) {
                    intent = new Intent(this, CekEmbungPimpinan.class);
                } else if (jabatan.contains("KABAG") || jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")) {
                    intent = new Intent(this, CekEmbungVerifikator.class);
                }else{
                    intent = new Intent(this, LaporanCekEmbung.class);
                    if (!dbHelper.isChatExist(remoteMessage.getData().get("idlaporan"), "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"))) {
                        dbHelper.newSingleChatData(remoteMessage.getData().get("idlaporan"), "bot", "SERVER", "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"));
                    }
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notificon)
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {return;}
                notificationManagerCompat.notify(notificationId, builder.build());
            }

            if (Objects.equals(remoteMessage.getData().get("topic"), "laporanKampanyeKarhutla")) {
                Intent intent;
                if (jabatan.contains("KAPOLRES")) {
                    intent = new Intent(this, KampanyePimpinan.class);
                } else if (jabatan.contains("KABAG") || jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")) {
                    intent = new Intent(this, KampanyeVerifikator.class);
                }else{
                    intent = new Intent(this, LaporanKampanyeCegahKarhutla.class);
                    if (!dbHelper.isChatExist(remoteMessage.getData().get("idlaporan"), "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"))) {
                        dbHelper.newSingleChatData(remoteMessage.getData().get("idlaporan"), "bot", "SERVER", "Status verifikasi:\n" + remoteMessage.getData().get("verifikasi"));
                    }
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.notificon)
                        .setBadgeIconType(R.drawable.notificon)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {return;}
                notificationManagerCompat.notify(notificationId, builder.build());
            }
        }
    }

    private void playSound() {
        BG = MediaPlayer.create(getBaseContext(), R.raw.alarm);
        BG.setLooping(true);
        BG.setVolume(100, 100);
        BG.start();

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(sosPattern, 0);
    }
}

