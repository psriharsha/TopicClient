package uk.co.kasl.topicclient;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.co.kasl.topicclient.MyComponents.MyBagConstraints;
import uk.co.kasl.topicclient.MyComponents.MyJFrame;

public class TopicChat extends MyJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String topicName = "Something";
	
	public void setTopicName(String newTopic){
		topicName = newTopic;
	}

	@Override
	public void disposeFrame() {
		// TODO Auto-generated method stub
		frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	public interface Listener{
		public void sendMessage(String message);
		public void closing();
		public void clearChatMessage();
		public void setChat(Vector<Message> chatMessages);
	}
	
	public void addListener(Listener l){
		listener = l;
	}
	
	public TopicChat(String newTopic){
		setTopicName(newTopic);
		frame  = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Topic Dashboard");
		addComponentsToFrame(frame.getContentPane());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				listener.closing();
			}
			
		});
		frame.pack();
	}
	
	private void addComponentsToFrame(Container pane) {
		// TODO Auto-generated method stub
		pane.setLayout(new GridBagLayout());
		textArea = new JTextArea(20,35);
		textArea.setEnabled(false);
		scrollPane = new JScrollPane(textArea);
		pane.add(scrollPane, new MyBagConstraints(0, 0, 1, 4));
		txtMsg = new JTextField(25);
		pane.add(txtMsg, new MyBagConstraints(0, 1, 0.75, 2));
		sendMsg = new JButton("Send");
		pane.add(sendMsg, new MyBagConstraints(2, 1, 0.25, 2));
		sendMsg.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				listener.sendMessage(txtMsg.getText().trim());
			}
			
		});
	}
	
	public void clearChatMsg(){
		txtMsg.setText("");
	}

	public TopicChat(){
		this("Something");
	}
	
	public static void main(String args[]){
		TopicChat topicChat = new TopicChat();
		topicChat.addListener(new Listener(){

			@Override
			public void sendMessage(String message) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(topicChat, "You're an Idiot and you don't understand. :(");
			}

			@Override
			public void closing() {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(topicChat, "I would have killed you if you didn't close me.");
			}

			@Override
			public void clearChatMessage() {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(topicChat, "Hahahahaha");
			}

			@Override
			public void setChat(Vector<Message> chatMessages) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(topicChat, "You bloody don't have any messages, and you know this already!!");
			}
			
		});
	}
	
	JFrame frame;
	JScrollPane scrollPane;
	JTextArea textArea;
	JTextField txtMsg;
	JButton sendMsg;
	Listener listener;
	TopicChat topicchat;

}