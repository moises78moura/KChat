package br.com.mmt.kchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.act_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        btn_enter.setOnClickListener{
            signIn()
        }

        text_account.setOnClickListener{
//            Log.i("Teste", "Executou")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(){

        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email devem ser informados!", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if(it.isSuccessful){
                Log.i("Teste -> Logou no chat", it.result.user.uid)
                val intent = Intent(this@LoginActivity, MessagesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }.addOnFailureListener{
            Log.e("Teste -> Deu ruim", it.message,it)
        }

    }

}
