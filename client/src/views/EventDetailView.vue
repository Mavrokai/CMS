<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import defaultImg from '../assets/calendaricon.jpg'
import { getRandomImageForEvent } from '@/utils/randomImages'

const route = useRoute()
const uid = computed(() => route.query.uid)
const event = ref(null)
const loading = ref(true)
const fallbackImg = ref(null)
const sorties = ref([])
const showCreate = ref(false)
const createVisibility = ref('public')
const createDate = ref('')
const createLoading = ref(false)
const createError = ref('')


const user = ref(null)
onMounted(async () => {
  loading.value = true
  try {
    const response = await axios.get(
      `http://localhost:8080/api/explore/v2.1/catalog/datasets/evenements-publics-openagenda/records?where=uid=\"${uid.value}\"`,
    )
    event.value = response.data.results[0]
    await fetchSorties()

const userData = JSON.parse(localStorage.getItem('user') || 'null')
    if (userData && userData.name) user.value = userData.name
  } catch (e) {
    event.value = null
  }
  loading.value = false
})

async function fetchSorties() {
  if (!uid.value) return
  try {
    const res = await axios.get(`http://localhost:8080/outings?eventId=${uid.value}`)
    sorties.value = res.data
  } catch {
    sorties.value = []
  }
}

async function createSortie() {
  createLoading.value = true
  createError.value = ''
  try {
    await axios.post(
      'http://localhost:8080/outings/create',
      {
        openAgendaEventId: uid.value,
        visibility: createVisibility.value,
        date: createDate.value,
      },
      {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      },
    )
    showCreate.value = false
    await fetchSorties()
  } catch (e) {
    createError.value = 'Erreur lors de la création'
  }
  createLoading.value = false
}

async function joinSortie(id) {
  try {
    await axios.post(
      `http://localhost:8080/outings/${id}/join`,
      {},
      {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      },
    )
    await fetchSorties()
  } catch {}
}

