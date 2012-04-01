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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;

/**	Main test class for maven ftp server plugin
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////
public class FtpServerMojoTest extends AbstractMojoTestCase {

private FTPClient	ftp;
private File		pom;
private int			port;
private FtpServer	ftpServer;


/** Read pom and start FTP server
*	@throws java.lang.Exception
*///////////////////////////////////////////////////////////////////////////////
public void setUp() throws Exception {
		
	
	super.setUp() ;

	pom = getTestFile("src/test/resources/unit/test-project/pom.xml");
	assertNotNull("Test pom not found",pom);
	assertTrue("Test pom not found",pom.exists());
	
	port = getFreePort();
	
	FtpServerRunMojo runMojo = (FtpServerRunMojo) lookupMojo("run", pom);

	// all this will be set on a real project (it's not on the test environment)
	runMojo.serverRoot = new File(PlexusTestCase.getBasedir()+"/target");
	runMojo.port = port;
	runMojo.mavenProject = new MavenProject();
	runMojo.mavenProject.getModel().setProperties(new Properties());
	runMojo.execute();
	ftpServer = (FtpServer) runMojo.mavenProject.getProperties().get(FtpServerConstants.FTPSERVER_KEY);
	
    ftp = new FTPClient();
    ftp.connect("localhost",port);    

}	// setUp


/** Run stop server mojo
* 	@throws java.lang.Exception
*///////////////////////////////////////////////////////////////////////////////
public void tearDown() throws Exception {

	
	if (ftp.isAvailable()) {
		if (ftp.isConnected()) {
			ftp.disconnect();
		}
	}
	
	FtpServerStopMojo stopMojo = (FtpServerStopMojo) lookupMojo("stop",pom);
	
	Properties properties = new Properties();
	properties.put(FtpServerConstants.FTPSERVER_KEY, ftpServer);
	stopMojo.mavenProject = new MavenProject();
	stopMojo.mavenProject.getModel().setProperties(properties);
	stopMojo.execute();
	
	super.tearDown();	// called last as it dismantles running mojo stuff
	
}	// tearDown


/** Basic pom test
*	@throws IOException 
* 	@throws Exception if any
*///////////////////////////////////////////////////////////////////////////////
public void testAdmin() throws FtpException, IOException {

	ftp.login("admin", "admin00");
	int reply = ftp.getReplyCode();
    assertTrue(FTPReply.isPositiveCompletion(reply));
	assertTrue("Can't login with admin user",ftp.isConnected());
    findRemoteItem("classes");
    ftp.disconnect();
	
}	// testAdmin


/** Demo user should be able to login
*	@throws SocketException
*	@throws IOException
*//////////////////////////////////////////////////////////////////////////////
public void testDemoUser() throws SocketException, IOException {
	
    ftp.login("demo", "demo");
	int reply = ftp.getReplyCode();
    assertTrue(FTPReply.isPositiveCompletion(reply));
    assertTrue("Can't login with demo user",ftp.isConnected());
    
	String filename = "test-file";
	putFile(filename);
	boolean found = findRemoteItem(filename);
	assertTrue(filename+" can't be uploaded",found);
    
    ftp.disconnect();
    
}	// testDemoUser


/** shouldn't be able to login
* 	@throws IOException
*//////////////////////////////////////////////////////////////////////////////
public void testDisabledUser() throws IOException {
	
	ftp.login("disabled", "disabled");
	assertNull("Can login with disabled user", ftp.getStatus());
	ftp.disconnect();
	
}	// testDisabledUser


/** Relative user root test
*	@throws IOException
*//////////////////////////////////////////////////////////////////////////////
public void testClassesRoot() throws IOException {
	
	ftp.login("classes-root", "classes-root");
	int reply = ftp.getReplyCode();
    assertTrue(FTPReply.isPositiveCompletion(reply));
	assertTrue(findRemoteItem("META-INF"));
	ftp.disconnect();
	
}	// testClassesRoot


/**	User shouldn't have write permissions
*	@throws IOException
*//////////////////////////////////////////////////////////////////////////////
public void testCannotWrite() throws IOException {
	
	ftp.login("write-disabled", "write-disabled");
	int reply = ftp.getReplyCode();
    assertTrue(FTPReply.isPositiveCompletion(reply));
	assertTrue(ftp.isConnected());
	
	String filename = "test-file2";
	putFile(filename);
	boolean found = findRemoteItem(filename);
	assertFalse(filename+" can be put on nonwrite permissions user",found);
	
	ftp.disconnect();
	
}	// testCannotWrite


/** Test that the stop mojo throws the appropriate exceptions and stops the server
 * @throws Exception if for some reason pom doesn't contain the stop mojo
*//////////////////////////////////////////////////////////////////////////////
public void testStopMojoExceptions() throws Exception {
	
	pom = getTestFile("src/test/resources/unit/test-project/pom.xml");
	FtpServerStopMojo stopMojo = (FtpServerStopMojo) lookupMojo("stop",pom);

	// project is null
	boolean fail = false;
	try {
		stopMojo.execute();
	} catch (MojoFailureException e) {	
		fail = true;
	}
	assertTrue(fail);
	
	// project doesn't have properties
	stopMojo.mavenProject = new MavenProject();
	try {
		stopMojo.execute();		
	} catch (MojoFailureException e) {
		fail = true;
	}
	assertTrue(fail);
	
}	// testStopMojoExceptions


/** Upload a file containing test data
* @throws IOException
*//////////////////////////////////////////////////////////////////////////////
private void putFile(String filename) throws IOException {

	String s = "TEST DATA";
	byte buf[] = s.getBytes(); 
	ByteArrayInputStream in = new ByteArrayInputStream(buf); 
	ftp.setFileType(FTP.BINARY_FILE_TYPE);
	ftp.storeFile(filename, in);
	in.close();
	
}	// putFile


/** Look for item in FTP server
* 	@param item name of file or folder to find remotely
*	@return true if found
*	@throws IOException
*//////////////////////////////////////////////////////////////////////////////
private boolean findRemoteItem(String item) throws IOException {

	FTPListParseEngine engine = ftp.initiateListParsing("");

    boolean found = false;
	while (engine.hasNext() && !found) {
       FTPFile[] files = engine.getNext(25); 
       for (int i=0; i<files.length && !found; i++) {
    	   FTPFile f = files[i];
    	   found = f.getName().contains(item);
       }
    }
	return found;

}	// findRemoteItem


/** Unfortunately, 'free available port mojo' can't easily be run under test circumstances
* 	as it requires a project instance to be set, therefore we do it by hand
*	@return free port number
*	@throws MojoExecutionException
*//////////////////////////////////////////////////////////////////////////////
private int getFreePort() throws MojoExecutionException {
    
	int port = 0;
    
    try    {
        ServerSocket socket = new ServerSocket( 0 );
        port = socket.getLocalPort();
        socket.close();
    }
    catch (IOException e) {
        throw new MojoExecutionException( "Error getting an available port from system", e );
    }

    return port;
    
}	//getFreePort


}
