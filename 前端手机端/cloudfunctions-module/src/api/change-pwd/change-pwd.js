'use strict';
const crypto = require('crypto')
const jwt = require('jwt-simple')
const {
	tokenExp
} = require('../../utils/constants.js')
const {
	encryptPassword
} = require('../../utils/encryptPassword.js')
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
			code: -1,
			errCode: 'TOKEN_INVALID',
			msg: '登录状态无效，请重新登录'
		}
	}

	if (validateResult.code !== 0) {
		return {
			code: -1,
			errCode: 'TOKEN_INVALID',
			msg: '登录状态无效，请重新登录'
		}
	}
	const username = validateResult.username

	const {
		oldPwd,
		newPwd
	} = event
	
	let userInfo = {
		username
	}

	if (!oldPwd || !newPwd || newPwd.length < 6) {
		return {
			code: -2,
			msg: '参数不正确'
		}
	}
	
	const userInDB = await db.collection('user').where({
		username,
		password: encryptPassword(oldPwd)
	}).get()
	
	let tokenSecret = crypto.randomBytes(16).toString('hex'),
		token = jwt.encode(userInfo, tokenSecret)
	let userUpdateResult
	if (userInDB.data && userInDB.data.length === 0) {
		return {
			code: -3,
			msg: '旧密码不正确'
		}
	} else {
		userUpdateResult = await db.collection('user').doc(userInDB.data[0]._id).update({
			password: encryptPassword(newPwd),
			tokenSecret,
			exp: Date.now() + tokenExp
		})
	}
	
	if (userUpdateResult.id || userUpdateResult.affectedDocs === 1) {
		return {
			code: 0,
			token,
			username,
			msg: '密码修改成功'
		}
	}
	
	return {
		code: -4,
		msg: '密码修改失败'
	}
};
