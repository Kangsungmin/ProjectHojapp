package com.UnknownCompany.Hojapp

import android.arch.lifecycle.AndroidViewModel
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_join.*
import java.sql.Types

class JoinActivity : AppCompatActivity() {

    var inputId :EditText? = null
    var inputPw :EditText? = null
    var inputPwChk:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
    }

    fun OnJoin(view: View)
    {
        if(inputPw != null)
        {
            var pwString : String = editPwText.text.toString()
            var pwChkString : String = editPwchkText.text.toString()

            Log.i("Tag",pwString)
            Log.d("Tag",pwChkString)
            if(pwString.equals(pwChkString))
            {
                /*정상적인 패스워드*/
                Log.d("Tag","패스워드 일치")
            }
            else
            {
                /*잘못된 패스워드*/
                Log.d("Tag","패스워드 불일치")
            }
        }
    }
}
