<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { GoogleLogin } from 'vue3-google-login'

const router = useRouter()
const user = ref(null)
const loading = ref(false)
const error = ref('')
const pseudo = ref('')
const password = ref('')
const isLoggedIn = ref(
  !!(typeof window !== 'undefined' && window.localStorage && localStorage.getItem('token')),
)

// Connexion classique avec appel backend
const handleLogin = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await fetch('http://localhost:8080/api/authenticate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ pseudo: pseudo.value, password: password.value }),
    })
    const data = await response.json()
    if (response.ok) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ name: data.name }))
      isLoggedIn.value = true
      router.push('/')
    } else {
      error.value = data.error || 'Erreur de connexion'
    }
  } catch (e) {
    error.value = 'Erreur réseau'
  }
  loading.value = false
}

// Connexion Google
async function onGoogleSuccess(response) {
  console.log('Réponse Google:', response)
  let profile
  if (response.profileObj) {
    profile = response.profileObj
  } else if (response.credential) {
    // Décoder le JWT Google
    const base64Url = response.credential.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
        .join(''),
    )
    profile = JSON.parse(jsonPayload)
  } else {
    error.value = 'Impossible de récupérer le profil Google'
    return
  }
  try {
    const res = await fetch('http://localhost:8080/auth/google', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: profile.email,
        name: profile.name,
        avatar: profile.picture || profile.imageUrl,
      }),
    })
    const data = await res.json()
    if (res.ok) {
      localStorage.setItem('token', data.token)
      localStorage.setItem(
        'user',
        JSON.stringify({ name: data.name, avatar: data.avatar, bio: data.bio }),
      )
      isLoggedIn.value = true
      setTimeout(() => {
        router.push('/')
      }, 100)
    } else {
      error.value = data.error || 'Erreur Google'
    }
  } catch (e) {
    error.value = 'Erreur réseau'
  }
}

function onGoogleError(error) {
  error.value = 'Erreur lors de la connexion Google'
}

// Déconnexion
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  user.value = null
  isLoggedIn.value = false
  router.push('/login')
}
</script>

<template>
  <div class="login-container">
    <h2>Connexion</h2>
    <form @submit.prevent="handleLogin" class="login-form">
      <input v-model="pseudo" type="text" placeholder="Pseudo" required />
      <input v-model="password" type="password" placeholder="Mot de passe" required />
      <button type="submit" :disabled="loading">Se connecter</button>
    </form>
    <div class="google-separator">ou</div>
    <div id="google-login-btn">
      <GoogleLogin
        :onSuccess="onGoogleSuccess"
        :onError="onGoogleError"
        :buttonConfig="{ theme: 'filled_blue', size: 'large' }"
      />
    </div>
    <button class="register-btn" @click="router.push('/register')" type="button">S'inscrire</button>
    <div v-if="loading" class="loading">Chargement...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <button v-if="isLoggedIn" class="logout-btn" @click="logout" type="button">Déconnexion</button>
  </div>
</template>

<style>
.login-container {
  max-width: 350px;
  margin: 60px auto;
  padding: 30px;
  background: #222;
  border-radius: 10px;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
}
.login-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.login-form input {
  padding: 8px;
  border-radius: 5px;
  border: none;
}
.login-form button {
  padding: 10px;
  background: #4285f4;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}
.google-separator {
  margin: 18px 0 8px 0;
  color: #bbb;
}
.google-btn {
  margin-top: 10px;
  padding: 10px;
  background: #fff;
  color: #222;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 100%;
  box-shadow: 0 1px 4px rgba(66, 133, 244, 0.15);
  transition: background 0.2s;
}
.google-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}
.register-btn {
  margin-top: 18px;
  padding: 10px;
  background: #43a047;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 100%;
}
.loading {
  margin-top: 10px;
  color: #fbc02d;
}
.error {
  margin-top: 10px;
  color: #e57373;
}
.logout-btn {
  margin-top: 18px;
  padding: 10px;
  background: #e53935;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  width: 100%;
}
</style>
