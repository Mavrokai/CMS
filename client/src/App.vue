<script setup lang="ts">
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import { ref, computed, onMounted } from 'vue'
const route = useRoute()
const router = useRouter()

const isLoggedIn = ref(false)
const user = ref(null)
const avatar = ref('')
const dropdownOpen = ref(false)

const getAvatarUrl = (avatarPath) => {
  if (!avatarPath) return 'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
  if (avatarPath.startsWith('https://')) return avatarPath
  if (avatarPath.startsWith('/uploads/avatars/')) {
    const filename = avatarPath.split('/').pop()
    return `http://localhost:8080/user/avatar/${filename}`
  }
  return avatarPath
}

const fetchUser = async () => {
  const userData = JSON.parse(localStorage.getItem('user') || 'null')
  const token = localStorage.getItem('token')
  if (userData && userData.name && token) {
    isLoggedIn.value = true
    try {
      const res = await fetch(
        `http://localhost:8080/user/me?pseudo=${encodeURIComponent(userData.name)}`,
        { headers: { Authorization: `Bearer ${token}` } },
      )
      if (res.ok) {
        const data = await res.json()
        user.value = data
        avatar.value =
          getAvatarUrl(data.avatar) ||
          'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
      } else {
        isLoggedIn.value = false
        user.value = null
        avatar.value = 'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
      }
    } catch {
      isLoggedIn.value = false
      user.value = null
      avatar.value = 'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
    }
  } else {
    isLoggedIn.value = false
    user.value = null
    avatar.value = 'https://ui-avatars.com/api/?name=Avatar&background=888&color=fff'
  }
}

onMounted(() => {
  fetchUser()
  window.addEventListener('user-updated', fetchUser)
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  isLoggedIn.value = false
  user.value = null
  avatar.value = ''
  dropdownOpen.value = false
  router.push('/login')
}
</script>

<template>
  <div class="app-bg">
    <nav class="navbar">
      <div class="navbar-left">
        <RouterLink to="/" class="logo-link">
          <span class="logo-text">Sortir<span class="logo-bold">Ensemble</span></span>
        </RouterLink>
      </div>
      <div class="navbar-center">
        <RouterLink to="/" class="nav-link">Accueil</RouterLink>
        <RouterLink to="/events" class="nav-link">Événements</RouterLink>
        <RouterLink to="/sorties" class="nav-link">Sorties</RouterLink>
        <RouterLink to="/about" class="nav-link">À propos</RouterLink>
      </div>
      <div class="navbar-right">
        <template v-if="isLoggedIn">
          <div class="avatar-dropdown" @click="dropdownOpen = !dropdownOpen">
            <img :src="avatar" :key="avatar" alt="Avatar" class="navbar-avatar" />
            <div v-if="dropdownOpen" class="dropdown-menu">
              <RouterLink to="/profile" class="dropdown-item" @click.stop="dropdownOpen = false"
                >Profil</RouterLink
              >
              <button class="dropdown-item" @click.stop="handleLogout">Déconnexion</button>
            </div>
          </div>
        </template>
        <template v-else>
          <template v-if="route.path === '/register'">
            <RouterLink to="/login" class="signup-btn">Se connecter</RouterLink>
          </template>
          <template v-else-if="route.path === '/login'">
            <RouterLink to="/register" class="signup-btn">S'inscrire</RouterLink>
          </template>
        </template>
      </div>
    </nav>
    <main :class="{ 'no-padding-top': route.path === '/profile' }">
      <RouterView />
    </main>

    <footer></footer>
  </div>
</template>

<style scoped>
footer {
  background-color: black;
  height: 300px;
  margin-top: 250px;
}

.app-bg {
  min-height: 100vh;
  background: #fafbfc;
}
.navbar {
  width: 100%;
  background: #fff;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.04);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  height: 64px;
  position: sticky;
  top: 0;
  z-index: 10;
}
.navbar-left {
  display: flex;
  align-items: center;
  flex: 0 0 auto;
}
.logo-link {
  text-decoration: none;
  margin-right: 18px;
}
.logo-text {
  font-size: 1.25rem;
  font-weight: 700;
  color: #7c3aed;
  letter-spacing: -1px;
}
.logo-bold {
  color: #222;
  font-weight: 700;
}
.navbar-center {
  display: flex;
  flex: 1 1 0;
  justify-content: center;
  align-items: center;
  gap: 18px;
}
.nav-link {
  color: #222;
  text-decoration: none;
  font-size: 1rem;
  font-weight: 500;
  margin-right: 8px;
  transition: color 0.15s;
}
.nav-link:last-child {
  margin-right: 0;
}
.nav-link:hover {
  color: #7c3aed;
}
.navbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 0 0 auto;
}
.search-btn {
  background: none;
  border: none;
  padding: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  margin-right: 8px;
}
.signup-btn {
  background: #7c3aed;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 7px 16px;
  font-size: 1rem;
  font-weight: 600;
  text-decoration: none;
  transition: background 0.15s;
  box-shadow: 0 1px 4px rgba(124, 58, 237, 0.08);
}
.signup-btn:hover {
  background: #5b21b6;
}
main {
  min-height: calc(100vh - 64px);
  width: 100%;
  display: block;
  align-items: initial;
  justify-content: initial;
}
.no-padding-top {
  padding-top: 0 !important;
}
.navbar-avatar {
  width: 38px !important;
  height: 38px !important;
  display: block !important;
  opacity: 1 !important;
  border-radius: 50% !important;
  object-fit: cover !important;
  background: #fff !important;
}
.avatar-dropdown {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.dropdown-menu {
  position: absolute;
  top: 48px;
  right: 0;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.12);
  min-width: 140px;
  z-index: 100;
  display: flex;
  flex-direction: column;
  padding: 8px 0;
}
.dropdown-item {
  padding: 10px 18px;
  color: #222;
  text-align: left;
  background: none;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.15s;
}
.dropdown-item:hover {
  background: #f3f0fd;
  color: #7c3aed;
}
</style>
