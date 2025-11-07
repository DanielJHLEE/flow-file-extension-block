# Flow File Extension Block

간단하고 깔끔한 설명서입니다. 이 저장소는 파일 확장자 차단(File Extension Block) 기능을 구현한 Spring Boot 애플리케이션을 포함합니다.

## 한줄 요약
특정 파일 확장자의 업로드/전송을 차단하도록 구성된 Spring Boot 기반 백엔드 예제 프로젝트입니다. 고정(FIXED) 확장자와 커스텀(CUSTOM) 확장자를 구분해 관리합니다.

## 주요 기능
- 확장자 화이트/블랙리스트 관리
- 고정(FIXED) 확장자와 커스텀(CUSTOM) 확장자 구분
- 커스텀 확장자 등록 시 IP 기반 이력 관리
- Thymeleaf 템플릿을 이용한 간단한 UI (extension_list)

## 기술 스택
- Java 21 (toolchain 설정)
- Spring Boot 3.4.x
- Spring Data JPA (MySQL)
- Thymeleaf
- Gradle

## 요구사항(Prerequisites)
- JDK 21
- MySQL (또는 JDBC 지원하는 DB)
- Windows: PowerShell 사용 권장 (레포지토리에 포함된 gradlew.bat 사용 가능)

## 빠른 시작(Windows / PowerShell)
1. 프로젝트 루트로 이동:

```powershell
cd .\file-extension-block
```

2. 애플리케이션 실행(개발용):

```powershell
.\gradlew.bat bootRun
```

3. 또는 빌드 후 실행:

```powershell
.\gradlew.bat clean build
# 생성된 jar 위치: build\libs\<프로젝트>-<버전>.jar
java -jar build\libs\file-extension-block-0.0.1-SNAPSHOT.jar
```

환경변수(선택)
- DB 사용자명: DB_USERNAME (기본값: root)
- DB 비밀번호: DB_PASSWORD (기본값: 9181)

예시: PowerShell에서 일시적으로 설정 후 실행

```powershell
$env:DB_USERNAME = 'root'; $env:DB_PASSWORD = '9181'; .\gradlew.bat bootRun
```

## 설정
- 애플리케이션 설정 파일: `file-extension-block/src/main/resources/application.yaml`
	- 기본 포트: 8080
	- datasource 설정(예: URL, username, password)
	- `security.disallowed-extensions`에 위험 확장자 목록이 포함되어 있음

## 프로젝트 구조 (핵심 경로)
- `file-extension-block/src/main/java/com/flow/file_extension_block/`
	- `domain/ext_file/`
		- `controller/` - 요청을 처리하는 컨트롤러
		- `dto/` - 데이터 전송 객체
		- `entity/` - JPA 엔티티
		- `repository/` - Spring Data JPA 리포지토리
		- `service/` - 비즈니스 로직
		- `validator/` - 확장자 검증 등
	- `global/config/` - 전역 설정 (예: WebConfig, SecurityProperties)
	- `global/exception/` - 공통 예외 처리

- `file-extension-block/src/main/resources/static/` - 정적 자원 (css/js)
- `file-extension-block/src/main/resources/templates/extension/` - Thymeleaf 템플릿 (extension_list.html)

## 테스트
테스트 실행:

```powershell
.\gradlew.bat test
```

## 참고 및 개발 메모
- `file-extension-block/HELP.md`에는 패키지명 관련 안내(원래 패키지명과 실제 사용 패키지명 차이)가 포함되어 있습니다.
- `application.yaml`에 샘플로 포함된 `security.disallowed-extensions` 항목을 참고하여 차단 정책을 조정하세요.

## 다음 단계 / 제안
- 로컬 DB 대신 Docker로 MySQL을 띄워 개발 환경을 일관화
- 프론트엔드와 연동 테스트 (파일 업로드 시 확장자 차단 확인)

---

필요하시면 README에 추가할 운영 배포 가이드(도커, CI 설정, 환경 별 프로파일)도 작성해 드리겠습니다.

