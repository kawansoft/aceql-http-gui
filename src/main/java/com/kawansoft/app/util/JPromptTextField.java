/**
 * 
 */
package com.kawansoft.app.util;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

/**
 * Equivalent THAT WORKS of JXTextField with prompt support
 * 
 * @author Nicolas de Pomereu
 *
 */
public class JPromptTextField extends JTextField {

    /**
     * 
     */
    private static final long serialVersionUID = -320263239861272140L;

   private JPromptTextField thisOne = this;
        
    private String promptText = null;
 
    UIDefaults defaults = UIManager.getDefaults();

    final Color defaultTextForegroundColor = defaults.getColor("TextField.foreground");
    final Color defaultPromptForegroundColor = Color.GRAY;
        
    private Color textForegroundColor = super.getForeground();
    private Color promptForegroundColor = defaultPromptForegroundColor;
       
    /**
     * 
     */
    public JPromptTextField() {

    }

    /**
     * @param text
     */
    public JPromptTextField(String text) {
	super(text);
    }

    /**
     * @param columns
     */
    public JPromptTextField(int columns) {
	super(columns);

    }

    /**
     * @param text
     * @param columns
     */
    public JPromptTextField(String text, int columns) {
	super(text, columns);
    }

    /**
     * @param doc
     * @param text
     * @param columns
     */
    public JPromptTextField(Document doc, String text, int columns) {
	super(doc, text, columns);
    }
    
    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        //this.textForegroundColor = c;
    }
    
    @Override
    public void setText(String t) {
	super.setText(t);
	
	if (t == null) {
	    super.setText(promptText);
	    super.setForeground(promptForegroundColor);
	}
        else {
            super.setForeground(defaultTextForegroundColor);
        }
    }
    
    
    /**
     * To be use in this class for tests instead of modified this.getText()
     * @return the real text
     */
    private String getRealText() {
        return super.getText();
    }
    
    @Override
    public String getText() {
        if (super.getText().equals(promptText)) {
            return "";
        }
        else {
            return super.getText();
        }
    }
    
      
    /**
     * Returns the prompt text
     * @return the prompt text
     */
    public String getPromptText() {
        return promptText;
    }
    
    /**
     * Sets the prompt text
     * @param promptText	the prompt text to set
     */
    public void setPromptText(String promptText) {
        this.promptText = promptText;
        
	this.setText(promptText);
	this.setForeground(promptForegroundColor);
	
        CaretListener listener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                //System.out.println("Dot: "+ caretEvent.getDot());
                //System.out.println("Mark: "+caretEvent.getMark());

                if (caretEvent.getDot() > 0 && thisOne.getRealText().equals(thisOne.promptText)) {
                    thisOne.setCaretPosition(0);
                }
            }
        };
            
        this.addCaretListener(listener);
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {                
        	if (thisOne.getRealText().equals(thisOne.promptText)) {                    
        	    thisOne.setText("");
        	    thisOne.setForeground(textForegroundColor);
                    thisOne.repaint();
        	}
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                                               
        	if (thisOne.getRealText().isEmpty()) {
        	    thisOne.setText(thisOne.promptText);
        	    thisOne.setForeground(promptForegroundColor);
        	    thisOne.setCaretPosition(0);
        	}
//                else {
//                    thisOne.setForeground(textForegroundColor);
//                    thisOne.repaint();
//                }
            }
        });
        
        this.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
        	if (thisOne.getRealText().equals(thisOne.promptText)) {
        	    thisOne.setCaretPosition(0);
        	    thisOne.setForeground(promptForegroundColor);
        	}
        	else {
        	    thisOne.setForeground(textForegroundColor);
                    thisOne.repaint();
        	}
            }

            @Override
            public void focusLost(FocusEvent fe) {
        	if (thisOne.getRealText().isEmpty()) {
        	    thisOne.setText(thisOne.promptText);
        	    thisOne.setForeground(promptForegroundColor);
        	}
                
                // To be sure
                if (thisOne.getRealText().equals(thisOne.promptText)) {
                   thisOne.setForeground(promptForegroundColor);
                }
            }
        });
        
    }

}
