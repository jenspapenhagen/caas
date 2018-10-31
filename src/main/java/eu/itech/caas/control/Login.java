package eu.itech.caas.control;

import eu.itech.caas.service.AuthenticationService;
import eu.itech.caas.service.SessionService;
import java.io.Serializable;
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

    private String username;

    private String password;

    public String login() {
        if (authService.authenticate(username, password)) {
            //set Session for this username
            HttpSession session = sessionService.getSession();
            session.setAttribute("username", username);

            return "overview?faces-redirect=true";
        } else {
            //logout
            return "login?faces-redirect=true";
        }
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
