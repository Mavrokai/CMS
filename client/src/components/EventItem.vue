<script setup>
defineProps({
  event: String,
})

console.log(event)
</script>

<template>
  <div>
    <h3>{{ event.name }}</h3>
    <img :src="event.imgurl" alt="Image de l'évenement" />
    <p>{{ event.description }}</p>
    <ul>
      <li>Adresse : {{ event.adresse }}</li>
      <li>Ville : {{ event.city }} {{ event.cityCode }}</li>
    </ul>
  </div>
</template>

<style scoped>
    ul{
        list-style: none;
    }
</style>