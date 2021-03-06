package eu.itech.caas.service;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionService Service
 *
 * @author jens.papenhagen
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    private static final Logger L = LoggerFactory.getLogger(SessionService.class);

    public HttpSession getSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String testUser = "TestUser";
        session.setAttribute("username", testUser);
        return session;
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public String getUserName() {
        HttpSession session = getSession();
        if (session == null) {
            return null;
        }

        return session.getAttribute("username").toString();
    }

    /**
     * this methode giveback the Ip of a request
     *
     * @return a IP-Address as String or null for altern user input
     */
    public String getIp() {
        HttpServletRequest request = getRequest();
        String xForwaredFor = request.getHeader("X-FORWARDED-FOR");
        if (xForwaredFor == null) {
            return request.getRemoteAddr();
        } else {
            //testing the user input
            if (!xForwaredFor.equals(request.getRemoteAddr())) {
                L.error("the X-FORWARDED-FOR {} is not the same as the Remote Addresse {}", xForwaredFor, request.getRemoteAddr());
                return null;
            }
        }

        return xForwaredFor;
    }

    public void distroySession() {
        HttpSession session = getSession();
        if (session == null) {
            return;
        }
        session.invalidate();
    }

}
