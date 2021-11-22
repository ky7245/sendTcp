package com.example.sendtcp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun clickSendButton(view: View) {
        val ipAddr = findViewById<EditText>(R.id.editTextTextIPAddr).text.toString() //"192.168.1.7"
        val portNum = 58080

        try {
            val inetSocketAddress = InetSocketAddress(ipAddr, portNum)

            val task = object : AsyncTask<InetSocketAddress, Void, Void>() {
                override fun doInBackground(vararg p0: InetSocketAddress?): Void? {
                    var socket: Socket? = null
                    try {
                        // connect
                        socket = Socket()
                        socket.connect(p0[0])
                        val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                        writer.write("test string.....")
                        writer.flush()

                        var received = reader.use { it.readText() }

                        val handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            val mainTextVeiew = findViewById<TextView>(R.id.rcvString)
                            var oldText = mainTextVeiew?.text
                            mainTextVeiew?.setText("$oldText \n $received")
                        })

                        Log.d(TAG, received.toString())

                        writer.close()
                        reader.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return null
                }
            }

            task.execute(inetSocketAddress)
        }catch (e:Exception) {
            // Err Display
            Snackbar.make(view, "送信エラー", LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MyActivity"
    }
}