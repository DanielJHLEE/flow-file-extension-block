package com.flow.file_extension_block.domain.ext_file.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import com.flow.file_extension_block.global.config.SecurityProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * 금지된 확장자 검증기
 * - ForbiddenExtension 어노테이션과 함께 사용
 */
@Component
@RequiredArgsConstructor
public class ForbiddenExtensionValidator implements ConstraintValidator<ForbiddenExtension, String> {

    private final SecurityProperties securityProperties;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        List<String> disallowedExtensions = securityProperties.getDisallowedExtensions();
        return !disallowedExtensions.contains(value.toLowerCase());
    }
}
