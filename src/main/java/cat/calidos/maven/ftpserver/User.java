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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;


/**	User configuration class (Plexus default values or expressions seem to be ignored
* 	for some reason so providing java defaults instead).
*	@author daniel.giribet
*///////////////////////////////////////////////////////////////////////////////
public class User implements org.apache.ftpserver.ftplet.User  {


/** @parameter
*	@required */
protected String name;

/** @parameter
*	@required */
protected String password;

/** @parameter expression="${permissions.idletime}" default-value="0"
*	@required */
protected int	maxIdleTime = 0;

/** @parameter default-value="true"
*	@required */
private boolean	enabled = true;

/** @parameter default-value="${project.build.directory}" 
*	@required */
protected File homeDirectory;

/** @parameter */
private File relativehomeDirectory;

/** @parameter default-value="true" 
*	@required */
protected boolean writePermission = true;

/** @parameter default-value="0" 
*	@required */
protected int maxLoginNumber = 0;

/** @parameter default-value="0" 
*	@required */
protected int maxLoginPerIp = 0;

/** @parameter default-value="0" 
*	@required */
protected int maxUploadRate = 0;

/** @parameter default-value="0" 
*	@required */
protected int maxDownloadRate = 0;

// if it is a copy it means the authorities were copied and not injected
// it it's not a copy then authority data was injected by maven
protected boolean isACopy = false;

protected List<Authority> authorities = null;


/** Empty constructor
*///////////////////////////////////////////////////////////////////////////////
public User() {}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getName()
*//////////////////////////////////////////////////////////////////////////////
public String getName() {
	return name;
}


/** @param name The name to set.
*///////////////////////////////////////////////////////////////////////////////
public void setName(String name) {
	this.name = name;
}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getPassword()
*//////////////////////////////////////////////////////////////////////////////
public String getPassword() {
	return password;
}


/** @param password The password to set.
*///////////////////////////////////////////////////////////////////////////////
public void setPassword(String password) {
	this.password = password;
}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getAuthorities()
*//////////////////////////////////////////////////////////////////////////////
public List<Authority> getAuthorities() {
	
	if (authorities==null) {
		// this means object is not a copy and authority parameters were set by maven
		// we need to build the authorities by hand
		
		authorities = new ArrayList<Authority>(4);
		
		if (writePermission) {
			authorities.add(new WritePermission());
		}
		authorities.add(new ConcurrentLoginPermission(maxLoginNumber, maxLoginPerIp));
		authorities.add(new TransferRatePermission(maxDownloadRate, maxUploadRate));
		
	}
	
	// if it were a copy object it could still have null authorities
	return Collections.unmodifiableList(authorities);

}	// getAuthorities


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getAuthorities(java.lang.Class)
*///////////////////////////////////////////////////////////////////////////////
public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
    
	List<Authority> selected = new ArrayList<Authority>();

    for (Authority authority : authorities) {
        if (authority.getClass().equals(clazz)) {
            selected.add(authority);
        }
    }

    return selected;

}	// getAuthorities


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#authorize(org.apache.ftpserver.ftplet.AuthorizationRequest)
*///////////////////////////////////////////////////////////////////////////////
public AuthorizationRequest authorize(AuthorizationRequest request) {

    if(getAuthorities() == null) {
        return null;
    }
    
    boolean authorized = false;
    for (Authority authority : authorities) {
    	
        if (authority.canAuthorize(request)) {
            authorized = true;

            request = authority.authorize(request);

            if (request == null) {
                return null;
            }
        }

    }	// for

    if (authorized) {
        return request;
    } else {
        return null;
    }
    
}	// authorize


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getMaxIdleTime()
*//////////////////////////////////////////////////////////////////////////////
public int getMaxIdleTime() {
	return maxIdleTime;
}


/** @param maxIdleTime The maxIdleTime to set.
*///////////////////////////////////////////////////////////////////////////////
public void setMaxIdleTime(int maxIdleTime) {
	this.maxIdleTime = maxIdleTime;
}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getEnabled()
*///////////////////////////////////////////////////////////////////////////////
public boolean getEnabled() {
	return enabled;
}


/** @param enabled The enabled to set.
*///////////////////////////////////////////////////////////////////////////////
public void setEnabled(boolean enab) {
	this.enabled = enab;
}


/* (non-Javadoc)
* @see org.apache.ftpserver.ftplet.User#getHomeDirectory()
*///////////////////////////////////////////////////////////////////////////////
public String getHomeDirectory() {
	//we do not want to call getpath on null, do we?
	if (homeDirectory==null) {
		return null;	// we thus avoid a nullpointer excp that causes hard-to-detect apache FTP server failure
	}
 	return homeDirectory.getPath();
}	// getHomeDirectory


/** @param home The home directory
*///////////////////////////////////////////////////////////////////////////////
public void setHomeDirectory(String home) {
	this.homeDirectory = new File(home);
}


/** @return Returns the relative home folder
*///////////////////////////////////////////////////////////////////////////////
protected File getRelativehomeDirectory() {
	return relativehomeDirectory;
}


}
