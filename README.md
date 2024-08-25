RetroGamesCollection

RetroGamesCollection é um aplicativo Android desenvolvido em Kotlin, que permite aos usuários gerenciar sua coleção de jogos retrô. O aplicativo oferece funcionalidades para adicionar, editar, remover e buscar jogos na coleção, com persistência de dados utilizando o Firebase Firestore.

Funcionalidades

Adicionar Jogos: Permite adicionar novos jogos à coleção, incluindo informações como título, desenvolvedora, e data de lançamento.
Editar Jogos: Permite editar as informações dos jogos existentes na coleção.
Remover Jogos: Remove jogos da coleção com confirmação de exclusão.
Buscar Jogos: Funcionalidade de busca para encontrar jogos específicos na coleção.
Exibição em Lista: Exibe os jogos em um RecyclerView com suporte a gestos para editar ou remover itens.
Autenticação: O aplicativo possui uma tela de login inicial para autenticar os usuários.

Tecnologias Utilizadas

Kotlin: Linguagem de programação principal.
Firebase Firestore: Serviço de banco de dados NoSQL para armazenamento e sincronização de dados em tempo real.
Firebase Authentication: Uma solução de identidade de usuário.

Estrutura do Projeto

1. MainActivity
Gerencia a exibição da lista de jogos e as interações do usuário.
Configura o RecyclerView, GestureDetector, e os botões de ação (FAB).
Realiza operações de CRUD (Create, Read, Update, Delete) no Firestore.
2. DataStore
Classe responsável pela interação com o Firebase Firestore.
Contém métodos para carregar, adicionar, editar, remover e buscar jogos.
Mantém uma lista local dos jogos carregados do Firestore para exibição no RecyclerView.
3. Game
Classe modelo que representa os dados de um jogo na coleção.

Como Executar o Projeto

Clone este repositório:
bash
Copiar código
git clone https://github.com/SeuUsuario/RetroGamesCollection.git
Abra o projeto no Android Studio.
Configure o Firebase Firestore com o aplicativo:
Crie um projeto no Firebase.
Adicione o google-services.json ao seu projeto Android.
Ative o Firestore Database no console do Firebase.
Compile e execute o aplicativo no emulador ou em um dispositivo físico.

Melhorias Futuras

Sincronização em tempo real da lista de jogos.
Melhorar a performance na busca por jogos.
Adicionar suporte a diferentes plataformas de jogos (e.g., console, arcade).
Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para enviar pull requests ou relatar problemas.