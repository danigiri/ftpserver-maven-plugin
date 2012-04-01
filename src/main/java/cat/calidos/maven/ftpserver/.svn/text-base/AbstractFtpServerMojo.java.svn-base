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


import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;


/**	Includes all common parameters
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public abstract class AbstractFtpServerMojo extends AbstractMojo {


/** Encompassing maven project
*	@parameter default-value="${project}" 
* 	@required
* 	@readonly
*/
protected MavenProject mavenProject;


/** Details of administration user
*	@parameter
*/
protected User	adminUser;

/** Extra user list
* 	@parameter
*/
protected List<User> users;

/** Server root folder
*	@parameter expression="${project.build.directory}"
*/
protected File serverRoot;


/** FTP server port
*	@parameter default-value="2221"
*	@required 
*/
protected int port;

/** Additional system property variables (to pass onto tests, etc.)
*	@parameter 
*/
protected Map<String,String> systemPropertyVariables;

/** Allow login anonymously
*	@parameter default-value="false"
*/
protected boolean allowAnonymousLogin = false;

}
