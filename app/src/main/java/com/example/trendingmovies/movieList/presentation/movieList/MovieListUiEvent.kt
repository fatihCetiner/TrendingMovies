package com.example.trendingmovies.movieList.presentation.movieList

sealed class MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent()
    data object Navigate: MovieListUiEvent()
}