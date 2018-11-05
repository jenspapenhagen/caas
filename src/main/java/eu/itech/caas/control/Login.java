package eu.itech.caas.control;

import eu.itech.caas.entity.UserAttempts;
import eu.itech.caas.service.AuthenticationService;
import eu.itech.caas.service.LoginAttempt;
import eu.itech.caas.service.SessionService;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 * simple Login Bean
 *
 * @author jens.papenhagen
 */
@Named
@ViewScoped
public class Login implements Serializable {

    @Inject
    private AuthenticationService authService;

    @Inject
    private SessionService sessionService;

    @Inject
    private LoginAttempt loginAttempt;

    private String username;

    private String password;

    public String login() {
        String ipAddress = sessionService.getIp();
        String output = "";

        //create a UserAttempts to slowdown bruteforce
        UserAttempts ua = new UserAttempts(ipAddress, 3, LocalDateTime.now());
        loginAttempt.add(ipAddress, ua);

        //no more attempts for this ip
        if (loginAttempt.getAttempts(ipAddress) <= 0) {
            //logout
            return "login?faces-redirect=true";
        }

        if (authService.authenticate(username, password)) {
            //set Session for this username
            HttpSession session = sessionService.getSession();
            if (session != null) {
                session.setAttribute("username", username);
                output = "overview.xhtml?faces-redirect=true";
            }
        } else {
            //logout
            output = "login.xhtml?faces-redirect=true";
        }

        return output;
    }

    public String logout() {
        sessionService.distroySession();
        return "login?faces-redirect=true";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
