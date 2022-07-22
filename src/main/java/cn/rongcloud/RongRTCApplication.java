package cn.rongcloud;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.MultipartConfigElement;
import java.util.StringJoiner;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@Slf4j
@RequestMapping("test")
@EnableScheduling
@MapperScan("cn.rongcloud.mic.*.mapper")
public class RongRTCApplication {

    public static void main(String[] args) {
        SpringApplication.run(RongRTCApplication.class, args);
        log.info("RongRTCApplication started");
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        factory.setMaxRequestSize(DataSize.ofMegabytes(500));
        return factory.createMultipartConfig();
    }

    @GetMapping("")
    public @ResponseBody
    String test() {
        StringJoiner stringJoiner = new StringJoiner("</br>");
        stringJoiner.add("RongRTC_Server发布成功！");
        return stringJoiner.toString();
    }
}
