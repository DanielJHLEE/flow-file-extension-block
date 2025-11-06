package com.flow.file_extension_block.domain.ext_file.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ext_file_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtFilePolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    @Size(max = 20, message = "확장자명은 최대 20자까지 입력 가능합니다.")
    private String name; // 확장자명 (최대 20자 제한)

    @Enumerated(EnumType.STRING)
    private Type type; // FIXED / CUSTOM

    private char pxStatus; // 고정 확장자 체크 상태 (Y/N)
    private char csAddStatus; // 커스텀 확장자 추가/삭제 (Y/N)
    private int isActive; // 0=대기,1=활성,2=비활성
    private String createdIp;
    private String note;

    @Column(columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime postDate;

    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private LocalDateTime updateDate;

    public enum Type {
        FIXED, CUSTOM
    }
}