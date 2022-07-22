package cn.rongcloud.mic.common.config;

import javax.servlet.annotation.WebServlet;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by sunyinglong on 2020/6/3
 */
//覆盖DispatcherServlet path
@WebServlet(urlPatterns = {"/api/*"}, loadOnStartup = 1, asyncSupported = true)
public class EndPointServlet extends DispatcherServlet {

    public EndPointServlet() {
        super(new AnnotationConfigWebApplicationContext());
    }
}
