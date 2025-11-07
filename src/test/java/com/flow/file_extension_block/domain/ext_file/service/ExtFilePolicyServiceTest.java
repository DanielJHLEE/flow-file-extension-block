package com.flow.file_extension_block.domain.ext_file.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 파일 확장자 정책 서비스 테스트
 * - 커스텀 확장자 등록 및 중복 예외 검증
 */
@SpringBootTest
class ExtFilePolicyServiceTest {

    @Autowired
    private ExtFilePolicyService service;

    /**
     * addCustomExtension_shouldRegisterSuccessfully
     * 커스텀 확장자 정상 등록 테스트
     * - 신규 확장자 등록 시 DB에 저장되는지 검증
     * - IP 해시 처리 여부 확인
     */
    @Test
    void addCustomExtension_shouldRegisterSuccessfully() {
        // given
        String name = "testext";
        String ip = "127.0.0.1";

        // when
        var entity = service.addCustomExtension(name, ip, 'Y', 0);

        // then
        assertNotNull(entity.getId());
        assertEquals("testext", entity.getName());
        assertEquals('Y', entity.getCsAddStatus());
        assertEquals('0', entity.getIsActive());
        assertTrue(entity.getCreatedIp().length() > 20); // 해시 확인
    }

    /**
     * addCustomExtension_shouldThrowExceptionWhenDuplicateName
     * 중복 확장자 등록 예외 테스트
     * - 동일한 확장자명이 이미 존재할 경우 IllegalArgumentException 발생
     */
    @Test
    void addCustomExtension_shouldThrowExceptionWhenDuplicateName() {
        // given
        String name = "duplicate";
        String ip = "127.0.0.1";

        // when
        service.addCustomExtension(name, ip, 'Y', 0);

        // then
        assertThrows(IllegalArgumentException.class, () ->
                service.addCustomExtension(name, ip, 'Y', 0)
        );
    }
}
