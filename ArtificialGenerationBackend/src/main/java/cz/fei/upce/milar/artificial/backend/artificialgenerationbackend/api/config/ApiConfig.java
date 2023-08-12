package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.config;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.interceptors.OperationInterceptor;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Autowired
    private TempFileService tempFileService;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OperationInterceptor(tempFileService))
                .addPathPatterns("/api/v1/scenes/getGeneratedScene/**");

        registry.addInterceptor(new OperationInterceptor(tempFileService))
                .addPathPatterns("/api/v1/models/getParsedModel/**");
    }
}
