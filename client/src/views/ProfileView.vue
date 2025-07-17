<template>
  <div class="profile-bg">
    <div class="profile-cover"></div>
    <div class="profile-card">
      <div class="profile-avatar-block">
        <img :src="user.avatar" class="profile-avatar" alt="Avatar" />
        <span class="profile-badge"> </span>
      </div>
      <div class="profile-info">
        <h2>{{ user.name }}</h2>
        <div class="profile-pseudo">{{ user.pseudo }}</div>
        <div class="profile-email">{{ user.email }}</div>
      </div>
      <button v-if="!editMode" class="edit-btn" @click="startEdit">
        <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
          <path
            stroke="#fff"
            stroke-width="2"
            d="M16.475 5.408a2.3 2.3 0 0 1 3.253 3.253l-9.9 9.9-4.243 1.06 1.06-4.243 9.83-9.97Z"
          />
        </svg>
        Modifier le profil
      </button>
      <form v-else @submit.prevent="saveProfile" class="edit-profile-form">
        <div class="edit-avatar-upload">
          <label class="avatar-preview">
            <img
              :src="editAvatarPreview || user.avatar"
              alt="Aperçu avatar"
              class="profile-avatar"
            />
            <input
              type="file"
              accept="image/*"
              @change="handleAvatarChange"
              style="display: none"
            />
            <span class="avatar-edit">Changer</span>
          </label>
        </div>
        <label>Pseudo</label>
        <input v-model="editPseudo" type="text" required />
        <label>Email</label>
        <input v-model="editEmail" type="email" required />
        <label>Bio</label>
        <textarea v-model="editBio" rows="3" />
        <button type="submit" class="main-btn" :disabled="loading">Enregistrer</button>
        <button type="button" class="main-btn cancel" @click="editMode = false">Annuler</button>
        <div v-if="error" class="error">{{ error }}</div>
      </form>
    </div>
    <div class="profile-stats">
      <div class="stat stat-purple">
        <span>{{ user.stats.sorties }}</span>
        <div>Sorties organisées</div>
      </div>
      <div class="stat stat-red">
        <span>{{ user.stats.participations }}</span>
        <div>Participations</div>
      </div>
    </div>
    <div class="profile-section">
      <div class="section-header">
        <span>À propos</span>
      </div>
      <div class="section-content">{{ user.about }}</div>
    </div>

    <div class="profile-tabs">
      <button :class="{ active: tab === 'mes' }" @click="tab = 'mes'">Mes sorties</button>
      <button :class="{ active: tab === 'org' }" @click="tab = 'org'">Organisées</button>
      <button :class="{ active: tab === 'part' }" @click="tab = 'part'">Participées</button>
      <button :class="{ active: tab === 'fav' }" @click="tab = 'fav'">Favoris</button>
    </div>
    <div v-if="tab === 'mes'" class="profile-events">
      <div v-for="s in sorties" :key="s.id" class="event-card">
        <img :src="s.image" class="event-img" />
        <div class="event-info">
          <div class="event-title">{{ s.titre }}</div>
          <div class="event-date">
            <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
              <rect x="3" y="5" width="18" height="16" rx="2" stroke="#888" stroke-width="2" />
              <path d="M16 3v4M8 3v4M3 9h18" stroke="#888" stroke-width="2" />
              <text x="12" y="17" text-anchor="middle" font-size="8" fill="#888">15</text>
            </svg>
            {{ s.date }}
          </div>
          <div class="event-participants">
            <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
              <circle cx="8" cy="8" r="4" stroke="#888" stroke-width="2" />
              <circle cx="16" cy="12" r="4" stroke="#888" stroke-width="2" />
            </svg>
            {{ s.participants }} participants
          </div>
        </div>
        <span class="event-status">{{ s.statut }}</span>
        <div class="event-actions">
          <button class="edit-btn event-edit">
            <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
              <path
                stroke="#fff"
                stroke-width="2"
                d="M16.475 5.408a2.3 2.3 0 0 1 3.253 3.253l-9.9 9.9-4.243 1.06 1.06-4.243 9.83-9.97Z"
              />
            </svg>
            Modifier
          </button>
          <button class="delete-btn">
            <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
              <path stroke="#fff" stroke-width="2" d="M6 6l12 12M6 18L18 6" />
            </svg>
            Supprimer
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const user = ref({
  name: '',
  pseudo: '',
  email: '',
  avatar: '',
  about: '',
  stats: {
    sorties: 0,
    participations: 0,
  },
})

const sorties = ref([])
const tab = ref('mes')

const editMode = ref(false)
const editPseudo = ref('')
const editEmail = ref('')
const editBio = ref('')
const editAvatarFile = ref(null)
const editAvatarPreview = ref('')
const loading = ref(false)
const error = ref('')

