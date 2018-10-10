package danazone.com.emailexample;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import danazone.com.emailexample.common.Common;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {
    private Intent mServiceIntent;
    private SensorService mSensorService;
    private Context ctx;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Socket mSocket;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
//        try {
//            mSocket = IO.socket(Common.URL_SOCKET);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        mSocket.connect();
//        mSocket.on("server_send_Data", getData);
//        mSocket.emit("client_send_Data", "Hello Thu");



        if (
                ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.SEND_SMS") == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                ) {

            mSensorService = new SensorService(getCtx());
            mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                startService(mServiceIntent);
            }


        } else {
            // Todo : Then Set Permission

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    "android.permission.SEND_SMS", Manifest.permission.RECEIVE_SMS
            }, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

//    private Emitter.Listener getData = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject jsonObject = (JSONObject) args[0];
//                    try {
//                        String ten = jsonObject.getString("noidung");
//                        Toast.makeText(MainActivity.this, ten, Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            mSensorService = new SensorService(getCtx());
            mServiceIntent = new Intent(getCtx(), mSensorService.getClass());

            if (!isMyServiceRunning(mSensorService.getClass())) {
                startService(mServiceIntent);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


}
