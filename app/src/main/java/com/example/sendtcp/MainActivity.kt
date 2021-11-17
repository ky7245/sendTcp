package com.example.sendtcp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun clickSendButton(view: View) {
        val ipAddr = "192.168.1.7"
        val portNum = 58080

        val inetSocketAddress = InetSocketAddress(ipAddr, portNum)

        val task = object : AsyncTask<InetSocketAddress, Void, Void>()  {
            override fun doInBackground(vararg p0: InetSocketAddress?): Void? {
                var socket: Socket? = null
                try {
                    // connect
                    socket = Socket()
                    socket.connect(p0[0])
                    val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))

                    writer.write("test string.....")
                    writer.close()

                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                    var received = reader.use { it.readText() }


                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return null
            }
        }

        task.execute(inetSocketAddress)
    }
}