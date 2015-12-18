package com.example;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by shivam on 12/17/15.
 */
public class JobSchedulerService extends JobService {

    private static final String TAG = JobSchedulerService.class.getSimpleName();
    private static final int TIME = 2000;
    private static final String PREF_KEY_COUNT = "scheduler_count";


    private AsyncTask<Void, Void, Void> mAsyncTask;
    private SharedPreferences mPreferences;

    public JobSchedulerService() {
        mAsyncTask = new MyAsyncTask();
    }


    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "starting job.");

        if (mPreferences == null) {
            mPreferences = getSharedPreferences(JobSchedulerService.class.getName(), Context.MODE_PRIVATE);
        }

        updateCount(0);



        if (mAsyncTask == null) {
            mAsyncTask = new MyAsyncTask();
        }
        mAsyncTask.execute();



        // true means job will be running longer. then it will be developers responsibility to
        // stop it.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        Log.d(TAG, "stopping job.");

        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }

        /*True to indicate to the JobManager whether you'd like to reschedule this job based on
        the retry criteria provided at job creation-time. False to drop the job. Regardless of
        the value returned, your job must stop executing.
          */
        return true;
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {

                int jobNumber = JobSchedulerService.this.mPreferences.getInt(PREF_KEY_COUNT, 0);
                updateCount(jobNumber+1);

                Log.d(TAG, "running job number " + jobNumber);

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
