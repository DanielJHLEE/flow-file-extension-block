package com.flow.file_extension_block.domain.ext_file.dto;

import com.flow.file_extension_block.domain.ext_file.entity.ExtFilePolicyEntity;
import com.flow.file_extension_block.domain.ext_file.validator.ForbiddenExtension;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 확장자 DTO
 * - 요청(ExtensionRequestDto)
 * - 응답(ExtFilePolicyDto)
 */
public class ExtFilePolicyDto {

    // 응답 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExtFilePolicyResponseDto {
        private Long id;
        private String name;
        private String type;
        private String pxStatus;
        private int isActive;

        public static ExtFilePolicyResponseDto fromEntity(ExtFilePolicyEntity e) {
            return ExtFilePolicyResponseDto.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .type(e.getType().name())
                    .pxStatus(String.valueOf(e.getPxStatus()))
                    .isActive(e.getIsActive())
                    .build();
        }
    }

    // 요청 DTO
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExtFilePolicyRequestDto {
        @NotBlank(message = "확장자명은 필수입니다.")
        @Size(max = 20, message = "확장자명은 최대 20자까지 입력 가능합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "확장자명은 영문과 숫자만 허용됩니다.")
        @ForbiddenExtension // 커스텀 검증
        private String name;

        @NotBlank(message = "IP 정보가 누락되었습니다.")
        private String ip;

        private Character csAddStatus; // 'Y' or 'N'
        private Integer isActive;      // 0, 1, 2
    }
}
