package com.example.breakingbad.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingbad.R
import com.example.breakingbad.data.models.Character
import com.example.breakingbad.databinding.ActivityMainBinding
import com.example.breakingbad.ui.details.DetailsFragment
import com.example.breakingbad.ui.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View, CharactersListener {

    @Inject
    lateinit var presenter: MainContract.Presenter
    lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        CharactersAdapter(arrayListOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.takeView(this)

        initializeViews()
        loadData()
    }

    private fun initializeViews() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = this@MainActivity.adapter
        }

        binding.searchBtn.setOnClickListener {
            hideKeyboard()
            val text = binding.searchET.text.toString()
            if(text.isBlank())
                Toast.makeText(this, getString(R.string.error_input_name), Toast.LENGTH_LONG).show()
            else {
                presenter.loadCharacters(name = text)
            }
        }

        binding.searchET.addTextChangedListener {
            if(it?.isBlank() == true)
                presenter.loadCharacters()
        }

        binding.filterBtn.setOnClickListener {
            binding.searchET.setText("")
            showFilterList()
        }
    }

    private fun loadData() {
        presenter.loadCharacters()
    }

    override fun showCharactersList(characters: ArrayList<Character>) {
        binding.warningTextView.visibility = View.GONE
        adapter.setData(characters)
    }

    private fun showFilterList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.filter_title))
        val seasons = arrayOf(
            getString(R.string.filter_all),
            getString(R.string.filter_1),
            getString(R.string.filter_2),
            getString(R.string.filter_3),
            getString(R.string.filter_4),
            getString(R.string.filter_5)
        )
        builder.setItems(seasons) { dialog, which ->
            when (which) {
                0 -> {
                    presenter.loadCharacters()
                }
                1, 2, 3, 4, 5 -> {
                    presenter.loadCharacters(season = which)
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showLoadingBar(show: Boolean) {
        binding.progressBar.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun showErrorOnCharactersList() {
        Toast.makeText(this, getString(R.string.error_loading_characters), Toast.LENGTH_LONG).show()
        binding.warningTextView.visibility = View.VISIBLE
        binding.warningTextView.text = getString(R.string.error_loading_characters)
    }

    override fun showEmptyList() {
        binding.warningTextView.visibility = View.VISIBLE
        binding.warningTextView.text = getString(R.string.warning_empty_characters)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    override fun onCharacterSelected(id: Int?) {
        id?.let { presenter.loadCharacter(id) }
    }

    override fun showCharacterDetails(character: Character) {
        val fm: FragmentManager = supportFragmentManager
        val detailsFragment: DetailsFragment =
            DetailsFragment.newInstance(character)
        detailsFragment.show(fm, "fragment_details")
    }
}