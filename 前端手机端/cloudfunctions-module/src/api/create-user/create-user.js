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

async function signUp(event) {
	const {
		username,
		password
	} = event

	let userInfo = {
		username
	}

	const userInDB = await db.collection('user').where(userInfo).get()

	let tokenSecret = crypto.randomBytes(16).toString('hex'),
		token = jwt.encode(userInfo, tokenSecret)

	let userUpdateResult
	if (userInDB.data && userInDB.data.length === 0) {
		userUpdateResult = await db.collection('user').add({
			...userInfo,
			password: encryptPassword(password),
			tokenSecret,
			exp: Date.now() + tokenExp
		})
	} else {
		return {
			code: -1,
			msg: '此操作员已添加，请勿重复添加'
		}
	}

	if (userUpdateResult.id || userUpdateResult.affectedDocs === 1) {
		return {
			code: 0,
			token,
			msg: '注册成功'
		}
	}

	return {
		code: -1,
		msg: '注册失败'
	}
}

// 批量注册操作员账号

async function signUpMany(event) {
	if (event.username) {
		return await signUpByAdmin(event);
	}
	const userList = [{
		username: 'admin',
		password: '123456'
	}]

	let resultList = [];
	for (let i = 0; i < userList.length; i++) {
		const res = await signUp(userList[i]);
		resultList.push(res.code);
	}
	
	let signUpManyResult = resultList.every((item) => {
		return item === 0
	})
	
	if (signUpManyResult) {
		return {
			msg: '操作员账号注册完成',
			userList
		}
	} else {
		return {
			msg: '一个或多个操作员账号注册失败，请检查是否已经添加了操作员账号'
		}
	}
}

// 超级管理员添加需认证是否登录
async function signUpByAdmin(event) {
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
	return await signUp(event);
}
exports.main = signUpMany
