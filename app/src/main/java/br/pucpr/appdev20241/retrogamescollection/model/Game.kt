package br.pucpr.appdev20241.retrogamescollection.model

//Clase para armazenar os dados dos jogos
class Game (
    //Variável para armazenar dados do desenvolvedor do jogo
    var gameFabricant: String = "",
    //Variável para armazenar dados do título do jogo
    var gameTitle: String = "",
    //Variável para armazenar o ano de lançamento do jogo
    var dataLancamento: String = "",
    //Variável para armazenar o ID do jogo
    var id: String = "",
) {
    // Construtor sem argumentos, necessário para o Firestore deserializar os objetos corretamente.
    constructor() : this("", "", "", "")
}