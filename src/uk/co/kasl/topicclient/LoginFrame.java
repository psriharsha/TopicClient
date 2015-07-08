package uk.co.kasl.topicclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import uk.co.kasl.topicclient.MyComponents.MyBagConstraints;
import uk.co.kasl.topicclient.MyComponents.MyJFrame;

public class LoginFrame extends MyJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginFrame(){
		frame = new JFrame("Topic Client");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addComponentsToFrame(frame.getContentPane());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
	}
	
	public LoginFrame(String user, String pass){
		frame = new JFrame("Topic Client");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addComponentsToFrame(frame.getContentPane());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
		txtuser.setText(user);
		txtpass.setText(pass);
	}

	private void addComponentsToFrame(Container pane) {
		// TODO Auto-generated method stub
		GridBagLayout gb = new GridBagLayout();
		pane.setLayout(gb);
		RepaintManager myPanelsManager = RepaintManager.currentManager(pane);
		lbluser = new JLabel("Username");
		pane.add(lbluser, new MyBagConstraints(0,0,0.5,1));
		txtuser = new JTextField(15);
		pane.add(txtuser, new MyBagConstraints(1,0,0.5,1));
		lblpass = new JLabel("Password");
		pane.add(lblpass, new MyBagConstraints(0,1,0.5,1));
		txtpass = new JPasswordField(15);
		pane.add(txtpass, new MyBagConstraints(1,1,0.5,1));
		btnsbmt = new JButton("Log Me in!!");
		pane.add(btnsbmt, new MyBagConstraints(0,2,1.0,2));
		lblresult = new JLabel("");
		lblresult.setForeground(Color.RED);
		lblresult.setText("   ");
		pane.add(lblresult, new MyBagConstraints(0,3,1.0,2));
		
		btnsbmt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				/*String message = "<login><username>" + txtuser.getText() + "</username><password>" + new String(txtpass.getPassword()) + "</password></login>";
				TopicClient client = TopicClient.getTopicClient();
				client.sendMessage(message);*/
				for(int i= 0 ; i< loginListeners.size(); i++){
					loginListeners.get(i).submit(txtuser.getText(), new String(txtpass.getPassword()));
				}
			}
			
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoginFrame loginFrame = new LoginFrame();
		loginFrame.addListener(new Listener(){

			@Override
			public void submit(String user, String pass) {
				// TODO Auto-generated method stub
				loginFrame.setResult("I know you're Joking.");
			}

			@Override
			public void cancel() {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
	}
	
	public void addListener(Listener l){
		loginListeners.add(l);
	}
	
	public interface Listener{
		public void submit(String user, String pass);
		public void cancel();
	}
	
	public void setResult(String text) {
		// TODO Auto-generated method stub
		lblresult.setText(text);
	}
	
	JLabel lbluser, lblpass, lblresult;
	JTextField txtuser;
	JPasswordField txtpass;
	JButton btnsbmt;
	Vector<Listener> loginListeners = new Vector<Listener>();
	JFrame frame;

	@Override
	public void disposeFrame() {
		// TODO Auto-generated method stub
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
