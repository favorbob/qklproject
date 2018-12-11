package cc.stbl.token.innerdisc.restapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket createSysApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder().name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("cc.stbl.token.innerdisc.restapi.admin"))
                .paths(PathSelectors.ant("/admin/**"))
                .build().globalOperationParameters(parameters).groupName("管理系统");
    }

    @Bean
    public Docket createAppApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder().name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("cc.stbl.token.innerdisc.restapi.app"))
                .paths(PathSelectors.ant(PathPrefix.API_PATH +"/**"))
                .build().globalOperationParameters(parameters).groupName("App接口");
    }

    @Bean
    public Docket createDebugApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("cc.stbl.token.innerdisc.restapi"))
                .paths(PathSelectors.ant("/debug/**"))
                .build().groupName("系统调试");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("佛祖保佑，永无Bug")
                .description("南无阿弥陀佛")
                .termsOfServiceUrl("http://119.23.239.101")
                .version("1.0")
                .build();
    }
}
