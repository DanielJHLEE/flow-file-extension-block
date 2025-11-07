🧱 **Flow File Extension Block**  
📄 **파일 확장자 차단 시스템 (File Extension Block System)**

📘 **프로젝트 개요**

Spring Boot 기반의 파일 확장자 차단 관리 시스템입니다.  
고정 확장자(FIXED)와 커스텀 확장자(CUSTOM)를 구분하여 관리하며,  
커스텀 확장자는 사용자 IP 기반으로 등록 이력을 SHA-256 해시 암호화하여 저장합니다.

⚙️ **주요 기능**    
🔹 고정 확장자 관리	사전에 정의된 확장자에 대한 차단 여부 설정 (체크박스)  
🔹 커스텀 확장자 관리	사용자가 직접 확장자 추가·삭제 (최대 200개)  
🔹 IP 해시 기록	커스텀 확장자 등록 시 IP 암호화 저장 (SYSTEM 예외 처리)  

## 기술 스택
- Java 21 (toolchain 설정)
- Spring Boot 3.4.x
- Spring Data JPA (MySQL)
- Thymeleaf
- Gradle

## 요구사항(Prerequisites)
- JDK 21
- MySQL (또는 JDBC 지원하는 DB)
- Windows: PowerShell, bash 사용 권장 (레포지토리에 포함된 gradlew.bat 사용 가능)

**☁️ 실제 배포 환경 (Production Environment)**
항목	내용
- 플랫폼	Render: (무료 Web Service 플랜)
- 배포 URL:	🔗 https://flow-file-extension-block.onrender.com
- 서버 포트	8080 (Spring Boot 기본 포트)
- 런타임 환경	Java 21 (Eclipse Temurin JDK, Gradle Toolchain 사용)
- 프레임워크	Spring Boot 3.4.11
- 빌드 방식	Gradle 빌드 후 JAR 실행 (java -jar build/libs/file-extension-block-0.0.1-SNAPSHOT.jar)
- DB 환경	H2 In-Memory DB (jdbc:h2:mem:testdb)
- JPA 설정
	- spring.jpa.hibernate.ddl-auto=update
	- spring.sql.init.mode=always
- 배포 프로세스	GitHub → Render Auto Deploy (main 브랜치 push 시 자동 빌드/배포)
- 서버 초기화 시 로직
- H2 테이블 자동 생성
- 기본 확장자 정책(FIXED) 자동 등록
- index.html 정적 페이지 렌더링
- 로그 레벨  org.hibernate.SQL=debug / org.hibernate.type.descriptor.sql=trace
- 보안 고려
	- 위험 확장자 리스트 YAML에서 관리
	- 사용자 IP SHA-256 해시 처리 저장

-----------------------------------------------------------------------------------------------------

