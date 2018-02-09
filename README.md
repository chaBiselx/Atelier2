# projet_api

> Projet de web client en interaction avec une api

## Déploiement de l'API

``` bash
cd APIDeploy

# création des images docker (en tant que root si nécéssaire)
./build.sh

# construction de l'archive api.war: utiliser maven (build avec votre IDE ou manuellement)

# lancement de l'API
docker-compose up
```

## Build Setup (client)

``` bash
cd frontend

# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).


## Framework

frontend :
- dropzone : https://github.com/rowanwins/vue-dropzone
- leaflet : https://github.com/KoRiGaN/Vue2Leaflet
- buefy : https://github.com/rafaelpimpa/buefy
