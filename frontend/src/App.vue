<template>
  <div id="app">
    <NavBar />
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'

export default {
  name: 'App',
  components: { NavBar }
}
</script>

<style>
#app {
  min-height: 100vh;
  background-color: #f5f5f5;
}
.main-content {
  padding-top: 60px;
  max-width: 1200px;
  margin: 0 auto;
  padding-left: 20px;
  padding-right: 20px;
}
</style>
