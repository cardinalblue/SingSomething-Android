package com.cardinalblue.singsomething.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jimytc on 12/24/15.
 */
public class RequestManager {
    private static RequestManager sInstance;

    public static synchronized RequestManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RequestManager(context);
        }
        return sInstance;
    }

    private RequestQueue mRequestQueue;
    private Context mCtx;

    private RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}