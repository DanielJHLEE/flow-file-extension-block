package com.flow.file_extension_block.domain.ext_file.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flow.file_extension_block.domain.ext_file.entity.ExtFilePolicyEntity;

@Repository
public interface ExtFilePolicyRepository extends JpaRepository<ExtFilePolicyEntity, Long> {
    /**
     * 확장자명 중복 체크용
     * - 커스텀 확장자 등록 시 사용
     */
    Optional<ExtFilePolicyEntity> findByName(String name);

    /**
     * 커스텀 확장자 개수 제한 (최대 200개 체크용)
     */
    long countByType(ExtFilePolicyEntity.Type type);

    /**
     * 고정 확장자 목록 조회
     * - UI 상단용 (type = 'FIXED')
     */
    List<ExtFilePolicyEntity> findByType(ExtFilePolicyEntity.Type type);

    /**
     * 커스텀 확장자 목록 조회
     * - UI 하단용 (type = 'CUSTOM')
     */
    List<ExtFilePolicyEntity> findByTypeOrderByPostDateDesc(ExtFilePolicyEntity.Type type);

}
