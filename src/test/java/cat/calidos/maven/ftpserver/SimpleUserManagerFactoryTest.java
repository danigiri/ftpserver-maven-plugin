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

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;

import cat.calidos.maven.ftpserver.users.SimpleUserManagerFactory;
import junit.framework.TestCase;


/**	Main test class for user manager factory
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public class SimpleUserManagerFactoryTest extends TestCase {


private SimpleUserManagerFactory	userManagerFactory;


/* (non-Javadoc)
*  @see junit.framework.TestCase#setUp()
*///////////////////////////////////////////////////////////////////////////////
protected void setUp() throws Exception {

	super.setUp();
	userManagerFactory = new SimpleUserManagerFactory();
	
}	// setUp


/** Test method for {@link cat.calidos.maven.ftpserver.users.SimpleUserManagerFactory#createUserManager()}.
* 	@throws FtpException 
*///////////////////////////////////////////////////////////////////////////////
public void testCreateUserManager() throws FtpException {

	UserManager userManager = userManagerFactory.createUserManager();
	assertNotNull(userManager);
	assertEquals("admin",userManager.getAdminName());
	assertTrue(userManager.isAdmin("admin"));
		
}	// testCreateUserManager


}
