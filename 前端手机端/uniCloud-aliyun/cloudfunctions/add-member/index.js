'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

function _interopDefault (ex) { return (ex && (typeof ex === 'object') && 'default' in ex) ? ex['default'] : ex; }

var crypto = _interopDefault(require('crypto'));

function createCommonjsModule(fn, module) {
	return module = { exports: {} }, fn(module, module.exports), module.exports;
}

var jwt_1 = createCommonjsModule(function (module) {
/*
 * jwt-simple
 *
 * JSON Web Token encode and decode module for node.js
 *
 * Copyright(c) 2011 Kazuhito Hokamura
 * MIT Licensed
 */

/**
 * module dependencies
 */



/**
 * support algorithm mapping
 */
var algorithmMap = {
  HS256: 'sha256',
  HS384: 'sha384',
  HS512: 'sha512',
  RS256: 'RSA-SHA256'
};

/**
 * Map algorithm to hmac or sign type, to determine which crypto function to use
 */
var typeMap = {
  HS256: 'hmac',
  HS384: 'hmac',
  HS512: 'hmac',
  RS256: 'sign'
};


/**
 * expose object
 */
var jwt = module.exports;


/**
 * version
 */
jwt.version = '0.5.6';

/**
 * Decode jwt
 *
 * @param {Object} token
 * @param {String} key
 * @param {Boolean} [noVerify]
 * @param {String} [algorithm]
 * @return {Object} payload
 * @api public
 */
jwt.decode = function jwt_decode(token, key, noVerify, algorithm) {
  // check token
  if (!token) {
    throw new Error('No token supplied');
  }
  // check segments
  var segments = token.split('.');
  if (segments.length !== 3) {
    throw new Error('Not enough or too many segments');
  }

  // All segment should be base64
  var headerSeg = segments[0];
  var payloadSeg = segments[1];
  var signatureSeg = segments[2];

  // base64 decode and parse JSON
  var header = JSON.parse(base64urlDecode(headerSeg));
  var payload = JSON.parse(base64urlDecode(payloadSeg));

  if (!noVerify) {
    if (!algorithm && /BEGIN( RSA)? PUBLIC KEY/.test(key.toString())) {
      algorithm = 'RS256';
    }

    var signingMethod = algorithmMap[algorithm || header.alg];
    var signingType = typeMap[algorithm || header.alg];
    if (!signingMethod || !signingType) {
      throw new Error('Algorithm not supported');
    }

    // verify signature. `sign` will return base64 string.
    var signingInput = [headerSeg, payloadSeg].join('.');
    if (!verify(signingInput, key, signingMethod, signingType, signatureSeg)) {
      throw new Error('Signature verification failed');
    }

    // Support for nbf and exp claims.
    // According to the RFC, they should be in seconds.
    if (payload.nbf && Date.now() < payload.nbf*1000) {
      throw new Error('Token not yet active');
    }

    if (payload.exp && Date.now() > payload.exp*1000) {
      throw new Error('Token expired');
    }
  }

  return payload;
};


/**
 * Encode jwt
 *
 * @param {Object} payload
 * @param {String} key
 * @param {String} algorithm
 * @param {Object} options
 * @return {String} token
 * @api public
 */
jwt.encode = function jwt_encode(payload, key, algorithm, options) {
  // Check key
  if (!key) {
    throw new Error('Require key');
  }

  // Check algorithm, default is HS256
  if (!algorithm) {
    algorithm = 'HS256';
  }

  var signingMethod = algorithmMap[algorithm];
  var signingType = typeMap[algorithm];
  if (!signingMethod || !signingType) {
    throw new Error('Algorithm not supported');
  }

  // header, typ is fixed value.
  var header = { typ: 'JWT', alg: algorithm };
  if (options && options.header) {
    assignProperties(header, options.header);
  }

  // create segments, all segments should be base64 string
  var segments = [];
  segments.push(base64urlEncode(JSON.stringify(header)));
  segments.push(base64urlEncode(JSON.stringify(payload)));
  segments.push(sign(segments.join('.'), key, signingMethod, signingType));

  return segments.join('.');
};

/**
 * private util functions
 */

function assignProperties(dest, source) {
  for (var attr in source) {
    if (source.hasOwnProperty(attr)) {
      dest[attr] = source[attr];
    }
  }
}

function verify(input, key, method, type, signature) {
  if(type === "hmac") {
    return (signature === sign(input, key, method, type));
  }
  else if(type == "sign") {
    return crypto.createVerify(method)
                 .update(input)
                 .verify(key, base64urlUnescape(signature), 'base64');
  }
  else {
    throw new Error('Algorithm type not recognized');
  }
}

function sign(input, key, method, type) {
  var base64str;
  if(type === "hmac") {
    base64str = crypto.createHmac(method, key).update(input).digest('base64');
  }
  else if(type == "sign") {
    base64str = crypto.createSign(method).update(input).sign(key, 'base64');
  }
  else {
    throw new Error('Algorithm type not recognized');
  }

  return base64urlEscape(base64str);
}

function base64urlDecode(str) {
  return Buffer.from(base64urlUnescape(str), 'base64').toString();
}

function base64urlUnescape(str) {
  str += new Array(5 - str.length % 4).join('=');
  return str.replace(/\-/g, '+').replace(/_/g, '/');
}

function base64urlEncode(str) {
  return base64urlEscape(Buffer.from(str).toString('base64'));
}

function base64urlEscape(str) {
  return str.replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
}
});

