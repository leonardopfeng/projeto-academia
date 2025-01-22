package br.com.lelis.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerMapperConfig {

    public Mapper mapper() {
        return DozerBeanMapperBuilder.create()
                .withMappingFiles("dozer-mapping.xml") // Ensures XML mapping is loaded
                .build();
    }
}
