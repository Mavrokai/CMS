import axios from 'axios'

interface ErrorResponse {
  message: string
  code?: string
}

interface Events {}

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response) {
      const errorData: ErrorResponse = error.response.data
      console.error("Erreur de l'API :", errorData.message || 'Erreur inconnue')
      return Promise.reject(
        new Error(errorData.message || `Erreur de l'API: ${error.response.status}`),
      )
    } else if (error.request) {
      console.error('Aucune réponse du serveur :', error.request)
      return Promise.reject(new Error('Le serveur ne répond pas. Veuillez réessayer plus tard.'))
    } else {
      console.error('Erreur de configuration de la requête :', error.message)
      return Promise.reject(new Error(`Erreur: ${error.message}`))
    }
  },
)

export async function getAllEvents(city: string, keywords: string, date: string, limit: bigint) {
  let queryParams: string[] = [];

  if (city.trim() !== "") {
    queryParams.push(`location_city="${encodeURIComponent(city.trim())}"`);
  }

  if (keywords.trim() !== "") {
    const keywordArray = keywords.split(',').map(kw => kw.trim()).filter(kw => kw !== '');
    if (keywordArray.length > 0) {
      const keywordQueries = keywordArray.map(kw => `keywords_fr="${encodeURIComponent(kw)}"`).join(' AND ');
      queryParams.push(`(${keywordQueries})`);
    }
  }

  if (date.trim() !== "") {
    queryParams.push(`daterange_fr="${encodeURIComponent(date.trim())}"`);
  }

  let fetchUrl = '/api/explore/v2.1/catalog/datasets/evenements-publics-openagenda/records';

  if (queryParams.length > 0) {
    fetchUrl += `?where=${queryParams.join(' AND ')}`;
  }

  fetchUrl += `${queryParams.length > 0 ? '&' : '?' }limit=${limit.toString()}`;

  try {
    console.log("Fetching URL:", fetchUrl);
    const response = await api.get(fetchUrl);
    console.log(response.data.records);
    return response.data.records;
  } catch (error) {
    console.error("Error fetching events:", error);
    throw error;
  }
}

export async function getAllCities() {
  try {
    const response = await api.get(
      '/api/explore/v2.1/catalog/datasets/evenements-publics-openagenda/records?select=location_city&group_by=location_city as city&limit=20000',
    )
    console.log(response.data.records)
    return response.data.records
  } catch (error) {
    throw error
  }
}

