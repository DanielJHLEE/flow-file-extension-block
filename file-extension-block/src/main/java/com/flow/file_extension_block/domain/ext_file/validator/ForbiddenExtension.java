package com.flow.file_extension_block.domain.ext_file.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 금지된 확장자 검증 어노테이션
 * - 보안상 위험한 확장자 등록 방지용
 * - ForbiddenExtensionValidator와 함께 사용
 */
@Documented
@Constraint(validatedBy = ForbiddenExtensionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbiddenExtension {

    String message() default "보안상 위험한 확장자는 등록할 수 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
