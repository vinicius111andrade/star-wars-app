package com.vdemelo.starwarswiki.domain.usecase

import com.vdemelo.starwarswiki.domain.DefaultErrors
import com.vdemelo.starwarswiki.domain.entity.RequestStatus
import com.vdemelo.starwarswiki.domain.entity.model.Planet
import com.vdemelo.starwarswiki.domain.entity.model.PlanetsList
import com.vdemelo.starwarswiki.domain.repository.StarWarsRemoteRepository

class PlanetsUseCase(private val remoteRepository: StarWarsRemoteRepository) {

    suspend fun fetchPlanets(page: Int = 0, search: String? = null): RequestStatus<PlanetsList> {
        val response = try {
            remoteRepository.fetchPlanets(page, search)
        } catch (e: Exception) {
            return RequestStatus.Error(message = DefaultErrors.UNKNOWN_ERROR.message)
        }
        val entity = PlanetsList(response)
        return RequestStatus.Success(data = entity)
    }

    suspend fun fetchPlanetDetails(speciesNumber: Int): RequestStatus<Planet> {
        val response = try {
            remoteRepository.fetchPlanetDetails(speciesNumber)
        } catch (e: Exception) {
            return RequestStatus.Error(message = DefaultErrors.UNKNOWN_ERROR.message)
        }
        val entity = Planet(response)
        return RequestStatus.Success(data = entity)
    }

}
