<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import TheWelcome from '../components/TheWelcome.vue'

const router = useRouter()
const userName = ref('')
const avatar = ref('')
const bio = ref('')

const getAvatarUrl = (avatarPath) => {
  if (!avatarPath) return null

  if (avatarPath.startsWith('https://')) {
    return avatarPath
  }

  if (avatarPath.startsWith('/uploads/avatars/')) {
    const filename = avatarPath.split('/').pop()
    return `http://localhost:8080/user/avatar/${filename}`
  }

  return avatarPath
}

onMounted(async () => {
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  const token = localStorage.getItem('token')
  if (user && user.name && token) {
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
        avatar.value = getAvatarUrl(data.avatar)
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
    <div v-if="avatar" class="avatar-container">
      <img :src="avatar" alt="Avatar" class="user-avatar" @error="handleAvatarError" />
    </div>
    <div v-else class="avatar-placeholder">
      <p>Aucun avatar disponible</p>
    </div>
    <p v-if="bio">Présentation : {{ bio }}</p>
    <TheWelcome />
  </main>
</template>

<style scoped>
.avatar-container {
  text-align: center;
  margin: 20px 0;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #43a047;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.avatar-placeholder {
  text-align: center;
  margin: 20px 0;
  color: #888;
}

.avatar-placeholder p {
  margin: 0;
  font-style: italic;
}
</style>

<script>
function handleAvatarError(event) {
  event.target.style.display = 'none'
}
</script>
