package com.gaspar;

import com.gaspar.util.logs.InterceptAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ConditionalOnClass(InterceptAspect.class)
@EnableAspectJAutoProxy
public class InterceptAspectAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public InterceptAspect interceptAspect() {
        return new InterceptAspect();
    }
}