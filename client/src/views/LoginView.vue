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
      width: 250,
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
  <div class="login-bg">
    <div class="login-card">
      <div class="login-icon">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
          <circle cx="12" cy="12" r="12" fill="#7c3aed" />
          <path
            d="M12 12c1.933 0 3.5-1.567 3.5-3.5S13.933 5 12 5s-3.5 1.567-3.5 3.5S10.067 12 12 12zm0 1.5c-2.33 0-7 1.167-7 3.5V19h14v-2c0-2.333-4.67-3.5-7-3.5z"
            fill="#fff"
          />
        </svg>
      </div>
      <h2 class="login-title">Connexion</h2>
      <div id="google-one-tap-button" class="google-button"></div>
      <div class="divider"><span>ou</span></div>
      <form @submit.prevent="handleLogin" class="login-form">
        <label for="pseudo">Nom d'utilisateur</label>
        <input v-model="pseudo" id="pseudo" type="text" placeholder="Nom d'utilisateur" required />
        <label for="password">Mot de passe</label>
        <input v-model="password" id="password" type="password" placeholder="********" required />
        <button type="submit" :disabled="loading" class="main-btn">Se connecter</button>
      </form>
      <div class="register-link">
        Pas encore de compte ? <a @click.prevent="router.push('/register')">S'inscrire</a>
      </div>
      <div v-if="loading" class="loading">Chargement...</div>
      <div v-if="error" class="error">{{ error }}</div>
    </div>
  </div>
</template>

<style>
.login-bg {
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
}
.login-card {
  margin-top: 6rem;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(44, 62, 80, 0.08);
  padding: 56px 48px 48px 48px;
  max-width: 600px;
  width: 100%;
  max-height: 1000px;
  height: 550px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-icon {
  background: #fff;
  border-radius: 50%;
  width: 72px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -60px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(124, 58, 237, 0.08);
}
.login-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 24px;
  margin-top: 0;
}
.google-btn {
  width: 100%;
  height: 100%;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 10px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
  color: #222;
  cursor: pointer;
  margin-bottom: 16px;
  transition: box-shadow 0.2s;
}
.google-btn:hover {
  box-shadow: 0 2px 8px rgba(66, 133, 244, 0.08);
}
.google-logo {
  width: 20px;
  height: 20px;
}
.divider {
  width: 100%;
  text-align: center;
  position: relative;
  margin: 16px 0 20px 0;
}
.divider span {
  background: #fff;
  color: #bbb;
  font-size: 14px;
  padding: 0 12px;
  position: relative;
  z-index: 1;
}
.divider:before {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  height: 1px;
  background: #e0e0e0;
  z-index: 0;
}
.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.login-form label {
  font-size: 13px;
  color: #444;
  text-align: left;
  margin-bottom: 2px;
  margin-top: 8px;
}
.login-form input {
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  background: #fafbfc;
  font-size: 15px;
  color: #222;
  outline: none;
  margin-bottom: 2px;
}
.login-form input:focus {
  border-color: #7c3aed;
}
.main-btn {
  margin-top: 16px;
  padding: 12px;
  background: #7c3aed;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s;
}
.main-btn:disabled {
  background: #b39ddb;
  cursor: not-allowed;
}
.register-link {
  margin-top: 18px;
  font-size: 14px;
  color: #444;
}
.register-link a {
  color: #7c3aed;
  text-decoration: none;
  font-weight: 500;
  cursor: pointer;
}
.register-link a:hover {
  text-decoration: underline;
}
.loading {
  margin-top: 10px;
  color: #fbc02d;
}
.error {
  margin-top: 10px;
  color: #e53935;
}
</style>
