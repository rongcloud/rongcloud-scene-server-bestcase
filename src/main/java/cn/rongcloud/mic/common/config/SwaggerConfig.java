package cn.rongcloud.mic.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 1. swagger配置类
 */
/*@Configuration
@EnableSwagger2*/
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig /*extends WebMvcConfigurerAdapter*/ {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("scene")
				.apiInfo(apiInfo())
				//是否开启 (true 开启  false隐藏。生产环境建议隐藏)
				//.enable(false)
				.select()
				//扫描的路径包,设置basePackage会将包下的所有被@Api标记类的所有方法作为api
				.apis(RequestHandlerSelectors.basePackage("cn.rongcloud.mic"))
				//指定路径处理PathSelectors.any()代表所有的路径
				.paths(PathSelectors.any())
				.build().globalOperationParameters(getParamterList());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				//设置文档标题(API名称)
				.title("融云融合场景接口文档")
				.description("")
				//.termsOfServiceUrl("http://localhost:8081/")
				//版本号
				.version("1.0.0")
				.build();
	}

	private List<Parameter> getParamterList(){
		ParameterBuilder header = new ParameterBuilder();
		List<Parameter> parameters 	= new ArrayList<>();
		header.name("Authorization").description("token 认证").modelRef(new ModelRef("string"))
				.parameterType("header").required(true).build();
		parameters.add(header.build());
		return parameters;
	}

	/**
	 * swagger静态资源请求配置
	 * @param registry
//	 */
/*	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		//springboot 集成swagger2.2后静态资源404，添加如下两行配置
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		super.addResourceHandlers(registry);
	}*/

}
