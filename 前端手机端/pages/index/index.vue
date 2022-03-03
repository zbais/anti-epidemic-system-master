<template>
	<view>

	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		onLoad() {
			const token = uni.getStorageSync('token')
			if (token) {
				uni.switchTab({
					url: '/pages/tabbar/member'
				})
			} else {
				uni.redirectTo({
					url: '/pages/login/login'
				})
			}
		}
	}
</script>

<style>

</style>
