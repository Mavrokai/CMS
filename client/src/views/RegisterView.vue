<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const pseudo = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const error = ref('')

const avatarFile = ref(null)
const avatarPreview = ref('https://ui-avatars.com/api/?name=Avatar&background=888&color=fff')

function handleAvatarChange(e) {
  const file = e.target.files[0]
  avatarFile.value = file
  if (file) {
    const reader = new FileReader()
    reader.onload = (ev) => {
      avatarPreview.value = ev.target.result
    }
    reader.readAsDataURL(file)
  } else {
    avatarPreview.value = 'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
  }
}

function decodeJwt(token) {
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

async function handleGoogleLogin(googleResponse) {
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
      window.dispatchEvent(new Event('user-updated'))
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
      text: 'signup_with',
    })
  }
})

const handleRegister = async () => {
  loading.value = true
  error.value = ''
  if (!pseudo.value || !email.value || !password.value || !confirmPassword.value) {
    error.value = 'Tous les champs sont obligatoires'
    loading.value = false
    return
  }
  if (password.value !== confirmPassword.value) {
    error.value = 'Les mots de passe ne correspondent pas'
    loading.value = false
    return
  }
  try {
    const formData = new FormData()
    formData.append('pseudo', pseudo.value)
    formData.append('email', email.value)
    formData.append('password', password.value)
    if (avatarFile.value) {
      formData.append('avatar', avatarFile.value)
    }
    const response = await fetch('http://localhost:8080/register', {
      method: 'POST',
      body: formData,
    })
    const data = await response.json()
    if (response.ok) {
      window.dispatchEvent(new Event('user-updated'))
      router.push('/login')
    } else {
      error.value = data.error || "Erreur lors de l'inscription"
    }
  } catch (e) {
    error.value = 'Erreur réseau'
  }
  loading.value = false
}
</script>

<template>
  <div class="register-bg">
    <div class="register-card">
      <div class="register-icon">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
          <circle cx="12" cy="12" r="12" fill="#7c3aed" />
          <path
            d="M12 12c1.933 0 3.5-1.567 3.5-3.5S13.933 5 12 5s-3.5 1.567-3.5 3.5S10.067 12 12 12zm0 1.5c-2.33 0-7 1.167-7 3.5V19h14v-2c0-2.333-4.67-3.5-7-3.5z"
            fill="#fff"
          />
          <circle cx="17" cy="7" r="2" fill="#fff" stroke="#7c3aed" stroke-width="1.5" />
        </svg>
      </div>
      <h2 class="register-title">Inscription</h2>
      <div class="register-subtitle">Rejoignez la communauté SortirEnsemble</div>
      <div id="google-one-tap-button" class="google-button"></div>
      <div class="divider"><span>ou</span></div>
      <form @submit.prevent="handleRegister" class="register-form" enctype="multipart/form-data">
        <div class="avatar-label">Avatar</div>
        <div class="avatar-upload">
          <label class="avatar-preview">
            <img :src="avatarPreview" alt="Aperçu avatar" />
            <input
              type="file"
              accept="image/*"
              @change="handleAvatarChange"
              style="display: none"
            />
            <span class="avatar-edit">Changer</span>
          </label>
        </div>
        <label for="pseudo">Nom d'utilisateur</label>
        <input v-model="pseudo" id="pseudo" type="text" placeholder="pseudo" required />
        <label for="email">Adresse email</label>
        <input v-model="email" id="email" type="email" placeholder="votre@email.com" required />
        <label for="password">Mot de passe</label>
        <input v-model="password" id="password" type="password" placeholder="********" required />
        <label for="confirmPassword">Confirmer le mot de passe</label>
        <input
          v-model="confirmPassword"
          id="confirmPassword"
          type="password"
          placeholder="********"
          required
        />
        <button type="submit" :disabled="loading" class="main-btn">Créer mon compte</button>
      </form>
      <div class="register-link">
        Déjà un compte ? <a @click.prevent="router.push('/login')">Se connecter</a>
      </div>
      <div v-if="loading" class="loading">Chargement...</div>
      <div v-if="error" class="error">{{ error }}</div>
    </div>
  </div>
</template>

<style>
.register-bg {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
}
.register-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(44, 62, 80, 0.08);
  padding: 48px 40px 40px 40px;
  max-width: 450px;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.register-icon {
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
.register-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
  margin-bottom: 6px;
  margin-top: 0;
  text-align: center;
}
.register-subtitle {
  color: #888;
  font-size: 15px;
  margin-bottom: 18px;
  text-align: center;
}
.google-button {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
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
.avatar-label {
  font-size: 13px;
  color: #444;
  text-align: left;
  margin-bottom: 2px;
  margin-top: 8px;
  width: 100%;
}
.avatar-upload {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  margin-bottom: 12px;
}
.avatar-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  position: relative;
}
.avatar-preview img {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  object-fit: cover;
  border: 2.5px solid #7c3aed;
  background: #fff;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.06);
}
.avatar-edit {
  font-size: 12px;
  color: #7c3aed;
  margin-top: 4px;
  text-decoration: underline;
  cursor: pointer;
}
.register-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.register-form label {
  font-size: 13px;
  color: #444;
  text-align: left;
  margin-bottom: 2px;
  margin-top: 8px;
}
.register-form input {
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  background: #fafbfc;
  font-size: 15px;
  color: #222;
  outline: none;
  margin-bottom: 2px;
}
.register-form input:focus {
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
