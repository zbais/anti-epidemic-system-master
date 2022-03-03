<template>
	<view >

		
		<view class="qiun-columns">
			<view class="qiun-bg-white qiun-title-bar qiun-common-mt">
				<view class="qiun-title-dot-light">数据可视化</view>
			</view>
			<view v-if="sign" style="text-align: center;margin-top:5px;">暂无数据</view>
			<view class="qiun-charts" v-else>
				<canvas canvas-id="canvasLineA" id="canvasLineA" class="charts" @touchstart="touchLineA"></canvas>
			</view>
		</view>
		<view style="margin-top: 10px;">
			<view class="qiun-bg-white qiun-title-bar qiun-common-mt">
				<view class="qiun-title-dot-light">登记表</view>
			</view>
			<z-table :tableData='tableData' :columns='columns' :showBottomSum="true"></z-table>
		</view>
	</view>
</template>

<script>
	import uCharts from '@/components/u-charts/u-charts.js';
	import _ from 'lodash';
	import moment from 'moment';
	import {
		formatDate
	} from '@/common/util.js';
	var _self;
	var canvaLineA = null;
	export default {
		data() {
			return {
				cWidth: '',
				cHeight: '',
				pixelRatio: 1,
				sign: true,
				titles: ['普通', '居家隔离', '发烧', '疑似', '确诊', '死亡'],
				tableData: [],
				columns: []
			}
		},
		onLoad() {
			_self = this;
			this.cWidth = uni.upx2px(750);
			this.cHeight = uni.upx2px(500);
			this.setColumns();
		},
		onShow() {
			this.getServerData();
		},
		methods: {
			getServerData() {
				uni.showLoading({
					title: "加载中..."
				});
				this.$cloud.callFunction({
					name: 'chart-member-list',
					data: {
						token: uni.getStorageSync('token'),
					}
				}).then((res) => {
					uni.hideLoading()
					if (res.result.code === 0) {
						if (res.result.data && res.result.data.length > 0) {
							this.fillData(res.result.data);
							this.sign = false;
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
			setColumns() {
				let columns = [];
				columns.push({
					title: "日期",
					key: "date"
				})
				for (let i in this.titles) {
					columns.push({
						title: this.titles[i],
						key: 'status' + i
					})
				}
				columns.push({
					title: "当日总计",
					key: "sum"
				})
				this.columns = columns;
			},
			fillData(initDatas) {
				let initDates = _.uniq(_.map(initDatas, (data) => {
					return formatDate(data._id.year, data._id.month, data._id.day)
				})).sort();
				let mDates = _.map(initDates, (mDate) => {
					let date = moment(mDate);
					return {
						year: date.year(),
						month: date.month() + 1,
						day: date.date()
					};
				})
				// 0普通、1居家隔离、2发烧、3疑似、4确诊、5死亡
				let statusDatas = [];
				for (let i = 0; i < 6; i++) {
					statusDatas.push(new Array(mDates.length).fill(0));
				}
				for (let j = 0, iLen = initDatas.length; j < iLen; j++) {
					for (let i = 0, len = mDates.length; i < len; i++) {
						if (mDates[i].year === initDatas[j]._id.year &&
							mDates[i].month === initDatas[j]._id.month &&
							mDates[i].day === initDatas[j]._id.day) {
							statusDatas[initDatas[j]._id.status][i] = initDatas[j].count;
						}
					}
				}
				let categories = initDates.map((initDate) => {
					return initDate.substr(5, 5);
				});
				this.getLineData(statusDatas, categories);
				this.getTableData(statusDatas, initDates);
			},
			getTableData(seriesList, dateList) {
				let tableData = [];
				for (let i in dateList) {
					let temp = {};
					temp["date"] = dateList[i];
					let sum = 0;
					for (let j in seriesList) {
						temp["status" + j] = seriesList[j][i];
						sum += seriesList[j][i];
					}
					temp["sum"] = sum;
					tableData.push(temp);
				}
				this.tableData = tableData;
			},
			getLineData(seriesList, categories) {
				let series = []
				let temp = [];
				for (let i in seriesList) {
					// 去掉普通
					if (i > 0) {
						series.push({
							"name": this.titles[i],
							"data": seriesList[i]
						})
						temp = temp.concat(seriesList[i]);
					}
				}
				let LineA = {
					"categories": categories,
					"series": series
				};
				_self.showLineA("canvasLineA", LineA, _.compact(_.uniq(_.flatten(temp))).length);
			},
			showLineA(canvasId, chartData, splitNumber) {
				canvaLineA = new uCharts({
					$this: _self,
					canvasId: canvasId,
					type: 'line',
					fontSize: 11,
					legend: {
						show: true,
						position: 'top'
					},
					dataLabel: false,
					dataPointShape: true,
					background: '#FFFFFF',
					pixelRatio: _self.pixelRatio,
					categories: chartData.categories,
					series: chartData.series,
					animation: true,
					xAxis: {
						type: 'grid',
						// disableGrid: true,
						labelCount: 7,
						scrollShow: true,
						rotateLabel: true
					},
					yAxis: {
						gridType: 'solid',
						gridColor: '#CCCCCC',
						splitNumber: splitNumber < 4 ? splitNumber : 5,
						format: (val) => {
							return val.toFixed(0) + '人'
						}
					},
					width: _self.cWidth * _self.pixelRatio,
					height: _self.cHeight * _self.pixelRatio,
					extra: {
						line: {
							type: 'straight'
						}
					}
				});

			},
			touchLineA(e) {
				canvaLineA.showToolTip(e, {
					format: function(item, category) {
						return category + ' ' + item.name + ':' + item.data
					}
				});
			}
		}
	}
	
	
</script>

<style>
	/*样式的width和height一定要与定义的cWidth和cHeight相对应*/
	
	.qiun-title-dot-light{
		   text-align: center;
			 color: #007AFF;
		}
	.qiun-charts {
		width: 750upx;
		height: 500upx;
		background-color: #FFFFFF;
	}

	.charts {
		width: 750upx;
		height: 500upx;
		background-color: #FFFFFF;
	}

</style>
