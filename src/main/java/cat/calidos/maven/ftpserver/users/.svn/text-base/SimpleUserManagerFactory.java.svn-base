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

package cat.calidos.maven.ftpserver.users;

import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.UserManagerFactory;


/** Factory to create basic Usermanagers, a skeleton by now
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public class SimpleUserManagerFactory implements UserManagerFactory {


private String 	adminName 	 			= "admin";
private boolean	anonymousLoginAllowed	= false;


/* (non-Javadoc)
*  @see org.apache.ftpserver.usermanager.UserManagerFactory#createUserManager()
*//////////////////////////////////////////////////////////////////////////////
public UserManager createUserManager() {

	SimpleUserManager simpleUserManager = new SimpleUserManager(
												adminName, 
												new ClearTextPasswordEncryptor(),
												anonymousLoginAllowed);
	return simpleUserManager;
	
}	// createUserManager



/** @param name of the administrator user
*//////////////////////////////////////////////////////////////////////////////
public void setAdminName(String name) {

	adminName = name;
	
}	// setAdminUserName


public void setAnonymousLoginAllowed(boolean allowAnonymousLogin) {

	anonymousLoginAllowed = allowAnonymousLogin;
	
}

}
