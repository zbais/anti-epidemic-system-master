'use strict';
const {
    validateToken
} = require('../../utils/validateToken.js')

const db = uniCloud.database()
exports.main = async (event = {}, context) => {
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

	const collection = db.collection('member_list')
	const id = event.id
	if (!id) {
		return {
			code: -1,
			msg: 'error:id'
		}
	}
	const res = await collection.where({
		member_id: id
	}).limit(1).get()
	return {
		code: 0,
		data: res.data[0] || {},
		msg: 'ok'
	}
};
