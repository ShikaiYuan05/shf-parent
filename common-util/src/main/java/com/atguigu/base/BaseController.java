package com.atguigu.base;

import org.springframework.ui.Model;

public class BaseController {
    public static final String PAGE_SUCCESS = "common/successPage";

    public String successPage(String messagePage, Model model) {
        model.addAttribute("messagePage", messagePage);
        return PAGE_SUCCESS;
    }
}
