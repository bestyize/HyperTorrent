package com.xunlei.download.provider


/**
 * @author : 亦泽
 * @date : 2023/3/14
 * @email : zhangrui10@bilibili.com
 */
class TaskInfo {
    var taskId: Long = 0
    var torrentFilePath: String = ""
    var taskState: Int = 0
}

enum class TaskState(val state: Int) {
    INIT(0),

}