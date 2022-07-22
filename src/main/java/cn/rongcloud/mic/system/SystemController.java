package cn.rongcloud.mic.system;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ApiIgnore
public class SystemController {

    @Value("${domainUrl}")
    private String domainUrl;


    private Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    UserService userService;

    @GetMapping("system")
    public ModelAndView system(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/system");
        return model;
    }


    @GetMapping("system/send")
    @ResponseBody
    public RestResult sendMessage(String msg,@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        logger.info("send message {}", msg);
        userService.sendBroadcastMessage(msg);
        return RestResult.success();
    }

    @GetMapping("video")
    public ModelAndView video(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = new ModelAndView();
        model.setViewName("/video");
        String path = request.getScheme() + "://" + domainUrl;
        String fileName = path + "/file/show?path=RC_RTC_Products2.mp4";// 文件名
        model.addObject("path", fileName);
        return model;
    }
}
