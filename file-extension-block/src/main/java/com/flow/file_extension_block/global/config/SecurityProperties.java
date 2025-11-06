package com.flow.file_extension_block.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 보안 설정 프로퍼티 매핑 클래스
 * - application.yml의 security.disallowed-extensions 값을 매핑
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private List<String> disallowedExtensions;

    public List<String> getDisallowedExtensions() {
        return disallowedExtensions;
    }

    public void setDisallowedExtensions(List<String> disallowedExtensions) {
        this.disallowedExtensions = disallowedExtensions;
    }
}