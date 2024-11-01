import request from '@/utils/request'

export function listGoods(query) {
  return request({
    url: '/goods/list',
    method: 'get',
    params: query
  })
}

export function deleteGoods(data) {
  return request({
    url: '/goods/delete',
    method: 'post',
    data
  })
}

export function publishGoods(data) {
  return request({
    url: '/goods/create',
    method: 'post',
    data
  })
}

export function detailGoods(id) {
  return request({
    url: '/goods/detail',
    method: 'get',
    params: { id }
  })
}

export function editGoods(data) {
  return request({
    url: '/goods/update',
    method: 'post',
    data
  })
}

export function listCatAndBrand() {
  return request({
    url: '/goods/catAndBrand',
    method: 'get'
  })
}

export function listStorage(query) {
  return request({
    url: '/storage/banner/list',
    method: 'get',
    params: query
  })
}

export function createStorage(data) {
  return request({
    url: '/storage/banner/create',
    method: 'post',
    data
  })
}

export function readStorage(data) {
  return request({
    url: '/storage/read',
    method: 'get',
    data
  })
}

export function updateStorage(data) {
  return request({
    url: '/storage/update',
    method: 'post',
    data
  })
}

export function deleteStorage(data) {
  return request({
    url: '/storage/delete',
    method: 'post',
    data
  })
}

const uploadPath = process.env.VUE_APP_BASE_API + '/storage/create'
export { uploadPath }