var jwtSimple = jwt_1;

const db = uniCloud.database();
async function validateToken(token) {
	const userFromToken = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString());
	const userInDB = await db.collection('user').where(userFromToken).get();
	if (userInDB.data.length !== 1) {
		return {
			code: -1,
			errCode: 'TOKEN_INVALID',
			msg: '查无此人'
		}
	}
	const userInfoDB = userInDB.data[0];
	let userInfoDecode;

	userInfoDecode = jwtSimple.decode(token, userInfoDB.tokenSecret);

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

var validateToken_1 = {
	validateToken
};

const {
	validateToken: validateToken$1
} = validateToken_1;

const db$1 = uniCloud.database();
const defaultMemberInfo = {
	id_type: '',
	id_card: '',
	name: '',
	phone: '',
	age: '',
	province: '',
	city: '',
	area: '',
	address: '',
	sex: -1,
    native: 2,
	regtime: ''
};

const defaultRecordInfo = {
	id_type: '',
	status: '',
	// 来自地区 {country: 0, province: 0, city: 0, district: 0, street: ""}
	from_address: null,
	from_hb: 0,
	from_wh: 0,
	// type:交通工具类型 0列车、1自驾车牌号、2公共汽车 car_plate: 交通工具信息
	traffic: null,
	temperature: '',
	// 现住址 {country: 0, province: 0, city: 0, district: 0, street: ""}
	check_in_address: null,
	check_in_time: '',
	// 0普通、1居家隔离、2发烧、3疑似、4确诊、5死亡， 每个状态对应的发生时间，如隔离时间、确诊时间、死亡时间
	body_status: 0,
	// 是否接触过确诊人员、对方姓名和联系方式 {status: 0, name: "", contact: ""}, // 0没有, 1有，
	contact_virus: null,
	contact_like_virus: null,
	contact_like_virus_region: null,
    access: null,
	comment: '',
	create_time: 0
};

