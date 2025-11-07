document.addEventListener("DOMContentLoaded", () => {
    const addBtn = document.getElementById("addCustom");
    const input = document.getElementById("customName");
    const customArea = document.querySelector(".custom-area");

    // ✅ 커스텀 확장자 추가
    addBtn.addEventListener("click", async () => {
        const name = input.value.trim();
        if (!name) return alert("확장자를 입력하세요.");

        // 클라이언트 IP 자동 감지 시도
        let clientIp = "SYSTEM"; // 기본값
        try {
            const ipRes = await fetch("https://api.ipify.org?format=json");
            const ipData = await ipRes.json();
            clientIp = ipData.ip || "SYSTEM";
        } catch (e) {
            console.warn("IP 조회 실패, SYSTEM으로 처리");
        }

        const payload = {
            name: name,
            ip: clientIp, // IP 있으면 실IP, 실패 시 SYSTEM
            csAddStatus: "Y",
            isActive: 0
        };

        addBtn.disabled = true;
        const res = await fetch("/api/ext-files/custom", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        addBtn.disabled = false;

        if (res.ok) {
            const data = await res.json();

            // DOM에 새 항목 즉시 추가 (reload 제거)
            const div = document.createElement("div");
            div.classList.add("custom-item");
            div.innerHTML = `
                <input type="checkbox" id="${data.id}" data-type="CUSTOM" checked>
                <label for="${data.id}">${data.name}</label>
                <button type="button" class="deleteBtn" data-id="${data.id}" data-type="CUSTOM">X</button>
            `;
            customArea.appendChild(div);
            input.value = "";

            // 새로 추가된 삭제버튼 이벤트 즉시 등록
            div.querySelector(".deleteBtn").addEventListener("click", handleDelete);
        } else {
            let err = {};
            try {
                err = await res.json();
            } catch {
                err.message = "서버 응답을 읽을 수 없습니다.";
            }
            alert(err.message || "추가 실패: 알 수 없는 오류");
        }
    });

    // ✅ 커스텀 확장자 PATCH 삭제 (상태 변경)
    document.querySelectorAll(".putCustomBtn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const id = btn.dataset.id;
            const type = btn.dataset.type;

            const res = await fetch(`/api/ext-files/custom/${id}?type=${type}&csAddStatus=N&isActive=2`, {
                method: "PATCH"
            });

            if (res.ok) {
                alert("삭제 완료");
                btn.parentElement.remove(); // ✅ DOM만 제거
            } else {
                alert("삭제 실패");
            }
        });
    });

    // ✅ 완전 삭제 핸들러 (함수로 분리)
    const handleDelete = async (e) => {
        const btn = e.target;
        const id = btn.dataset.id;

        const res = await fetch(`/api/ext-files/custom/${id}`, {
            method: "DELETE"
        });

        if (res.ok) {
            btn.parentElement.remove(); // ✅ 즉시 화면에서 제거
        } else {
            alert("삭제 실패");
        }
    };

    // 커스텀 삭제 버튼 이벤트 바인딩
    document.querySelectorAll(".deleteBtn").forEach(btn => {
        btn.addEventListener("click", handleDelete);
    });

    // ✅ 고정 확장자 체크박스 변경
    document.querySelectorAll("input[type=checkbox][data-type=FIXED]").forEach(cb => {
        cb.addEventListener("change", async () => {
            const id = cb.id;
            const checked = cb.checked ? "Y" : "N";
            const isActive = cb.checked ? 0 : 2;

            await fetch(`/api/ext-files/${id}/status?pxStatus=${checked}&isActive=${isActive}`, {
                method: "PUT"
            });
        });
    });
});
