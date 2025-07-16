<template>
  <h3>Liste des événements</h3>
  <div v-if="loading">Récupération des événements</div>
  <div v-if="events">
    <div v-for="event in events" :key="event.uid">
      <EventItem event/>
    </div>
  </div>
  <div v-else><p>Aucun événements trouvés.</p></div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getAllEvents } from '@/utils/api'
import EventItem from '@/components/EventItem.vue'

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

<style></style>
