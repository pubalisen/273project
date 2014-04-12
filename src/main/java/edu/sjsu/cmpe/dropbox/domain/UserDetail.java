//Declare user fields
/**
 * 
 */
package edu.sjsu.cmpe.dropbox.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Team-Projections			
 *
 */
public class UserDetail {

	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
	
	private String emailId;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailid() {
		return emailId;
	}

	/**
	 * @param emailid the emailid to set
	 */
	public void setEmailid(String emailid) {
		this.emailId = emailid;
	}
}
