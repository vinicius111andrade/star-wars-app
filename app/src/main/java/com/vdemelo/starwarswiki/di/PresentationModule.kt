package com.vdemelo.starwarswiki.di

import com.vdemelo.starwarswiki.ui.HomeViewModel
import com.vdemelo.starwarswiki.ui.screens.species.details.SpeciesDetailsViewModel
import com.vdemelo.starwarswiki.ui.screens.species.list.SpeciesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { SpeciesListViewModel(get()) }
    viewModel { SpeciesDetailsViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}
