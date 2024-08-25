package br.pucpr.appdev20241.retrogamescollection.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Database {
    // Instância do Firestore, utilizada para interagir com o banco de dados na nuvem.
    private val db = FirebaseFirestore.getInstance()

    companion object {
        // Nome da coleção onde os jogos serão armazenados no Firestore.
        const val DB_COLLECTION_GAMES = "games"
        // Campos da coleção de jogos.
        const val DB_FIELD_DEVELOPER = "gameFabricant"
        const val DB_FIELD_TITLE = "gameTitle"
        const val DB_FIELD_YEAR = "dataLancamento"
    }

    // Função para adicionar um novo jogo ao Firestore.
    fun addGame(game: Game): Task<Void> {
        // Cria um mapa de dados que representa o jogo a ser adicionado.
        val gameData = hashMapOf(
            DB_FIELD_DEVELOPER to game.gameFabricant,
            DB_FIELD_TITLE to game.gameTitle,
            DB_FIELD_YEAR to game.dataLancamento
        )
        // Adiciona o jogo na coleção "games" com o ID fornecido.
        return db.collection(DB_COLLECTION_GAMES).document(game.id.toString()).set(gameData)
    }
    // Função para recuperar todos os jogos do Firestore, ordenados pelo título.
    fun getAllGames(): Task<QuerySnapshot> {
        return db.collection(DB_COLLECTION_GAMES)
            .orderBy(DB_FIELD_TITLE)
            .get()
    }
    // Função para editar os dados de um jogo existente no Firestore.
    fun editGame(game: Game): Task<Void> {
        // Cria um mapa de dados atualizado para o jogo.
        val gameData = hashMapOf(
            DB_FIELD_DEVELOPER to game.gameFabricant,
            DB_FIELD_TITLE to game.gameTitle,
            DB_FIELD_YEAR to game.dataLancamento
        )
        // Atualiza os dados do jogo com o ID fornecido.
        return db.collection(DB_COLLECTION_GAMES).document(game.id.toString())
            .set(gameData)
    }
    // Função para remover um jogo do Firestore baseado no ID do jogo.
    fun removeGame(gameId: String): Task<Void> {
        return db.collection(DB_COLLECTION_GAMES).document(gameId.toString()).delete()
    }
    // Função para buscar jogos no Firestore cujo título corresponda a um texto de busca.
    fun searchGamesWithName(searchText: String): Task<QuerySnapshot> {
        return db.collection(DB_COLLECTION_GAMES)
            .whereGreaterThanOrEqualTo(DB_FIELD_TITLE, searchText)
            .whereLessThanOrEqualTo(DB_FIELD_TITLE, "$searchText\uf8ff")
            .get()
    }
}