async function leaveSortie(id) {
  try {
    await axios.post(
      `http://localhost:8080/outings/${id}/leave`,
      {},
      {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      },
    )
    await fetchSorties()
  } catch {}
}
async function destroySortie(id) {
  try {
    await axios.delete(`http://localhost:8080/outings/${id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
    })
    await fetchSorties()
  } catch {}
}

const eventImg = computed(() => {
  if (fallbackImg.value) return fallbackImg.value
  if (event.value && event.value.originalimage) {
    return event.value.originalimage
  }
  if (event.value && event.value.uid) {
    return getRandomImageForEvent(event.value.uid)
  }
  return defaultImg
})

const onImgError = () => {
  if (event.value && event.value.uid) {
    fallbackImg.value = getRandomImageForEvent(event.value.uid)
  } else {
    fallbackImg.value = defaultImg
  }
}

const timings = computed(() => {
  if (event.value && event.value.timings) {
    try {
      return JSON.parse(event.value.timings)
    } catch {
      return []
    }
  }
  return []
})

const tags = computed(() => event.value?.keywords_fr || [])
</script>

<template>
  <div v-if="!loading && event" class="event-detail-page">
    <div class="event-detail-header">
      <img :src="eventImg" class="event-detail-img" alt="Image événement" @error="onImgError" />
      <div class="event-detail-title-block">
        <div class="event-detail-tags">
          <span v-for="tag in tags" :key="tag" class="event-tag">{{ tag }}</span>
        </div>
        <h1 class="event-detail-title">{{ event.title_fr }}</h1>
        <div class="event-detail-meta">
          <span class="event-detail-meta-item">
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
              <rect x="3" y="5" width="18" height="16" rx="2" stroke="#888" stroke-width="2" />
              <path d="M16 3v4M8 3v4M3 9h18" stroke="#888" stroke-width="2" />
            </svg>
            {{ event.daterange_fr }}
          </span>
          <span class="event-detail-meta-item">
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
              <path d="M12 8v4l3 2" stroke="#888" stroke-width="2" />
              <circle cx="12" cy="12" r="10" stroke="#888" stroke-width="2" />
            </svg>
            <template v-if="timings.length">
              {{
                new Date(timings[0].begin).toLocaleTimeString([], {
                  hour: '2-digit',
                  minute: '2-digit',
                })
              }}
              -
              {{
                new Date(timings[0].end).toLocaleTimeString([], {
                  hour: '2-digit',
                  minute: '2-digit',
                })
              }}
            </template>
            <template v-else>
              {{ event.daterange_fr }}
            </template>
          </span>
          <span class="event-detail-meta-item">
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
              <path
                d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7Z"
                stroke="#888"
                stroke-width="2"
              />
              <circle cx="12" cy="9" r="2.5" stroke="#888" stroke-width="2" />
            </svg>
            {{ event.location_address }}
          </span>
          <span class="event-detail-meta-item" v-if="event.conditions_fr">
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
              <path d="M16 21v-2a4 4 0 0 0-8 0v2" stroke="#22c55e" stroke-width="2" fill="none" />
              <circle cx="12" cy="7" r="4" stroke="#22c55e" stroke-width="2" fill="none" />
            </svg>
            <span class="event-detail-tarif">{{ event.conditions_fr }}</span>
          </span>
        </div>
      </div>
    </div>
    <div class="event-detail-content">
      <div class="event-detail-main">
        <div class="event-detail-actions">
          <button class="main-btn" @click="showCreate = !showCreate">Créer une sortie</button>
        </div>
        <div v-if="showCreate" class="create-sortie-form">
          <h3>Nouvelle sortie</h3>
          <label>Date/heure</label>
          <input type="datetime-local" v-model="createDate" />
          <label>Visibilité</label>
          <select v-model="createVisibility">
            <option value="public">Publique</option>
            <option value="private">Privée</option>
          </select>
          <button class="main-btn" :disabled="createLoading" @click="createSortie">Créer</button>
          <span v-if="createError" class="error">{{ createError }}</span>
        </div>
        <section class="event-detail-section about-section">
          <h2 class="about-title">À propos de cet événement</h2>
          <p class="about-desc" v-html="event.longdescription_fr || event.description_fr"></p>
        </section>
        <section class="event-detail-section">
          <h2>Programme détaillé</h2>
          <div class="event-detail-programme">
            <div v-for="(item, i) in timings" :key="i" class="event-detail-programme-item">
              <span
                class="event-detail-programme-dot"
                :style="{
                  background: ['#6d28d9', '#ef4444', '#22c55e', '#fbbf24', '#a3a3a3'][i % 5],
                }"
              ></span>
              <div class="event-detail-programme-info">
                <div class="event-detail-programme-title">{{ event.title_fr }}</div>
                <div class="event-detail-programme-desc" v-html="event.description_fr"></div>
                <div class="event-detail-programme-scene">{{ event.location_address }}</div>
              </div>
              <div class="event-detail-programme-heure">
                {{
                  new Date(item.begin).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit',
                  })
                }}
              </div>
            </div>
            <div v-if="!timings.length">
              <div class="event-detail-programme-item">
                <span class="event-detail-programme-dot" :style="{ background: '#6d28d9' }"></span>
                <div class="event-detail-programme-info">
                  <div class="event-detail-programme-title">{{ event.title_fr }}</div>
                  <div class="event-detail-programme-desc" v-html="event.description_fr"></div>
                  <div class="event-detail-programme-scene">{{ event.location_address }}</div>
                </div>
                <div class="event-detail-programme-heure">
                  {{ event.daterange_fr }}
                </div>
              </div>
            </div>
          </div>
        </section>
        <section class="event-detail-section">
          <h2 class="sorties-title">
            Sorties pour cet événement
            <span class="sorties-count"
              >{{ sorties.length }} sortie{{ sorties.length > 1 ? 's' : '' }} créée{{
                sorties.length > 1 ? 's' : ''
              }}</span
            >
          </h2>
          <div v-if="sorties.length" class="sorties-list">
            <div v-for="sortie in sorties" :key="sortie.id" class="sortie-card">
              <div class="sortie-header">
                <span class="sortie-title">{{ sortie.title || 'Sortie' }}</span>
                <span
                  class="sortie-badge"
                  :class="sortie.visibility === 'public' ? 'public' : 'private'"
                >
                  {{ sortie.visibility === 'public' ? 'Public' : 'Privé' }}
                </span>
              </div>
              <div class="sortie-org">Organisé par {{ sortie.organizer.pseudo }}</div>
              <div class="sortie-meta">
                <span class="sortie-participants">
                  <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                    <circle cx="8" cy="8" r="4" stroke="#888" stroke-width="2" />
                    <circle cx="16" cy="12" r="4" stroke="#888" stroke-width="2" />
                  </svg>
                  {{ sortie.participants.length }} participant{{
                    sortie.participants.length > 1 ? 's' : ''
                  }}
                </span>
                <span class="sortie-heure">
                  <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                    <path d="M12 8v4l3 2" stroke="#888" stroke-width="2" />
                    <circle cx="12" cy="12" r="10" stroke="#888" stroke-width="2" />
                  </svg>
                  RDV {{ sortie.date ? sortie.date.substring(11, 16) : '--:--' }}
                </span>
              </div>
              <div class="sortie-avatars">
                <img
                  v-for="(p, i) in sortie.participants.slice(0, 3)"
                  :key="i"
                  :src="
                    p.avatar && p.avatar !== ''
                      ? p.avatar.startsWith('http')
                        ? p.avatar
                        : 'http://localhost:8080' + p.avatar
                      : 'https://ui-avatars.com/api/?name=' +
                        encodeURIComponent(p.pseudo) +
                        '&background=7c3aed&color=fff'
                  "
                  class="sortie-avatar"
                  :alt="p.pseudo"
                />
                <span v-if="sortie.participants.length > 3" class="sortie-avatar-more"
                  >+{{ sortie.participants.length - 3 }}</span
                >
              </div>
              <template v-if="user && sortie.organizer.pseudo === user">
                <button class="sortie-btn join" @click="destroySortie(sortie.id)">
                  Détruire la Sortie
                </button>
              </template>
              <template v-else-if="user && sortie.participants.some((p) => p.pseudo === user)">
                <button class="sortie-btn invite" @click="leaveSortie(sortie.id)">Quitter</button>
              </template>
              <template v-else>
                <button
                  v-if="sortie.visibility === 'public'"
                  class="sortie-btn join"
                  @click="joinSortie(sortie.id)"
                >
                  Rejoindre
                </button>
                <button v-else class="sortie-btn invite" disabled>Sur invitation</button>
              </template>
            </div>
          </div>
          <div v-else class="sorties-empty">Aucune sortie pour cet événement.</div>
        </section>
      </div>
      <aside class="event-detail-aside">
        <div class="event-detail-aside-block">
          <h3 class="event-detail-aside-title">Informations pratiques</h3>
          <div class="event-detail-aside-info">
            <div class="info-row">
              <span class="icon purple">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <rect
                    x="3"
                    y="5"
                    width="18"
                    height="16"
                    rx="2"
                    stroke="currentColor"
                    stroke-width="2"
                  />
                  <path d="M16 3v4M8 3v4M3 9h18" stroke="currentColor" stroke-width="2" />
                </svg>
              </span>
              <div>
                <b>Date</b><br />
                {{ event.daterange_fr }}
              </div>
            </div>
            <div class="info-row">
              <span class="icon purple">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <path d="M12 8v4l3 2" stroke="currentColor" stroke-width="2" />
                  <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" />
                </svg>
              </span>
              <div>
                <b>Horaires</b><br />
                <template v-if="timings.length">
                  {{
                    new Date(timings[0].begin).toLocaleTimeString([], {
                      hour: '2-digit',
                      minute: '2-digit',
                    })
                  }}
                  -
                  {{
                    new Date(timings[0].end).toLocaleTimeString([], {
                      hour: '2-digit',
                      minute: '2-digit',
                    })
                  }}
                </template>
                <template v-else>
                  {{ event.daterange_fr }}
                </template>
              </div>
            </div>
            <div class="info-row">
              <span class="icon purple">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <path
                    d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7Z"
                    stroke="currentColor"
                    stroke-width="2"
                  />
                  <circle cx="12" cy="9" r="2.5" stroke="currentColor" stroke-width="2" />
                </svg>
              </span>
              <div>
                <b>Lieu</b><br />
                {{ event.location_address }}
              </div>
            </div>
            <div class="info-row">
              <span class="icon purple">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <path
                    d="M16 21v-2a4 4 0 0 0-8 0v2"
                    stroke="#22c55e"
                    stroke-width="2"
                    fill="none"
                  />
                  <circle cx="12" cy="7" r="4" stroke="#22c55e" stroke-width="2" fill="none" />
                </svg>
              </span>
              <div>
                <b>Tarif</b><br />
                <span class="tarif-green">{{ event.conditions_fr }}</span>
              </div>
            </div>
            <div class="info-row" v-if="event.canonicalurl">
              <span class="icon purple">
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
                  <path
                    d="M7 17l10-10M17 7v4a1 1 0 0 1-1 1h-4"
                    stroke="currentColor"
                    stroke-width="2"
                  />
                  <rect
                    x="3"
                    y="3"
                    width="18"
                    height="18"
                    rx="4"
                    stroke="currentColor"
                    stroke-width="2"
                  />
                </svg>
              </span>
              <div>
                <b>Site web</b><br />
                <a :href="event.canonicalurl" target="_blank" class="site-link">{{
                  event.canonicalurl
                }}</a>
              </div>
            </div>
          </div>
        </div>
        <div class="event-detail-aside-block">
          <h3 class="event-detail-aside-title">Localisation</h3>
          <div class="event-detail-aside-map">
            {{ event.location_address }}<br />
            <a
              :href="'https://maps.google.com/?q=' + encodeURIComponent(event.location_address)"
              target="_blank"
            >
              Voir sur Google Maps
            </a>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<style scoped>
.event-detail-page {
  background: #fafbfc;
  min-height: 100vh;
  padding-bottom: 40px;
}
.event-detail-header {
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 0;
  overflow: hidden;
  border-radius: 0 0 18px 18px;
}
.event-detail-img {
  width: 100%;
  aspect-ratio: 16/5;
  max-height: 200px;
  object-fit: cover;
  display: block;
  margin: 0 auto;
}
.event-detail-title-block {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  padding: 32px 48px 18px 48px;
  background: linear-gradient(0deg, rgba(0, 0, 0, 0.45) 60%, rgba(0, 0, 0, 0.01) 100%);
  color: #fff;
}
.event-detail-tags {
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
}
.event-tag {
  background: #7c3aed;
  color: #fff;
  border-radius: 8px;
  padding: 4px 14px;
  font-size: 0.98rem;
  font-weight: 600;
}
.event-detail-title {
  font-size: 2.2rem;
  font-weight: 800;
  margin: 0 0 8px 0;
  color: #fff;
  text-shadow: 0 2px 8px rgba(44, 62, 80, 0.18);
}
.event-detail-meta {
  display: flex;
  gap: 24px;
  font-size: 1.1rem;
  align-items: center;
}
.event-detail-meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #fff;
  background: rgba(0, 0, 0, 0.18);
  border-radius: 6px;
  padding: 4px 10px;
}
.event-detail-tarif {
  color: #22c55e;
  font-weight: 700;
}
.event-detail-content {
  display: flex;
  gap: 32px;
  max-width: 1300px;
  margin: -40px auto 0 auto;
  padding-top: 80px;
  align-items: flex-start;
}
.event-detail-main {
  flex: 1 1 0;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(44, 62, 80, 0.08);
  padding: 32px 32px 24px 32px;
  margin-bottom: 0;
}
.event-detail-actions {
  display: flex;
  gap: 18px;
  margin-bottom: 24px;
}
.main-btn {
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
}
.fav-btn {
  background: #fff;
  color: #e53935;
  border: 1.5px solid #e53935;
  border-radius: 8px;
  padding: 8px 18px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-right: 8px;
}
.share-btn {
  background: #fff;
  color: #7c3aed;
  border: 1.5px solid #7c3aed;
  border-radius: 8px;
  padding: 8px 18px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
}
.event-detail-section {
  margin-bottom: 32px;
}
.event-detail-section h2 {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 10px;
}
.event-detail-desc {
  color: #444;
  font-size: 1.05rem;
  white-space: pre-line;
}
.event-detail-programme {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.event-detail-programme-item {
  display: flex;
  align-items: center;
  gap: 18px;
  background: #fafbfc;
  border-radius: 10px;
  padding: 12px 18px;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.04);
}
.event-detail-programme-dot {
  width: 8px;
  height: 48px;
  border-radius: 8px;
  display: inline-block;
  margin-right: 8px;
}
.event-detail-programme-info {
  flex: 1 1 0;
}
.event-detail-programme-title {
  font-weight: 700;
  font-size: 1.08rem;
  margin-bottom: 2px;
}
.event-detail-programme-desc {
  color: #888;
  font-size: 0.98rem;
}
.event-detail-programme-scene {
  color: #7c3aed;
  font-size: 0.95rem;
}
.event-detail-programme-heure {
  font-size: 1.1rem;
  font-weight: 600;
  color: #888;
  margin-left: 12px;
}
.event-detail-sorties {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.event-detail-sortie-card {
  background: #fafbfc;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  padding: 18px 24px;
  margin-bottom: 8px;
}
.event-detail-sortie-title {
  font-size: 1.1rem;
  font-weight: 700;
}
.event-detail-sortie-org {
  color: #888;
  font-size: 0.98rem;
  margin-bottom: 2px;
}
.event-detail-sortie-meta {
  display: flex;
  gap: 18px;
  align-items: center;
  margin-bottom: 8px;
}
.event-detail-sortie-participants {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #888;
}
.event-detail-sortie-heure {
  color: #888;
  font-size: 0.98rem;
}
.event-detail-sortie-avatars {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
}
.event-detail-sortie-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.08);
}
.event-detail-sortie-btn {
  background: #7c3aed;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 7px 16px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-top: 6px;
}
.event-detail-sortie-btn[disabled] {
  background: #bbb;
  color: #fff;
}
.event-detail-sortie-create {
  background: none;
  border: 2px dashed #7c3aed;
  color: #7c3aed;
  border-radius: 8px;
  padding: 10px 0;
  font-size: 1.1rem;
  font-weight: 700;
  width: 100%;
  margin-top: 10px;
  cursor: pointer;
}
.event-detail-aside {
  width: 340px;
  flex: 0 0 340px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.event-detail-aside-block {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  padding: 18px 24px;
  margin-bottom: 0;
}
.event-detail-aside-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.info-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}
.icon {
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
}
.icon.purple svg {
  color: #7c3aed;
  fill: #7c3aed;
}
.tarif-green {
  color: #22c55e;
  font-weight: 600;
}
.site-link {
  color: #7c3aed;
  text-decoration: underline;
  word-break: break-all;
}
.event-detail-aside-map {
  background: #fafbfc;
  border-radius: 8px;
  padding: 18px;
  color: #888;
  font-size: 0.98rem;
  text-align: center;
}
.event-detail-aside-meteo {
  font-size: 2.2rem;
  text-align: center;
}
.event-detail-aside-title {
  font-weight: bold;
}
.event-detail-aside-info b {
  font-weight: bold;
}
.create-sortie-form {
  background: #fafbfc;
  border-radius: 10px;
  padding: 18px;
  margin-bottom: 18px;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.04);
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 400px;
}
.error {
  color: #e53935;
  margin-top: 6px;
}
.about-section {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(44, 62, 80, 0.06);
  padding: 36px 40px 32px 40px;
  margin-bottom: 32px;
  margin-top: 18px;
}
.about-title {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 24px;
  color: #222;
}
.about-desc {
  color: #374151;
  font-size: 1.15rem;
  line-height: 1.7;
  white-space: pre-line;
}
.sorties-title {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 18px;
  color: #222;
  display: flex;
  align-items: center;
  gap: 18px;
}
.sorties-count {
  font-size: 1.1rem;
  color: #888;
  font-weight: 500;
  margin-left: auto;
}
.sorties-list {
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.sortie-card {
  background: #fff;
  border-radius: 12px;
  border: 1.5px solid #ececec;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.04);
  padding: 24px 28px 18px 28px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
}
.sortie-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2px;
}
.sortie-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #222;
}
.sortie-badge {
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  padding: 3px 16px;
  margin-left: 8px;
  background: #f3f4f6;
  color: #888;
}
.sortie-badge.public {
  background: #d1fae5;
  color: #059669;
}
.sortie-badge.private {
  background: #dbeafe;
  color: #2563eb;
}
.sortie-org {
  color: #888;
  font-size: 1.05rem;
  margin-bottom: 2px;
}
.sortie-meta {
  display: flex;
  gap: 18px;
  align-items: center;
  color: #444;
  font-size: 1.05rem;
  margin-bottom: 6px;
}
.sortie-participants,
.sortie-heure {
  display: flex;
  align-items: center;
  gap: 6px;
}
.sortie-avatars {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
}
.sortie-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 1px 4px rgba(44, 62, 80, 0.08);
}
.sortie-avatar-more {
  background: #ececec;
  color: #888;
  border-radius: 50%;
  padding: 6px 10px;
  font-size: 1rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 2px;
}
.sortie-btn {
  align-self: flex-end;
  margin-top: 6px;
  font-size: 1.08rem;
  font-weight: 600;
  border-radius: 8px;
  padding: 8px 28px;
  border: none;
  cursor: pointer;
  transition: background 0.15s;
}
.sortie-btn.join {
  background: linear-gradient(90deg, #7c3aed 0%, #5b21b6 100%);
  color: #fff;
}
.sortie-btn.invite {
  background: #ececec;
  color: #888;
  cursor: not-allowed;
}
.sorties-empty {
  color: #888;
  font-size: 1.1rem;
  margin-top: 18px;
  text-align: center;
}
@media (max-width: 1024px) {
  .event-detail-content {
    flex-direction: column;
    gap: 0;
  }
  .event-detail-aside {
    width: 100%;
    flex: 1 1 0;
    flex-direction: row;
    gap: 18px;
    margin-top: 24px;
  }
  .event-detail-aside-block {
    flex: 1 1 0;
  }
}
@media (max-width: 768px) {
  .event-detail-header {
    margin-bottom: 0;
  }
  .event-detail-title-block {
    padding: 18px 12px 12px 12px;
  }
  .event-detail-content {
    flex-direction: column;
    gap: 0;
    padding: 0 4px;
  }
  .event-detail-main {
    padding: 18px 8px 18px 8px;
  }
  .event-detail-aside {
    flex-direction: column;
    width: 100%;
    gap: 12px;
  }
  .about-section {
    padding: 18px 8px 18px 8px;
  }
  .about-title {
    font-size: 1.3rem;
  }
  .about-desc {
    font-size: 1rem;
  }
  .sorties-title {
    font-size: 1.2rem;
  }
  .sortie-card {
    padding: 12px 6px 12px 6px;
  }
}
</style>
