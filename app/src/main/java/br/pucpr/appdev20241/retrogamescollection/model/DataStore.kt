package br.pucpr.appdev20241.retrogamescollection.model

import android.content.Context
import android.icu.text.Transliterator.Position
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object DataStore {

    // Lista que mantém todos os jogos carregados do Firestore.
    val games: MutableList<Game> = arrayListOf()

    // Instância do Firestore para interagir com o banco de dados na nuvem.
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Nome da coleção no Firestore onde os jogos são armazenados.
    private const val DB_COLLECTION_GAMES = "games"

    // Método para carregar os jogos do Firestore
    fun loadGames(callback: (Boolean) -> Unit) {
        firestore.collection("games")
            .get()
            .addOnSuccessListener { result ->
                // Limpa a lista antes de adicionar novos jogos.
                games.clear()
                for (document in result) {
                    // Converte o documento Firestore para um objeto Game.
                    val game = document.toObject(Game::class.java).apply {
                        // Define o ID do jogo com o ID do documento.
                        id = document.id
                    }
                    // Adiciona o jogo à lista.
                    games.add(game)
                }
                // Indica que os jogos foram carregados com sucesso.
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("DataStore", "Erro ao carregar jogos: ", exception)
                // Indica que houve um erro ao carregar os jogos.
                callback(false)
            }
    }

    // Método para obter um jogo específico da lista com base na posição.
    fun getGame(position: Int): Game {
        return games[position]
    }

    // Método para adicionar um novo jogo à coleção "games" no Firestore.
    fun addGame(game: Game, onComplete: ((Boolean) -> Unit)? = null) {
        val gameData = hashMapOf(
            "gameFabricant" to game.gameFabricant,
            "gameTitle" to game.gameTitle,
            "dataLancamento" to game.dataLancamento
        )

        firestore.collection(DB_COLLECTION_GAMES)
            .add(gameData)
            .addOnSuccessListener { documentReference ->
                // Define o ID do jogo com o ID do documento.
                game.id = documentReference.id
                // Adiciona o jogo à lista local.
                games.add(game)
                // Indica que o jogo foi adicionado com sucesso.
                onComplete?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("RetroApp!", "Erro ao adicionar o jogo", e)
                // Indica que houve um erro ao adicionar o jogo.
                onComplete?.invoke(false)
            }
    }

    // Método para editar um jogo existente no Firestore e atualizar a lista local.
    fun editGame(position: Int, game: Game, onComplete: ((Boolean) -> Unit)? = null) {
        val gameId = getGame(position).id ?: return
        val gameData = hashMapOf(
            "gameFabricant" to game.gameFabricant,
            "gameTitle" to game.gameTitle,
            "dataLancamento" to game.dataLancamento
        )

        firestore.collection(DB_COLLECTION_GAMES).document(gameId)
            .set(gameData)
            .addOnSuccessListener {
                // Atualiza o jogo na lista local.
                games[position] = game
                onComplete?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("RetroApp!", "Erro ao editar o jogo", e)
                onComplete?.invoke(false)
            }
    }

    // Método para remover um jogo do Firestore e da lista local.
    fun removeGame(position: Int, onComplete: ((Boolean) -> Unit)? = null) {
        val gameId = getGame(position).id ?: return

        firestore.collection(DB_COLLECTION_GAMES).document(gameId)
            .delete()
            .addOnSuccessListener {
                // Remove o jogo da lista local.
                games.removeAt(position)
                onComplete?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("RetroApp!", "Erro ao remover o jogo", e)
                onComplete?.invoke(false)
            }
    }

    // Método para buscar jogos pelo título no Firestore.
    fun searchGames(searchText: String, onComplete: ((Boolean) -> Unit)? = null) {
        // Busca todos os jogos se o texto estiver vazio.
        val query = if (searchText.isEmpty()) {
            firestore.collection(DB_COLLECTION_GAMES)
        } else {
            //Busca os jogos de acordo com o digitado na busca
            firestore.collection(DB_COLLECTION_GAMES)
                .whereGreaterThanOrEqualTo("gameTitle", searchText)
                .whereLessThanOrEqualTo("gameTitle", "$searchText\uf8ff")
        }

        query.get()
            .addOnSuccessListener { result ->
                // Limpa a lista antes de adicionar novos resultados.
                games.clear()
                for (document in result) {
                    val game = document.toObject(Game::class.java)
                    game.id = document.id
                    games.add(game)
                }
                onComplete?.invoke(true)
            }
            .addOnFailureListener { e ->
                Log.e("RetroApp!", "Erro ao buscar os jogos", e)
                onComplete?.invoke(false)
            }
    }
}