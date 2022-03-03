<!--丛林野战军上传于2020-01-31 18：38 QQ:31853722-->
<template>
	<view class="content">
		<view v-if="showxg==0">参数传递错误</view>
		<view class="ryxq" v-if="showxq!=0">
			<!-- <view class="xq-title">登记单位<view>
					<image @click="gbxq()" src="/static/gb.png" style="width:50upx;height:50upx;display: flex;position: absolute;right:20upx;top:20upx;"></image>
				</view>
			</view> -->
			<view class="xq-list">
				<!-- <view class="items">记录人员：春晖社区（{{ryinfo['shry']}}）</view> -->
				<view class="items">最后更新：{{ryinfo.create_time|formatTime}}</view>
			</view>
			<view class="xq-title" style="position: relative;">人员信息</view>
			<view class="xq-list">
				<view class="items">姓名：{{ryinfo['name']}}</view>
				<view class="items">性别：{{ryinfo.sex|sex}}</view>
				<view class="items">是否本地户籍：{{ryinfo.native|boolean}}</view>
				<view class="items">年龄：{{ryinfo.age|age}}</view>
				<view class="items">证件类型：{{ryinfo.id_type|idType}}</view>
				<view class="items">证件号码：{{ryinfo.id_card||'未知'}}</view>
				<view class="items">联系电话：{{ryinfo.phone}}</view>
			</view>
			<view class="xq-title">到访信息</view>
			<view class="xq-list">
				<view class="items">本单位联系人：{{ryinfo.access.name||'无'}}</view>
				<view class="items">联系人电话：{{ryinfo.access.phone||'无'}}</view>
				<view class="items">到访事由：{{ryinfo.access.comment||'无'}}</view>
			</view>
			<view class="xq-title">来源地信息</view>
			<view class="xq-list">
				<view class="items">是否来自武汉：{{ryinfo.from_wh|boolean}}</view>
				<view class="items">是否来自湖北：{{ryinfo.from_hb|boolean}}</view>
				<view class="items">出行方式：{{ryinfo.traffic.type|trafficType}}</view>
				<view class="items">车牌或列车号：{{ryinfo.traffic.car_plate||'未知'}}</view>
				<view class="items">来源：{{ryinfo.from_address|addres}}</view>
				<view class="items">现居：{{ryinfo.check_in_address|addres}}</view>
			</view>
			<view class="xq-title">健康状况</view>
			<view class="xq-list">
				<!-- <view class="items">身体状况：{{ryinfo.body_status.status|bodyStatus}}</view> -->
				<view class="items">体温：{{ryinfo.temperature}}</view>
				<!-- <view class="items">检测时间：{{ryinfo.body_status.time|formatTime}}</view> -->
				<!-- <view class="items">其它症状：{{ryinfo['qtzz']}}</view> -->
				<!-- <view class="items">是否接触确诊病人：{{ryinfo.contact_virus.status|boolean}}</view>
				<view class="items">确诊病人姓名：{{ryinfo.contact_virus.name||'无'}}</view>
				<view class="items">确诊病人电话：{{ryinfo.contact_virus.contact||'无'}}</view>
				<view class="items">是否接触疑似病人：{{ryinfo.contact_like_virus.status|boolean}}</view>
				<view class="items">疑似病人姓名：{{ryinfo.contact_like_virus.name||'无'}}</view>
				<view class="items">疑似病人电话：{{ryinfo.contact_like_virus.contact||'无'}}</view>
				<view class="items">是否接来自疫区人员：{{ryinfo.contact_like_virus_region.status|boolean}}</view>
				<view class="items">来自疫区人员姓名：{{ryinfo.contact_like_virus_region.name||'无'}}</view>
				<view class="items">来自疫区人员电话：{{ryinfo.contact_like_virus_region.contact||'无'}}</view> -->
			</view>
			<!-- <view style="width:700upx;margin:auto;text-align:center;margin-top:20upx;padding-top:20upx;border-top:#cccccc 1upx solid;float: left;margin-bottom:40upx;"><button
				 type="primary" style="width:340upx;float:left;">编辑信息</button><button type="warn" style="width:340upx;margin-left:0upx;float:right"
				 @click="gbxq()">关闭</button></view> -->
		</view>
		<!-- <view class="bottom">
			<navigator url="gl">
				<view class="bottom-list">
					<image src="/static/gl.png" style="width:60upx;height:60upx"></image>人员管理
				</view>
			</navigator>
			<navigator url="add">
				<view class="bottom-list">
					<image src="/static/tj.png" mode="widthFix"></image>添加人员
				</view>
			</navigator>
			<navigator url="my">
				<view class="bottom-list">
					<image src="/static/my.png" mode="widthFix"></image>我的
				</view>
			</navigator>
		</view> -->
	</view>
