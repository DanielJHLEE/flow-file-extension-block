-- =====================================================
-- 초기화: 기존 데이터 삭제 후 재삽입
-- =====================================================
DELETE FROM ext_policy;

-- =====================================================
-- 🔹 1. 고정 확장자 (FIXED)
-- UI에는 리스트로 표시되지만 아직 체크되지 않음
-- → px_status = 'N' (언체크)
-- → cs_add_status = 'N' (추가/삭제 무관, 기본값)
-- → is_active = 0 (차단 대기 상태)
-- =====================================================
INSERT INTO ext_policy (name, type, px_status, cs_add_status, is_active, created_ip, note)
VALUES
('bat', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('cmd', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('com', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('cpl', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('exe', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('scr', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('js',  'FIXED', 'N', 'N', 0, 'SYSTEM', null);

-- =====================================================
-- 🔹 2. 커스텀 확장자 (CUSTOM)
-- 사용자 추가 확장자 — 등록만 되어 있고 아직 차단되지 않음
-- → px_status = 'N' (체크박스 해제)
-- → cs_add_status = 'Y' (사용자 추가됨)
-- → is_active = 0 (등록 대기 상태)
-- =====================================================
INSERT INTO ext_policy (name, type, px_status, cs_add_status, is_active, created_ip, note)
VALUES
('sh', 'CUSTOM', 'N', 'Y', 0, '192.168.0.15', null),
('ju', 'CUSTOM', 'N', 'Y', 0, '192.168.0.15', null),
('ch', 'CUSTOM', 'N', 'Y', 0, '192.168.0.15', null);