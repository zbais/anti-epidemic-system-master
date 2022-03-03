//我在做这个文件，我的群昵称是King
<template>
	<view clss="container">
		
		<image class="bg-set" src="../../static/bj2.png"></image>
		<view class="content"></view>
		
		<view class="search uni-flex" style="align-items: center;margin-bottom:20upx;padding:20upx;background-color:#0A98D5;">
			<view class="uni-flex  bg-white" style="flex: 1;padding: 10upx 30upx;border-radius: 80upx;align-items: center;">
				<picker :range="searchType" @change="change">
					<view style="display: flex;align-items: center;">{{searchType[index]}}<text class="uni-icon uni-icon-arrowdown"
						 style="font-size: 16px;height: 16px;padding-left: 10upx;"></text></view>
				</picker>
				<input type="text" v-model="searchKey" :placeholder="'请输入搜索的'+searchType[index]" style="flex:1" @confirm="search"
				 v-if="index<2" />
				<view style="display: flex;align-items: center;flex: 1;" v-if="index==2">
					<input type="text" disabled="true" v-model="startDate" placeholder="开始日期" style="border-bottom: 1px solid #d1d1d1;padding-left: 10upx;"
					 @tap="openCalendar" />
					<text style="padding: 0 10px;">至</text>
					<input type="text" disabled="true" v-model="endDate" placeholder="结束日期" style="border-bottom: 1px solid #d1d1d1;padding-left: 10upx;"
					 @tap="openCalendar" />
				</view>
				<text class="uni-icon uni-icon-search" style="color: #999999;font-size: 24px;" @tap="search"></text>
			</view>

		</view>
		<uni-list>
			<uni-list-item v-for="(item,index) in list" :key="index" @click="openDetail(item)" :title="item.name" :note="dateUtils.format(item.create_time) + item.address"></uni-list-item>
		</uni-list>
		<view class="uni-flex" style="justify-content: center;padding: 10upx;">
			{{contentText[loadingType]}}
		</view>
		<uni-calendar ref="calendar" :date="info.date" :insert="info.insert" :lunar="info.lunar" :startDate="info.startDate"
		 :endDate="info.endDate" :range="info.range" @confirm="confirm" />
	</view>
</template>

