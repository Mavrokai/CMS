<script setup>
import { ref, watch, computed } from 'vue'
import { getRandomImageForEvent } from '@/utils/randomImages'
import defaultImg from '../assets/calendaricon.jpg'

const props = defineProps({
  itemevent: Object,
})

const initialImg = computed(() => {
  if (props.itemevent.originalimage) {
    return props.itemevent.originalimage
  } else {
    return getRandomImageForEvent(props.itemevent.uid) || defaultImg
  }
})

const eventImg = ref(initialImg.value)

watch(initialImg, (val) => {
  eventImg.value = val
})

function handleImgError() {
  eventImg.value = getRandomImageForEvent(props.itemevent.uid) || defaultImg
}
</script>

<template>
  <div class="event_card">
    <img :src="eventImg" alt="Image de l'évenement" class="event_img" @error="handleImgError" />
    <div class="event_info">
      <div class="event_date">
        <b>{{ itemevent.daterange_fr }}</b>
      </div>
      <h3>{{ itemevent.title_fr }}</h3>
      <p>{{ itemevent.description }}</p>
      <p>Adresse : {{ itemevent.location_address }}</p>
      <button>
        <router-link :to="`/details?uid=${itemevent.uid}`" class="details">détails</router-link>
      </button>
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
  height: 100%;
  min-width: 250px;
  min-height: 300px;
  max-width: 600px;
  max-height: 500px;
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

.details {
  color: white;
}

.event_info {
  padding: 10px;
}
</style>
