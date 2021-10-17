package com.example.maskmap20

import android.content.ClipData
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import org.json.JSONArray
import java.net.URL
import java.util.concurrent.Executors
import org.json.JSONException

import org.json.JSONObject
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter

import android.widget.Spinner
import android.content.ClipData.Item

import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

import android.content.Intent
import android.net.Uri

import android.text.TextUtils











class MainActivity : AppCompatActivity() {
    fun reload (json:String)
    {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        Executors.newSingleThreadExecutor().execute {
            val jsonArray = JSONArray(json)

            val linearLayout1 =
                findViewById<View>(R.id.linearlayout) as LinearLayout
            runOnUiThread {
                linearLayout1.removeAllViews()
            }
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val country =
                    jsonObject.getString("醫事機構地址")[0].toString() + jsonObject.getString(
                        "醫事機構地址"
                    )[1].toString() + jsonObject.getString("醫事機構地址")[2].toString()
                var area =
                    jsonObject.getString("醫事機構地址")[3].toString() + jsonObject.getString(
                        "醫事機構地址"
                    )[4].toString()
                if (jsonObject.getString("醫事機構地址")[4].toString() != "區" )
                    area += jsonObject.getString("醫事機構地址")[5].toString()


                if (spinner.selectedItem == country && spinner2.selectedItem == area
                )
                    runOnUiThread {
                        val inflater =
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val view: View =
                            inflater.inflate(R.layout.item, null, true) //讀取的page2.
                        val textViewName =
                            view.findViewById<TextView>(R.id.textViewName)
                        val textViewChildCount =
                            view.findViewById<TextView>(R.id.textViewChildCount)
                        val textViewAdultCount =
                            view.findViewById<TextView>(R.id.textViewAdultCount)
                        val textViewLocate =
                            view.findViewById<TextView>(R.id.textViewLocate)
                        val textViewPhone =
                            view.findViewById<TextView>(R.id.textViewPhone)
                        val textViewUpdate =
                            view.findViewById<TextView>(R.id.textViewUpdate)
                        textViewName.text = jsonObject.getString("醫事機構名稱")
                        textViewAdultCount.text = jsonObject.getString("成人口罩剩餘數")
                        textViewChildCount.text = jsonObject.getString("兒童口罩剩餘數")
                        textViewLocate.text = jsonObject.getString("醫事機構地址")
                        textViewUpdate.text = jsonObject.getString("來源資料時間")
                        textViewPhone.text = jsonObject.getString("醫事機構電話")
                        if(checkBox.isChecked) {
                            if (jsonObject.getString("兒童口罩剩餘數").toInt() > 0)
                                linearLayout1.addView(view) //加入畫面上
                        }
                        else if(checkBox2.isChecked){
                            if (jsonObject.getString("成人口罩剩餘數").toInt() > 0)
                                linearLayout1.addView(view) //加入畫面上
                        }
                        else {
                            linearLayout1.addView(view) //加入畫面上
                        }
                        view.setOnClickListener(){
                            val phoneNo: String = jsonObject.getString("醫事機構電話")
                            if (!TextUtils.isEmpty(phoneNo)) {
                                val dial = "tel:$phoneNo"
                                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(dial)))
                            }
                        }
                    }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolBar))
        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val reload = findViewById<ImageButton>(R.id.reload)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        val mycontext = this
        var json = ""
        Executors.newSingleThreadExecutor().execute {
            json = URL("https://quality.data.gov.tw/dq_download_json.php?nid=116285&md5_url=53a72b2dcfdd9ecae43afda4b86089be").readText()
            val jsonArray = JSONArray(json)
            val arraySpinner : ArrayList<String> = ArrayList()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                runOnUiThread {
                    val country = jsonObject.getString("醫事機構地址")[0].toString()+jsonObject.getString("醫事機構地址")[1].toString()+jsonObject.getString("醫事機構地址")[2].toString()

                    if (!arraySpinner.contains(country)) {
                        arraySpinner.add(country)
                    }

                }
            }
            runOnUiThread {
                val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner)
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
                spinner.adapter = spinnerArrayAdapter
            }

        }

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Executors.newSingleThreadExecutor().execute {
                    val jsonArray = JSONArray(json)
                    var arraySpinner: ArrayList<String> = ArrayList()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        runOnUiThread {
                            val country =
                                jsonObject.getString("醫事機構地址")[0].toString() + jsonObject.getString(
                                    "醫事機構地址"
                                )[1].toString() + jsonObject.getString("醫事機構地址")[2].toString()
                            var area =
                                jsonObject.getString("醫事機構地址")[3].toString() + jsonObject.getString(
                                    "醫事機構地址"
                                )[4].toString()
                            if (jsonObject.getString("醫事機構地址")[4].toString() != "區" )
                                area += jsonObject.getString("醫事機構地址")[5].toString()

                            if (country == spinner.selectedItem) {
                                if (!arraySpinner.contains(area)) {
                                    arraySpinner.add(area)
                                    println(arraySpinner)
                                }
                            }
                        }
                    }

                    runOnUiThread {
                        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(mycontext, android.R.layout.simple_spinner_item, arraySpinner)
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
                        spinner2.adapter = spinnerArrayAdapter
                    }

                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinner2.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                reload(json)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        reload.setOnClickListener(){
            Executors.newSingleThreadExecutor().execute {
                json = URL("https://quality.data.gov.tw/dq_download_json.php?nid=116285&md5_url=53a72b2dcfdd9ecae43afda4b86089be").readText()
            }
            reload(json)
        }
        checkBox.setOnClickListener(){
            reload(json)
        }
        checkBox2.setOnClickListener(){
            reload(json)
        }
    }


}