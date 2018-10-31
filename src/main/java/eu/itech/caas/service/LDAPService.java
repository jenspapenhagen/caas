package eu.itech.caas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
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

    private final Properties env;

    private DirContext connection;

    private boolean connected;

    private String defaultDisplayName;

    /**
     * WARNING add the right Address of your LDAP Servere here.
     */
    public LDAPService() {
        //TODO change the URL Active Directory
        //for adding more than one AD use a plus.
        //example env.put(Context.PROVIDER_URL, "ldap://hostone:636/" + "ldap://hosttwo:636/");
        env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://your.ad.server.here:636"); // 636 for SSL | 389 for no SSL
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.REFERRAL, "follow");
        env.put(Context.SECURITY_PROTOCOL, "ssl"); // Specify SSL         
    }

    /**
     * Connect to the LDAP Server
     *
     * @param user
     * @param password
     * @param defaultDisplayName defaultDisplayname example:
     * 'cn=employee,cn=users,ou=school,dc=openiam,dc=com'
     */
    public void connect(String user, String password, String defaultDisplayName) {
        //no login cred.
        if (user == null || password == null || defaultDisplayName == null) {
            L.error("user or password was null");
            close();
            return;
        }
        if (user.isEmpty() || password.isEmpty() || defaultDisplayName.isEmpty()) {
            L.error("user or password was empty");
            close();
            return;
        }
        //adding the default DisplayName
        this.defaultDisplayName = defaultDisplayName;

        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            connection = new InitialDirContext(env);
            L.info("Used this LDAP Server {}", connection.getEnvironment().get(Context.PROVIDER_URL));
            connected = true;
        } catch (NamingException ex) {
            L.error("Login Problem: {}", ex);
            close();
        }
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
    public boolean authentication() {
        return connected;
    }

    /**
     * Get a User by Uid
     *
     * @param uid
     *
     * @return SearchResult for this User Copyright (c) 2014 Patrick Stillhart
     */
    public SearchResult getUser(String uid) {
        String searchFilter = "(&(objectClass=person)(uid=" + uid + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        try {
            if (!connected) {
                L.error("connection is not active");
                return null;
            }
            NamingEnumeration<SearchResult> results = connection.search(this.defaultDisplayName, searchFilter, searchControls);
            if (results.hasMoreElements()) {
                SearchResult searchResult = results.nextElement();

                //make sure there is not another item available, there should be only 1 match
                if (results.hasMoreElements()) {
                    L.error("Matched multiple users for the accountName: {}", uid);
                    return null;
                }
                return searchResult;
            }
        } catch (NamingException ex) {
            L.error("Couldn't find user with uid {} error: {}", uid, ex.getMessage());
        }

        return null;
    }

    /**
     * Deletes an user
     *
     * @param uid the uid
     * @return true if successful Copyright (c) 2014 Patrick Stillhart
     */
    public boolean deleteUser(String uid) {
        try {
            connection.destroySubcontext(uid);
            return true;
        } catch (NamingException ex) {
            L.error("Deleting error: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Update user attributes
     *
     * @param uid the uid from user to modify
     * @param mods a list with modifications
     * @return true if successful Copyright (c) 2014 Patrick Stillhart
     */
    public boolean updateUser(String uid, ModificationItem[] mods) {
        try {
            connection.modifyAttributes("uid=" + uid + "," + this.defaultDisplayName, mods);
            return true;
        } catch (NamingException ex) {
            L.error("Update error: {} ", ex.getMessage());
            return false;
        }
    }

    /**
     * creates a new user
     *
     * @param uid the uid (same as in the list)
     * @param entry a list with attributes
     * @return true if successful 
     * Copyright (c) 2014 Patrick Stillhart
     */
    public boolean addUser(String uid, Attributes entry) {
        try {
            connection.createSubcontext("uid=" + uid + "," + this.defaultDisplayName, entry);
            L.error("AddUser: added entry {}.", uid);
            return true;
        } catch (NameAlreadyBoundException ex) {
            L.error("AddUser: Entry Already Exists (68)");
            return false;
        } catch (NamingException ex2) {
            L.error("AddUser: error adding entry. {}", ex2.getMessage());
            return false;
        }
    }

    /**
     * Accepts custom filters and returns there result
     * http://www.google.com/support/enterprise/static/gapps/docs/admin/en/gads/admin/ldap.5.4.html
     *
     * @param searchFilter a ldap search filter (e.g.
     * '(objectClass=posixaccount)')
     * @return List<SearchResult> with all results / null for nothing Copyright
     * (c) 2014 Patrick Stillhart
     */
    public List<SearchResult> getResultByCustomFilter(String searchFilter) throws NamingException {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> results = connection.search(defaultDisplayName, searchFilter, searchControls);

        if (results.hasMoreElements()) {
            List<SearchResult> searchResults = new ArrayList<>();
            while (results.hasMore()) {
                searchResults.add(results.next());
            }
            return searchResults;

        }
        return null;
    }

    private void close() {
        connected = false;
        try {
            connection.close();
            L.error("Connection closed!");
        } catch (NamingException ex) {
            L.error("Close: failed to close connection: {}", ex.getMessage());
        }
    }

}
