package eu.itech.caas.service;

import java.util.Hashtable;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Here LDAPService are collected
 *
 * @author jens.papenhagen
 */
@Named
public class LDAPService {

    private static final Logger L = LoggerFactory.getLogger(LDAPService.class);

    private final Hashtable<String, String> env;

    /**
     * WARNING add the right
     * Address of your LDAP Servere here.
     */
    public LDAPService() {
        //TODO change the URL Active Directory
        //for adding more than one AD use a plus.
        //example env.put(Context.PROVIDER_URL, "ldap://hostone:636/" + "ldap://hosttwo:636/");
        env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://your.ad.server.here:636"); // 636 for SSL | 389 for no SSL
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.REFERRAL, "follow");
        env.put(Context.SECURITY_PROTOCOL, "ssl"); // Specify SSL    
        
        //yes HashTable is a obsolete Collection but InitialDirContext is requesting one.
    }

    /**
     * Authentication agains an or many LDAP Server WARNING add the right
     * Address of your LDAP Servere here.
     *
     * @param user
     * @param password
     * @return false for login Problems (like worng password), true for working
     * login cred.
     */
    public boolean authentication(String user, String password) {
        //no login cred.
        if (user == null || password == null) {
            L.error("user or password was null");
            return false;
        }
        if (user.isEmpty() || password.isEmpty()) {
            L.error("user or password was empty");
            return false;
        }

        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // attempt to authenticate
        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
            L.info("Used this LDAP Server {}", ctx.getEnvironment().get(Context.PROVIDER_URL));
            ctx.close();
        } catch (NamingException ex) {
            L.error("Login Problem: {}", ex);
            return false;
        }

        return true;
    }
    
    /**
     * Authentication the user Prometheus agains an or many LDAP Server.
     * 
     * @param password
     * @return false for login Problems (like worng password), true for working
     * login cred.
     */
    public boolean authenticationPrometheus(String password){
        //no login cred.
        if (password == null) {
            L.error("password was null");
            return false;
        }
        if (password.isEmpty()) {
            L.error("password was empty");
            return false;
        }

        env.put(Context.SECURITY_PRINCIPAL, "prometheus");
        env.put(Context.SECURITY_CREDENTIALS, password);

        // attempt to authenticate
        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
            L.info("Used this LDAP Server {}", ctx.getEnvironment().get(Context.PROVIDER_URL));
            ctx.close();
        } catch (NamingException ex) {
            L.error("Login Problem: {}", ex);
            return false;
        }
        
         return true;
    }

}
