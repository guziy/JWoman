package main.gui;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public class PosIntInputVerifier  extends InputVerifier{

	
	private int lastGood;
	
	public PosIntInputVerifier(int initialValue) {
		lastGood = initialValue;
	}
	
	@Override
	public boolean verify(JComponent input) {
		// TODO Auto-generated method stub
		try{
			Object v = ((JFormattedTextField)input).getValue();
			
			if (v == null) {
				return false;
			}
			
			int num = Integer.parseInt(v.toString().trim());			
			return num >= 0;
		
		} catch (NumberFormatException nfex){
			return false;
		}
	}

}
