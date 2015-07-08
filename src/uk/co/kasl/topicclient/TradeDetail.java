package uk.co.kasl.topicclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import uk.co.kasl.topicclient.MyComponents.MyBagConstraints;
import uk.co.kasl.topicclient.MyComponents.MyJFrame;

public class TradeDetail extends MyJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<String> topicList = new Vector<String>();
	
	public TradeDetail(Vector<String> topics){
		frame  = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Topic Dashboard");
		addComponentsToFrame(frame.getContentPane(), topics);
		topics.clear();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}
	
	public TradeDetail(){
		frame  = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToFrame(frame.getContentPane());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}

	private void addComponentsToFrame(Container pane, Vector<String> oldTopics) {
		// TODO Auto-generated method stub
		topicPanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		pane.setLayout(gb);
		topicPanel.setLayout(gb);
		JLabel topic;
		for(int i=0; i<oldTopics.size(); i++){
			topic = new JLabel(oldTopics.get(i));
			System.out.println(oldTopics.get(i));
			topic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
			topicPanel.add(topic,new MyBagConstraints(0, i, 1, 1));
			topicPanel.revalidate();
			topics.add(topic);
			oldTopics.remove(i);
		}		
		pane.add(topicPanel, new MyBagConstraints(0, 0, 1, 1));
		myPane = pane;
		addTopic(1);
		addEventListeners();
	}
	
	private void addComponentsToFrame(Container pane) {
		// TODO Auto-generated method stub
		topicPanel = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		pane.setLayout(gb);
		topicPanel.setLayout(gb);
		JLabel topic = new JLabel("Politics");
		/*topic.setSize(new Dimension(50,100));
		topic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));*/
		topicPanel.add(topic,new MyBagConstraints(0, 0, 1, 1));
		topics.add(topic);
		topic = new JLabel("Sports");
		topic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		topicPanel.add(topic,new MyBagConstraints(0, 1, 1, 1));
		topics.add(topic);
		topic = new JLabel("Entertainment");
		topic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		topicPanel.add(topic,new MyBagConstraints(0, 2, 1, 1));
		topics.add(topic);
		pane.add(topicPanel, new MyBagConstraints(0, 0, 1, 1));
		myPane = pane;
		addTopic(1);
		addEventListeners();
	}
	
	public void addTopic(int loc){
		addTopic = new JButton("Add a new Topic");
		myPane.add(addTopic,new MyBagConstraints(0, loc, 1, 1));
		addTopic.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String newTopic = null;
				newTopic = JOptionPane.showInputDialog(getParent(), "What are you interested in?","New Topic",JOptionPane.PLAIN_MESSAGE);
				if(newTopic != null && newTopic.trim().length() > 0){
					JLabel newLabel = new JLabel(newTopic);
					topicPanel.add(newLabel, new MyBagConstraints(0, topics.size(), 1, 1));
					topicPanel.revalidate();
					topics.add(newLabel);
					addEventListeners();
					frame.pack();
					for(int i = 0; i < listeners.size() ; i++){
						listeners.get(i).addTopic(newTopic);
					}
					//addTopic(topics.size());
				}
			}
			
		});
	}

	private void addEventListeners() {
		// TODO Auto-generated method stub
		for(int i =0; i< topics.size(); i++){
			JLabel myLabel = topics.get(i);
			myLabel.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					for(int j=0; j< listeners.size(); j++){
						listeners.get(j).selectTopic(myLabel.getText());
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					myLabel.setForeground(Color.RED);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					myLabel.setForeground(Color.BLUE);
					myLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					myLabel.setForeground(Color.BLACK);
				}
				
			});
		}
	}

	@Override
	public void disposeFrame() {
		// TODO Auto-generated method stub
		frame.dispatchEvent(new WindowEvent(frame,WindowEvent.WINDOW_CLOSING));
	}
	
	public interface Listener{
		public void selectTopic(String topic);
		public void addTopic(String topic);
	}
	
	public void addListener(Listener l){
		listeners.add(l);
	}
	
	public void removeListener(Listener l){
		listeners.remove(l);
	}
	
	public static void main(String args[]){
		TradeDetail tradeDetail = new TradeDetail();
		tradeDetail.addListener(new Listener(){

			@Override
			public void selectTopic(String topic) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(tradeDetail, "Stop Joking mate!! You can't access " + topic );
			}

			@Override
			public void addTopic(String topic) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(tradeDetail, "Do you even expect you can make a difference? " + topic);
			}
			
		});
	}
	
	JFrame frame;
	Vector<Listener> listeners = new Vector<Listener>();
	Vector<JLabel> topics = new Vector<JLabel>();
	JButton addTopic;
	Container myPane;
	JPanel topicPanel;

	public void updateTopics(Vector<String> oldTopics) {
		// TODO Auto-generated method stub
		/*for(int i = 0; i<oldTopics.size(); i++){
			boolean flag = true;
			for(int j=0; j<topics.size(); j++){
				if(oldTopics.get(i).equals(topics.get(j))){
					flag = false;
				}
			}
			if(flag){
				JLabel newLabel = new JLabel(oldTopics.get(i));
				topicPanel.add(newLabel, new MyBagConstraints(0, topics.size(), 1, 1));
				topicPanel.revalidate();
				topics.add(newLabel);
			}
		}*/
		if(!oldTopics.containsAll(topics)){
			topics.clear();
			oldTopics.copyInto(topics);
		}
	}

}
