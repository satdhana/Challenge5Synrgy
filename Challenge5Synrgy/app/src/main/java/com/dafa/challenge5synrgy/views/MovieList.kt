package com.dafa.challenge5synrgy.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.dafa.challenge5synrgy.adapters.MovieListAdapter
import com.dafa.challenge5synrgy.api.RequestState
import com.dafa.challenge5synrgy.databinding.ActivityMovieListBinding
import com.dafa.challenge5synrgy.listeners.OnMovieClickListener
import com.dafa.challenge5synrgy.models.Movies
import com.dafa.challenge5synrgy.viewmodels.MovieViewModel

class MovieList : AppCompatActivity() {
    private var _binding: ActivityMovieListBinding? = null
    private val binding get() = _binding!!
    private var adapter: MovieListAdapter? = null
    private var layoutManager: LayoutManager? = null
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        requestThenObserveAnychangeGenres()
        viewModel.getPopularMovie()
        observeAnychangePopularMovie()
        setupRecycleView()

        adapter?.onMovieClickListener(object : OnMovieClickListener{
            override fun onMoveClick(movies: Movies, genres: String) {
                val intent = Intent(this@MovieList, MovieDetail::class.java)
                intent.putExtra(MovieDetail.movie, movies)
                intent.putExtra(MovieDetail.genres, genres)
                startActivity(intent)
            }
        })

        binding.searchButton.setOnClickListener{
            val query = binding.search.text.toString()
            when {
                query.isEmpty() -> binding.search.error = "Please insert keyword"
                else -> {
                    val intent = Intent(this, SearchMovie::class.java)
                    intent.putExtra(SearchMovie.query, query)
                    startActivity(intent)
                }
            }
        }
    }

    fun observeAnychangePopularMovie() {
        viewModel.popularResponse.observe(this) {
            if (it != null) {
                when(it){
                    is RequestState.Loading -> showLoading()
                    is RequestState.Success -> {
                        hideLoading()
                        it.data?.results?.let{data -> adapter?.differ?.submitList(data.toList())}
                    }
                    is RequestState.Error -> {
                        hideLoading()
                        Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun requestThenObserveAnychangeGenres() {
        viewModel.getGenres().observe(this) {
            if (it != null) {
                when(it){
                    is RequestState.Loading -> {}
                    is RequestState.Success -> it.data.genres?.let{data -> adapter?.setGenre(data)}
                    is RequestState.Error -> Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun setupRecycleView() {
        adapter = MovieListAdapter()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.apply {
            movieList.adapter = adapter
            movieList.layoutManager = layoutManager
            movieList.addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getPopularMovie()
            }
        }
    }
    private fun showLoading() {
        binding.loading.show()
    }

    private fun hideLoading() {
        binding.loading.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}