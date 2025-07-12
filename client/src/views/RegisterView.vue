<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const pseudo = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const avatarFile = ref(null)
const avatarPreview = ref('https://ui-avatars.com/api/?name=Avatar&background=888&color=fff')
const loading = ref(false)
const error = ref('')

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
  <div class="register-container">
    <h2>Inscription</h2>
    <div class="avatar-preview">
      <img :src="avatarPreview" alt="Aperçu avatar" />
    </div>
    <form @submit.prevent="handleRegister" class="register-form" enctype="multipart/form-data">
      <input v-model="pseudo" type="text" placeholder="Pseudo" required />
      <input v-model="email" type="email" placeholder="Email" required />
      <input type="file" accept="image/*" @change="handleAvatarChange" />
      <input v-model="password" type="password" placeholder="Mot de passe" required />
      <input
        v-model="confirmPassword"
        type="password"
        placeholder="Confirmer le mot de passe"
        required
      />
      <button type="submit" :disabled="loading">S'inscrire</button>
    </form>
    <button class="login-btn" @click="router.push('/login')" type="button">
      Retour à la connexion
    </button>
    <div v-if="loading" class="loading">Chargement...</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<style>
.register-container {
  max-width: 370px;
  margin: 60px auto;
  padding: 30px;
  background: #23272f;
  border-radius: 12px;
  color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.18);
  text-align: center;
}
.avatar-preview {
  display: flex;
  justify-content: center;
  margin-bottom: 18px;
}
.avatar-preview img {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #43a047;
  background: #fff;
  box-shadow: 0 1px 6px rgba(67, 160, 71, 0.12);
}
.register-form {
  display: flex;
  flex-direction: column;
  gap: 13px;
}
.register-form input[type='file'] {
  background: #fff;
  color: #23272f;
  border-radius: 5px;
  padding: 7px;
}
.register-form input {
  padding: 9px;
  border-radius: 5px;
  border: none;
}
.register-form button {
  padding: 11px;
  background: #43a047;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  font-size: 1.1em;
}
.login-btn {
  margin-top: 20px;
  padding: 10px;
  background: #4285f4;
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
</style>
