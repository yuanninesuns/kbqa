package org.horizon.exception;

import org.horizon.common.security.auth.ajax.AjaxLoginProcessingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sang on 2018/1/2.
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) {
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        Map<String, String> map = new HashMap<>();
        map.put("status", "error");
        if (e instanceof DataIntegrityViolationException) {
            map.put("msg", "该角色尚有关联的资源或用户，删除失败!");
        }else{
            logger.error("CustomExceptionResolver..", e);
            map.put("msg", "操作失败!");
        }
        mv.addAllObjects(map);
        return mv;
    }
}
