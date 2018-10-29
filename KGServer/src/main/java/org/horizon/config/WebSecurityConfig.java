//package org.horizon.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.horizon.common.HrUtils;
//import org.horizon.common.security.CustomCorsFilter;
//import org.horizon.common.security.RestAuthenticationEntryPoint;
//import org.horizon.common.security.auth.ajax.AjaxLoginProcessingFilter;
//import org.horizon.common.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
//import org.horizon.common.security.auth.jwt.SkipPathRequestMatcher;
//import org.horizon.common.security.auth.jwt.extractor.TokenExtractor;
//import org.horizon.service.HrService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.config.annotation.ObjectPostProcessor;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * taos 2018/03/01.
// * 修改权限认证体系，接入JWT
// */
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
//    public static final String AUTHENTICATION_URL = "/api/auth/login";
//    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
//    public static final String API_ROOT_URL = "/api/**";
//
//    @Autowired
//    HrService hrService;
//    @Autowired
//    UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
//    @Autowired
//    UrlAccessDecisionManager urlAccessDecisionManager;
//    @Autowired
//    AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(hrService).passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/index.html", "/static/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
//                        o.setAccessDecisionManager(urlAccessDecisionManager);
//                        return o;
//                    }
//                }).and().formLogin().loginPage("/index.html").loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").permitAll().failureHandler(new AuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//                httpServletResponse.setContentType("application/json;charset=utf-8");
//                PrintWriter out = httpServletResponse.getWriter();
//                StringBuffer sb = new StringBuffer();
//                sb.append("{\"status\":\"error\",\"msg\":\"");
//                if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
//                    sb.append("用户名或密码输入错误，登录失败!");
//                } else if (e instanceof DisabledException) {
//                    sb.append("账户被禁用，登录失败，请联系管理员!");
//                } else {
//                    sb.append("登录失败!");
//                }
//                sb.append("\"}");
//                out.write(sb.toString());
//                out.flush();
//                out.close();
//            }
//        }).successHandler(new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                httpServletResponse.setContentType("application/json;charset=utf-8");
//                PrintWriter out = httpServletResponse.getWriter();
//                ObjectMapper objectMapper = new ObjectMapper();
//                String s = "{\"status\":\"success\",\"msg\":" + objectMapper.writeValueAsString(HrUtils.getCurrentHr()) + "}";
//                out.write(s);
//                out.flush();
//                out.close();
//            }
//        }).and().logout().permitAll().and().csrf().disable().exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler);
//    }
//    }
