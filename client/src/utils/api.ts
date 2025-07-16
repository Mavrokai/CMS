import axios from 'axios'

interface ErrorResponse {
  message: string;
  code?: string; // Code d'erreur optionnel
}

interface Events {
  
}

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.response.use(response => {
  return response;
}, (error) => {
  if (error.response) {
    const errorData: ErrorResponse = error.response.data; // Type l'objet d'erreur
    console.error("Erreur de l'API :", errorData.message || 'Erreur inconnue');
    return Promise.reject(new Error(errorData.message || `Erreur de l'API: ${error.response.status}`));
  } else if (error.request) {
    console.error("Aucune réponse du serveur :", error.request);
    return Promise.reject(new Error("Le serveur ne répond pas. Veuillez réessayer plus tard."));
  } else {
    console.error("Erreur de configuration de la requête :", error.message);
    return Promise.reject(new Error(`Erreur: ${error.message}`));
  }
});

export async function getAllEvents() {
    try {
    const response = await api.get("/api/datasets");
    console.log(response.data)
    return response.data;
  } catch (error) {
    throw error;
  }
}