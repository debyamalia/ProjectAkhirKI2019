//package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

class MyJButton extends JButton
{
	public MyJButton(String icon)
	{
		super(icon);
		setFont(new Font("Corbel", Font.BOLD, 14));
		setContentAreaFilled(true);
		setBorderPainted(true);
	}
}

class MyJLabel extends JLabel
{
	public MyJLabel(String caption)
	{
		super(caption);
		setForeground(Color.WHITE);
		setFont(new Font("Corbel", Font.BOLD, 13));
	}
	
	public MyJLabel(String caption, Font font, Color foreground, Color background)
	{
		super(caption);
		setForeground(foreground);
		setBackground(background);
		setFont(new Font("Corbel", Font.BOLD, 13));
	}
}

class MyJTextField extends JTextField
{
	public MyJTextField(String caption, int size, Color foreground, Color background)
	{
		super(caption, size);
		setForeground(foreground);
		setBackground(background);
		setFont(new Font("Corbel",Font.BOLD,13));
	}
}

class MyJPanel extends JPanel
{
      private Image img;
	  public MyJPanel() 
	  {	  super();
		  this.setOpaque(false);
	  }
	  public MyJPanel(Image img) 
	  {  this.img = img; 
	  }
}

class MyJCheckBox extends JCheckBox
{  
	public MyJCheckBox(String caption) 
	  { super(caption);
	    setFont(new Font("Corbel",Font.BOLD,13));
	    setForeground(Color.WHITE);
	    setOpaque(false);
	  }
}

class MyJSlider extends JSlider
{
    public MyJSlider(int x, int y, int z)
    {	super(x,y,z);
        setOpaque(false);
        setForeground(Color.CYAN);
    }
}

// Class to perform utility operations
class UtilityOperations
{	
	public static MyJPanel createBorderedPanel(MyJPanel panel, String title)
	{
		MyJPanel newPanel= new MyJPanel();
		LineBorder lb=new LineBorder(new Color(200,200,200), 3);
		TitledBorder name=new TitledBorder(title);
		name.setBorder(lb);
		name.setTitleFont(new Font("Calibri", Font.BOLD, 18));
		name.setTitleColor(Color.WHITE);
		newPanel.setLayout(new BorderLayout());
		newPanel.add(panel, BorderLayout.CENTER);
		newPanel.setBorder(name);
		return newPanel;
	}
}