<template>
	<view class="container">
		<form @submit="formSubmit" class="login-form">
			<view class="section">
				<input name="username" placeholder="用户名" />
			</view>
			<view class="section">
				<input name="newpwd" password="true" placeholder="新密码" />
			</view>
			<view class="section">
				<input name="confirmpwd" password="true" placeholder="确认新密码" />
			</view>
			<button class="login-btn" formType="submit" type="primary">确认</button>
		</form>
	</view>
</template>

<script>
	let _self;
	export default {
		data() {
			return {};
		},
		methods: {
			formSubmit: function(e) {
				let formData = e.detail.value;
				let flag = this.validation(formData);
				if (!flag) {
					return false;
				}
				uni.showLoading({
					title: '处理中...'
				})
				var data = {
					username: formData.username,
					password: formData.newpwd,
					token: uni.getStorageSync('token')
				}

				this.$cloud.callFunction({
					name: 'create-user',
					data
				}).then((res) => {
					uni.hideLoading()
					if (res.result.code === 0) {
						uni.showModal({
							content: '新建账户成功',
							showCancel: false,
							success() {
								uni.switchTab({
									url: '/pages/tabbar/member'
								})
							}
						})
					} else {
						return Promise.reject(new Error(res.result.msg))
					}
				}).catch((err) => {
					uni.hideLoading()
					uni.showModal({
						content: err.message || '新建账户失败',
						showCancel: false
					})
				})
			},
			validation: function(_formData) {
				if (_formData.username.trim() == "") {
					uni.showToast({
						title: '请输入用户名！',
						icon: 'none',
						duration: 2000
					});
					return false;
				}
				if (_formData.newpwd == "") {
					uni.showToast({
						title: '请输入新密码！',
						icon: 'none',
						duration: 2000
					});
					return false;
				} else {
					if (_formData.newpwd.length < 6) {
						uni.showToast({
							title: '密码不能少于6位！',
							icon: 'none',
							duration: 2000
						});
						return false;
					}
				}
				if (_formData.confirmpwd == "") {
					uni.showToast({
						title: '请输入确认密码！',
						icon: 'none',
						duration: 2000
					});
					return false;
				}
				if (_formData.newpwd != _formData.confirmpwd) {
					uni.showToast({
						title: '新密码和确认密码不匹配！',
						icon: 'none',
						duration: 2000
					});
					return false;
				}
				return true;
			}
		}
	}
</script>

<style scoped="">
	.container {
		padding: 30px;
	}
	
	.section {
		margin: 20px;
		padding: 10px 0px;
		border-bottom: 2upx solid #eee;
	}
	
	.login-btn {
		margin: 30px 20px 0px 20px;
	}
</style>
