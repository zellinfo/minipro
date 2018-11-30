package com.jycx.minipro.web

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Created by fengym on 2018/11/29.
 */
@Controller
@Slf4j
class MiniProgController {
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    def home() throws IOException {
        return "index"
    }
}
