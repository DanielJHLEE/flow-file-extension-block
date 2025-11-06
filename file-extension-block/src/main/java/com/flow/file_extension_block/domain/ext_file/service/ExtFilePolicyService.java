package com.flow.file_extension_block.domain.ext_file.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flow.file_extension_block.domain.ext_file.entity.ExtFilePolicyEntity;
import com.flow.file_extension_block.domain.ext_file.repository.ExtFilePolicyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExtFilePolicyService {

    private final ExtFilePolicyRepository extensionRepository;

    /**
     * IP 해시 처리 (SHA-256)
     * - 평문 IP를 그대로 저장하지 않고, 보안용으로 해시 처리
     */
    private String hashIp(String ip) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(ip.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("IP 해시 처리 중 오류 발생", e);
        }
    }

    /**
     * 전체 확장자 목록 조회 (고정 + 커스텀)
     */
    public List<ExtFilePolicyEntity> findAll() {
        return extensionRepository.findAll();
    }

    /**
     * 커스텀 확장자 목록 조회
     */
    public List<ExtFilePolicyEntity> findByType(String type) {
        try {
            ExtFilePolicyEntity.Type enumType = ExtFilePolicyEntity.Type.valueOf(type.toUpperCase());
            return extensionRepository.findByType(enumType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 type 값입니다. (입력값=" + type + ")");
        }
    }

    /**
     * 단일 확장자 상세 조회
     */
    public ExtFilePolicyEntity findById(Long id) {
        return extensionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 확장자가 존재하지 않습니다. id=" + id));
    }

    /**
     * 고정 확장자 상태 변경 (체크 / 언체크)
     * px_status : 'Y' or 'N'
     * is_active : 0(대기중), 1(활성화), 2(비활성화)
     * type : FIXED or CUSTOM (선택적)
     */
    @Transactional
    public void updateFixedStatus(Long id, char pxStatus, Integer isActive) {
        ExtFilePolicyEntity extFile = extensionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 확장자가 존재하지 않습니다. (id=" + id + ")"));

        // 유효성 검증
        if (pxStatus != 'Y' && pxStatus != 'N') {
            throw new IllegalArgumentException("pxStatus 값은 'Y' 또는 'N'만 가능합니다. (입력값=" + pxStatus + ")");
        }

        // pxStatus와 isActive 조합 처리
        if (pxStatus == 'Y') {
            // 체크 상태
            if (isActive != null && isActive == 1) {
                // 전송됨 → 활성화(실제 차단하라는 데이터)
                extFile.setIsActive(1);
            } else {
                // 단순 체크만 → 대기중
                extFile.setIsActive(0);
            }
        } else if (pxStatus == 'N') {
            // 체크 해제 상태 -> 비활성화
            if (isActive != null && isActive == 2) {
                // 전송됨 → 비활성화
                extFile.setIsActive(2);
            } else {
                // 단순 해제만 → 대기중
                extFile.setIsActive(0);
            }
        }

        // px_status도 반영
        extFile.setPxStatus(pxStatus);
        extFile.setType(ExtFilePolicyEntity.Type.FIXED);

        extensionRepository.save(extFile);
    }

    /**
     * 커스텀 확장자 추가
     * - 중복 체크 포함
     * - 최대 200개 제한
     * - IP 해시 처리 포함
     * - csAddStatus에 따라 isActive 값 설정
     */
    @Transactional
    public ExtFilePolicyEntity addCustomExtension(String name, String ip, Character csAddStatus, Integer isActive) {
        // 1. 등록 개수 제한 (최대 200개)
        long count = extensionRepository.countByType(ExtFilePolicyEntity.Type.CUSTOM);
        if (count >= 200) {
            throw new IllegalStateException("커스텀 확장자는 최대 200개까지만 추가할 수 있습니다.");
        }

        // 2. 중복 확장자 방지
        extensionRepository.findByName(name).ifPresent(e -> {
            throw new IllegalArgumentException("이미 존재하는 확장자입니다: " + name);
        });

        // 3. 신규 확장자 생성
        ExtFilePolicyEntity policy = new ExtFilePolicyEntity();
        policy.setName(name);
        policy.setType(ExtFilePolicyEntity.Type.CUSTOM);
        policy.setCsAddStatus('Y');
        policy.setIsActive(0); // 대기중
        // IP 해시 적용
        String hashedIp = hashIp(ip);
        policy.setCreatedIp(hashedIp);

         // isActive 처리
         // 'Y'인 경우 1 또는 0, 'N'인 경우 2 또는 0 설정
        if (csAddStatus == 'Y') {
            policy.setIsActive(isActive != null && isActive == 1 ? 1 : 0);
        } else if (csAddStatus == 'N') {
            policy.setIsActive(isActive != null && isActive == 2 ? 2 : 0);
        }

        policy.setPxStatus('N'); // 기본값 'N'
        // 수정일 설정
        policy.setUpdateDate(LocalDateTime.now());

        // 보안 로그 기록
        log.info("[SECURITY] Custom extension added → name={}, ipHash={}, status={}, active={}, updateDate={}",
                name, hashedIp.substring(0, 12) + "...", csAddStatus, policy.getIsActive(), policy.getUpdateDate());

        return extensionRepository.save(policy);
    }

    /**
     * 커스텀 확장자 삭제
     */
    @Transactional
    public void deleteCustomExtension(Long id, String type, Integer isActive, char csAddStatus) {
        ExtFilePolicyEntity extFile = extensionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 확장자가 존재하지 않습니다. (id=" + id + ")"));

        // 타입 검증
        if (!"CUSTOM".equalsIgnoreCase(type) || !ExtFilePolicyEntity.Type.CUSTOM.equals(extFile.getType())) {
            throw new IllegalArgumentException("CUSTOM 타입만 수정 가능합니다. (입력값=" + type + ")");
        }

        // 상태값 검증
        if (csAddStatus != 'Y' && csAddStatus != 'N') {
            throw new IllegalArgumentException("csAddStatus 값은 'Y' 또는 'N'만 가능합니다. (입력값=" + csAddStatus + ")");
        }

        // 상태 업데이트
        extFile.setCsAddStatus(csAddStatus);

         // isActive 처리
         // 'Y'인 경우 1 또는 0, 'N'인 경우 2 또는 0 설정
        if (csAddStatus == 'Y') {
            extFile.setIsActive(isActive != null && isActive == 1 ? 1 : 0);
        } else if (csAddStatus == 'N') {
            extFile.setIsActive(isActive != null && isActive == 2 ? 2 : 0);
        }

        // 수정일 설정
        extFile.setUpdateDate(LocalDateTime.now());

        extensionRepository.save(extFile);

        log.info("[INFO] Custom extension status updated → id={}, type={}, csAddStatus={}, updateDate={}",
                id, type, csAddStatus, extFile.getUpdateDate());
    }
}
