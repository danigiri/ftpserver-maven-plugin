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

import java.util.Properties;

import org.apache.ftpserver.FtpServer;
import org.apache.maven.plugin.MojoFailureException;


/** Stop the running FTP server
*	@author daniel giribet
*	@goal stop
*	@phase post-integration-test
*///////////////////////////////////////////////////////////////////////////////
public class FtpServerStopMojo extends AbstractFtpServerMojo {


/* (non-Javadoc)
* @see org.apache.maven.plugin.Mojo#execute()
*//////////////////////////////////////////////////////////////////////////////
public void execute() throws MojoFailureException {

	getLog().debug("Stopping FTP server...");
	Properties properties = null;
	if (mavenProject!=null) {
		properties = mavenProject.getProperties();
	} else {
		throw new MojoFailureException("Can't access maven project to stop FTP server (null)");
	}
	
	if (properties!=null) {
		FtpServer ftpServer;
		try {
			ftpServer = (FtpServer) properties.get(FtpServerConstants.FTPSERVER_KEY);
		} catch (ClassCastException e) {
			throw new MojoFailureException("Context doesn't contain a valid ftp server instance",e);
		}
		if (ftpServer==null) {
			throw new MojoFailureException("Context doesn't contain any ftp server instance");
		}
		if (!ftpServer.isStopped()) {
			ftpServer.stop();
			getLog().info("FTP server stopped.");
		} else {
			getLog().info("FTP server was stopped already");
		}
	} else {
		throw new MojoFailureException("Maven project has null properties",new NullPointerException());
	}
	
}	// execute

}
