package com.inter.rickmorty.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inter.rickmorty.ui.detail.CharacterDetailScreen
import com.inter.rickmorty.ui.home.HomeScreen
import com.inter.rickmorty.ui.vm.SharedViewModel

private const val HomeScreen = "Home"
private const val CharacterScreen = "Detail"

@Composable
fun MainNavHost(
    mainNavController: NavHostController,
    modifier: Modifier
) {

    NavHost(navController = mainNavController, startDestination = HomeScreen) {
        composable(HomeScreen) { backStackEntry ->
            val viewModel: SharedViewModel = hiltViewModel(backStackEntry)

            HomeScreen(modifier, viewModel) {
                mainNavController.navigate(CharacterScreen)
            }
        }

        composable(CharacterScreen) {
            // Get the existing ViewModel from HomeScreen but if not fallback to create
            val viewModel: SharedViewModel =
                if (mainNavController.previousBackStackEntry != null)
                    hiltViewModel(mainNavController.previousBackStackEntry!!)
                else
                    hiltViewModel()

            CharacterDetailScreen(
                modifier,
                viewModel
            )
        }
    }
}