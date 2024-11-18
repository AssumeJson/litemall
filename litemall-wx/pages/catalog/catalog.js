var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
  data: {
    tabIndex: "scroll-0",//右边瞄点项
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
    page_show: false,
    navHeight: '',
    menuButtonInfo: {},
    searchMarginTop: 0, // 搜索框上边距
    searchWidth: 0, // 搜索框宽度
    searchHeight: 0, // 搜索框高度
    screenHeight: 0, // 屏幕可用高度
    tabBarHeight: 0, // tabbar高度
    show: false,
    isSticky: false,
    currentIndex: 0,
  },
  onLoad: function (options) {
    this.getCatalog();
    this.getTabBarHeight();
    var systeminfo = wx.getSystemInfoSync()
    this.setData({
      movehight: systeminfo.windowHeight,
      movehight2: systeminfo.windowHeight - 100
    })
    this.setData({
      menuButtonInfo: wx.getMenuButtonBoundingClientRect()
    })
    const {
      top,
      width,
      height,
      right
    } = this.data.menuButtonInfo
    wx.getSystemInfo({
      success: (res) => {
        const {
          statusBarHeight
        } = res
        const margin = top - statusBarHeight
        const {
          windowHeight
        } = res; // 获取当前屏幕可用高度
        this.setData({
          navHeight: (height + statusBarHeight + (margin * 2)),
          searchMarginTop: statusBarHeight + margin, // 状态栏 + 胶囊按钮边距
          searchHeight: height, // 与胶囊按钮同高
          searchWidth: right - width - 20, // 胶囊按钮右边坐标 - 胶囊按钮宽度 = 按钮左边可使用宽度
          screenHeight: windowHeight - (height + statusBarHeight + (margin * 2)) - 30 // 因为这里是所谓的75px 设计稿，所以1px等于2rpx
        })
      }
    });
  },
  getTabBarHeight: function () {
    const systemInfo = wx.getSystemInfoSync();
    const {
      windowHeight,
      windowWidth
    } = systemInfo;

    // 获取 tabBar 配置
    const menuButtonInfo = wx.getMenuButtonBoundingClientRect();
    const customBarHeight = menuButtonInfo.bottom + menuButtonInfo.top - systemInfo.statusBarHeight;

    // 默认 tabBar 高度
    let tabBarHeight = 50; // 默认值，单位为 px

    // 如果有 tabBar 配置
    if (wx.canIUse('getMenuButtonBoundingClientRect')) {
      tabBarHeight = customBarHeight;
    } else {
      // iOS 和 Android 的默认 tabBar 高度不同
      if (systemInfo.platform === 'ios') {
        tabBarHeight = 49; // iOS 默认 tabBar 高度
      } else {
        tabBarHeight = 50; // Android 默认 tabBar 高度
      }
    }

    // 将 tabBar 高度转换为 rpx 单位
    const tabBarHeightRpx = Math.round(tabBarHeight / windowWidth * 750);

    this.setData({
      tabBarHeight: tabBarHeightRpx
    });

    console.log('tabBarHeight:', tabBarHeightRpx);
  },
  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.getCatalog();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },
  getCatalog: function () {
    //CatalogList
    let that = this;
    wx.showLoading({
      title: '加载中...',
    });
    util.request(api.CatalogList).then(function (res) {
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
    util.request(api.GoodsCount).then(function (res) {
      that.setData({
        goodsCount: res.data
      });
    });
  },
  getCurrentCategory: function (id) {
    let that = this;
    util.request(api.CatalogCurrent, {
        id: id
      })
      .then(function (res) {
        that.setData({
          currentCategory: res.data.currentCategory,
          currentSubCategoryList: res.data.currentSubCategory
        });
      });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },

  scrollToCategory: function (index) {
    const query = wx.createSelectorQuery();
    query.select('.nav .category-item').boundingClientRect(function (rects) {
      if (rects) {
        const targetRect = rects[index];
        if (targetRect) {
          this.selectComponent('.nav').scrollTo({
            scrollTop: targetRect.top - 50, // 调整 50 以适应吸顶效果
            duration: 300
          });
        }
      }
    }).exec();
  },
  switchCate: function (event) {
    const index = event.currentTarget.dataset.index;
    var that = this;
    var currentTarget = event.currentTarget;
    const offsetTop = event.target.offsetTop;

    if (this.currentIndex > index) {
      
    }

    this.setData({tabIndex: `scroll-${index}`})
    // console.log("scroll:",this.tabIndex);
    // // 滚动到指定分类
    // const query = wx.createSelectorQuery().in(this);
    // query.selectAll('.sticky-header').boundingClientRect(function (rects) {
    //   console.log("rects:", rects);
    //   if (rects) {
    //     const targetRect = rects[index];
    //     that.setData({
    //       currentIndex: index
    //     });

    //   }
    // }).exec();
    // this.getCurrentCategory(event.currentTarget.dataset.id);
  },
  showPopup() {
    var that = this;
    if (that.allCoupons == null) {
      util.request(api.CouponList).then(function (res) {
        that.setData({
          allCoupons: res.data.list
        });
      });
    }
    this.setData({
      show: true
    });
  },


  getScreenHeight: function () {
    const systemInfo = wx.getSystemInfoSync();
    this.setData({
      screenHeight: systemInfo.windowHeight
    });
  },

  onScroll: function (event) {
    const scrollTop = event.detail.scrollTop;
    const that = this;
    const offsetTop = event.target.offsetTop;

    // 创建选择器查询对象
    const query = wx.createSelectorQuery();

    // 选择所有吸顶标题
    query.selectAll('.sticky-header').boundingClientRect(function (rects) {
      if (rects) {
        console.log("rects:",rects);
        let newIndex = 0;
        for (let i = 0; i < rects.length; i++) {
          if (rects[i].top <= offsetTop) {
            newIndex = i;
          } else {
            break;
          }
        }
        if (newIndex !== that.data.currentIndex) {
          that.setData({
            currentIndex: newIndex
          });
        }
      }
    }).exec();
  },

  onClose() {
    this.setData({
      show: false
    });
  },
  handleClick: function (event) {
    const dataset = event.currentTarget.dataset;
    const id = dataset.id;
    const name = dataset.name;

    wx.navigateTo({
      url: `/pages/goods/goods?id=${id}`
    });
  }
})