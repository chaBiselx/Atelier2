import Vue from 'vue'
import api from '@/services/api'
import ls from '@/services/ls'
import store from '@/store'

export default {
	install: (Vue, options = {}) => {
		// Add a request interceptor
		api.interceptors.request.use( function (config) {
			if(ls.get('token')){
				if(!config.headers) {
					config.headers = {}
				}
				config.headers.authorization = ls.get('token');
			}
			return config
		}, function (error) {
			return Promise.reject(error)
		})

		// Add a response interceptor
		api.interceptors.response.use(function (response) {
			return response;
		}, function (error) {
			if(error.response && error.response.status == 401){
				store.dispatch('auth/logout', ! error.response.data.error.indexOf("wrong token"))
				options.router.push({name: 'signup'})
			}
			return Promise.reject(error)
		})
	}
}
