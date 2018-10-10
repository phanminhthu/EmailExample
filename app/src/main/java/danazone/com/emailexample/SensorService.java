package danazone.com.emailexample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import danazone.com.emailexample.common.Common;
import danazone.com.emailexample.common.MySingleton;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SensorService extends Service {
    public int counter = 0;
    Context context;
    private List<Admin> list;
    int a;
    String n;
    private boolean mCheck;
    private Socket mSocket;
    private NotificationCompat.Builder notBuilder;

    private static final int MY_NOTIFICATION_ID = 12345;

    private static final int MY_REQUEST_CODE = 100;


    public SensorService(Context applicationContext) {
        super();
        context = applicationContext;
        Log.i("HERE", "here service created!");
    }

    public SensorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.notBuilder = new NotificationCompat.Builder(this);

        // Thông báo sẽ tự động bị hủy khi người dùng click vào Panel

        this.notBuilder.setAutoCancel(true);
        try {
            mSocket = IO.socket(Common.URL_SOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        list = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
            startTimer();
        mSocket.on("server_send_Data", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    String ten = jsonObject.getString("noidung");
                    System.out.println("777777777777777777777: " + ten);
                   // createNotification("hfhhfhfhf","hfhfhhfhhfhfh");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent("ac.in.ActivityRecognition.RestartSensor");
        broadcastIntent.putExtra("", "");
        sendBroadcast(broadcastIntent);
    }

    private Timer timer;

    public void startTimer() {
        // int max = list.get(0).getId();
        final int[] max = new int[1];
        //set a new Timer
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notification();
                mSocket.emit("client_send_Data", "Hello Thu");
                loadData();
                if (list.size() > 0) {
                    if (SessionManager.getInstance().getKeySaveId().equals("")) {
                        SessionManager.getInstance().setKeySaveId(list.get(list.size() - 1).getPhone());
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(SessionManager.getInstance().getKeySaveId(), null, "hahahahhahahha", null, null);
                    }
                    if (!SessionManager.getInstance().getKeySaveId().equals(list.get(list.size() - 1).getPhone())) {
                        SessionManager.getInstance().updateSaveId(list.get(list.size() - 1).getPhone());

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(SessionManager.getInstance().getKeySaveId(), null, "hahahahhahahha", null, null);
                    }
                }
                list.clear();
            }

        }, 0, 5000);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notification() {
        Intent resultIntent = new Intent(getApplicationContext(), NextActivity.class);
        resultIntent.putExtra("jjj", "jjj");
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        @SuppressLint("WrongConstant")
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Context context = getApplicationContext();
        Notification.Builder builder;
        builder = new Notification.Builder(context)
                .setContentTitle("Hello")
                .setContentText("hahahha")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    private void loadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Common.URL_VIEW_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Admin admin = new Admin();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String phone = jsonobject.getString("phone");
                        a = (Integer.valueOf(jsonobject.getString("iduser")));
                        admin.setId(a);
                        admin.setPhone(phone);
                        list.add(admin);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Loi", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}