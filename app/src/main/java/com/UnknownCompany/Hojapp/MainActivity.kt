package com.UnknownCompany.Hojapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var joinActivityButton = findViewById<View>(R.id.joinEmailButton) as Button

        joinActivityButton.setOnClickListener{
            val nextIntent = Intent(this, JoinActivity::class.java)
            startActivity(nextIntent)
        }
    }
}
