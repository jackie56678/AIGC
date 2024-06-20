import request from '@/utils/request'

export function list() {
    return request({
      url: '/ocr/list',
      method: 'get'
    })
}

// export function add(report,date) {
//   return request({
//     url: '/ocr/add',
//     method: 'post',
//     data: {
//         report:report,
//         date:date
//     }
//   });
// }

export function list2(hid) {
    return request({
      url: '/ocr/list',
      method: 'get',
      params:{
        hid:hid
      }
    })
}