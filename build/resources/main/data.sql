-- =====================================================
-- 초기화: 기존 데이터 삭제 후 재삽입
-- =====================================================
DELETE FROM ext_file_policy;

-- =====================================================
-- 🔹 1. 고정 확장자 (FIXED)
-- UI에는 리스트로 표시되지만 아직 체크되지 않음
-- → px_status = 'N' (Uncheck)
-- → px_status = 'Y' (Check)
-- → is_active = 0 (차단 대기 상태)
-- =====================================================
INSERT INTO ext_file_policy (name, type, px_status, cs_add_status, is_active, created_ip, note)
VALUES
('bat', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('cmd', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('com', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('cpl', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('exe', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('scr', 'FIXED', 'N', 'N', 0, 'SYSTEM', null),
('js',  'FIXED', 'N', 'N', 0, 'SYSTEM', null);