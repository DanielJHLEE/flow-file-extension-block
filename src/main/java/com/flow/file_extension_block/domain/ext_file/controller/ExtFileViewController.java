package com.flow.file_extension_block.domain.ext_file.controller;

import com.flow.file_extension_block.domain.ext_file.service.ExtFilePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 확장자 뷰 컨트롤러
 * - 확장자 관리 화면 렌더링
 **/
@Controller
@RequiredArgsConstructor
public class ExtFileViewController {

    private final ExtFilePolicyService extensionService;

    @RequestMapping("/api/extensions")
    public String viewExtensions(Model model) {
        model.addAttribute("fixedList", extensionService.findByType("FIXED"));
        model.addAttribute("customList", extensionService.findByType("CUSTOM"));
        return "extension/extension_list"; // templates/extension/extension_list.html
    }
}
