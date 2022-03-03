# 出入人员登记系统 数据库(初稿)


#### 用户表
```
user
{
    "_id": "", // string，自生成
    "guid": "", // string 用户唯一标识，注册时生成无法修改
    "username": "", // string 用户
    "password": "", // string 密码(禁止明文)
    "wx_open_id": "", // string 关联微信openid  用户code 换取
    "id_card": "", // string 身份证号，需验证符合规则
    "name": "", // string 不能为空
    "phone": "", // string 不能为空，需验证符合规则
    "age": 18, // int
    "sex": 0, // int (0女, 1男, 2未知)
    "photo": "", // string 图片url地址
    "status": 0, // int 0正常 1冻结
    "permission": 0, // int 默认2，0超级管理员, 1普通管理员, 2普通
    "create_time": 0, // 时间戳 GMT
    "create_ip": "" // 注册 ip
}
```

#### 成员表
```
member
{
    "_id": "", // string，自生成
    "guid": "", // string 用户唯一标识，添加时生成无法修改
    "id_type": 0, // int 证件类型 0 身份证, 1 护照
    "id_card": "", // string 证件号码，需验证符合规则
    "name": "", // string 不能为空
    "phone": "", // string 不能为空，需验证符合规则
    "age": 18, // int
    "sex": 0, // int (0女, 1男, 2未知)
    "photo": "", // string 图片url地址
    "address": "{country: 0, province: 0, city: 0, district: 0, street: ''}",（身份证地址信息）
    "city": "", //string (市-搜索字段)
    "area": "", //(区-搜索字段)
    "address": "", //身份证地址
    "regtime": "" //（入库时间）
}
```

#### 成员登记
```
// 新增成员登记, 需要检测 member表中是否存在 id_card，同一个人可以增加多条记录
member_list
{
	"_id": "", // string，自生成
	"id_card": 0, // 关联身份证ID
	"status": 0, // 0正常, 1删除
	"from_address": "{country: 0, province: 0, city: 0, district: 0, street: ''}", // 来自地区
	"from_hb": 0, // 默认0，没有来之湖北
	"traffic": {type: 0, car_plate: ""}, // 交通工具类型 0列车、1自驾车牌号 2公共汽车
	"temperature": 0, // 体温
	"check_in_address": "{country: 0, province: 0, city: 0, district: 0, street: ''}", // 现住址
	"check_in_time": 0, // 入住时间 时间戳 GMT
	"body_status": {status: 0, time: 0}, // int 0普通、1居家隔离、2发烧、3疑似、4确诊、5死亡， 每个状态对应的发生时间，如隔离时间、确诊时间、死亡时间
	"contact_virus": {status: 0, name: "", contact: ""}, // 0没有, 1有，是否接触过确诊人员、对方姓名和联系方式
	"contact_like_virus": {status: 0, name: "", contact: ""}, // 0没有, 1有，是否接触过疑似人员、对方姓名和联系方式
	"contact_like_virus_region": {status: 0, name: "", contact: ""}, // 0没有, 1有，是否接触过来自疫区的人员、对方姓名和联系方式
	"comment": "", // 备注
	"create_time": 0, // 时间戳 GMT
	"access": "{name: '', phone: '', comment: ''}" // 到访单位信息，联系人姓名、联系人电话、事由
}
```


#### 关键字搜索库
```
// 增加人员时需要写入备用搜索与统计
member_check_in_address
{
  "_id": "", // string，自生成
  "id_card": 0, // 关联身份证ID
  "id_member": 0, //关联新增成员登记ID
  "age": 0, // int// 缺省为0
  "sex": 0, // int (0女, 1男, 2未知)
  "xm_name":0, //姓名--搜索
  "in_country": 0, //登记地_搜索国家
  "in_province": 0, //登记地_搜索省
  "in_city": 0, //登记地_搜索市
  "in_district": 0, //登记地_搜索区
  "in_street": 0, //登记地_搜索详细地址
  "form_country": 0, //来自地_搜索国家
  "form_province": 0, //来自地_搜索省
  "form_city": 0, //来自地_搜索市
  "form_district": 0, //来自地_搜索区
  "form_street": 0, //来自地_搜索详细地址
  "create_time": 0, // 时间戳 登记时间 搜索
  "history_time": 0 //int 0本月,1三月内,2半年内,3一年内，4三年内,5超过三年。
}
```


#### 成员操作历史
```
// 增加人员时需要写入
member_opera_history
{
    "_id": "", // string，自生成
    "user_guid": "", // string 用户唯一标识
    "member_id": 0, // string 成员唯一标识
    "status": 0, // int 0新增，1删除
    "create_time": 0, // int 时间戳 GMT
    "create_ip": "" // string 当前操作ip
}
```
