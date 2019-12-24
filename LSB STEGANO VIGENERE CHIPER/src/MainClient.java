//package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;

public class MainClient extends WindowAdapter implements ActionListener
{   
	Font font;
	private JFrame mainFrame;
	private JPanel mainPanel, panelButtons;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private MyJButton btnEmbedFile, btnRetrieveFile, btnEmbedMessage, btnRetrieveMessage, btnClose;
	private MyJButton btnAbout;
	private BackEndHandler back;

	private MainClient()throws Exception
	{		
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Corbel",Font.BOLD,14)));
		
		mainFrame= new JFrame("PS2 Steganography");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.addWindowListener(this); 
		
		JLabel background = new JLabel(new ImageIcon(getClass().getResource("computer.jpg")));
		mainFrame.setContentPane(background);
		mainFrame.setLayout(new GridBagLayout());
		
		ImageIcon img = new ImageIcon(getClass().getResource("logo.png"));
		mainFrame.setIconImage(img.getImage());
		
		mainPanel= new MyJPanel();
		panelButtons= new MyJPanel();

		// Prepare About panel
		gbl= new GridBagLayout();
		gbc= new GridBagConstraints();
			
		// Prepare the Buttons panel
		panelButtons.setBackground(Color.PINK);
		gbl= new GridBagLayout();
		panelButtons.setLayout(gbl);
		panelButtons.setPreferredSize(new Dimension(500,300));
		LineBorder lb=new LineBorder(new Color(200,200,200), 3);
		TitledBorder name=new TitledBorder(" Supported Operations : ");
		name.setBorder(lb);
		name.setTitleFont(new Font("Calibri", Font.BOLD, 18));
		name.setTitleColor(Color.WHITE);
		panelButtons.setBorder(name);
		
		btnEmbedMessage=    new MyJButton("Embed Message");
		btnEmbedFile=       new MyJButton("Embed File");
		btnRetrieveMessage= new MyJButton("Retrieve Message");
		btnRetrieveFile=    new MyJButton("Retrieve File");
		btnAbout=           new MyJButton("About");
		btnClose=			new MyJButton("Close");

		// Add action listeners for the buttons
		btnEmbedMessage.addActionListener(this);
		btnEmbedFile.addActionListener(this);
		btnRetrieveMessage.addActionListener(this);
		btnRetrieveFile.addActionListener(this);
		btnAbout.addActionListener(this);
		btnClose.addActionListener(this);
		
		// Add buttons to Panel
		gbc.insets=new Insets(4,4,4,4);
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.fill= GridBagConstraints.HORIZONTAL;

        gbc.gridy=0;	
		panelButtons.add(btnEmbedMessage,gbc);
		
		gbc.gridy=1;	
		panelButtons.add(btnEmbedFile,gbc);

		gbc.gridy=2; 	
		panelButtons.add(btnRetrieveMessage,gbc);

		gbc.gridy=3;	
		panelButtons.add(btnRetrieveFile,gbc);

		gbc.gridy=4;  
	    panelButtons.add(btnAbout,gbc);
	    
	    gbc.gridy=5;
	    panelButtons.add(btnClose, gbc);
			
		// Add the Panel to the mainPanel
		gbl= new GridBagLayout();
		mainPanel.setLayout(gbl);

	    JLabel myLabel=new JLabel("Welcome to PS2 Steganography Environment");
	    myLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
	    myLabel.setForeground(new Color(116,59,76));
		gbc.gridy=1; 
		mainPanel.add(myLabel,gbc);
		gbc.gridy=2;	
		mainPanel.add(new JLabel(" "),gbc);
		gbc.gridy=3;
		mainPanel.add(panelButtons,gbc);
		
		Container tempPanel= mainFrame.getContentPane();
	    tempPanel.add(mainPanel);
   	    
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize((int)(d.width*0.7), (int)(d.height*0.6));
		mainFrame.setLocation((int)(d.width- 0.7*d.width)/2, (int)(d.height- 0.7*d.height)/2);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);	
	}

	// Listener methods
	public void actionPerformed(ActionEvent e)
	{	Object source= e.getSource();

		//Embed message operation
		if(source== btnEmbedMessage)
		{	back= new BackEndHandler(this, BackEndHandler.EMBED_MESSAGE);
			back.start();
		}

		// Retrieve message operation
		if(source== btnRetrieveMessage)
		{	back= new BackEndHandler(this, BackEndHandler.RETRIEVE_MESSAGE);
			back.start();
		}

		// Embed file operation
		if(source== btnEmbedFile )
		{	back= new BackEndHandler(this, BackEndHandler.EMBED_FILE);
			back.start();
		}

		// Retrieve file operation
		if(source== btnRetrieveFile )
		{	back= new BackEndHandler(this, BackEndHandler.RETRIEVE_FILE);
			back.start();
		}
        
		//Close window operation
		if(source== btnClose)
		{   int result= JOptionPane.showConfirmDialog(mainFrame, "Are you sure that you want to close PS2 Steganography?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
			if(result== JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
		}
        
		//About Button
		if(source== btnAbout)
			Steganograph.showAboutDialog();	
	}
	
	// Main method
	public static void main(String args[])
	{
		try 
		{  new MainClient(); 
		}
		catch(Exception e) 
		{   e.printStackTrace();
		}
	}
}