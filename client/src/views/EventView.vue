<template>
  <div id="main">
    <h1>Liste des événements</h1>
    <div id="filters_container">
      <h3>Filtres</h3>
      <div id="filters">
        <div class="filter">
          <label for="city">Ville</label>
          <input
            type="text"
            list="cities"
            name="city"
            id="city"
            v-model="selectedCity"
            placeholder="Ville"
          />
          <datalist id="cities">
            <option
              v-for="city in cityList"
              :key="city.record.fields.city"
              :value="city.record.fields.city"
            >
              {{ city.record.fields.city }}
            </option>
          </datalist>
        </div>
        <div class="filter">
          <label for="keywords">Mots clés</label>
          <input
            type="text"
            name="keywords"
            id="keywords"
            v-model="keywords"
            placeholder="Mots clés"
          />
        </div>
        <div class="filter">
          <label for="date">Date</label>
          <input type="date" name="date" id="date" v-model="date" />
        </div>
        <div class="filter">
          <label for="limit">Événement par page</label>
          <select name="limit" id="limit" v-model="limit">
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
          </select>
        </div>
      </div>
    </div>
    <div v-if="loading">Récupération des événements</div>
    <div v-else>
      <div v-if="events && events.length > 0" id="events">
        <div v-for="event in events" :key="event.record.fields.uid">
          <EventItem :itemevent="event.record.fields" />
        </div>
      </div>
      <div v-else-if="error">
        <p>Une erreur est survenue lors du chargement des événements : {{ error }}</p>
      </div>
      <div v-else><p>Aucun événement trouvé.</p></div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getAllCities, getAllEvents } from '@/utils/api'
import EventItem from '../components/EventItem.vue'

const route = useRoute()

const loading = ref(false)
const events = ref(null)
const error = ref(null)
const cityList = ref([])
const selectedCity = ref('')
const limit = ref(10)
const keywords = ref('')
const date = ref('')
const regexCities = /^[a-zA-Z\s-\'']+/

watch([selectedCity, keywords, date, limit], fetchData, { immediate: true })

async function fetchData() {
  error.value = null
  events.value = null
  loading.value = true

  try {
    events.value = await getAllEvents(selectedCity.value, keywords.value, date.value, limit.value)
    const rawCities = await getAllCities()

    cityList.value = rawCities.filter((city) => regexCities.test(city.record.fields.city))
    console.log(events.value)
  } catch (err) {
    error.value = err.toString()
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
h1 {
  font-weight: bold;
}

.filter {
  display: flex;
  flex-direction: column;
}

#main {
  padding: 0% 5%;
}

#filters_container {
  background-color: white;
  width: 100%;
  height: 160px;
  border-radius: 12px;
  box-shadow: 1px 1px whitesmoke;
  margin: 2% 0%;
  padding: 1%;
}

#filters {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

#events {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 3% 5%;
  margin: auto;
}
</style>
