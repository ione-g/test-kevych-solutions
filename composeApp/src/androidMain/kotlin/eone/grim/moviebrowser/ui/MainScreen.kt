package eone.grim.moviebrowser.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eone.grim.moviebrowser.ui.nav.Route

@Composable
fun MainScreen(nav: NavHostController = rememberNavController()) {


            NavHost(
                navController = nav,
                startDestination = Route.Movies.path
            ) {
                composable(Route.Movies.path) { MovieListScreen(nav) }
                composable(Route.Details.path) { entry ->
                    val id = entry.arguments?.getString("id")?.toLongOrNull() ?: return@composable
                    MovieDetailsScreen(id)
                }
            }


}