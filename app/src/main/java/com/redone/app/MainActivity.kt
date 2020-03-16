package com.redone.app

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        var valueUSD: Float? = null
    }

    private var TAG: String = "MainActivity";
    private var builder: JobInfo? = null
    private var scheduler: JobScheduler? = null
    private var resultCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getListBtn: Button = findViewById<Button>(R.id.getUsdList)
        getListBtn.setOnClickListener {
            startActivity(Intent(this, QuotesActivity::class.java))
        }

        val submitBtn: Button = findViewById<Button>(R.id.submitBtn)
        val editText: EditText = findViewById(R.id.valueUSD)
        var str: String

        submitBtn.setOnClickListener {
            str = editText.text.toString()

            if(str.trim().length > 0) {
                valueUSD = str.toFloat()
                val t = Toast.makeText(this, "Entered value: " + valueUSD.toString(), Toast.LENGTH_LONG)
                t.show()
                scheduleJob()
            } else {
                val t = Toast.makeText(this, "Empty value!", Toast.LENGTH_LONG)
                t.show()
            }
        }

    }

    private fun scheduleJob() {
        val componentName = ComponentName(this, DailyJob::class.java)
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder(666, componentName)
                //.setOverrideDeadline(2000) //fix lollipop
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(24 * 60 * 60 * 1000)
                .build()
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        resultCode = scheduler!!.schedule(builder)

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

}
