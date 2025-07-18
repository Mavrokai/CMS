import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
    },
    {
      path: '/',
      name: 'home',
      component: () => import('../views/EventView.vue'),
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfileView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/details',
      name: 'event-detail',
      component: () => import('../views/EventDetailView.vue'),
    },
  ],
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const isLoggedIn = !!localStorage.getItem('token')

  if (authRequired && !isLoggedIn) {
    return next('/login')
  }
  next()
})

export default router
