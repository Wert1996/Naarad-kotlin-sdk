package com.naarad.naaradsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naarad.naaradsdk.auth.NaaradAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NaaradAuth(this, "", "", "").initialiseApp()

    }
}