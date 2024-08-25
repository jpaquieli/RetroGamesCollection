package br.pucpr.appdev20241.retrogamescollection.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev20241.retrogamescollection.databinding.AdapterGamesBinding
import br.pucpr.appdev20241.retrogamescollection.model.Game

class GamesAdapter(var games: MutableList<Game>):RecyclerView.Adapter<GamesAdapter.GameHolder>() {

    // ViewHolder interno que mantém a referência aos elementos da UI para cada item do RecyclerView
    inner class GameHolder(val binding: AdapterGamesBinding):
            RecyclerView.ViewHolder(binding.root)

    // Cria e retorna um novo ViewHolder (GameHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        AdapterGamesBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            return GameHolder(this)
        }
    }

    // Retorna o tamanho da lista de jogos (número de itens no RecyclerView)
    override fun getItemCount(): Int = games.size

    // Vincula os dados de um jogo específico (na posição 'position') aos elementos da UI no ViewHolder
    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        games[position].run {
            holder.binding.txtYear.text = this.dataLancamento
            holder.binding.txtDeveloper.text = this.gameFabricant
            holder.binding.txtTitle.text= this.gameTitle
        }
    }
}