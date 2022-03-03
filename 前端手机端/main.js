import Vue from 'vue'
import App from './App'

Vue.config.productionTip = false

App.mpType = 'app'

Vue.prototype.$host = 'https://unidemo.dcloud.net.cn/';

Vue.prototype.$cloud = uniCloud

const app = new Vue({
    ...App
})
app.$mount()
