<template>
  <div id="main">
    <h1>Liste des événements</h1>
    <div></div>
    <div v-if="loading">Récupération des événements</div>
    <div v-else>
      <div v-if="events" id="events">
        <div v-for="event in events" :key="event.record.fields.uid">
          <EventItem :itemevent="event.record.fields" />
        </div>
      </div>
      <div v-else><p>Aucun événements trouvés.</p></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getAllEvents } from '@/utils/api'
import EventItem from '../components/EventItem.vue'

const route = useRoute()

const loading = ref(false)
const events = ref(null)
const error = ref(null)

// watch(() => route.params.query, fetchData, { immediate: true })
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

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
h1 {
  font-weight: bold;
}

#main {
  padding: 0% 5%;
}

#events {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
</style>
