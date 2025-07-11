<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import TheWelcome from '../components/TheWelcome.vue'

const router = useRouter()
const userName = ref('')
const avatar = ref('')
const bio = ref('')

onMounted(async () => {
  // Récupérer le nom de l'utilisateur depuis le localStorage
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  const token = localStorage.getItem('token')
  if (user && user.name && token) {
    // Appel API protégé pour récupérer le profil complet
    try {
      const res = await fetch(
        `http://localhost:8080/user/me?pseudo=${encodeURIComponent(user.name)}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )
      if (res.ok) {
        const data = await res.json()
        userName.value = data.pseudo
        avatar.value = data.avatar
        bio.value = data.bio
      } else {
        router.push('/login')
      }
    } catch (e) {
      router.push('/login')
    }
  } else {
    router.push('/login')
  }
})
</script>

<template>
  <main>
    <h2 v-if="userName">Bienvenue sur My_events, {{ userName }} !</h2>
    <img
      v-if="avatar"
      :src="avatar"
      alt="Avatar"
      style="width: 80px; height: 80px; border-radius: 50%; margin: 10px auto; display: block"
    />
    <p v-if="bio">Présentation : {{ bio }}</p>
    <TheWelcome />
  </main>
</template>
