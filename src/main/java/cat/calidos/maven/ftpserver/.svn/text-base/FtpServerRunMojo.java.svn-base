/*
*    Copyright 2011 Daniel Giribet
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package cat.calidos.maven.ftpserver;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.maven.plugin.MojoFailureException;

import cat.calidos.maven.ftpserver.users.SimpleUserManagerFactory;

/** Mojo to start the apache FTP server as an integration instance
*	@goal run
*	@phase pre-integration-test
*///////////////////////////////////////////////////////////////////////////////
public class FtpServerRunMojo extends AbstractFtpServerMojo {

private FtpServerFactory			serverFactory;
private FtpServer					server;
private SimpleUserManagerFactory	simpleUserManagerFactory;

private ListenerFactory	factory;


/* 	(non-Javadoc)
* 	@see org.apache.maven.plugin.Mojo#execute()
*///////////////////////////////////////////////////////////////////////////////
public void execute() throws MojoFailureException {

	initServer();
	setupUserManager();
	runServer();

} // execute


/** Create a server instance with default values
*///////////////////////////////////////////////////////////////////////////////
private void initServer() {

	// add relevant system property variables
	if (systemPropertyVariables!=null) {
		Set<?> propertyNames = systemPropertyVariables.keySet();
		for (Iterator<?> iterator = propertyNames.iterator(); iterator.hasNext();) {
			String propertyName = (String) iterator.next();
			String propertyValue = (String) systemPropertyVariables.get(propertyName);
			getLog().debug("Setting system variable '"+propertyName+"'");
			System.setProperty(propertyName, propertyValue);
		}
	}
	
	serverFactory = new FtpServerFactory();
	factory = new ListenerFactory();

	// set the port of the listener
	getLog().debug("Using port "+port);
	factory.setPort(port);
	serverFactory.addListener("default", factory.createListener());
	server = serverFactory.createServer();

}	// initServer
	

/** Configure relevant users according to configuration
*	@throws MojoFailureException 
*//////////////////////////////////////////////////////////////////////////////
private void setupUserManager() throws MojoFailureException {
		
	simpleUserManagerFactory = new SimpleUserManagerFactory();
	
	if (serverRoot==null) {
		throw new MojoFailureException("Server root can't be null");
	}
	
	if (adminUser==null) {
		getLog().debug("adminUser is null, creating default...");	
		createDefaultAdmin();		
	}
	simpleUserManagerFactory.setAdminName(adminUser.name);
	
	simpleUserManagerFactory.setAnonymousLoginAllowed(allowAnonymousLogin);
	
	UserManager userManager = simpleUserManagerFactory.createUserManager();
	addAdminUser(userManager);
	addRegularUsers(userManager);
	
	serverFactory.setUserManager(userManager);

}	// setupUserManager



/** Add administrator user to manager
* @throws MojoFailureException
*//////////////////////////////////////////////////////////////////////////////
private void addAdminUser(UserManager userManager) throws MojoFailureException {

	if (adminUser.homeDirectory==null) {
		adminUser.setHomeDirectory(serverRoot.getAbsolutePath());
	}

	try {
		getLog().debug("Saving adminUser...");
		userManager.save(adminUser);
	} catch (FtpException e) {
		getLog().error("Bad admin user configuration");
		throw new MojoFailureException("Can't add admin user", e);
	}

}	// addAdminUser



/** Add non-administrator users if any
* @param userManager
* @throws MojoFailureException
*//////////////////////////////////////////////////////////////////////////////
private void addRegularUsers(UserManager userManager) throws MojoFailureException {

	if (users!=null) {
		
		User anonymous = null;
		
		for (User user : users) {
			
			if (user.getHomeDirectory()==null) {
				user.setHomeDirectory(serverRoot.getAbsolutePath());
			}
			if (user.getRelativehomeDirectory()!=null) {
				user.setHomeDirectory(user.getHomeDirectory()+"/"+user.getRelativehomeDirectory());
			}
			
			try {
				getLog().debug("Adding user '"+user.getName()+"'");
				userManager.save(user);
			} catch (FtpException e) {
				throw new MojoFailureException("Problem adding user '"+user.getName()+"'",e);
			}
			
			if (allowAnonymousLogin && user.name.equals("anonymous")) {
				anonymous = user;
			}
			
		}
		
		if (allowAnonymousLogin && anonymous==null) {
			throw new MojoFailureException("Anonymous logins allowed but user 'anonymous' not defined");
		}
		
	}	// if
	
}	// addRegularUsers



/** Create default administrator user 'admin' user with 'admin' password
*//////////////////////////////////////////////////////////////////////////////
private void createDefaultAdmin() {

	adminUser = new User();
	adminUser.setName("admin");
	adminUser.setPassword("admin");

}	// createDefaultAdmin



/** Startup the server and store it on the project properties if possible
*	@throws MojoFailureException 
*//////////////////////////////////////////////////////////////////////////////
private void runServer() throws MojoFailureException {

	getLog().debug("About to start FTP server...");
	
	try {
		
		server.start();
		getLog().info("FTP server started.");

	} catch (FtpException e) {

		getLog().error("Could not start FTP server...");
		throw new MojoFailureException("Could not start FTP server instance", e);
		
	}
	
	if (mavenProject!=null) {
		Properties properties = mavenProject.getProperties();
		properties.put(FtpServerConstants.FTPSERVER_KEY, server);
	} else {
		throw new MojoFailureException("Can't add ftpserver instance as maven project is null");
	}
	
}	// runServer

    
}