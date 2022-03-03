'use strict';
const {
	validateToken
} = require('../../utils/validateToken.js')
const db = uniCloud.database()
exports.main = async (event = {}, context) => {
	let operator_username = event.operator_username

	if (!operator_username) {
		let validateResult
		try {
			validateResult = await validateToken(event.token)
		} catch (e) {
			//TODO handle the exception
			return {
				code: -3,
				errCode: 'TOKEN_INVALID',
				msg: '登录状态无效，请重新登录'
			}
		}

		if (validateResult.code !== 0) {
			return {
				code: -3,
				errCode: 'TOKEN_INVALID',
				msg: '登录状态无效，请重新登录'
			}
		}
		operator_username = validateResult.username
	} else {
		const userCollection = db.collection('user')
		let checkOperatorResult = await userCollection.where({
			username: operator_username
		}).get()
		if (checkOperatorResult.data && checkOperatorResult.data.length === 0) {
			return {
				code: -4,
				errCode: 'OPERATOR_ID_INVALID',
				msg: '操作员信息无效，请重新扫码录入'
			}
		}
	}
	let options = operator_username === 'admin' ? {} : {
		operator_username
	}
	options.body_status = {
		// 有用户新增数据出现time为null的情况，暂未重现，先做判断规避
		// time: db.command.neq(0)
		time: db.command.nin([0, null])
	}
	try {
		const collection = db.collection('member_list')
		const $ = db.command.aggregate
		const res = await collection.aggregate().match(options).group({
			_id: {
				month: {
					$month: "$body_status.time"
				},
				day: {
					$dayOfMonth: "$body_status.time"
				},
				year: {
					$year: "$body_status.time"
				},
				status: "$body_status.status"
			},
			count: $.sum(1)
		}).end();
		return {
			code: 0,
			data: res.data,
			msg: "查询成功"
		}
	} catch (e) {
		return {
			code: -2,
			msg: '查询失败'
		}
	}
};
