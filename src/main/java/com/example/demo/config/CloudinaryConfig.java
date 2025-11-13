package com.example.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfi85jz5k",
                "api_key", "826461163347336",
                "api_secret", "RhMK39PRO5C5P9ovC-p0edV0MA8",
                "secure", true
        ));
    }
}
