import './assets/main.css'

import { createPinia } from 'pinia'
import { createApp } from 'vue'

import GoogleLogin from 'vue3-google-login'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(GoogleLogin, {
  clientId: import.meta.env.VITE_GOOGLE_CLIENT_ID,
})

app.mount('#app')
