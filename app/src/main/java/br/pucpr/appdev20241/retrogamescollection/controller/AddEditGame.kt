package br.pucpr.appdev20241.retrogamescollection.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.pucpr.appdev20241.retrogamescollection.databinding.ActivityAddEditGameBinding
import br.pucpr.appdev20241.retrogamescollection.model.DataStore
import br.pucpr.appdev20241.retrogamescollection.model.Game

class AddEditGame: AppCompatActivity() {

    // View binding para acessar os elementos de UI
    private lateinit var binding: ActivityAddEditGameBinding

    // Armazena a posição do jogo, usada para edição
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera a posição do jogo que foi enviada via Intent, se disponível
        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
        }

        // Configura o botão "Salvar" para salvar o jogo
        binding.btnSave.setOnClickListener {
            getData()?.let { game ->
                saveGame(game)
            }?: run{
                showMessage("Campos inválidos")
            }
        }
        // Configura o botão "Cancelar" para fechar a Activity sem salvar
        binding.btnCancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    // Loga quando a Activity é pausada
    override fun onPause() {
        super.onPause()
        Log.d("RetroCollection", "Activity Pausada")
    }

    // Loga quando a Activity é destruída
    override fun onDestroy() {
        super.onDestroy()

        Log.d("RetroCollection","Activity Destruída" )
    }


    // Recupera os dados do jogo a partir dos campos de entrada de texto
    private fun getData(): Game? {

        val gameFabricant = binding.txtDeveloper.text.toString()
        val gameTitle = binding.txtTitle.text.toString()
        val dataLancamento = binding.txtYear.text.toString().toIntOrNull() ?: 0

        // Verifica se os campos obrigatórios estão preenchidos
        if (gameFabricant.isEmpty() || dataLancamento == 0)
            return null

        return Game(gameFabricant, gameTitle, dataLancamento.toString())
    }

    // Configura os campos de entrada com os dados do jogo que está sendo editado
    private fun setData(position: Int) {
        DataStore.getGame(position).run {
            binding.txtDeveloper.setText(this.gameFabricant)
            binding.txtTitle.setText(this.gameTitle)
            binding.txtYear.setText(this.dataLancamento)
        }
    }

    // Salva o jogo, chamando o método de adicionar ou editar conforme a situação
    private fun saveGame(game: Game) {
        if (position == -1)
            DataStore.addGame(game)
        else
            DataStore.editGame(position, game)
        Intent().run {
            putExtra("game", game.gameTitle)
            setResult(RESULT_OK, this)
        }
        finish()
    }

    // Exibe uma mensagem em um AlertDialog
    fun showMessage(message: String) {
        AlertDialog.Builder(this).run {
            title = "Retro Games Collection"
            setMessage(message)
            setPositiveButton("Ok", null)
            show()
        }
    }
}



