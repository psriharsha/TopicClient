package uk.co.kasl.topicclient;

import java.net.Socket;

public class User {

	public String username;
	public String password;
	public Socket socket;
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public User(String user, String pass, Socket sock){
		setUsername(user);
		setPassword(pass);
		setSocket(sock);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}