<template>
	<view clss="container">>
		<image class="bg-set" src="../../static/bj1.png"></image>
		<view class="content"></view>
		<uni-list>
			<uni-list-item :title="username" show-arrow="false" thumb="/static/font/user.png"></uni-list-item>
			<uni-list-item title="修改密码" thumb="/static/font/pwd.png" @click="modifyPwd()"></uni-list-item>
			<!-- <uni-list-item title="关于" thumb="/static/font/about.png" @click="aboutUs()"></uni-list-item> -->
			<uni-list-item v-if="username === 'admin'" title="新建账户" thumb="/static/font/exit.png" @click="newUser()"></uni-list-item>
			<uni-list-item title="切换账号/退出" thumb="/static/font/exit.png" @click="exit()"></uni-list-item>
		</uni-list>
		<view class="list">
			<view class="tips">可以让外来人员扫描此码，自助填写信息</view>
			<view>
				<view class="qrimg">
				    <view class="qrimg-i">
				    	<tki-qrcode v-if="ifShow" cid="qrcode2" ref="qrcode2" :val="val" :size="size" :onval="onval" :loadMake="loadMake" :usingComponents="true" @result="qrR" />
				    </view>
				</view>
			</view>
		</view>
	</view>

</template>

<script>
	import tkiQrcode from "@/components/tki-qrcode/tki-qrcode.vue"
	import uniList from "@/components/uni-list/uni-list.vue"
	import uniListItem from "@/components/uni-list-item/uni-list-item.vue"
	import uniIcon from "@/components/uni-icons/uni-icons.vue"
	export default {
		components: {
			tkiQrcode,
			uniList,
			uniListItem,
			uniIcon			
		},
		data() {			
			return {
				ifShow: true,
				username: uni.getStorageSync('username'),
				val: '',
				size: 350, // 二维码大小
				onval: true, // val值变化时自动重新生成二维码
				loadMake: true // 组件加载完成后自动生成二维码
			}
		},
		onLoad() {
			// #ifdef H5
			this.val = location.href.replace('/pages/tabbar/my', `/pages/tabbar/add?id=${this.username}`)
			// #endif
			// #ifndef H5
			//微信的话这里要写死为小程序的二维码地址或者是H5平台的二维码地址 
			// this.val = 'https://wlry.m3w.cn/#/pages/tabbar/add?id=' + uni.getStorageSync('username')
			// #endif
		},
		methods: {
			creatQrcode() {
				this.$refs.qrcode._makeCode()
			},
			qrR(res) {
				this.src = res
			},
			//修改密码
			modifyPwd() {
				uni.navigateTo({
					url: '/pages/change-pwd/change-pwd'
				})
			},
			//关于我们
			aboutUs() {
				uni.navigateTo({
					url: ''
				})
			},
			//退出
			exit() {
				uni.removeStorageSync('token')
				uni.removeStorageSync('username')
				uni.reLaunch({
					url: '/pages/login/login'
				})
			},
			newUser() {
				uni.navigateTo({
					url: '/pages/new-user/new-user'
				})
			}
		}
	}
</script>

<style>
	.list {
		padding: 20px 0px;
		text-align: center;
		border-top: solid 1px #DDDDDD;
	}

	.tips {
		line-height: 80px;
		font-size: 14px;
		color: #999999;
	}
	.bg-set{
	    position: fixed;
	    width: 100%;
	    height: 100%;
	    top: 0;
	    left: 0;
	    z-index: 1;
			pointer-events: none; 
			opacity: 0.4;
	}
</style>
