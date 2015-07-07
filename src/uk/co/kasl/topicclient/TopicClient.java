package uk.co.kasl.topicclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TopicClient {

	Socket s = null;
	BufferedReader in = null;
	PrintWriter out = null;
	public static TopicClient client = null;
	private TopicClient(){
		try {
			s = new Socket("localhost",11111);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static TopicClient getTopicClient(){
		if(client == null){
			client = new TopicClient();
		}
		return client;
	}
	
	public String sendMessage(String message){
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new PrintWriter(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = "";
		out.println(message);
		out.flush();
		result = "Success";
		return result;
	}
	
}