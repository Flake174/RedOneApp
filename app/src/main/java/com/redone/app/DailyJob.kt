package com.redone.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.redone.app.resources.Controller
import com.redone.app.resources.IRecords
import com.redone.app.resources.Records
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DailyJob: JobService() {

    companion object {
        var lastQuote: Float? = null
    }

    private val TAG = "DailyJobService"
    private var jobCancelled = false
    private var controller: Controller? = null
    private var iRecords: IRecords? = null
    private var dateFormat: DateFormat? = null
    private var currentDate: String? = null
    private var dateWeekAgo: String? = null
    private var intent: Intent? = null
    private var pendingIntent: PendingIntent? = null
    private var alarmManager: AlarmManager? = null

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job started")
        if (isValueNotEmpty()) {
            doCompareJob(params!!)
        } else {
            onStopJob(params)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }

    private fun isValueNotEmpty():Boolean {
        return if (MainActivity.valueUSD != null) {
            true
        } else {
            val t = Toast.makeText(this, "Empty USD entered value!", Toast.LENGTH_LONG)
            t.show()
            false
        }
    }

    private fun doCompareJob(params: JobParameters) {
        getLastQuote()
        Log.d(TAG, "Job finished")
        jobFinished(params, false)
    }

    private fun getLastQuote() {

        controller = Controller()
        iRecords = controller!!.start()
        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar: Calendar? = Calendar.getInstance()
        currentDate = (dateFormat as SimpleDateFormat).format(calendar?.time)
        calendar?.add(Calendar.DAY_OF_YEAR, -7)
        dateWeekAgo = (dateFormat as SimpleDateFormat).format(calendar?.time)

        iRecords?.loadRecords(dateWeekAgo, currentDate)?.enqueue(object : Callback<Records?> {
            override fun onResponse(call: Call<Records?>, response: Response<Records?>) {
                if (response.isSuccessful) {
                    val records: Records? = response.body()
                    lastQuote = records!!.quotesList?.last()?.value?.replace(",", ".")?.toFloat()
                    compareQuotes(lastQuote!!)
                } else {
                    println(response.errorBody())
                }
            }
            override fun onFailure(call: Call<Records?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun compareQuotes(value: Float) {
        if (value > MainActivity.valueUSD!!) {
            sendNotification()
        }
    }

    private fun sendNotification() {
        intent = Intent(applicationContext, NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(applicationContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance()

        alarmManager!!.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}