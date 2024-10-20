package com.example.trendingmovies.movieList.data.repository

import coil.network.HttpException
import com.example.trendingmovies.movieList.data.local.movie.MovieDatabase
import com.example.trendingmovies.movieList.data.mapper.toMovie
import com.example.trendingmovies.movieList.data.mapper.toMovieEntity
import com.example.trendingmovies.movieList.data.remote.MovieApi
import com.example.trendingmovies.movieList.domain.model.Movie
import com.example.trendingmovies.movieList.domain.repository.MovieListRepository
import com.example.trendingmovies.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {

    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load movie"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load movie"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load movie"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(Resource.Success(
                data = movieEntities.map { movieEntity ->
                    movieEntity.toMovie(category)
                }
            ))
            emit(Resource.Loading(false))

        }

    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {

            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(
                        data = movieEntity.toMovie(movieEntity.category)
                    )
                )

                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error("Couldn't load movie"))
        }
    }
}