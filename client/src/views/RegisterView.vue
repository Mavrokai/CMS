<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const pseudo = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const error = ref('')

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
    const response = await fetch('http://localhost:8080/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        pseudo: pseudo.value,
        email: email.value,
        password: password.value,
      }),
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
    <form @submit.prevent="handleRegister" class="register-form">
      <input v-model="pseudo" type="text" placeholder="Pseudo" required />
      <input v-model="email" type="email" placeholder="Email" required />
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
  max-width: 350px;
  margin: 60px auto;
  padding: 30px;
  background: #222;
  border-radius: 10px;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
}
.register-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.register-form input {
  padding: 8px;
  border-radius: 5px;
  border: none;
}
.register-form button {
  padding: 10px;
  background: #43a047;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
}
.login-btn {
  margin-top: 18px;
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
