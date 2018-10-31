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
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
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
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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

    public String distroySession() {
        //set Session invalid
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "/pages/login?faces-redirect=true";
    }

}
