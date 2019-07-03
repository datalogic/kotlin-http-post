package com.datalogic.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import android.os.StrictMode
import android.util.Log


class MainActivity : AppCompatActivity() {
    var serverResponse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        val postButton = findViewById<Button>(R.id.post)
        val postText = findViewById<EditText>(R.id.text)

        postButton.setOnClickListener {
            if (postText.text.toString().equals("")) {
                Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show()
            }
            else {
                postData(postText.text.toString())
                if(serverResponse.contains("success", ignoreCase = true)) {
                    Toast.makeText(this, "Barcode stored successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to store barcode", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun postData(text: String) {
        var postParam = URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8")
        val serverURL = "http://datalogictoolsdemo.online/store-data.php"
        val url = URL(serverURL)

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"

            val wr = OutputStreamWriter(outputStream)
            wr.write(postParam)
            wr.flush()

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLIne = it.readLine()
                while(inputLIne != null) {
                    response.append(inputLIne)
                    inputLIne = it.readLine()
                }

                it.close()
                serverResponse = "$response"
            }
        }

    }
 }
