import api from './api'
import type { Regista } from '../types'

export async function getRegisti(): Promise<Regista[]> {
  const { data } = await api.get<Regista[]>('/registi')
  return data
}
