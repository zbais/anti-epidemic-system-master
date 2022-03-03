const {
  validateToken
} = require('../../utils/validateToken.js')

async function report(event) {
  try {
    return await validateToken(event.token)
  } catch (e) {
    //TODO handle the exception
    return {
      code: -2,
      errCode: 'TOKEN_INVALID',
      msg: 'token无效'
    }
  }
};

exports.main = report
