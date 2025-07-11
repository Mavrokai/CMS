<script>
import googleOneTap from 'google-one-tap';

export default {
  data() {
    return {
      user: null,
    };
  },
  mounted() {
    const options = {
      client_id: import.meta.env.VITE_GOOGLE_CLIENT_ID,
      auto_select: false,
      cancel_on_tap_outside: false,
      context: 'signin',
    };

    googleOneTap(options, (response) => {
      if (response && response.credential) {
        // Decode the JWT credential
        const base64Url = response.credential.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
          atob(base64)
            .split('')
            .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
            .join('')
        );

        const profileInfo = JSON.parse(jsonPayload);

        this.user = {
          name: profileInfo.name,
          email: profileInfo.email,
          picture: profileInfo.picture,
        };

        console.log('User Profile Information:', this.user);
      } else {
        console.error('No credential received or invalid response.');
      }
    });
  },
};
</script>

<template>
  <div v-if="user" class="user-profile">
    <img :src="user.picture" alt="Photo de profil" class="profile-picture" />
    <h3>{{ user.name }}</h3>
    <p>{{ user.email }}</p>
  </div>
  <div v-else>
    <p>Chargement du profile</p>
  </div>
</template>

<style>
.user-profile {
  text-align: center;
  margin-top: 20px;
}
.profile-picture {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 10px;
}
</style>