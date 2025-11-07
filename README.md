# Flow File Extension Block

📄 파일 확장자 차단 시스템 (File Extension Block System)
📘 프로젝트 개요

Spring Boot 기반의 파일 확장자 차단 관리 시스템입니다.

고정 확장자(FIXED)와 커스텀 확장자(CUSTOM)를 구분하여 관리하며,
커스텀 확장자는 사용자 IP 기반으로 등록 이력을 해시 암호화하여 저장합니다.

⚙️ 주요 기능
구분	설명
🔹 고정 확장자 관리	사전에 정의된 확장자에 대한 차단 여부 설정 (체크박스)
🔹 커스텀 확장자 관리	사용자가 직접 확장자 추가·삭제 (최대 200개)
🔹 IP 해시 기록	커스텀 확장자 등록 시 IP 암호화 저장 (SYSTEM 예외 처리)
🔹 상태 복원	새로고침(F5) 후에도 DB 상태 기반으로 UI 상태 자동 반영

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

🧠 상태값 정의
구분	px_status / cs_add_status	is_active	의미
대기	Y	0	체크됨 (대기중)
활성	Y	1	실제 차단 적용
비활성	N	2	체크 해제

📡 API 명세
Method	Endpoint	설명
GET	/api/ext-files	전체 확장자 목록 조회
GET	/api/ext-files/fixed	고정 확장자 목록 조회
GET	/api/ext-files/custom	커스텀 확장자 목록 조회
PUT	/api/ext-files/{id}/status	고정 확장자 상태 변경
POST	/api/ext-files/custom	커스텀 확장자 추가
PATCH	/api/ext-files/custom/{id}	커스텀 확장자 비활성화
DELETE	/api/ext-files/custom/{id}	커스텀 확장자 완전 삭제

🧩 빠른 시작 (Bash / macOS / Linux)

1️⃣ 프로젝트 루트로 이동

cd ./file-extension-block


2️⃣ 애플리케이션 실행 (개발용)

./gradlew bootRun


3️⃣ 또는 빌드 후 실행

./gradlew clean build
# 생성된 jar 위치: build/libs/<프로젝트>-<버전>.jar
java -jar build/libs/file-extension-block-0.0.1-SNAPSHOT.jar

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



