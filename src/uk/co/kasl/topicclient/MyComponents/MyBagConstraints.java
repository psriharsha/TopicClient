package uk.co.kasl.topicclient.MyComponents;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MyBagConstraints extends GridBagConstraints{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyBagConstraints(int x, int y, double weight,int width){
		this.insets = new Insets(10,10,10,10);
		this.gridx = x;
		this.gridy = y;
		this.weightx = weight;
		this.gridwidth = width;
		this.anchor = GridBagConstraints.CENTER;
	}

}
