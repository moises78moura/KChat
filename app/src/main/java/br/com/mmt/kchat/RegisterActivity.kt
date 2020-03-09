package br.com.mmt.kchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.act_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)

        button_cadastrar.setOnClickListener{

            val email = edit_cad_email.text.toString()
            val nome = edit_text_nome.text.toString()
            val password = edit_cad_password.text.toString()
            val confirmPassword = edit_confirm_pass.text.toString()

            Log.i("Teste",  "Nome: ${nome} Email: ${email} | senha: ${password} | Confirmação : ${confirmPassword}")

        }

    }
}
