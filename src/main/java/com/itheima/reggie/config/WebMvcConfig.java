package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
    /**
     * 扩展mvc框架的消息转换器
     * SpringMVC有默认的消息转换器，但是由于默认的转换器，在将java对象转成JSON数据时，
     * 将id转换成了int类型，当后端将id(雪花算法生成的ID，long类型）递交给前段时，最后三位的精度就丢失，导致ID发生改变
     * 进一步导致ID再传回后端使用时，会导致修改失败这种情况，所以现在通过自己定义的消息转化器，将id转换成String类型使用
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("修改转换器。。。");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter= new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将这个消息转换器加入到mvc框架中的转换器集合中去
        converters.add(0,messageConverter);
    }
}
