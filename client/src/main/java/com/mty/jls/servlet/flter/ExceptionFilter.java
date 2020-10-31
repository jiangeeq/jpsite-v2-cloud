package com.mty.jls.servlet.flter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 定义 ExceptionFilter，将捕捉的异常交给异常处理的 Controller
 *
 * @author jiangpeng
 * @date 2020/10/1413:56
 */
public class ExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            request.setAttribute("exception", e);
            request.getRequestDispatcher("/filterError").forward(request, response);
        }
    }
}
