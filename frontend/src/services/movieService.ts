import api from './api'
import type { Film, NuovoFilm } from '../types'

export async function getMovies(): Promise<Film[]> {
  const { data } = await api.get<Film[]>('/movies')
  return data
}

export async function createMovie(nuovoFilm: NuovoFilm): Promise<Film> {
  const { data } = await api.post<Film>('/movies', nuovoFilm)
  return data
}
