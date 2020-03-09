package br.com.mmt.kchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.act_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        text_account.setOnClickListener{
//            Log.i("Teste", "Executou")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
