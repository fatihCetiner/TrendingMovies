package com.example.trendingmovies.movieList.presentation.movieList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trendingmovies.movieList.presentation.componentes.MovieItem
import com.example.trendingmovies.movieList.util.Category


@Composable
fun UpcomingMoviesScreen(
    movieLisState: MovieListState,
    navController: NavController,
    onEvent: (MovieListUiEvent) -> Unit
){
    if(movieLisState.upcomingMovieList.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ){
            items(movieLisState.upcomingMovieList.size){index ->
                MovieItem(
                    movie = movieLisState.upcomingMovieList[index],
                    navHostController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                if(index >= movieLisState.upcomingMovieList.size - 1 && !movieLisState.isLoading){
                    onEvent(MovieListUiEvent.Paginate(Category.UPCOMING))
                }
            }
        }
    }

}