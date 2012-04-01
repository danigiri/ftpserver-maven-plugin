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

package cat.calidos.maven.ftpserver.users;

import java.util.HashMap;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;


/** In-memory trivial user manager
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public class SimpleUserManager implements UserManager {

private HashMap<String, User>	users;
private String					adminName;
private PasswordEncryptor		passwordEncryptor;
private boolean					anonymousLoginAllowed;


/** Basic parameter constructor
* 	@param adminName	default admin
* 	@param passwordEncryptor encryptor used
*//////////////////////////////////////////////////////////////////////////////
public SimpleUserManager(String adminName, PasswordEncryptor passwordEncryptor, boolean anonymousOK) {

	this.adminName		   = adminName;
	this.passwordEncryptor = passwordEncryptor;
	anonymousLoginAllowed  = anonymousOK;
	users 				   = new HashMap<String, User>();

}	// SimpleUserManager


/*	(non-Javadoc)
*	@see org.apache.ftpserver.ftplet.UserManager#authenticate(org.apache.ftpserver.ftplet.Authentication)
*//////////////////////////////////////////////////////////////////////////////
public User authenticate(Authentication auth) throws AuthenticationFailedException {

	if (auth == null) {
		throw new AuthenticationFailedException(new NullPointerException());
	}

	if (auth instanceof UsernamePasswordAuthentication) {

		UsernamePasswordAuthentication authentication = (UsernamePasswordAuthentication) auth;
		String username = authentication.getUsername();
		String password = authentication.getPassword();

		if (username == null) {
			throw new AuthenticationFailedException(new NullPointerException(
					"Null username"));
		}
		password = password != null ? password : "";

		User user = users.get(username);
		if (user == null) {
			throw new AuthenticationFailedException("Authentication failed");
		}

		if (passwordEncryptor.matches(password, user.getPassword())) {
			return user;
		} else {
			throw new AuthenticationFailedException("Authentication failed");
		}

	} else if (auth instanceof AnonymousAuthentication) {

		if (!anonymousLoginAllowed) {
			throw new AuthenticationFailedException("Anonymous login not allowed");
		}
		User anonymous = users.get("anonymous");
		if (anonymous == null) {
			throw new AuthenticationFailedException("Anonymous login supported but user not found");
		}
		return anonymous;
		
	} else {
		throw new UnsupportedOperationException("Authentication not supported");
	}
	
}	// authenticate


/*	(non-Javadoc)
*	@see org.apache.ftpserver.ftplet.UserManager#delete(java.lang.String)
*//////////////////////////////////////////////////////////////////////////////
public void delete(String username) throws FtpException {

	if (!doesExist(username)) {
		throw new FtpException("Can't delete user");
	}
	users.remove(username);

}	// delete


/*  (non-Javadoc)
* 	@see org.apache.ftpserver.ftplet.UserManager#doesExist(java.lang.String)
*///////////////////////////////////////////////////////////////////////////////
public boolean doesExist(String username) throws FtpException {

	return users.containsKey(username);

}	// doesExist


/*	(non-Javadoc)
* 	@see org.apache.ftpserver.ftplet.UserManager#getAllUserNames()
*//////////////////////////////////////////////////////////////////////////////
public String[] getAllUserNames() throws FtpException {

	return users.keySet().toArray(new String[0]);
	
}	// getAllUserNames


/* 	(non-Javadoc)
* 	@see org.apache.ftpserver.ftplet.UserManager#getUserByName(java.lang.String)
*///////////////////////////////////////////////////////////////////////////////
public User getUserByName(String name) throws FtpException {

	if (!users.containsKey(name)) {
		throw new FtpException("Can't get user");
	}
	return users.get(name);

}	// getUserByName



/* 	(non-Javadoc)
* 	@see org.apache.ftpserver.ftplet.UserManager#save(org.apache.ftpserver.ftplet.User)
*//////////////////////////////////////////////////////////////////////////////
public void save(org.apache.ftpserver.ftplet.User user) throws FtpException {

	if (user == null) {
		throw new FtpException(new NullPointerException("Null user save"));
	}
	String username = user.getName();
	if (username == null) {
		throw new FtpException(new NullPointerException("Null username save"));
	}

	users.put(username, (User) user);

}	// save


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.UserManager#getAdminName()
*//////////////////////////////////////////////////////////////////////////////
public String getAdminName() throws FtpException {
	return adminName;
}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.UserManager#isAdmin(java.lang.String)
*//////////////////////////////////////////////////////////////////////////////
public boolean isAdmin(String username) throws FtpException {

	if (adminName==null){
		return false;
	}
	return adminName.equalsIgnoreCase(username);

}	// isAdmin

}
