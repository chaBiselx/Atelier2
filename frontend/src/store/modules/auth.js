import api from '../../services/api'
import ls from '@/services/ls'

const initialState = {
	connectedAdmin: false,
	user: {}
}

export default {
	namespaced: true,//permet d'y accéder de façon nommée
	state: {
		connectedAdmin:false,
		user:{}
	},
	getters: {
		isConnected(state){
			return state.connectedAdmin
		},
		getConnectedUser(state){
			return state.user.nom
		}
	},
	mutations: {
		setConnectedUser(state,u){
			state.user=u
			state.connectedAdmin=true
		},
		initState(state){
			Object.assign(state, initialState)
		}
	},
	actions: {
		signup({commit},credentials){
			api.post('/user',credentials).then(response=>{
			}).catch(error => {
				console.log(error)
				})
		},
		signin ({commit}, user){
			return api.post('/auth', user).then((response) => {
				ls.set('token',response.headers.authorization)
				commit('setConnectedUser', response.data)
			}).catch((err) => {
				return Promise.reject('Login failed')
			})
		},

		logout({commit}){
			commit('initState')
			ls.remove('token')
		}
	}
}
