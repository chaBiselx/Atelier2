import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'

import Home from '@/components/Home'
import geoloc from '@/components/content_test/geolocalisation'
import result from '@/components/content_test/result'
import backOffice from '@/components/back-office/FormAjoutPartie'
import inscription from '@/components/login/FormInscription'
import connexion from '@/components/login/FormConnexion'

Vue.use(Router)

export const router = new Router({
  routes: [
    {
      path: '/geolocation',
      name: 'geoloc',
      component: geoloc,
    },

  	{
    	path: '/',
    	name: 'home',
    	component: Home
    },
    {
    	path: '/result',
    	name: 'result',
    	component: result
    },
    {
      path: '/admin',
      name: 'admin',
      component: backOffice
    },
    {
      path: '/inscription',
      name: 'inscrption',
      component: inscription
    },
    {
      path: '/connexion',
      name: 'connection',
      component: connexion
    },
    {
      path: '*',
      redirect: '/'
    }

  ]
})