<script>
	// #ifdef H5
	import xlsxUtil from '@/common/xlsxUtil.js'
	import fileSaver from 'file-saver'
	import moment from 'moment'
	import {
		exportTitle,
		bodyStatus,
		trafficType
	} from "@/common/name.js"
	// #endif
	import {
		dateUtils
	} from '@/common/util.js';
	var _this;
	export default {
		data() {
			return {
				searchKey: '',
				list: [],
				exportList: [],
				page: 1,
				loadingType: 0,
				index: 0,
				searchType: ['姓名', '手机', '日期'],
				showCalendar: false,
				startDate: '',
				endDate: '',
				info: {
					startDate: '2019-12-01',
					endDate: '2025-10-15',
					lunar: true,
					range: true,
					insert: false,
					selected: []
				},
				contentText: ["上拉显示更多", "正在加载...", "没有更多数据了"],
				dateUtils: dateUtils

			}
		},
		onLoad() {
			_this = this;
		},
		onShow() {
			this.reload();
		},
		onReachBottom() {
			this.page = this.page + 1;
			if (this.loadingType !== 0) {
				return;
			}
			this.loadingType = 1;
			this.load();
		},
		methods: {
			change: function(e) {
				this.index = e.detail.value;
				if (this.index == 2) {
					this.openCalendar();
				}
			},
			openCalendar: function() {
				this.$refs.calendar.open();
			},
			confirm: function(res) {
				this.startDate = res.range.data[0];
				this.endDate = res.range.data[res.range.data.length - 1];
				this.search();
			},
			search: function() {
				_this.reload();
			},
			load: function() {
				uni.showLoading();
				var startDate = '';
				var endDate = '';
				var name = '';
				var phone = '';
				if (this.index === 0) {
					name = this.searchKey;
				} else if (this.index === 1) {
					phone = this.searchKey;
				}
				if (this.startDate && this.startDate != '') {
					startDate = new Date(this.startDate + ' 00:00:00').toISOString();
					endDate = new Date(this.endDate + ' 23:59:59').toISOString();
				} else if (this.index === 2) {
					// 如果日期没选择成功就index就会走普通逻辑，就得把index还原
					this.index = 0
				}
				this.$cloud.callFunction({
					name: 'search',
					data: {
						token: uni.getStorageSync('token'),
						page: this.page,
						startDate: startDate,
						endDate: endDate,
						name: name,
						phone: phone,
						pageSize: 10
					}
				}).then((res) => {
					console.log('res', res)
					uni.hideLoading();
					if (res.result.code === 0) {
						if (res.result.data.length == 0) {
							this.loadingType = 2;
							return;
						} else {
							var list = [];
							res.result.data.forEach(s => {
								if (s.member && s.member.length > 0) {
									Object.keys(s.member[0]).forEach(key => {
										if (key !== '_id') {
											s[key] = s.member[0][key]
										}
									})
								}
								s.list_id = s._id;
								list.push(s)
							});
							_this.list = _this.list.concat(list);
							this.loadingType = 0;
						}
					} else {
						return Promise.reject(new Error(res.result.msg))
					}

				}).catch((err) => {
					uni.hideLoading()
					uni.showModal({
						content: err.message || '查询失败',
						showCancel: false
					})
				})

			},
			openDetail: function(item) {
				uni.navigateTo({
					url: `../member-detail/member-detail?item=${encodeURIComponent(
	    JSON.stringify(item)
	  )}`
				});
			},
			reload: function() {
				_this.page = 1;
				_this.loadingType = 0;
				_this.list = [];
				// _this.exportList = [];
				_this.load();
			},
			// #ifdef H5
			exportTableData() {
				uni.showActionSheet({
					itemList: ['导出当前', '导出今日全部', '导出昨日全部'],
					success: async function(res) {
						if (res.tapIndex == 0) {
							_this.exportData(_this.list);
						} else {
							let [startDate, endDate] = ['', ''];
							if (res.tapIndex == 1) {
								startDate = new Date(moment().format('YYYY-MM-DD 00:00:00')).toISOString();
								endDate = new Date(moment().format('YYYY-MM-DD 23:59:59')).toISOString();
							} else if ((res.tapIndex == 2)) {
								let yesterday = moment().subtract(1, 'days');
								startDate = new Date(yesterday.format('YYYY-MM-DD 00:00:00')).toISOString();
								endDate = new Date(yesterday.format('YYYY-MM-DD 23:59:59')).toISOString();
							}
							const count = await _this.countList(startDate, endDate).catch((err) => {
								return Promise.reject(new Error(err.message || '导出失败'))
							});
							let pages = count ? Math.ceil(count / 100) : 0;
							await _this.getExportList(pages, startDate, endDate);
						}
					},
					fail: function(res) {
						console.log(res.errMsg);
					}
				});
			},
			async getExportList(pages, startDate, endDate) {
				let promiseList = [];
				let exportlist = [];
				for (let i = 1; i <= pages; i++) {
					promiseList.push(_this.$cloud.callFunction({
						name: 'export',
						data: {
							page: i,
							pageSize: 100,
							startDate: startDate,
							endDate: endDate,
							token: uni.getStorageSync('token')
						}
					}).then((res) => {
						if (res.result.code === 0) {
							let list = [];
							res.result.data.forEach(s => {
								if (s.member && s.member.length > 0) {
									Object.keys(s.member[0]).forEach(key => {
										if (key !== '_id') {
											s[key] = s.member[0][key]
										}
									})
								}
								s.list_id = s._id;
								list.push(s)
							});
							exportlist = exportlist.concat(list);
						} else {
							return Promise.reject(new Error(res.result.msg))
						}
					}).catch((err) => {
						return Promise.reject(new Error(err.message || '导出失败'))
					}))
				}
				Promise.all(promiseList).then((res) => {
					uni.hideLoading();
					_this.exportData(exportlist);
				}).catch((err) => {
					uni.hideLoading()
					uni.showModal({
						content: err.message || '导出失败',
						showCancel: false
					})
				})
			},
			async countList(startDate, endDate) {
				const res = await _this.$cloud.callFunction({
					name: 'count-member-list',
					data: {
						startDate: startDate,
						endDate: endDate,
						token: uni.getStorageSync('token')
					}
				}).catch((err) => {
					return Promise.reject(new Error(err.message || '导出失败'))
				})
				if (res.result.code === 0) {
					return res.result.count || 0;
				} else {
					return Promise.reject(new Error(res.result.msg))
				}
			},
			exportData(list) {
				let arr = [];
				for (let data of list) {
					let info = {};
					Object.keys(exportTitle).map((key) => {
						if (key in data) {
							switch (key) {
								case 'from_hb':
									info[exportTitle[key]] = data[key] == 1 ? '是' : '否'
									break;
								case 'from_wh':
									info[exportTitle[key]] = data[key] == 1 ? '是' : '否'
									break;
								case 'check_in_time':
									info[exportTitle[key]] = data[key] ? moment(data[key]).format('YYYY-MM-DD') : '';
									break;
								case 'check_out_time':
									info[exportTitle[key]] = data[key] ? moment(data[key]).format('YYYY-MM-DD HH:mm:ss') : '';
									break;
								case 'traffic':
									if (data[key]['type'] == -1) {
										info[exportTitle[key]['type']] = '无';
										info[exportTitle[key]['car_plate']] = '无';
									} else {
										info[exportTitle[key]['type']] = trafficType[data[key]['type']];
										info[exportTitle[key]['car_plate']] = data[key]['car_plate'];
									}
									break;
								case 'body_status':
									info[exportTitle[key]['status']] = bodyStatus[data[key]['status']];
									info[exportTitle[key]['time']] = data[key]['time'] ? moment(data[key]['time']).format(
											'YYYY-MM-DD HH:mm:ss') :
										'';
									break;
								case 'id_type':
									info[exportTitle[key]] = data[key] == 0 ? '身份证' : '护照';
									break;
								case 'sex':
									if (data[key] != -1) {
										info[exportTitle[key]] = data[key] % 2 == 1 ? '男' : '女';
									} else {
										info[exportTitle[key]] = '不确定';
									}
									break;
								default:
									if (typeof exportTitle[key] === 'object') {
										Object.keys(exportTitle[key]).map((k) => {
											if (data[key] && (k in data[key])) {
												if (k === 'status' && data[key][k] != '') {
													info[exportTitle[key][k]] = data[key][k] == 1 ? '是' : '否';
												} else {
													info[exportTitle[key][k]] = data[key][k];
												}
											} else {
												info[exportTitle[key][k]] = '';
											}

										})
									} else {
										info[exportTitle[key]] = data[key];
									}
							}

						} else {
							if (typeof exportTitle[key] === 'object') {
								Object.keys(exportTitle[key]).map((k) => {
									info[exportTitle[key][k]] = '';
								})
							} else {
								info[exportTitle[key]] = '';
							}
						}
					})
					arr.push(info)
				}
				console.log(arr)
				const xlsx = xlsxUtil.jsonToExcel(arr, 'Sheet1')
				const blob = new Blob([xlsx], {
					type: 'application/octet-stream'
				})
				fileSaver.saveAs(blob, 'Info.xlsx')
			}
			// #endif
		},
		onNavigationBarButtonTap(e) {
			// #ifdef H5
			_this.exportTableData()
			// #endif
		}
	}
</script>

<style>
	@import "../../common/uni.css";

	page {
		background-color: #EEEEEE;
	}

	.fixed {
		position: fixed;
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
	}

	.flex-sub {
		flex: 1;
	}

	.bg-white {
		background-color: white;
	}
	.bg-set{
	    position: fixed;
	    width: 100%;
	    height: 100%;
	    top: 0;
	    left: 0;
	    z-index: 1;
			pointer-events: none; 
			opacity: 0.3;
	}
</style>
