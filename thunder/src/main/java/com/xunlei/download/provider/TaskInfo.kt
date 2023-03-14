package com.xunlei.download.provider

import com.xunlei.downloadlib.parameter.XLTaskInfo


/**
 * @author : 亦泽
 * @date : 2023/3/14
 * @email : zhangrui10@bilibili.com
 */
class TaskInfo {
    var taskId: Long = 0
    var torrentFilePath: String = ""
    var magnetHash  = ""
    var xlTaskInfo: XLTaskInfo?=null
}

enum class TaskState(val state: Int) {
    INIT(0), // 未下载状态或暂停态
    DOWNLOADING(1), // 正在下载状态
    FINISH(2), // 下载完成
    COPYRIGHT(3) // 版权方要求不能下载
}