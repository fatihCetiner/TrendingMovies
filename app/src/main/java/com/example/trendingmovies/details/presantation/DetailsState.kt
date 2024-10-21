package com.example.trendingmovies.details.presantation

import com.example.trendingmovies.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
)
