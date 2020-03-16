package com.redone.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redone.app.resources.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class QuotesActivity : AppCompatActivity() {

    private var arrayList = ArrayList<String>()
    private var controller: Controller? = null
    private var iRecords: IRecords? = null
    private val adapter = QuotesAdapter(arrayList)
    private var dateFormat: DateFormat? = null
    private var currentDate: String = ""
    private var dateMonthAgo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quotes_activity)
        getQuotes()
    }

    private fun getQuotes() {
        //val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        controller = Controller()
        iRecords = controller!!.start()
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        currentDate = (dateFormat as SimpleDateFormat).format(calendar.time)

        calendar.add(Calendar.MONTH, -1)
        dateMonthAgo = (dateFormat as SimpleDateFormat).format(calendar.time)

        iRecords?.loadRecords(dateMonthAgo, currentDate)
            ?.enqueue(object : Callback<Records?> {
                override fun onResponse(call: Call<Records?>, response: Response<Records?>) {
                    if (response.isSuccessful) {
                        val records: Records? = response.body()
                        records!!.quotesList?.forEach { record: Quote ->
                            arrayList.add("Nominal: " + record.nominal + "  Value: " + record.value + "  Date: " + record.date)
                        }
                    } else {
                        println(response.errorBody())
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Records?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

}
