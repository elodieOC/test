package com.mUI.microserviceUI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getErrorPath() {
        return "redirect:/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.error("Error with status code " + status + " happened. Support! Do something about it!");
        model.addAttribute("status", status);
        return "error/error";
    }
}
