package danazone.com.emailexample;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class CustomVolleyRequest {
    private static CustomVolleyRequest customVolleyRequest;
    private static Context context;
    private RequestQueue requestQueue;

    private CustomVolleyRequest(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();

    }

    public static synchronized CustomVolleyRequest getInstance(Context context) {
        if (customVolleyRequest == null) {
            customVolleyRequest = new CustomVolleyRequest(context);
        }
        return customVolleyRequest;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
        return requestQueue;
    }

}