const getAvatarUrl = (avatarPath) => {
  if (!avatarPath) return null
  if (avatarPath.startsWith('https://')) return avatarPath
  if (avatarPath.startsWith('/uploads/avatars/')) {
    const filename = avatarPath.split('/').pop()
    return `http://localhost:8080/user/avatar/${filename}`
  }
  return avatarPath
}

function startEdit() {
  editMode.value = true
  editPseudo.value = user.value.pseudo
  editEmail.value = user.value.email
  editBio.value = user.value.about
  editAvatarPreview.value = user.value.avatar
  editAvatarFile.value = null
}

function handleAvatarChange(e) {
  const file = e.target.files[0]
  editAvatarFile.value = file
  if (file) {
    const reader = new FileReader()
    reader.onload = (ev) => {
      editAvatarPreview.value = ev.target.result
    }
    reader.readAsDataURL(file)
  } else {
    editAvatarPreview.value = user.value.avatar
  }
}

async function saveProfile() {
  loading.value = true
  error.value = ''
  const localUser = JSON.parse(localStorage.getItem('user') || 'null')
  const token = localStorage.getItem('token')
  if (!localUser || !localUser.name || !token) {
    error.value = 'Non authentifié'
    loading.value = false
    return
  }
  try {
    const formData = new FormData()
    formData.append('pseudo', localUser.name)
    if (editPseudo.value && editPseudo.value !== user.value.pseudo) {
      formData.append('newPseudo', editPseudo.value)
    }
    if (editEmail.value && editEmail.value !== user.value.email) {
      formData.append('email', editEmail.value)
    }
    if (editBio.value !== user.value.about) {
      formData.append('bio', editBio.value)
    }
    if (editAvatarFile.value) {
      formData.append('avatar', editAvatarFile.value)
    }
    const res = await fetch('http://localhost:8080/user/me', {
      method: 'PUT',
      headers: { Authorization: `Bearer ${token}` },
      body: formData,
    })
    const data = await res.json()
    if (res.ok) {
      editMode.value = false
      await reloadProfile()
      window.dispatchEvent(new Event('user-updated'))
    } else {
      error.value = data.error || 'Erreur lors de la mise à jour'
    }
  } catch (e) {
    error.value = 'Erreur réseau'
  }
  loading.value = false
}

async function reloadProfile() {
  const localUser = JSON.parse(localStorage.getItem('user') || 'null')
  const token = localStorage.getItem('token')
  if (localUser && localUser.name && token) {
    try {
      const res = await fetch(
        `http://localhost:8080/user/me?pseudo=${encodeURIComponent(editPseudo.value || localUser.name)}`,
        { headers: { Authorization: `Bearer ${token}` } },
      )
      if (res.ok) {
        const data = await res.json()
        user.value = {
          name: data.nom || data.pseudo || '',
          pseudo: data.pseudo || '',
          email: data.email || '',
          avatar: getAvatarUrl(data.avatar),
          about: data.bio || '',
          stats: {
            sorties: data.sorties || 0,
            participations: data.participations || 0,
          },
        }
      }
    } catch {}
  }
}

onMounted(reloadProfile)
</script>

