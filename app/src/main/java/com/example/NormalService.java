package com.example;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shivam on 12/17/15.
 */
public class NormalService extends Service {

    private static final String TAG = NormalService.class.getSimpleName();
    private static final int TIME = 2000;
    private static final String PREF_KEY_COUNT = "service_count";


    private AsyncTask<Void, Void, Void> mAsyncTask;
    private SharedPreferences mPreferences;

    public NormalService() {
        mAsyncTask = new MyAsyncTask();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "starting job.");

        if (mPreferences == null) {
            mPreferences = getSharedPreferences(NormalService.class.getName(), Context
                    .MODE_PRIVATE);
        }

        updateCount(0);

        if (mAsyncTask == null) {
            mAsyncTask = new MyAsyncTask();
        }
        mAsyncTask.execute();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "stopping job.");

        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }

    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {

                int jobNumber = NormalService.this.mPreferences.getInt(PREF_KEY_COUNT, 0);
                updateCount(jobNumber + 1);

                Log.d(TAG, "running job number " + jobNumber);

                NetworkHandler.execute("data");

                try {
                    Thread.sleep(TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            return null;
        }

    }


    private void updateCount(int count) {
        this.mPreferences.edit().putInt(PREF_KEY_COUNT, count).apply();
    }

}
