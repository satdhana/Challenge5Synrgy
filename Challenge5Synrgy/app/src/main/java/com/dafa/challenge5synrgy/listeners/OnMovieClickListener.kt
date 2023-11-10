package com.dafa.challenge5synrgy.listeners

import com.dafa.challenge5synrgy.models.Genres
import com.dafa.challenge5synrgy.models.Movies

interface OnMovieClickListener {
    fun onMoveClick(movies: Movies, genres: String)
}