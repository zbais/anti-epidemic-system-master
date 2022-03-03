//我在做这个文件，我的群昵称是AHello
<template>

	  

  <view class="container">
		<image class="bg-set" src="../../static/bj4.png"></image>
		<view class="content"></view>
    <!-- <view class="tag">
			<uni-tag style="margin: 5upx 5upx;" v-for="(item,index) in tags" :key="index" @click="tagQuery(index)" :text="item.text"
			 :type="item.type"></uni-tag>
    </view>-->
    <view v-if="!members.length" class="none">暂无数据</view>
    <view class="flex-item flex-item-V uni-bg-blue">
      <uni-list>
        <uni-list-item
          v-for="(item,index) in members"
          :key="index"
          @click="openDetail(item)"
          :color="item.is_danger ? 'red':''"
          :title="item.is_danger ? `${item.name}(感染者同程)` : item.name"
          :note="item.address||'地址未填写'"
        ></uni-list-item>
      </uni-list>
    </view>
  </view>
</template>

<script>
import uniList from "@/components/uni-list/uni-list.vue";
import uniListItem from "@/components/uni-list-item/uni-list-item.vue";
import uniTag from "@/components/uni-tag/uni-tag.vue";
export default {
  components: {
    uniList,
    uniListItem,
    uniTag
  },
  data() {
    return {
      members: [],
      page: 0,
      data: []
      // tags: [{
      // 		"text": "所有",
      // 		"type": "default"
      // 	},
      // 	{
      // 		"text": "普通",
      // 		"type": "primary"
      // 	},
      // 	{
      // 		"text": "隔离",
      // 		"type": "success"
      // 	},
      // 	{
      // 		"text": "发烧",
      // 		"type": "success"
      // 	},
      // 	{
      // 		"text": "疑似",
      // 		"type": "success"
      // 	},
      // 	{
      // 		"text": "确诊",
      // 		"type": "warning"
      // 	},
      // 	{
      // 		"text": "死亡",
      // 		"type": "error"
      // 	}
      // ]
    };
  },
  onLoad() {
    this.loadData();
  },
  onPullDownRefresh() {
    this.members = [];
    this.page = 0;
    this.loadData();
  },
  onReachBottom() {
    this.loadData();
  },
  methods: {
    loadData() {
      uni.showLoading({
        title: "加载中..."
      });

      this.$cloud
        .callFunction({
          name: "member-list",
          data: {
            token: uni.getStorageSync("token"),
            page: this.page,
            length: 15
          }
        })
        .then(({ result }) => {
          uni.hideLoading();
          uni.stopPullDownRefresh();
          if (result.code === -3) {
            // 登陆无效
            uni.showModal({
              content: "登陆状态无效",
              showCancel: false,
              complete: () => {
                uni.redirectTo({
                  url: "/pages/login/login"
                });
              }
            });
            return;
          }
          if (result.code !== 0) {
            uni.showToast({
              icon: "none",
              title: result.msg
            });
            return;
          }
          this.page++;
          this.members.push(...result.data);
          // 二次检测是否是感染者
          this.members.forEach(async (v, i) => {
            if (v.is_danger) return;
            if (this.data.length === 0) {
              // 获取第三方库数据
              const getData = await this.$cloud.callFunction({
                name: "get-data",
                data: {}
              });
              this.data = getData.result.data || [];
            }
            if (
              this.data.length > 0 &&
              v.traffic.car_plate &&
              v.check_in_time
            ) {
              const findData = this.data.find(val => {
                return (
                  val.t_no
                    .toUpperCase()
                    .includes(v.traffic.car_plate.toUpperCase()) &&
                  val.t_date.includes(v.check_in_time.substring(0, 10) || "")
                );
              });
              v.is_danger = !!findData;
              // this.members[i] = v;
              this.$set(this.members, i, v);
            }
          });
        })
        .catch(err => {
          uni.hideLoading();
          uni.stopPullDownRefresh();
          uni.showToast({
            icon: "none",
            title: "数据加载失败"
          });
        });
    },
    //明细
    openDetail: function(item) {
      console.log(item);
      uni.navigateTo({
        url: `../member-detail/member-detail?item=${encodeURIComponent(
          JSON.stringify(item)
        )}`
      });
    },
    //标签搜索
    tagQuery: function(e) {}
  }
};
</script>

<style>
.tag {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  padding: 10px 10px;
}

.none {
  text-align: center;
  color: #cccccc;
  line-height: 100px;
}

.bg-set{
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    z-index: 1;
		pointer-events: none; 
		opacity: 0.3;
}
</style>