</template>

<script>
	import {
		trafficType,
		bodyStatus
	} from "@/common/name.js"
	export default {
		data() {
			return {
				ryinfo: {
					body_status: {},
					traffic: {},
					contact_virus: {},
					contact_like_virus: {},
					contact_like_virus_region: {},
					access: {}
				},
				showxg: 1,
				showxq: 1
			}
		},
		filters: {
			age(val) {
				return val ? `${val}岁` : '未知'
			},
			idType(val) {
				return (['身份证', '护照'])[val] || '未知'
			},
			boolean(val) {
				return (['否', '是'])[val] || '未知'
			},
			trafficType(val) {
				return (trafficType)[val] || '未知'
			},
			bodyStatus(val) {
				return (bodyStatus)[val] || '未知'
			},
			addres(val) {
				return (typeof val === 'object' ? val.street : val) || '未知'
			},
			formatTime(val) {
				if (!val) {
					return '无'
				}
				const date = new Date(val)
				return `${date.getFullYear()}年${date.getMonth()+1}月${date.getDate()}日 ${date.getHours()}时${date.getMinutes()}分${date.getSeconds()}秒`
			},
			sex(val) {
				return (['女', '男'])[Number(val) % 2] || '未知'
			}
		},
		onLoad({
			item
		} = {}) {
			try {
				const ryinfo = Object.assign(this.ryinfo, JSON.parse(decodeURIComponent(item)))
				if (!ryinfo.list_id) {
					this.loadData(ryinfo._id)
				}
			} catch (e) {
				console.error(e)
			}
		},
		methods: {
			loadData(id) {
				uni.showLoading({
					title: "加载中..."
				});

				this.$cloud.callFunction({
					name: 'member-detail',
					data: {
						token: uni.getStorageSync('token'),
						id
					}
				}).then(({
					result
				}) => {
					uni.hideLoading()
					console.log(result)
                    if (result.code === -3) {// 登陆无效
                      uni.showModal({
                        content: "登陆状态无效",
                        showCancel: false,
                        complete: () => {
                          uni.redirectTo({
                              url:"/pages/login/login"
                          })
                        }
                      })
                      return;
                    }
					if (result.code !== 0) {
						uni.showToast({
							icon: 'none',
							title: result.msg
						})
						return
					}
					const info = result.data
					Object.keys(info).forEach(key => {
						if (key !== '_id') {
							this.ryinfo[key] = info[key]
						}
					})
					console.log(this.ryinfo)
				}).catch(err => {
					uni.hideLoading()
					uni.showToast({
						icon: 'none',
						title: '数据加载失败'
					})
				})
			}
		}
	}
</script>

<style>
	page {
		background: #ffffff;
	}

	.ryxq {
		display: flex;
		width: 750upx;
		/* height: 100%; */
		background: #ffffff;

		/* position: fixed;
		z-index: 99;
		margin-top: 0upx;
		top: 0upx; */
		flex-direction: column;
		overflow: auto;

	}

	.xq-list {

		flex-direction: row;
		font-size: 30upx;
		line-height: 60upx;
		flex-wrap: wrap;
		margin-top: 20upx;
		height: auto;
		margin-bottom: 20upx;
	}

	.items {
		float: left;
		overflow: hidden;
		background: #efefef;
		height: 50upx;
		line-height: 50upx;
		margin: 20upx 20upx 0upx 20upx;
		padding: 5upx 15upx 5upx 15upx;
		border-radius: 20upx;

	}

	.xq-title {

		padding: 20upx;
		border-bottom: #cccccc 2upx solid;
	}
</style>