**🧠 상태값 정의(확장자 차단 로직)**
구분	**px_status** / **cs_add_status**  /**is_active**

**(고정 확장자&활성화여부)**
1. px_status=**Y**&is_active=**0**	체크됨 :  **대기**
2. px_status=**Y**&is_active=**1** 체크됨 : **활성화(차단활성화)**
3. px_status=**Y**&is_active=**2**	체크됨 :  **비활성화**
4. px_status=**N**&is_active=**0**	언체크 :  **대기**

**(커스텀 확장자&활성화여부)**  
5. cs_add_status=**Y**&is_active=**0** 추가 : **대기**  
6. cs_add_status=**Y**&is_active=**1** 추가 : **활성화(차단활성화)**  
7. cs_add_status=**N**&is_active=**2** 삭제 : **비활성화**    
 
**📡 API 명세**  
Method	Endpoint	설명  
**GET**	/api/ext-files	전체 확장자 목록 조회  
**GET**	/api/ext-files/fixed	고정 확장자 목록 조회  
**GET**	/api/ext-files/custom	커스텀 확장자 목록 조회  
**PUT**	/api/ext-files/{id}/status	고정 확장자 상태 변경  
**POST**	/api/ext-files/custom	커스텀 확장자 추가  
**PATCH**	/api/ext-files/custom/{id}	커스텀 확장자 비활성화  
**DELETE**	/api/ext-files/custom/{id}	커스텀 확장자 완전 삭제    

**🧩 빠른 시작 (Bash / macOS / Linux)**  
1️⃣ 프로젝트 루트로 이동

**cd ./file-extension-block**


2️⃣ 애플리케이션 실행 (개발용) 

**./gradlew bootRun**


3️⃣ 또는 빌드 후 실행  
**./gradlew clean build**  

# 생성된 jar 위치: build/libs/<프로젝트>-<버전>.jar
**java -jar build/libs/file-extension-block-0.0.1-SNAPSHOT.jar**

**환경변수(선택)**
- DB 사용자명: DB_USERNAME (기본값: root)
- DB 비밀번호: DB_PASSWORD (기본값: 9181)

**예시**: PowerShell에서 일시적으로 설정 후 실행

```**powershell**
$env:DB_USERNAME = 'root';  
$env:DB_PASSWORD = '9181';
```
**.\gradlew.bat bootRun**

## 설정
- 애플리케이션 설정 파일: `file-extension-block/src/main/resources/application.yaml`
	- 기본 포트: 8080
	- datasource 설정(예: URL, username, password)
	- `security.disallowed-extensions`에 위험 확장자 목록이 포함되어 있음

## 프로젝트 구조 (핵심 경로)
- `file-extension-block/src/main/java/com/flow/file_extension_block/`
	- `domain/ext_file/`
		- `controller/` - 요청을 처리하는 컨트롤러xx
		- `dto/` - 데이터 전송 객체
		- `entity/` - JPA 엔티티
		- `repository/` - Spring Data JPA 리포지토리
		- `service/` - 비즈니스 로직
		- `validator/` - 확장자 검증 등
	- `global/config/` - 전역 설정 (예: WebConfig, SecurityProperties)
	- `global/exception/` - 공통 예외 처리

- `file-extension-block/src/main/resources/static/` - 정적 자원 (css/js)
- `file-extension-block/src/main/resources/templates/extension/` - Thymeleaf 템플릿 (extension_list.html)


**🧩 개발 시 고려사항 및 문제해결 과정**

**(느낀점)**  
처음에는 간단한 UI입력 창 통신 주고받으면서 그리면 되겠구나라고 생각했지만
생각보다 고려해야할 사항이나 점검해야할 부분들이 많아서 생각을 많이 하게 되었던 과제이자
짧은 프로젝트였던 것 같습니다.

처음에는 왜 '전송'버튼 폼 형식이 없지? 라고 생각했지만 곰곰히 생각해보고
도달 해보았을 때 아 이건 폼 '전송'하기 이전에 데이터들을 유효한지 또는 올바른 값인지에 대해서 1차적으로 점검하고

백엔드에서 처리할 때 데이터들의 보안성, 안전성에 대해 많은 고민을 가졌던 시간이였습니다.

그래도 나름 생각했던 것들을 정리해보았습니다.

**1. 고정 확장자(기본 정책) 유지 전략**  
기본 확장자(exe, sh 등)는 항상 시스템 초기 로딩 시 DB에 반영되어야 하며, 새로고침이나 재배포 후에도 유지되어야 한다고 판단.

H2(in-memory DB) 사용 시 매번 데이터가 초기화되는 문제를 고려하여,
초기 데이터 로딩(sql init) 설정을 추가 (spring.sql.init.mode=always)하여 기본값이 자동 반영되도록 처리.

**2. 커스텀 확장자 중복 등록 방지**  
사용자가 같은 확장자를 반복 등록할 수 없도록 하기 위해 findByName()으로 중복 체크를 수행하고, 이미 존재할 경우 GlobalHandlerException에서 전역으로 처리.
프론트에 데이터를 전달했을 때, 페이지 렌더링 오류에 대해서 다시 한번 점검.

문제 상황 예시
sh를 추가한 뒤 다시 sh 추가 시 → 예외 발생
대소문자 구분을 방지하기 위해 .equalsIgnoreCase() 또는 DB unique constraint 활용

**3. 보안 고려 – IP 저장 방식**  
커스텀 확장자 등록 시, 사용자의 IP를 평문으로 저장하는 것은 위험하다고 판단.
SHA-256 해시 알고리즘을 적용해 IP를 해시 형태로 저장하도록 구현.
이를 통해 개인정보 노출을 방지하고, 로그에는 해시의 일부만 표시하도록 처리 (substring(0,12)).

**4. 커스텀 확장자 상태 관리 로직 정립**  
과제 요구사항에 따라 px_status(차단 여부)와 is_active(활성 상태)의 조합으로 상태를 표현해야 함.
이를 명확히 구분하기 위해 아래와 같이 매핑 정의:  

**px_status**	**is_active**	**의미**
Y	0	체크만 된 대기중
Y	1	체크 + 실제 차단 적용
N	0	해제된 대기중
N	2	체크 해제 + 비활성화

이 로직을 updateFixedStatus() 내부에 명시적으로 분기 처리하여 안정성 확보.

**5. 커스텀 확장자 최대 200개 제한**  
무분별한 추가를 방지하기 위해 DB 쿼리(countByType)로 현재 등록된 커스텀 개수를 조회하고,
200개 이상 시 globalExceptionHandler로 응답처리 하도록 설정.

**6. 서버 배포 환경(Render)에서의 Cold Start / H2 초기화 문제**  
Render 무료 플랜의 cold start latency 문제로, 서버 재시작 시 첫 API 호출이 실패하는 현상 발생.  
이를 해결하기 위해 application-render.yml에서

spring.jpa.hibernate.ddl-auto=update  
spring.sql.init.mode=always  
설정을 명시하여 재시작 시 DB 구조 및 초기 데이터가 자동 보존되도록 처리.
첫 API 호출 시 대기 시간을 고려하여 클라이언트가 재시도할 수 있도록 설계.

**7. 정적 리소스(index.html)와 API 충돌 이슈**
/ 경로의 index.html과 /api/... 경로의 REST API가 공존하면서,
초기에는 /api 요청이 정적 리소스로 매핑되어 500 오류 발생.

이를 해결하기 위해 Controller 경로를 /api/ext-files로 명확히 분리하고,
정적 리소스 우선순위를 낮춰 충돌을 방지.

**8. 예외 메시지 설계 (가독성과 디버깅 편의성)**
API 호출 시 발생하는 예외를 단순히 500으로 반환하는 대신,  
사용자와 개발자 모두 이해하기 쉽게 한글 기반의 상세 메시지를 작성.  

예: "이미 존재하는 확장자입니다: sh", "CUSTOM 타입만 수정 가능합니다."

**9. 배포 검증 및 테스트**
Render 배포 후 /api/ext-files 호출 시 500 오류 발생 → 내부 로그를 통해 Repository Bean 정상 등록 여부 확인.

원인은 초기 Hibernate 세션 활성화 타이밍 문제로,
실제 배포 환경에서는 첫 요청 지연 후 정상 작동 확인.

✅ 핵심 요약
단순히 CRUD 구현에 그치지 않고,
**보안(해시처리) + 예외처리 + 상태관리 로직 + 배포 안정성까지**
고려하여 확장성 있는 구조로 개발.




