package com.thewind.hypertorrent.config.model

import com.thewind.hypertorrent.config.model.AppConfig

/**
 * @author: read
 * @date: 2023/4/12 上午2:04
 * @description:
 */
class AppConfigResponse {
    var code: Int = -1
    var msg: String? = null
    var data: AppConfig? = null
}