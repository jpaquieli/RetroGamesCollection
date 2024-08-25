package br.pucpr.appdev20241.retrogamescollection.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.pucpr.appdev20241.retrogamescollection.R
import br.pucpr.appdev20241.retrogamescollection.RegisterActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita o modo "Edge to Edge", que integra a interface do aplicativo com as bordas da tela
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Configuração para ajustar a visualização conforme os "insets" do sistema (barras de status, navegação etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa o Firebase
        FirebaseApp.initializeApp(this)

        // Configura os botões de registro e login
        configureRegister()
        configureLogin()
    }

    // Configura o botão de registro para abrir a tela de registro
    private fun configureRegister() {
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // Inicia a RegisterActivity quando o botão de registro é clicado
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Configura o botão de login para autenticar o usuário com Firebase Authentication
    private fun configureLogin() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Verifica se os campos de email e senha estão preenchidos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para autenticar o usuário com Firebase Authentication
    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginUsuario", "Login efetuado com sucesso")
                    Toast.makeText(this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    // Inicia a MainActivity após login bem-sucedido
                } else {
                    Log.d("LoginUsuario", "Erro no login", task.exception)
                    Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginUsuario", "Erro ao tentar realizar login", e)
            }
    }
}