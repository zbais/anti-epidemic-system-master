const crypto = require('crypto')
const {
	passSecret
} = require('./constants.js')

function encryptPassword(password) {
	const hmac = crypto.createHmac('sha1', passSecret.toString('ascii'));
	hmac.update(password);
	return hmac.digest('hex');
}

module.exports = {
	encryptPassword
}
