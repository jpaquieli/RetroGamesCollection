package br.pucpr.appdev20241.retrogamescollection

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Obtém referências aos elementos da UI
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val passwordEditText2 = findViewById<EditText>(R.id.passwordEditText2)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val password2 = passwordEditText2.text.toString()
            Log.d(TAG, "Clicou no botão de registro com email: $email e senha: $password")
            // Verifica se todos os campos estão preenchidos
            if (email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                // Verifica se as senhas coincidem
                if (password == password2){
                    registerUser(email, password)
                    Toast.makeText(this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show()   }
                else{
                    Toast.makeText(this, "As senhas não condizem", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para registrar um novo usuário no Firebase Auth
    private fun registerUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        Log.d(TAG, "Tentando registrar o usuário")
        // Tenta criar um novo usuário com o email e a senha fornecidos
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Cadastro efetuado com sucesso")
                    Toast.makeText(this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a atividade de registro e volta para a atividade principal
                } else {
                    Log.d(TAG, "Erro no registro de um novo usuário", task.exception)
                    Toast.makeText(this, "Erro no registro de um novo usuário", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao tentar registrar o usuário", e)
            }
    }
}
