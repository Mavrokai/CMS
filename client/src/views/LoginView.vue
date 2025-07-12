<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = ref(null)
const loading = ref(false)
const error = ref('')
const pseudo = ref('')
const password = ref('')
const isLoggedIn = ref(
  !!(typeof window !== 'undefined' && window.localStorage && localStorage.getItem('token')),
)

const decodeJwt = (token) => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        })
        .join(''),
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    return null
  }
}

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

const handleGoogleLogin = async (googleResponse) => {
  loading.value = true
  error.value = ''

  const decodedToken = decodeJwt(googleResponse.credential)

  if (!decodedToken) {
    error.value = 'Erreur lors du décodage des données Google'
    loading.value = false
    return
  }

  const requestData = {
    googleId: decodedToken.sub,
    email: decodedToken.email,
    name: decodedToken.name,
    picture: decodedToken.picture,
  }

  try {
    const response = await fetch('http://localhost:8080/api/auth/google', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestData),
    })

    const data = await response.json()

    if (response.ok) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ name: data.name }))
      isLoggedIn.value = true
      router.push('/')
    } else {
      error.value = data.error || 'Erreur de connexion Google'
    }
  } catch (e) {
    error.value = 'Erreur réseau'
  }
  loading.value = false
}

onMounted(() => {
  if (typeof window !== 'undefined' && window.google) {
    window.google.accounts.id.initialize({
      client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
      callback: handleGoogleLogin,
      auto_select: false,
      cancel_on_tap_outside: true,
    })

    window.google.accounts.id.renderButton(document.getElementById('google-one-tap-button'), {
      theme: 'outline',
      size: 'large',
      width: 200,
      text: 'signin_with',
    })
  }
})

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

    <div class="divider">
      <span>ou</span>
    </div>

    <div id="google-one-tap-button" class="google-button"></div>

    <button class="register-btn" @click="router.push('/register')" type="button">S'inscrire</button>
    <div v-if="loading" class="loading">Chargement...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <button v-if="isLoggedIn" class="logout-btn" @click="logout" type="button">Déconnexion</button>
  </div>
</template>

<style>
.login-container {
  max-width: 400px;
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
.divider {
  margin: 20px 0;
  position: relative;
  text-align: center;
}
.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #444;
}
.divider span {
  background: #222;
  padding: 0 10px;
  color: #888;
  font-size: 14px;
}
.google-button {
  margin: 15px 0;
  display: flex;
  justify-content: center;
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
