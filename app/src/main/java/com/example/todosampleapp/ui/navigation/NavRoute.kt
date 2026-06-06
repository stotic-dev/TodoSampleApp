package com.example.todosampleapp.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object TodoList : NavRoute

    @Serializable
    data object TodoAdd : NavRoute

    @Serializable
    data class TodoDetail(
        val id: Int,
    ) : NavRoute
}
