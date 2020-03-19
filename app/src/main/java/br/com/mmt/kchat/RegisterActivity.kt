package br.com.mmt.kchat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.act_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var mSelectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)

        button_cadastrar.setOnClickListener{
            createUser()
        }

        btn_add_img.setOnClickListener{
            selectPhoto()
        }
    }

    private fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            mSelectedUri = data ?.data
            Log.i("Teste -> URI da Imagem ", mSelectedUri.toString())
            val bitmap = MediaStore.Images.Media.getBitmap( contentResolver, mSelectedUri)
            img_photo.setImageBitmap(bitmap)
            btn_add_img.alpha = 0f

        }
    }

    private fun createUser(){
        val email = edit_cad_email.text.toString()
        val nome = edit_text_nome.text.toString()
        val password = edit_cad_password.text.toString()
        val confirmPassword = edit_confirm_pass.text.toString()

        validateEntries()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Log.i("Teste", "UserID é ${it.result.user.uid}")
                    saveUserInFirebase()
                }
            }.addOnFailureListener{
                Log.e("Teste", it.message, it)
            }
    }

    private fun saveUserInFirebase(){
        val fileName = UUID.randomUUID().toString()
        val reference = FirebaseStorage.getInstance().getReference("/images/${fileName}")

        mSelectedUri ?.let {
            reference.putFile(it)
                .addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        Log.i("Teste -> imagem ", it.toString())

                        val url = it.toString()
                        val name = edit_text_nome.text.toString()
                        val email = edit_cad_email.text.toString()
                        val uid = FirebaseAuth.getInstance().uid!!

                        val user = User(uid, name,email,url)

                        FirebaseFirestore.getInstance().collection("users").add(user)
                            .addOnSuccessListener {
                            Log.i("Teste -> Associou", it.id)
                                val intent = Intent(this@RegisterActivity, MessagesActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                        }.addOnFailureListener {
                            Log.e("Teste -> Deu ruim", it.message, it)
                        }

                }
            }
        }
    }

    private fun validateEntries(){
        val email = edit_cad_email.text.toString()
        val nome = edit_text_nome.text.toString()
        val password = edit_cad_password.text.toString()
        val confirmPassword = edit_confirm_pass.text.toString()
      //  val isValid = false
        if(nome.isEmpty() || nome.length < 2){
            Toast.makeText(this, "Nome incorreto! Informe um nome válido!", Toast.LENGTH_LONG)
            return
        }

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email e senha devem ser informados!", Toast.LENGTH_LONG)
            return
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Senha não confere com a anterior!", Toast.LENGTH_LONG)
            return
        }
    }


}
