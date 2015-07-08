package uk.co.kasl.topicclient.MyComponents;

import javax.swing.JFrame;

public abstract class MyJFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract void disposeFrame();
	
	public abstract void setTitle(String title);
	
}