package com.lc;
public class UserBean {
	private String username;
	private String password;
	
	public boolean valid=false;

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String newUsername) {
		username = newUsername;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean newValid) {
		valid = newValid;
	}
}