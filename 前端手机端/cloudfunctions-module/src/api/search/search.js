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
	const options = operator_username === 'admin' ? {} : {
		operator_username
	}
	const collection = db.collection('member')
	const dbCmd = db.command;
	const $ = db.command.aggregate
	let res = null;
	try {
		if (event.name != '' || event.phone != '') {
			let where = event.name != '' ? {
				name: new RegExp(event.name)
			} : {
				phone: new RegExp(event.phone)
			}
			let temp = await collection.where(where).get();
			let member_ids = [];
			if (temp.data.length) {
			    temp.data.forEach(item => {
					member_ids.push(item._id);
				});
			}
			options.member_id = dbCmd.in(member_ids)
			res = await db.collection('member_list').aggregate().match(options).sort({
				create_time: -1
			}).skip((event.page - 1) * event.pageSize).limit(event.pageSize).lookup({
				from: 'member',
				localField: 'member_id',
				foreignField: '_id',
				as: 'member',
			}).end();
		} else if (event.startDate != '') {
			options.create_time = dbCmd.gte(event.startDate).and(dbCmd.lte(event.endDate))
			res = await db.collection('member_list').aggregate().match(options).sort({
				create_time: -1
			}).skip((event.page - 1) * event.pageSize).limit(event.pageSize).lookup({
				from: 'member',
				localField: 'member_id',
				foreignField: '_id',
				as: 'member',
			}).end();
		} else {
			res = await db.collection('member_list').aggregate().match(options).sort({
				create_time: -1
			}).skip((event.page - 1) * event.pageSize).limit(event.pageSize).lookup({
				from: 'member',
				localField: 'member_id',
				foreignField: '_id',
				as: 'member',
			}).end();
		}
		return {
			code: 0,
			data: res.data,
			msg: "查询成功"
		}

	} catch (e) {
		return {
			code: -2,
			msg: '搜索失败'
		}
	}
};
