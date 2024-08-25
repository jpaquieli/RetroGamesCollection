package br.pucpr.appdev20241.retrogamescollection.controller

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import br.pucpr.appdev20241.retrogamescollection.databinding.ActivityMainBinding
import br.pucpr.appdev20241.retrogamescollection.model.DataStore
import br.pucpr.appdev20241.retrogamescollection.view.GamesAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout

class MainActivity : AppCompatActivity() {

    // Binding para a Activity, permite acesso fácil aos elementos da UI
    private lateinit var binding: ActivityMainBinding

    // Adapter para o RecyclerView, que exibirá a lista de jogos
    private lateinit var adapter: GamesAdapter

    // Detector de gestos, usado para detectar toques simples e longos na lista
    private lateinit var gesture: GestureDetector

    // Callback para resultado da Activity de adição de um novo jogo
    private val addGameForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                // Recarrega a lista de jogos do Firestore
                DataStore.loadGames { success ->
                    if (success) {
                        adapter.notifyDataSetChanged()
                        // Notifica o adapter para atualizar a UI
                        Snackbar.make(
                            binding.root,
                            "Jogo ${intent.getStringExtra("game")} adicionado com sucesso",
                            Snackbar.LENGTH_LONG
                        ).show()
                        // Exibe um Snackbar para informar que o jogo foi adicionado
                    } else {
                        Log.e("MainActivity", "Erro ao carregar jogos")
                    }
                }
            }
        }
    }

    // Callback para resultado da Activity de edição de um jogo existente
    private final var editGameForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                DataStore.loadGames { success ->
                    if (success) {
                        adapter.notifyDataSetChanged()
                        Snackbar.make(
                            binding.root,
                            "Jogo ${intent.getStringExtra("game")} adicionado com sucesso",
                            Snackbar.LENGTH_LONG
                        ).show()
                        // Exibe um Snackbar para informar que o jogo foi editado
                    } else {
                        Log.e("MainActivity", "Erro ao carregar jogos")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o binding da Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carrega os jogos do Firestore no DataStore
        DataStore.loadGames { success ->
            if (success) {
                Log.d("MainActivity", "Jogos carregados: ${DataStore.games.size}")
                adapter.notifyDataSetChanged()
            } else {
                Log.e("MainActivity", "Erro ao carregar jogos")
            }
        }

        loadRecycleView()
        configureGesture()
        configureFab()
        configureSearch()
        configureRecycleViewEvents()
    }

    // Sobrescreve o comportamento do botão de "voltar" para exibir um diálogo de confirmação
    override fun onBackPressed() {
        AlertDialog.Builder(this).run {
            setMessage("Tem certeza que deseja fechar esta tela?")
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                super.onBackPressed()  // Chama o comportamento padrão (fecha a Activity)
            }
            setNegativeButton(getString(android.R.string.cancel), null)
            show()
        }
    }

    // Configura o RecyclerView, definindo layout e adapter
    private fun loadRecycleView() {
        LinearLayoutManager(this).run {
            this.orientation = LinearLayoutManager.HORIZONTAL
            // Define a orientação horizontal
            binding.rcvGames.layoutManager = this
            adapter = GamesAdapter(DataStore.games)
            // Adapter recebe a lista de jogos do DataStore
            binding.rcvGames.adapter = adapter
        }
    }

    // Configura gestos para detectar toques e longos toques nos itens da lista
    private fun configureGesture() {
        gesture = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // Detecta toque simples para editar um jogo
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.rcvGames.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.rcvGames.getChildAdapterPosition(child).apply {
                            Intent(this@MainActivity, AddEditGame::class.java).run {
                                putExtra("position", this@apply)
                                editGameForResult.launch(this)
                            }
                        }
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            // Detecta toque longo para remover um jogo
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)

                binding.rcvGames.findChildViewUnder(e.x, e.y).run {
                    this?.let { view ->
                        binding.rcvGames.getChildAdapterPosition(view).apply {
                            val game = DataStore.getGame(this)
                            AlertDialog.Builder(this@MainActivity).run {
                                setMessage("Tem certeza que deseja remover esse jogo?")
                                setPositiveButton("Excluir") { _, _ ->
                                    DataStore.removeGame(this@apply) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Jogo ${game.gameTitle} removido com sucesso",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            adapter.notifyDataSetChanged()
                                        } else {
                                            Log.e("MainActivity", "Erro ao remover o jogo")
                                        }
                                    }
                                }
                                setNegativeButton("Cancelar", null)
                                show()
                            }
                        }
                    }
                }
            }
        })
    }

    // Configura eventos de toque para o RecyclerView usando o GestureDetector
    private fun configureRecycleViewEvents() {
        binding.rcvGames.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y).run {
                    return (this != null && gesture.onTouchEvent(e))
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    // Abre a Activity para adicionar um novo jogo
    private fun addCity() {
        Intent(this, AddEditGame::class.java).run {
            addGameForResult.launch(this)
        }
    }

    // Configura o botão flutuante (FAB) para adicionar um novo jogo
    private fun configureFab() {
        binding.fab.setOnClickListener {
            addCity()
        }
    }

    // Configura a funcionalidade de busca de jogos no Firestore
    private fun configureSearch() {
        binding.btnSearch.setOnClickListener {
            val searchText = binding.txtSearch.text.toString()

            DataStore.searchGames(searchText) { success ->
                if (success) {
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("MainActivity", "Erro ao realizar a busca")
                }
            }
        }
    }
}