const passSecret = 'dx8ygdpzcm97qcfennu0q' //用于用户数据库密码加密的密钥，使用一个比较长的随机字符串即可，正式上线是切记更换

const tokenExp = 30 * 24 * 3600000

module.exports = {
	passSecret,
	tokenExp
}
