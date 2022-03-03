<template>

	<view class="content">
		<form @submit="sendsj">
			<view class="list">
				<view class="list-title">人员信息</view>
				<view class="list-item">姓名： <input name="xm" placeholder="请输入人员姓名" /></view>
				<view class="list-item">证件类型：<picker mode="selector" style="padding:0px;margin:0px;" range-key="lable" :range="zj"
					 @change="zjChange">
						<view class="xiala">{{zjlx}}
							<image src="/static/xl.png"></image>
						</view>
					</picker>
				</view>
				<view class="list-item">证件号码：<input name="zjhm" placeholder="请输入证件号码" /></view>
				<view class="list-item">年龄：<picker mode="selector" style="padding:0px;margin:0px;" range-key="lable" :range="nl"
					 @change="nlChange">
						<view class="xiala">{{xznl}}
							<image src="/static/xl.png"></image>
						</view>
					</picker>
				</view>
				<view class="list-item">性别：<picker mode="selector" style="padding:0px;margin:0px;" range-key="lable" :range="xb"
					 @change="xbChange">
						<view class="xiala">{{xzxb}}
							<image src="/static/xl.png"></image>
						</view>
					</picker>
				</view>
				<view class="list-item">联系电话：<input name="phonenumber" placeholder="请输入联系电话" /></view>
			</view>
			<view class="list">
				<view class="list-title">来源地信息</view>
				<view class="list-item">是否来自武汉：<button v-if="lzwh==''" type="primary" size="mini" @click="jcly(1)">是</button><button
					 v-if="lzwh==''" type="warn" size="mini" style="margin-left:30upx;" @click="jcly(2)">否</button><button size="mini"
					 v-if="lzwh!=''" @click="cxly(1)">{{lzwh}}</button></view>
				<view class="list-item">是否来自湖北：<button type="primary" v-if="lzhb==''" size="mini" @click="jcly(3)">是</button><button
					 type="warn" v-if="lzhb==''" size="mini" style="margin-left:30upx;" @click="jcly(4)">否</button><button size="mini"
					 v-if="lzhb!=''" @click="cxly(2)">{{lzhb}}</button></view>
				<view class="list-item">来源地详细地址：<input name="lydaddr" placeholder="请输入地址" /></view>
				<view class="list-item">出行方式：<picker mode="selector" style="padding:0px;margin:0px;" range-key="lable" :range="cx"
					 @change="cxfsChange">
						<view class="xiala">{{cxfs}}
							<image src="/static/xl.png"></image>
						</view>
					</picker>
				</view>
				<view v-if="cxlx!=''" class="list-item">{{cxlx}}：<input placeholder="请务必输入正确的信息" /></view>
				<view class="list-item">现居地详细地址：<input name="xjd" placeholder="请输入详细地址" /></view>
			</view>
			<view class="list">
				<view class="list-title">健康状况</view>
				<view class="list-item">体温：
					<slider value="37" @change="twChange" min="36.5" step="0.1" max="40" show-value style="width: 400upx;" />
				</view>
				<view class="list-item">检测时间： <picker @change="jcdate" mode="date">
						<button size="mini">{{jcrq}}</button>
					</picker>
					<picker @change="jctime" mode="time">
						<button size="mini" style="margin-left:20upx;margin-right:20upx">{{jcsj}}</button>
					</picker>
				</view>
				<view class="list-item">其它症状： <checkbox-group @change="qtzz" style="display: flex;flex-direction: row;flex-wrap:wrap">
						<label class="uni-list-cell uni-list-cell-pd" style="display: flex;flex-direction: row;margin-top:20upx;" v-for="item in items"
						 :key="item.value">
							<view style="margin-left:20upx;height:50upx;line-height: 50upx;">
								<checkbox :value="item.value" :checked="item.checked" />
							</view>
							<view style="display: flex;height:50upx;line-height: 50upx;">{{item.name}}</view>
						</label>
					</checkbox-group>
				</view>
				<view class="list-item">是否接触确诊人员：<button v-if="jcqz==''" type="primary" size="mini" @click="jcry(1)">是</button><button
					 type="warn" v-if="jcqz==''" size="mini" style="margin-left:30upx;" @click="jcry(2)">否</button><button v-if="jcqz!=''"
					 @click="cxjc(1)" size="mini">{{jcqz}}</button></view>
				<view v-if="jcqz=='是'">
					<view class="list-item">接触人员姓名： <input name="jcname" placeholder="请输入人员姓名" /></view>
					<view class="list-item">接触人员电话：<input name="jcphone" placeholder="请输入联系电话" /></view>
				</view>
				<view class="list-item">是否接触疑似人员：<button v-if="jcys==''" type="primary" size="mini" @click="jcry(3)">是</button><button
					 type="warn" v-if="jcys==''" size="mini" style="margin-left:30upx;" @click="jcry(4)">否</button><button v-if="jcys!=''"
					 @click="cxjc(2)" size="mini">{{jcys}}</button></view>
				<view v-if="jcys=='是'">
					<view class="list-item">接触人员姓名： <input name="ysname" placeholder="请输入人员姓名" /></view>
					<view class="list-item">接触人员电话：<input name="ysphone" placeholder="请输入联系电话" /></view>
				</view>
				<button style="width: 100%;" form-type="submit">提交信息</button>

			</view>
		</form>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '海陵区城东街道外来人员信息登录管理系统',
				zj: [{
					lable: '身份证',
					value: '身份证'
				}, {
					lable: '驾驶证',
					value: '驾驶证'
				}, {
					lable: '护照',
					value: '护照'
				}, {
					lable: '士官证',
					value: '士官证'
				}],
				zjlx: '请选择证件类型',
				xznl: '输入年龄',
				xzxb: '选择性别',
				nl: [], //年龄
				xb: [{
					lable: '男',
					value: '男'
				}, {
					lable: '女',
					value: '女'
				}],
				cxfs: '出行方式',
				cx: [{
					lable: '汽车',
					value: '汽车'
				}, {
					lable: '火车',
					value: '火车'
				}, {
					lable: '飞机',
					value: '飞机'
				}, {
					lable: '其它',
					value: '其它'
				}],
				cxlx: '',
				jcsj: '选择时间',
				jcrq: '选择日期',
				lzwh: '',
				lzhb: '',
				jcqz: '',
				jcys: '',
				tw: 0, //体温
				otherzz: '', //其它症状
				items: [{
						value: '咳嗽',
						name: '咳嗽'
					},
					{
						value: '头痛',
						name: '头痛',
					},
					{
						value: '乏力',
						name: '乏力',
					},
					{
						value: '呕吐',
						name: '呕吐',
					},
					{
						value: '腹泄',
						name: '腹泄',
					},
					{
						value: '昏迷',
						name: '昏迷',
					}

				],
				senddata: [] //存放最终的表单数据
			}
		},
		onLoad() {
			for (let i = 0; i < 100; i++) {
				this.nl.push({
					lable: i,
					value: i
				})
			}
		},
		methods: {
			zjChange: function(e) {
				this.zjlx = this.zj[e.detail.value].value
			},
			nlChange: function(e) {
				this.xznl = this.nl[e.detail.value].value
			},
			xbChange: function(e) {
				this.xzxb = this.xb[e.detail.value].value
			},
			twChange: function(e) {
				this.tw = e.detail.value;
			},
			cxfsChange: function(e) {
				this.cxfs = this.cx[e.detail.value].value
				if (this.cxfs == '汽车') {
					this.cxlx = "车牌号";
				}
				if (this.cxfs == '火车') {
					this.cxlx = "火车班次";
				}
				if (this.cxfs == '飞机') {
					this.cxlx = "航班号";
				}
				if (this.cxfs == '其它') {
					this.cxlx = "出行方式说明";
				}
			},
			qtzz: function(e) {
				console.log(e.detail.value);
				this.otherzz = e.detail.value;
			},
			jcdate: function(e) {
				console.log(e.detail.value);
				this.jcrq = e.detail.value;
			},
			jctime: function(e) {
				console.log(e.detail.value);
				this.jcsj = e.detail.value;
			},
			cxjc: function(e) {
				if (e == 1) {
					this.jcqz = '';
				}
				if (e == 2) {
					this.jcys = '';
				}
			},
			jcry: function(e) {
				if (e == 1) {
					this.jcqz = "是";
				}
				if (e == 2) {
					this.jcqz = "否";
				}
				if (e == 3) {
					this.jcys = "是";
				}
				if (e == 4) {
					this.jcys = "否";
				}
			},
			jcly: function(e) {
				if (e == 1) {
					this.lzwh = "是";
				}
				if (e == 2) {
					this.lzwh = "否";
				}
				if (e == 3) {
					this.lzhb = "是";
				}
				if (e == 4) {
					this.lzhb = "否";
				}
			},
			cxly: function(e) {
				if (e == 1) {
					this.lzwh = "";
				}
				if (e == 2) {
					this.lzhb = "";
				}
			},
			sendsj: function(e) {
				this.senddata = [];
				console.log(e.detail.value);
				var obj = e.detail.value;
				for (let key in obj) {
					console.log(key + '---' + obj[key])
					//this.senddata.push({'key':obj[key]})
					this.senddata[key] = obj[key];
				}
				this.senddata['zjlx'] = this.zjlx; //证件类型
				this.senddata['nl'] = this.xznl; //年龄
				this.senddata['xb'] = this.xzxb; //性别
				this.senddata['sfwh'] = this.lzwh; //是否来自武汉
				this.senddata['sfhb'] = this.lzhb; //是否来看成湖北
				this.senddata['cxfs'] = this.cxfs; //出行 方式
				this.senddata['jcrq'] = this.jcrq; //检测日期
				this.senddata['jcsj'] = this.jcsj; //检测时间
				this.senddata['tw'] = this.tw; //检测时间
				this.senddata['qtzz'] = this.otherzz; //其它症状
				this.senddata['jcqz'] = this.jcqz; //接触确诊
				this.senddata['jcys'] = this.jcys; //接触疑似
				for (let nkey in this.senddata) {
					this.senddata.push("{" + nkey + ":'" + this.senddata[nkey] + "'}");
				}
				if (this.senddata['xm'] == "" || this.senddata['phonenumber'] == "") {
					uni.showToast({
						title: '姓名和电话不能为空',
						icon: 'none'
					})
				} else {
					var outstr = JSON.stringify(this.senddata);
					outstr = outstr.replace(/"/g, "")
					uni.showModal({
						title: '获取的数据是：',
						content: outstr,
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				}
				console.log(this.senddata);

			},
            addUser() {
                uni.showLoading({
                    title: "处理中..."
                });

                this.$cloud.callFunction({
                    name: 'add-member', // 云函数名
                    data: {}, // 表单数据
                    success: (res) => {
                    },
                    fail: (err) => {},
                    complete: (c) => {
                        uni.hideLoading();
                    }
                })
            }
		}
	}
</script>

<style>
	page {
		background: #f0f0f0;
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-bottom: 50upx;
	}

	.list {
		display: flex;
		flex-direction: column;
		width: 680upx;
		margin: auto;
		margin-top: 20upx;
		background: #ffffff;
		border-radius: 20upx;
		padding-bottom: 20upx;
	}

	.list-title {
		width: 580upx;
		padding: 20upx;
		display: flex;
		font-size: 40upx;
		font-weight: bold;
		border-bottom: #f0f0f0 1upx solid;
		margin: auto;
		margin-bottom: 20upx;

	}

	.list-item {
		width: 580upx;
		padding: 20upx;
		display: flex;
		margin: auto;
		font-size: 30upx;
		align-items: center;
	}

	.list-item input {
		background: #f0f0f0;
		border-radius: 10upx;
		padding-left: 20upx;
		padding-right: 20upx;
		width: 300upx;
		height: 60upx;
		color: #999999;
		font-size: 30upx;

	}

	.xiala {
		display: flex;
		background: #f0f0f0;
		padding: 10upx;
		border-radius: 10upx;
		color: #999999;
		font-size: 30upx;

	}

	.xiala image {
		display: flex;
		padding-top: 5upx;
		width: 30upx;
		height: 30upx;
		margin-left: 10upx;

	}
</style>
