package main.gui;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public class PosIntInputVerifier  extends InputVerifier{

	@Override
	public boolean verify(JComponent input) {
		// TODO Auto-generated method stub
		try{
			Object v = ((JFormattedTextField)input).getValue();
			
			if (v == null) {
				return false;
			}
			
			int num = Integer.parseInt(v.toString());
			
			return num > 0;
			
		} catch (NumberFormatException nfex){
			
		}
		return false;
	}

}
