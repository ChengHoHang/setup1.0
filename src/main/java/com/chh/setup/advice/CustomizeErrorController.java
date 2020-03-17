package com.chh.setup.advice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author chh
 * @date 2020/2/3 0:18
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {
    
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml() {
        return new ModelAndView("/error.html");
    }
    
}
