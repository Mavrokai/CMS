<template>
  <div id="main">
    <h1>Liste des événements</h1>
    <!-- <div id="filters">
      <h3>Filtres</h3>
      <label for="city">Ville</label>
      <select name="city" id="city">
        <option value="" selected>Toutes les villes</option>
        <option value="Lille">Lille</option>
      </select>
    </div> -->
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
import { getAllEvents } from '@/utils/api'
import EventItem from '../components/EventItem.vue'

const route = useRoute()

const loading = ref(false)
const events = ref(null)
const error = ref(null)

watch(() => route.params.query, fetchData, { immediate: true })
// Pour récupérer la query

async function fetchData() {
  error.value = null
  events.value = null
  loading.value = true

  try {
    events.value = await getAllEvents()
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

#main {
  padding: 0% 5%;
}

#filters {
  background-color: white;
  width: 100%;
  height: 160px;
  border-radius: 12px;
  box-shadow: 1px 1px whitesmoke;
  margin: 2% 0%;
  padding: 1%;
}

#events {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 3% 5%;
  margin: auto;
}
</style>
