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

/**
 * 
 */

package cat.calidos.maven.ftpserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import cat.calidos.maven.ftpserver.users.SimpleUserManager;
import junit.framework.TestCase;


/**	user manager test class
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public class SimpleUserManagerTest extends TestCase {


private SimpleUserManager	userManager;


/* (non-Javadoc)
* @see junit.framework.TestCase#setUp()
*///////////////////////////////////////////////////////////////////////////////
protected void setUp() throws Exception {

	super.setUp();
	
	String adminName = "admin";
	userManager = new SimpleUserManager(adminName,new ClearTextPasswordEncryptor(),false);
	saveUser("demo","demo");
	saveUser("demo2","demo");
	saveUser(adminName,"administrator");
	
}	// setUp


/**
* Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#SimpleUsermanager(java.lang.String, org.apache.ftpserver.usermanager.PasswordEncryptor)}.
*///////////////////////////////////////////////////////////////////////////////
public void testSimpleUsermanager() {

	try {
		
		assertTrue(userManager.isAdmin("admin"));
		assertEquals(userManager.getAdminName(),"admin");
		
	} catch (FtpException e) {
		fail(e.getMessage());
	}

}	// testSimpleUsermanager


/**
* Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#authenticate(org.apache.ftpserver.ftplet.Authentication)}.
*///////////////////////////////////////////////////////////////////////////////
public void testAuthenticate() {

	loginShouldWork("demo", "demo");
	loginShouldFail("demo", "FAIL");
	loginShouldFail("DOESNTEXIST", "whatever");
	loginShouldWork("admin", "administrator");
	loginShouldFail("admin", "FAIL");
	
}	// testAuthenticate


/** Test anonymous login
*	@throws FtpException 
*//////////////////////////////////////////////////////////////////////////////
public void testAnonymous() throws FtpException {
	boolean fail = false;
	AnonymousAuthentication anonymousAuth = new AnonymousAuthentication();
	try {
		userManager.authenticate(anonymousAuth);
	} catch (AuthenticationFailedException e) {
		fail = true;
	}
	assertTrue(fail);
	
	userManager = new SimpleUserManager("admin",new ClearTextPasswordEncryptor(),true);	
	saveUser("anonymous","whatever");

	try {
		userManager.authenticate(anonymousAuth);
	} catch (AuthenticationFailedException e) {
		fail(e.getMessage());
	}
	
}

/**
 * Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#delete(java.lang.String)}.
 * @throws FtpException 
 *///////////////////////////////////////////////////////////////////////////////
public void testDelete() throws FtpException {

	String username = "demo";
	assertTrue(userManager.doesExist(username));
	userManager.delete(username);
	assertFalse(userManager.doesExist(username));
	
	boolean fail = false;
	try {
		userManager.delete("DOESNTEXIST");
	} catch (FtpException e) {
		fail = true;
	}
	assertTrue(fail);
	
}	// testDelete


/**
* Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#doesExist(java.lang.String)}.
* @throws FtpException 
*///////////////////////////////////////////////////////////////////////////////
public void testDoesExist() throws FtpException {

	assertTrue(userManager.doesExist("demo"));
	assertTrue(userManager.doesExist("demo2"));
	assertTrue(userManager.doesExist("admin"));
	assertFalse(userManager.doesExist("DOESNTEXIST"));
	
}	// testDoesExist


/** Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#getAllUserNames()}.
* 	@throws FtpException 
*///////////////////////////////////////////////////////////////////////////////
public void testGetAllUserNames() throws FtpException {

	String[] usernames = userManager.getAllUserNames();
	assertNotNull(usernames);
	assertTrue(usernames.length == 3);
	HashSet<String> userSet = new HashSet<String>();
	Collections.addAll(userSet, usernames[0], usernames[1], usernames[2]);
	assertTrue(userSet.contains("demo"));
	assertTrue(userSet.contains("demo2"));
	assertTrue(userSet.contains("admin"));
	
} 	// testGetAllUserNames


/** Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#getUserByName(java.lang.String)}.
*	@throws FtpException 
*///////////////////////////////////////////////////////////////////////////////
public void testGetUserByName() throws FtpException {

	User demo = (User) userManager.getUserByName("demo");
	assertNotNull(demo);
	assertEquals("demo", demo.getName());
	User admin = (User) userManager.getUserByName("admin");
	assertNotNull(admin);
	assertEquals("admin", admin.getName());
	
	boolean failed = false;
	try {
		userManager.getUserByName("DOESNTEXIST");
	} catch (FtpException e) {
		failed = true;
	}
	assertTrue(failed);
	
}	// testGetUserByName


/** Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManager#save(org.apache.ftpserver.ftplet.User)}.
*	@throws FtpException 
*///////////////////////////////////////////////////////////////////////////////
public void testSave() throws FtpException {

	String username = "demo"+Math.random();
	saveUser(username,"demo");
	userManager.doesExist(username);
	
	String newPassword  = "NEW"+Math.random();
	saveUser(username, newPassword);
	assertNotNull(userManager.authenticate(new UsernamePasswordAuthentication(username,newPassword)));

}	// testSave


/**
 * @param username TODO
 * @param password TODO
* 
*//////////////////////////////////////////////////////////////////////////////
private void loginShouldWork(String username, String password) {

	UsernamePasswordAuthentication auth = new UsernamePasswordAuthentication(username, password);
	try {
		userManager.authenticate(auth);
	} catch (AuthenticationFailedException e) {
		fail(e.getMessage());
	}
	
}	// loginShouldWork


/**
 * @param username TODO
 * @param password TODO
* 
*//////////////////////////////////////////////////////////////////////////////
private void loginShouldFail(String username, String password) {

	UsernamePasswordAuthentication auth;
	boolean fail = false;
	auth = new UsernamePasswordAuthentication(username, password);
	try {
		userManager.authenticate(auth);
	} catch (AuthenticationFailedException e) {
		fail = true;
	}
	assertTrue(fail);
}


/** helper to save a typical user
* @throws FtpException
*//////////////////////////////////////////////////////////////////////////////
private void saveUser(String username, String password) throws FtpException {

	User user = new cat.calidos.maven.ftpserver.User();
	user.setName(username);
	user.setPassword(password);
	user.setEnabled(true);
	user.setHomeDirectory("/");
	user.setMaxIdleTime(1000);
	List<Authority> authorities = new ArrayList<Authority>();
	authorities.add(new WritePermission());
	authorities.add(new ConcurrentLoginPermission(2, 2));
	authorities.add(new TransferRatePermission(4200, 4200));
	userManager.save(user);
	
}	// saveUser

}
