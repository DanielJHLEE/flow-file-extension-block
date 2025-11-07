package com.flow.file_extension_block.domain.ext_file.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flow.file_extension_block.domain.ext_file.dto.ExtFilePolicyDto;
import com.flow.file_extension_block.domain.ext_file.entity.ExtFilePolicyEntity;
import com.flow.file_extension_block.domain.ext_file.service.ExtFilePolicyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ext-files")
@RequiredArgsConstructor
public class ExtFilePolicyController {

    private final ExtFilePolicyService extensionService;

    /**
     * 전체 확장자 목록 조회
     * - 고정 + 커스텀 모두 포함
     * GET /api/ext-files
     */
    @GetMapping
    public List<ExtFilePolicyEntity> getAllExtensions() {
        return extensionService.findAll();
    }

    /**
     * 고정 확장자 목록 조회
     * GET /api/ext-files/fixed
     */
    @GetMapping("/fixed")
    public List<ExtFilePolicyEntity> getFixedExtensions() {
        return extensionService.findByType("FIXED");
    }

    /**
     * 커스텀 확장자 목록 조회
     * GET /api/ext-files/custom
     */
    @GetMapping("/custom")
    public List<ExtFilePolicyEntity> getCustomExtensions() {
        return extensionService.findByType("CUSTOM");
    }

    /**
     * 단일 확장자 상세 조회
     * GET /api/ext-files/{id}
     */
    @GetMapping("/{id}")
    public ExtFilePolicyEntity getExtensionById(@PathVariable Long id) {
        return extensionService.findById(id);
    }

    /**
     * 고정 확장자 상태 변경 (체크 / 언체크)
     * PUT /api/ext-files/{id}/status
     * ?pxStatus=Y&isActive=1
     * ex) px_status='Y', is_active=1, Type=FIXED/CUSTOM
     */
    @PutMapping("/{id}/status")
    public void updateFixedStatus(
            @PathVariable Long id,
            @RequestParam char pxStatus,
            @RequestParam int isActive
    ) {
        extensionService.updateFixedStatus(id, pxStatus, isActive);
    }

    /**
     * 고정 확장자 차단 확정 (Y/1, N/2)
     * - 실제 차단 적용 시 사용
     */
    @PostMapping("/apply")
    public ResponseEntity<String> applyAllExtensions(
            @RequestBody List<ExtFilePolicyDto.ExtFilePolicyRequestDto> updates
    ) {
        extensionService.applyAllExtensions(updates);
        return ResponseEntity.ok("전체 저장 완료");
    }

    /**
     * 커스텀 확장자 추가
     * POST /api/ext-files/custom
     * - ip는 프론트에서 fetch 시 클라이언트IP나 더미로 전달 가능
     *      ex) name=sh, ip=192.168.0.10 -> 암호화
     *   cs_add_status='Y' or 'N'
     *   is_active=0(대기),1(활성),2(비활성)
     *   type=FIXED or CUSTOM
     */
    @PostMapping("/custom")
    public ExtFilePolicyEntity addCustomExtension(
            @Valid @RequestBody ExtFilePolicyDto.ExtFilePolicyRequestDto requestDto
    ) {
        return extensionService.addCustomExtension(
            requestDto.getName(),
            requestDto.getIp(),
            requestDto.getCsAddStatus(),
            requestDto.getIsActive()
        );
    }
    
    /**
     * 커스텀 확장자 삭제
     * DELETE /api/ext-files/custom/{id}
     * - X 버튼 눌렀을 때 해당 id update
     * - 고정 확장자는 삭제 불가
     * - 커스텀 확장자만 삭제 가능
     * - cs_add_status='Y' or 'N'
     * - is_active=0(대기),1(활성),2(비활성)
     * - type=FIXED or CUSTOM
     */
    @PatchMapping("/custom/{id}")
    public void deleteCustomExtension(
            @PathVariable Long id, 
            @RequestParam String type,  
            @RequestParam(required = false) Integer isActive,
            @RequestParam char csAddStatus
    ) {
        extensionService.deleteCustomExtension(id, type, isActive, csAddStatus);
    }

    /**
     * 커스텀 확장자 완전 삭제
     * DELETE /api/ext-files/custom/{id}
     * - DB에서 완전 삭제
     */
    @DeleteMapping("/custom/{id}")
    public void deleteCustomExtension(@PathVariable Long id) {
        extensionService.deleteCustomExtension(id);
    }
}