<style scoped>
.profile-bg {
  background: #fafbfc;
  min-height: 100vh;
  padding-bottom: 40px;
  padding-top: 0;
  margin-top: 0;
}
.profile-cover {
  width: 100%;
  height: 180px;
  background: linear-gradient(90deg, #7c3aed 60%, #a78bfa 100%);
  border-radius: 0 0 18px 18px;
  position: relative;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
  margin-top: 0;
}
.cover-img {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.18;
  z-index: 0;
}
.cover-btn {
  position: absolute;
  right: 32px;
  bottom: 18px;
  background: #fff;
  color: #7c3aed;
  border: none;
  border-radius: 8px;
  padding: 7px 18px;
  font-size: 1rem;
  font-weight: 500;
  box-shadow: 0 1px 4px rgba(124, 58, 237, 0.08);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 2;
}
.profile-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(44, 62, 80, 0.08);
  padding: 32px 32px 24px 32px;
  max-width: 900px;
  width: 100%;
  margin: -60px auto 0 auto;
  display: flex;
  align-items: center;
  gap: 32px;
  position: relative;
}
.profile-avatar-block {
  position: relative;
  min-width: 96px;
}
.profile-avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #fff;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.12);
}
.profile-badge {
  position: absolute;
  bottom: 6px;
  right: 6px;
  width: 28px;
  height: 28px;
  background: #7c3aed;
  border-radius: 50%;
  border: 2px solid #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.1rem;
  box-shadow: 0 1px 4px rgba(124, 58, 237, 0.12);
}
.profile-info {
  flex: 1;
}
.profile-info h2 {
  margin: 0 0 2px 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: #222;
}
.profile-pseudo {
  color: #888;
  font-size: 1rem;
  margin-bottom: 2px;
}
.profile-email {
  color: #bbb;
  font-size: 0.98rem;
}
.edit-btn {
  background: #7c3aed;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 8px 18px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
  box-shadow: 0 1px 4px rgba(124, 58, 237, 0.08);
  display: flex;
  align-items: center;
  gap: 8px;
}
.edit-btn:hover {
  background: #5b21b6;
}
.profile-stats {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin: 32px auto 0 auto;
  max-width: 900px;
}
.stat {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  padding: 18px 32px;
  text-align: center;
  min-width: 140px;
  font-size: 1.1rem;
}
.stat span {
  font-size: 1.4rem;
  font-weight: 700;
  display: block;
}
.stat-purple span {
  color: #7c3aed;
}
.stat-red span {
  color: #e53935;
}
.stat-green span {
  color: #43a047;
}
.stat-yellow span {
  color: #fbc02d;
}
.profile-section {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  padding: 22px 32px 18px 32px;
  max-width: 900px;
  margin: 28px auto 0 auto;
}
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.1rem;
  font-weight: 600;
  color: #222;
  margin-bottom: 8px;
}
.edit-icon {
  background: none;
  border: none;
  color: #7c3aed;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 2px 4px;
  display: flex;
  align-items: center;
}
.section-content {
  color: #444;
  font-size: 1.05rem;
  white-space: pre-line;
}
.social {
  margin-right: 18px;
  color: #444;
  font-size: 1.05rem;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
.profile-tabs {
  display: flex;
  justify-content: flex-start;
  gap: 48px;
  margin: 32px auto 0 auto;
  max-width: 900px;
  border-bottom: 2px solid #ececec;
}
.profile-tabs button {
  background: none;
  border: none;
  color: #888;
  font-size: 1.1rem;
  font-weight: 600;
  padding: 8px 0 12px 0;
  border-bottom: 2.5px solid transparent;
  cursor: pointer;
  transition:
    color 0.15s,
    border-color 0.15s;
  margin-bottom: -2px;
}
.profile-tabs button.active {
  color: #7c3aed;
  border-bottom: 2.5px solid #7c3aed;
}
.profile-events {
  max-width: 900px;
  margin: 24px auto 0 auto;
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.event-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  display: flex;
  align-items: center;
  padding: 18px 24px;
  gap: 24px;
  position: relative;
}
.event-img {
  width: 80px;
  height: 80px;
  border-radius: 12px;
  object-fit: cover;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.08);
}
.event-info {
  flex: 1;
}
.event-title {
  font-size: 1.15rem;
  font-weight: 600;
  color: #222;
}
.event-date,
.event-participants {
  color: #888;
  font-size: 0.98rem;
  margin-top: 2px;
}
.event-status {
  background: #e0f7fa;
  color: #43a047;
  font-size: 0.95rem;
  font-weight: 600;
  border-radius: 8px;
  padding: 4px 12px;
  margin-left: 18px;
}
.event-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-left: 18px;
}
.event-edit {
  background: #7c3aed;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 0.98rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s;
}
.event-edit:hover {
  background: #5b21b6;
}
.delete-btn {
  background: #e53935;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-size: 0.98rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s;
}
.delete-btn:hover {
  background: #b71c1c;
}
.icon {
  display: inline-block;
  font-style: normal;
  font-size: 1.1em;
  vertical-align: middle;
}
.edit-profile-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 12px;
}
.edit-avatar-upload {
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
  width: 96px;
  height: 96px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #7c3aed;
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
.main-btn.cancel {
  background: #bbb;
  color: #fff;
}
.error {
  margin-top: 10px;
  color: #e53935;
}
@media (max-width: 480px) {
  .profile-card,
  .profile-stats,
  .profile-section,
  .profile-tabs,
  .profile-events {
    flex-direction: column !important;
    gap: 12px !important;
    align-items: stretch !important;
    width: 100% !important;
    max-width: 100vw !important;
  }
  .profile-avatar {
    width: 64px;
    height: 64px;
  }
  .edit-btn,
  .main-btn,
  .cancel {
    width: 100%;
    margin-bottom: 8px;
  }
  .edit-profile-form input,
  .edit-profile-form textarea {
    width: 100%;
    box-sizing: border-box;
  }
}
@media (min-width: 481px) and (max-width: 768px) {
  .profile-card,
  .profile-stats,
  .profile-section,
  .profile-tabs,
  .profile-events {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
    width: 100%;
    max-width: 95vw;
  }
  .profile-avatar {
    width: 80px;
    height: 80px;
  }
}
</style>
