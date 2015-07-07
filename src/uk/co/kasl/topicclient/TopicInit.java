package uk.co.kasl.topicclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.Timer;

public class TopicInit {
	
	public String username;
	public String password;
	public Socket s = null;
	public BufferedReader in = null;
	public PrintWriter out = null;
	public Vector<JFrame> myFrames = new Vector<JFrame>();
	Thread receiveData;
	Thread sendData;
	Boolean connect = true;
	List<String> msgs = new ArrayList<String>();
	
	
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

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public TopicInit(String user,String pass,Socket socket){
		setUsername(user);
		setPassword(pass);
		setS(socket);
		showLoginFrame(user, pass);
		openConnection();
		Timer timer = new Timer(2000,open);
		timer.start();
	}
	
	public ActionListener open = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			openConnection();
			//refreshTrades();
		}
		
	};
	
	private synchronized void openConnection() {
		// TODO Auto-generated method stub
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new PrintWriter(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendData = new Thread(){
			public void run(){
				while(msgs.size() > 0){
					String temp = msgs.get(0);
					out.println(temp);
					out.flush();
					System.out.println(temp);
					msgs.remove(0);
				}
			}
		};
		receiveData = new Thread(){
			public void run(){
				String received;
				do{
					try {
						received = in.readLine();
						System.out.println(received);
					} catch (IOException e) {
						continue;
					}
				}while(connect);
			}
		};
		sendData.setDaemon(true);
		receiveData.setDaemon(true);
		sendData.start();
		receiveData.start();
		new Thread()
		{
			public void run(){
				while(connect){
					try {
						sendData.join();
						receiveData.join();
						disposeAllFrames();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}
	
	private void disposeAllFrames(){
		for(int i= 0; i< myFrames.size(); i++)
			myFrames.get(i).dispose();
	}

	private void showLoginFrame(String user, String pass) {
		// TODO Auto-generated method stub
		new LoginFrame(user,pass).addListener(new LoginFrame.Listener(){

			@Override
			public void submit(String user, String pass) {
				// TODO Auto-generated method stub
				setUsername(user);
				setPassword(pass);
				login();
			}
			
		});
	}
	
	public void login(){
		String log = "<login><username>" + username + "</username><password>" + password + "</password></login>";
		msgs.add(log);
	}
	
	public void addMyFrame(JFrame frame){
		myFrames.add(frame);
	}

	public static void main(String args[]){
		try {
			new TopicInit("","", new Socket("localhost",11111));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}