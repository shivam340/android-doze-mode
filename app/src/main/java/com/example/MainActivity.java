package com.example;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private JobScheduler mJobScheduler = null;
    private Button mButtonNormalService = null;
    private Button mButtonJobService = null;
    private static final int JOB_ID = 2015;
    private static final int PERIODIC_TIME = 3000;
    public static boolean isServiceRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mButtonJobService = (Button) findViewById(R.id.btn_job_service);
        mButtonJobService.setOnClickListener(this);


        mButtonNormalService = (Button) findViewById(R.id.btn_normal_service);
        mButtonNormalService.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_normal_service) {
            startJobsUsingNormalService();
        } else if (view.getId() == R.id.btn_job_service) {

            if(  isJobRunning() ) {
                mJobScheduler.cancel(JOB_ID);
                mButtonJobService.setText(getResources().getString(R.string.start_job_using_scheduler));
                return;
            }

            mButtonJobService.setText(getResources().getString(R.string.stop_job_using_scheduler));
            startJobsUsingJobSchedular();
        }
    }

    private void startJobsUsingJobSchedular() {

        if (mJobScheduler == null) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }

        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(JOB_ID, new ComponentName(this, JobSchedulerService.class));
        jobInfoBuilder.setPeriodic(PERIODIC_TIME);

        JobInfo jobInfo = jobInfoBuilder.build();
        mJobScheduler.schedule(jobInfo);

    }

    private void startJobsUsingNormalService() {

        Intent serviceIntent = new Intent(MainActivity.this, NormalService.class);

        if ( isServiceRunning ){
            stopService(serviceIntent);
            mButtonNormalService.setText(getResources().getString(R.string.start_service));
            isServiceRunning = false;
            return;
        }

        mButtonNormalService.setText(getResources().getString(R.string.stop_service));
        startService(serviceIntent);
        isServiceRunning = true;
    }


    public boolean isJobRunning() {

        if (mJobScheduler != null) {

            for (JobInfo info : mJobScheduler.getAllPendingJobs()) {
                if (info.getId() == JOB_ID) {
                    return true;
                }
            }
        }

        return false;
    }
}
