package uk.co.kasl.topicclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.co.kasl.topicclient.TradeDetail.Listener;

public class TopicInit {
	
	public String username;
	public String password;
	public Socket s = null;
	public BufferedReader in = null;
	public PrintWriter out = null;
	private static LoginFrame loginFrame;
	private static TradeDetail tradeDetail;
	Thread receiveData;
	Thread sendData;
	Boolean connect = true;
	Vector<String> msgs = new Vector<String>();
	Map<String,String> data = new HashMap<String,String>();
	Node node;
	NodeList nodeList = null;
	MessageType cmd = MessageType.PING;
	Map<String,TopicChat> liveTopics = new HashMap<String,TopicChat>();
	
	/*private LoginFrame getLoginFrame(){
		return loginFrame;
	}*/
	
	private enum MessageType{
		LOGIN, /* Login */
		GET_TOPIC, /* Gets all the topics */
		NEW,
		SELECT, /* When a new Subject has been selected */
		APPEND, /* When a message has been sent */
		MESSAGE, /* Message received from server */
		PING; /* Nothing really happens */
		
		public static MessageType getType(String type){
			MessageType msgType = PING;
			switch(type){
			case "login" : msgType = LOGIN; break;
			case "get" : msgType = GET_TOPIC; break;
			case "new" : msgType = NEW; break;
			case "ping" : msgType = PING; break;
			case "select" : msgType = SELECT; break;
			case "append" : msgType = APPEND; break;
			}
			return msgType;
		}
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
	}
	
	private synchronized void openConnection() {
		// TODO Auto-generated method stub
		sendData = new Thread(){
			public void run(){
				synchronized(msgs){
						try {
							msgs.wait(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				while(true){
					synchronized(msgs){
						try{
							if(msgs.size() > 0){
								out = new PrintWriter(new PrintWriter(s.getOutputStream()));
								String temp = msgs.get(0);
								out.println(temp);
								out.flush();
								System.out.println(temp);
								msgs.remove(0);
							}
						}catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			}
		};
		receiveData = new Thread(){
			public void run(){
				String received;
				do{
					try {
						in = new BufferedReader(new InputStreamReader(s.getInputStream()));
						received = in.readLine();
						parseMessageReceived(received);
					} catch (IOException e) {
						continue;
					}
				}while(true);
			}
		};
		sendData.setDaemon(true);
		receiveData.setDaemon(true);
		sendData.start();
		receiveData.start();
		new Thread()
		{
			public void run(){
				while(true){
					try {
						sendData.join();
						receiveData.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}

	private void showLoginFrame(String user, String pass) {
		// TODO Auto-generated method stub
		loginFrame = new LoginFrame(user,pass);
		loginFrame.addListener(new LoginFrame.Listener(){

			@Override
			public void submit(String user, String pass) {
				// TODO Auto-generated method stub
				setUsername(user);
				setPassword(pass);
				login();
			}

			@Override
			public void cancel() {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
	}
	
	public void parseMessageReceived(String message){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		InputSource is = new InputSource();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		    is.setCharacterStream(new StringReader(message));
			Document doc = builder.parse(is);
			data = new HashMap<>();
			node = doc.getDocumentElement();
			String command = node.getNodeName();
			cmd = MessageType.getType(command);
			nodeList = doc.getDocumentElement().getChildNodes();
			for(int i = 0; i<nodeList.getLength(); i++){
				node = nodeList.item(i);
				if(node instanceof Element){
					data.put(node.getNodeName(), node.getTextContent());
				}
			}
			switch(cmd){
			case LOGIN: {
				String result = data.get("result");
				if(result.equals("success")){
					System.out.println(result);
					loginFrame.disposeFrame();
					getTradeDetail();
				}else if(result.equals("failed")){
					loginFrame.setResult("Invalid Username/Password combination.");
				}
			} break;
			case PING:
				break;
			case GET_TOPIC:
				Vector<String> oldTopics = new Vector<String>();
				Iterator<Entry<String, String>> it = data.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String,String> pair = (Map.Entry<String,String>) it.next();
					String key = (String) pair.getKey();
					if(key.contains("name")){
						oldTopics.add((String) pair.getValue());
					}
				}
				showTradeDetailFrame(oldTopics);
				oldTopics.clear();
				break;
			case APPEND:
				String res = data.get("result");
				break;
			case SELECT:
				manageSelect(data);
				break;
			case NEW:
				break;
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void manageSelect(Map<String, String> xmlData) {
		// TODO Auto-generated method stub
		boolean exist = false;
		Iterator<Entry<String, TopicChat>> iterate = liveTopics.entrySet().iterator();
		while(iterate.hasNext()){
			Map.Entry<String, TopicChat> pair = (Entry<String, TopicChat>) iterate.next();
			if(pair.getKey().equals(xmlData.get("topic"))){
				exist = true;
			}
		}
		if(xmlData.get("username").equals(username) && (exist == false)){
			System.out.println("Time for new Topic");
				TopicChat chat = new TopicChat();
				liveTopics.put(xmlData.get("topic"), chat);
				chat.addListener(new TopicChat.Listener(){

					@Override
					public void sendMessage(String message) {
						// TODO Auto-generated method stub
						//JOptionPane.showMessageDialog(chat, "Oru nimisham machha!!");
						String toSend = "<append><topic>" + xmlData.get("topic") +"</topic><user>" + username + "</user><msg>" + message + "</msg></append>";
						sendData(toSend);
					}

					@Override
					public void closing() {
						// TODO Auto-generated method stub
						System.out.println(xmlData.get("topic") + " window has been closed");
						liveTopics.remove(xmlData.get("topic"));
					}

					@Override
					public void clearChatMessage() {
						// TODO Auto-generated method stub
						chat.clearChatMsg();
					}

					@Override
					public void setChat(Vector<Message> chatMessages) {
						// TODO Auto-generated method stub
						chat.setChatHistory(chatMessages);
					}
					
				});
				chat.setTitle(getUsername() + " - " + xmlData.get("topic"));
			}
	}
	
	private void sendData(String toSend){
		synchronized(sendData){
			msgs.add(toSend);
			sendData.notify();
		}
	}

	private void showTradeDetailFrame(Vector<String> oldTopics) {
		// TODO Auto-generated method stub
		if(tradeDetail == null){
			tradeDetail = new TradeDetail(oldTopics);
			tradeDetail.addListener(new Listener(){

				@Override
				public void selectTopic(String topic) {
					// TODO Auto-generated method stub
						sendData("<select><username>" + username +"</username><topic>" + topic + "</topic></select>");
				}

				@Override
				public void addTopic(String topic) {
					// TODO Auto-generated method stub
						sendData("<new><name>" + topic +"</name></new>");
				}

				@Override
				public void closing() {
					// TODO Auto-generated method stub
					System.out.println("Topic Dashboard has been closed");
					tradeDetail = null;
					checkFrames();
				}
				
			});
		}else{
			tradeDetail.updateTopics(oldTopics);
		}
		tradeDetail.setTitle(username + " @ Dashboard");
	}

	private void getTradeDetail() {
		// TODO Auto-generated method stub
			sendData("<get></get>");
	}
	
	private void checkFrames() {
		// TODO Auto-generated method stub
		if(tradeDetail == null){
			System.exit(0);
		}
	}

	public void login(){
			String log = "<login><username>" + username + "</username><password>" + password + "</password></login>";
			sendData(log);
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