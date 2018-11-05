package eu.itech.caas.filter;

import eu.itech.caas.service.SessionService;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a simple Webfilter for all xhtml pages
 *
 * @author jens.papenhagen
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {

    private static final Logger L = LoggerFactory.getLogger(AuthFilter.class);

    @Inject
    private SessionService sessionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String reqURI = httpServletRequest.getRequestURI();
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            
            HttpSession session = ((HttpServletRequest) request).getSession(); 
            
           
     

            //  allow user to proceed if url is index.xhtml or user logged in
            if (session != null || sessionService.getUserName() != null) {
                //user is loged in 
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/overview.xhtml?faces-redirect=true");
            } else {
                // session expired
                if (httpServletRequest.getRequestedSessionId() != null && !httpServletRequest.isRequestedSessionIdValid()) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.xhtml?state=expired");
                } else if (!reqURI.contains("javax.faces.resource") || reqURI.contains("login.xhtml")) {
                     //fast bypass for loging the Login Page. NO filter here needed
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.xhtml");
                } else {
                    chain.doFilter(httpServletRequest, response);
                }
            }

        } catch (IOException | ServletException ex) {
            L.error("Throwable a {}", ex.getMessage());
        }
    }

}
