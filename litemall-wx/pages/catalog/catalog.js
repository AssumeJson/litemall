var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
  data: {
    categoryList: [],
    currentCategory: {},
    currentSubCategoryList: {},
    allSubCategoryList: {},
    allGoodsCategories: [],
    goodsAndItemNameList: {},
    allCoupons: {},
    banner: {},
    scrollLeft: 0,
    scrollTop: 0,
    goodsCount: 0,
    scrollHeight: 0,
    // 实现搜索框上移到状态栏中
    page_show:false,
    navHeight: '',
    menuButtonInfo: {},
    searchMarginTop: 0, // 搜索框上边距
    searchWidth: 0, // 搜索框宽度
    searchHeight: 0 ,// 搜索框高度
    screenHeight: 0, // 屏幕可用高度
    show: false,
    assembleList: [{
      title: '李菲儿发起了拼团'
    },
    {
      title: '赵小丽发起了拼团'
    },
    {
      title: '张璐璐璐发起了拼团'
    }
  ],
  },
  onLoad: function(options) {
    this.getCatalog();
    var systeminfo=wx.getSystemInfoSync()
    console.log(systeminfo.windowHeight)
    this.setData({
      movehight:systeminfo.windowHeight,
      movehight2:systeminfo.windowHeight-100
    })
    this.setData({
      menuButtonInfo: wx.getMenuButtonBoundingClientRect()
    })
    const { top, width, height, right } = this.data.menuButtonInfo
    wx.getSystemInfo({
      success: (res) => {
        const { statusBarHeight } = res
        const margin = top - statusBarHeight
        const { windowHeight } = res; // 获取当前屏幕可用高度
        this.setData({
          navHeight: (height + statusBarHeight + (margin * 2)),
          searchMarginTop: statusBarHeight + margin, // 状态栏 + 胶囊按钮边距
          searchHeight: height,  // 与胶囊按钮同高
          searchWidth: right - width -20,// 胶囊按钮右边坐标 - 胶囊按钮宽度 = 按钮左边可使用宽度
          screenHeight: windowHeight - (height + statusBarHeight + (margin * 2)) - 60 // 因为这里是所谓的75px 设计稿，所以1px等于2rpx
        })
      }
    });
  },
  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.getCatalog();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },
  getCatalog: function() {
    //CatalogList
    let that = this;
    wx.showLoading({
      title: '加载中...',
    });
    util.request(api.CatalogList).then(function(res) {
      that.setData({
        ads: res.data.ads,
        categoryList: res.data.categoryList,
        currentCategory: res.data.currentCategory,
        currentSubCategoryList: res.data.currentSubCategory,
        allSubCategoryList: res.data.allSubCategoryList,
        allGoodsCategories: res.data.allGoodsCategories,
        goodsAndItemNameList: res.data.goodsAndItemNameList
      });
      wx.hideLoading();
    });
    util.request(api.GoodsCount).then(function(res) {
      that.setData({
        goodsCount: res.data
      });
    });
  },
  getCurrentCategory: function(id) {
    let that = this;
    util.request(api.CatalogCurrent, {
        id: id
      })
      .then(function(res) {
        that.setData({
          currentCategory: res.data.currentCategory,
          currentSubCategoryList: res.data.currentSubCategory
        });
      });
  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  },
  switchCate: function(event) {
    var that = this;
    var currentTarget = event.currentTarget;
    if (this.data.currentCategory.id == event.currentTarget.dataset.id) {
      return false;
    }
    this.getCurrentCategory(event.currentTarget.dataset.id);
  },
  showPopup() {
    console.log("dianji");
    var that = this;
    if(that.allCoupons == null){
      util.request(api.CouponList).then(function(res){
        that.setData({
          allCoupons: res.data.list
        });
      });
    }
    this.setData({ show: true });
  },

  onClose() {
    this.setData({ show: false });
  },
  handleClick: function(event) {
    const dataset = event.currentTarget.dataset;
    const id = dataset.id;
    const name = dataset.name;

    wx.navigateTo({
      url: `/pages/goods/goods?id=${id}`
    });
  }
})