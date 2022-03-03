const jwt = require('jwt-simple')

const db = uniCloud.database()
async function validateToken(token) {
	const userFromToken = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString())
	const userInDB = await db.collection('user').where(userFromToken).get()
	if (userInDB.data.length !== 1) {
		return {
			code: -1,
			errCode: 'TOKEN_INVALID',
			msg: '查无此人'
		}
	}
	const userInfoDB = userInDB.data[0]
	let userInfoDecode

	userInfoDecode = jwt.decode(token, userInfoDB.tokenSecret)

	function checkUser(userFromToken, userInfoDB) {
		return Object.keys(userFromToken).every(function(item) {
			return userFromToken[item] === userInfoDB[item] && userFromToken[item] === userInfoDecode[item]
		})
	}


	if (userInfoDB.exp > Date.now() && checkUser(userFromToken, userInfoDB)) {
		return {
			code: 0,
			username: userInfoDB.username,
			msg: 'token验证成功'
		}
	}

	if (userInfoDB.exp < Date.now()) {
		return {
			code: -3,
			errCode: 'TOKEN_EXPIRED',
			msg: 'token已失效'
		}
	}

	return {
		code: -2,
		errCode: 'TOKEN_INVALID',
		msg: 'token无效'
	}

}

module.exports = {
	validateToken
}
