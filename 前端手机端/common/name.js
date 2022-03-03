const exportTitle = {
	name: '姓名',
	phone: '电话',
	age: '年龄',
	id_type: '证件类型',
	id_card: "证件号码",
	// city: '城市',
	// area: '区域',
	// province: '区县',
	// address: '地址',
	sex: '性别',
	// regtime: '注册时间',
	from_address: {
		addressStr: "来源地区",
		street: "来源地区详细地址"
	},
	from_hb: "来自湖北",
	from_wh: "来自武汉",
	traffic: {
		type: "交通工具类型",
		car_plate: "交通工具信息"
	},
	temperature: '体温',
	check_in_address: {
		addressStr: "到访地区",
		street: "到访详细地址"
	},
	check_in_time: "到达日期",
	check_out_time: "离开时间",
	body_status: {
		status: "身体状态",
		time: "测量时间"
	},
	contact_virus: {
		status: "是否接触过确诊人员",
		name: "确诊人员姓名",
		contact: "确诊人员联系方式"
	},
	contact_like_virus: {
		status: "是否接触过疑似人员",
		name: "疑似人员姓名",
		contact: "疑似人员联系方式"
	},
	contact_like_virus_region: {
		status: "是否接触过来自疫区的人员",
		name: "对方姓名",
		contact: "联系方式"
	},
	comment: '备注',
	operator_username: '操作者'
}
const bodyStatus = ['普通', '居家隔离', '发烧', '疑似', '确诊', '死亡']
const trafficType = ['火车', '自驾', '汽车', '飞机']

module.exports = {
	exportTitle,
	bodyStatus,
	trafficType
}