var main = async (event = {}, context) => {
	let operator_username = event.operator_username;

	if (!operator_username) {
		let validateResult;
		try {
			validateResult = await validateToken$1(event.token);
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
		operator_username = validateResult.username;
	} else {
		const userCollection = db$1.collection('user');
		let checkOperatorResult = await userCollection.where({
			username: operator_username
		}).get();
		if (checkOperatorResult.data && checkOperatorResult.data.length === 0) {
			return {
				code: -4,
				errCode: 'OPERATOR_ID_INVALID',
				msg: '操作员信息无效，请重新扫码录入'
			}
		}
	}

	const currentTime = new Date().toISOString();

	if (event.check_in_time) {
		event.check_in_time = new Date(event.check_in_time).toISOString();
	}

	if (event.body_status && event.body_status.time) {
		event.body_status.time = new Date(event.body_status.time).toISOString();
	}

	// 测试数据
	// event = {
	// 	id_type: 0,
	// 	id_card: '341222198008086001',
	// 	name: '测试姓名',
	// 	phone: '18811118888',
	// 	age: 40,
	// 	sex: 2,
	// 	// 来自地区 {country: 0, province: 0, city: 0, district: 0, street: ""}
	// 	from_address: {
	// 		country: 0,
	// 		province: 0,
	// 		city: 0,
	// 		district: 0,
	// 		street: "测试地址"
	// 	},
	// 	from_hb: 0,
	// 	from_wh: 0,
	// 	// type:交通工具类型 0列车、1自驾车牌号、2公共汽车 car_plate: 交通工具信息
	// 	traffic: {
	// 		type: 0,
	// 		car_plate: 'K123'
	// 	},
	// 	temperature: '36.5',
	// 	// 现住址 {country: 0, province: 0, city: 0, district: 0, street: ""}
	// 	check_in_address: {
	// 		country: 0,
	// 		province: 0,
	// 		city: 0,
	// 		district: 0,
	// 		street: "测试地址"
	// 	},
	// 	check_in_time: currentTime,
	// 	// 0普通、1居家隔离、2发烧、3疑似、4确诊、5死亡， 每个状态对应的发生时间，如隔离时间、确诊时间、死亡时间
	// 	body_status: {
	// 		status: 0,
	// 		time: currentTime
	// 	},
	// 	// 是否接触过确诊人员、对方姓名和联系方式 {status: 0, name: "", contact: ""}, // 0没有, 1有，
	// 	contact_virus: {
	// 		status: 1,
	// 		name: "",
	// 		contact: ""
	// 	},
	// 	contact_like_virus: {
	// 		status: 1,
	// 		name: "",
	// 		contact: ""
	// 	},
	// 	contact_like_virus_region: {
	// 		status: 1,
	// 		name: "",
	// 		contact: ""
	// 	},
	// 	comment: '测试额外信息'
	// }
	const memberCollection = db$1.collection('member');
	const recordCollection = db$1.collection('member_list');
	// TODO 操作记录更新
	// const oprateCollection = db.collection('member_opera_history')

	// TODO 操作员id
	// const operator_id = ''

	const phone = event.phone;

	const memberInfo = {};
	const recordInfo = {};

	// member
	Object.keys(defaultMemberInfo).forEach(key => {
		if (key in event && event[key] !== '') {
			memberInfo[key] = event[key];
		}
	});
	if (event.from_address) {
		memberInfo.province = event.from_address.province;
		memberInfo.city = event.from_address.city;
		memberInfo.area = event.from_address.district;
		memberInfo.address = event.from_address.street;
	}

	// record
	Object.keys(defaultRecordInfo).forEach(key => {
		if (key in event) {
			recordInfo[key] = event[key];
		}
	});
	recordInfo.create_time = currentTime;


	if (!memberInfo.name) {
		return {
			code: -1,
			msg: '姓名不能为空'
		}
	}
	if (!memberInfo.phone) {
		return {
			code: -1,
			msg: '联系电话不能为空'
		}
	}

	// 没有事务，无法回滚。只能一步到底

	try {
		const memberInDb = await memberCollection.where({
			phone
		}).get();

		let memberUpdateResult,
			member_id;
		if (memberInDb.data && memberInDb.data.length === 0) {
			memberUpdateResult = await memberCollection.add(Object.assign({
				regtime: currentTime,
				status: 0
			}, memberInfo));
			member_id = memberUpdateResult.id;
		} else {
			memberUpdateResult = await memberCollection.doc(memberInDb.data[0]._id).update(memberInfo);
			member_id = memberInDb.data[0]._id;
		}

		const recordUpdateResult = await recordCollection.add({
			...recordInfo,
			member_id,
			operator_username
		});

		// TODO 操作记录更新
		// const operateInfo = {
		// 	member_id,
		// 	status: 0,
		// 	create_time: currentTime
		// }

		return {
			code: 0,
			msg: '记录添加成功'
		}
	} catch (e) {
		return {
			code: -2,
			msg: '记录添加失败'
		}
	}
};

var addMember = {
	main: main
};

exports.default = addMember;
exports.main = main;
