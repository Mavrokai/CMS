<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
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
        <RouterLink to="/register" class="signup-btn">S'inscrire</RouterLink>
      </div>
    </nav>
    <main>
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
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
  padding-top: 32px;
}
</style>
