<script setup>
import { computed } from 'vue'
import defaultImg from '../assets/calendaricon.jpg'

const props = defineProps({
  itemevent: Object,
})

const setEventImg = computed(() => {
  if (props.itemevent.originalimage) {
    return props.itemevent.originalimage
  } else {
    return defaultImg
  }
})
</script>

<template>
  <div class="event_card">
    <img :src="setEventImg" alt="Image de l'évenement" class="event_img" />
    <div class="event_info">
      <div class="event_date">
        <b>{{ itemevent.daterange_fr }}</b>
      </div>
      <h3>{{ itemevent.title_fr }}</h3>
      <p>{{ itemevent.description }}</p>
      <p>Adresse : {{ itemevent.location_address }}</p>
      <button>Voir détails</button>
    </div>
  </div>
</template>

<style scoped>
h3 {
  font-weight: bold;
}

button {
  background-color: #5e17eb;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 5px;
  text-align: center;
  color: white;
}

.event_card {
  background-color: #00000000;
  border: 1px solid whitesmoke;
  border-radius: 10px;
  box-shadow: 1px 1px whitesmoke;
  margin: 1%;
  width: 500px;
}

.event_img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 10px 10px 0 0;
}

.event_date {
  background-color: #e5e7eb;
  width: fit-content;
  max-width: 25%;
  padding: 2px 8px;
  border-radius: 5px;
  text-align: center;
  margin-bottom: 10px;
}

.event_info {
  padding: 10px;
}
</style>
