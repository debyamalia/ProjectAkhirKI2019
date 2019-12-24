//package src;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import java.awt.*;
import java.awt.event.*;

public class PreRetrieveGUI extends JFrame implements ActionListener
{
	public static boolean RETRIEVE_MESSAGE= true;
	public static boolean RETRIEVE_FILE= false;
	private MyJLabel lblMaster, lblMasterFile;
	private MyJLabel lblContains, lblContainsWhat, lblCompressed, lblCompressedStatus;
	private MyJLabel lblCompression, lblCompressionRatio;
	private MyJLabel lblEncrypted, lblEncryptedStatus, lblRequested, lblRequestedOperation;
	private MyJButton btnGo, btnCancel;

	private boolean operation;
	private SteganoInformation info;

	public PreRetrieveGUI(SteganoInformation info, boolean operation)
	{
		super("Master File Information");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		UIManager.put("Label.disabledForeground", new ColorUIResource(Color.CYAN));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Corbel",Font.BOLD,14)));
		
		JLabel background = new JLabel(new ImageIcon(getClass().getResource("computer2.jpg")));
		setContentPane(background);
		setLayout(new GridBagLayout());
		
		ImageIcon img = new ImageIcon(getClass().getResource("logo.png"));
		setIconImage(img.getImage());
		
		this.operation= operation;
		this.info= info;
		Font arialFont= new Font("Corbel", Font.BOLD, 14);

		lblMaster= new MyJLabel("Master File", arialFont, Color.CYAN, Color.gray);
		lblMasterFile= new MyJLabel(info.getFile().getName(), arialFont, Color.WHITE, Color.gray);
		lblContains= new MyJLabel("Contains", arialFont, Color.CYAN, Color.gray);

		byte features= info.getFeatures();
		if(features == Steganograph.UUM || features== Steganograph.UEM
				|| features == Steganograph.CUM || features== Steganograph.CEM)
			lblContainsWhat= new MyJLabel("Embedded Message", arialFont, Color.WHITE, Color.gray);
		else
			lblContainsWhat= new MyJLabel("Embedded File", arialFont, Color.WHITE, Color.gray);

		lblCompressed= new MyJLabel("Compressed", arialFont, Color.CYAN, Color.gray);
		lblCompression= new MyJLabel("Compression Ratio", arialFont, Color.CYAN, Color.gray);
		lblCompressionRatio= new MyJLabel(""+ info.getCompressionRatio()+ " %", arialFont, Color.WHITE, Color.gray);
		
		if(features == Steganograph.CUM || features== Steganograph.CUF
				|| features == Steganograph.CEM || features== Steganograph.CEF)
			lblCompressedStatus= new MyJLabel("YES", arialFont, Color.WHITE, Color.gray);
		else
		{
			lblCompressedStatus= new MyJLabel("NO", arialFont, Color.WHITE, Color.gray);
			lblCompression.setEnabled(false);
			lblCompressionRatio.setEnabled(false);
		}
		
		lblEncrypted= new MyJLabel("Encrypted", arialFont, Color.CYAN, Color.gray);
		if(features== Steganograph.UEM || features== Steganograph.CEM
				|| features== Steganograph.UEF || features== Steganograph.CEF)
			lblEncryptedStatus= new MyJLabel("YES", arialFont, Color.WHITE, Color.gray);
		else
			lblEncryptedStatus= new MyJLabel("NO", arialFont, Color.WHITE, Color.gray);

		lblRequested= new MyJLabel("Requested Operation", arialFont, Color.CYAN, Color.gray);

		if(operation== RETRIEVE_MESSAGE)
			lblRequestedOperation= new MyJLabel("Retrieve Message", arialFont, Color.WHITE, Color.gray);
		else
			lblRequestedOperation= new MyJLabel("Retrieve File", arialFont, Color.WHITE, Color.gray);

		btnGo= new MyJButton("Proceed");
		btnCancel= new MyJButton("Cancel");
		btnGo.addActionListener(this);
		btnCancel.addActionListener(this);

		MyJPanel panelUpper= new MyJPanel();
		GridBagLayout gbl= new GridBagLayout();
		GridBagConstraints gbc= new GridBagConstraints();
		panelUpper.setLayout(gbl);

		gbc.anchor= gbc.WEST;
		gbc.insets=new Insets(4,4,4,4);
		gbc.gridx= 1; gbc.gridy= 1;	
		panelUpper.add(lblMaster,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblMasterFile,gbc);
		gbc.gridx= 1; gbc.gridy= 2;	
		panelUpper.add(lblContains,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblContainsWhat,gbc);
		gbc.gridx= 1; gbc.gridy= 3;	
		panelUpper.add(lblCompressed,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblCompressedStatus,gbc);
		gbc.gridx= 1; gbc.gridy= 4;	
		panelUpper.add(lblCompression,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblCompressionRatio,gbc);
		gbc.gridx= 1; gbc.gridy= 5;	
		panelUpper.add(lblEncrypted,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblEncryptedStatus,gbc);
		gbc.gridx= 2; gbc.gridy= 6;	
		panelUpper.add(new JLabel("   "),gbc);
		gbc.gridx= 1; gbc.gridy= 7;	
		panelUpper.add(lblRequested,gbc);
		gbc.gridx= 3; 
		panelUpper.add(lblRequestedOperation,gbc);

		MyJPanel panelLower= new MyJPanel();
		new BoxLayout(panelLower, BoxLayout.X_AXIS);
		panelLower.add(btnGo);
		panelLower.add(new JLabel("   "));
		panelLower.add(btnCancel);
        panelUpper.setPreferredSize(new Dimension(400,200));
		panelUpper= UtilityOperations.createBorderedPanel(panelUpper, " File Information : ");

		MyJPanel mainPanel= new MyJPanel();
		gbl= new GridBagLayout();
		mainPanel.setLayout(gbl);
		gbc.anchor= GridBagConstraints.CENTER;
		gbc.gridx= 1; gbc.gridy= 1; 
		mainPanel.add(panelUpper,gbc);
		gbc.gridy= 2; 
		mainPanel.add(new JLabel("   "),gbc);
		gbc.gridy= 3; 
		mainPanel.add(panelLower,gbc);

		Container tempPanel=getContentPane();
	    tempPanel.add(mainPanel);
	    
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		int width= (int)(0.4* d.width);
		int height= (int)(0.5* d.height);
		setSize(width, height);
		setLocation((d.width- width)/2, (d.height- height)/2);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{   
		Font arialFont = new Font("Corbel", Font.BOLD, 13); 
		
		if(e.getSource()== btnCancel)
			dispose();
		else
		{
			int result;
			String password= null;
			if(lblEncryptedStatus.getText().equals("YES"))
			{
				Object message[]= new Object[3];
				message[0]= new MyJLabel("This is an encrypted zone.", arialFont , Color.red, Color.gray);
				message[1]= new MyJLabel("Please enter password to continue..", arialFont , Color.CYAN, Color.gray );
				JPasswordField pass= new JPasswordField(17);
				message[2]= pass;
				String options[]= {"Retrieve Now", "Cancel"};
				do
				{
					result= JOptionPane.showOptionDialog(null, message, "Encrypted Zone"
						, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

					if(result== 1)
						return;
					password= new String(pass.getPassword());
					if(password.length()<8)
						JOptionPane.showMessageDialog(null, "Length of password should be atleast 8 Characters", "Invalid password", JOptionPane.OK_OPTION);
				}while(password.length()<8);				
			}

			if(operation== RETRIEVE_MESSAGE	&&
					lblContainsWhat.getText().equals("Embedded File"))
			{
				result= JOptionPane.showConfirmDialog(null, "This file contains an embedded file\nwhereas you have requested to retrieve a message.\n\nWould you like to retrieve the file instead?", "Incorrect Request!", JOptionPane.YES_NO_OPTION);
				if(result== JOptionPane.NO_OPTION)
					dispose();
			}

			if(operation== RETRIEVE_FILE &&
					lblContainsWhat.getText().equals("Embedded Message"))
			{
				result= JOptionPane.showConfirmDialog(null, "This file contains an embedded message\nWhereas you have requested to retrieve a file.\n\nWould you like to retrieve the message instead?", "Incorrect Request!", JOptionPane.YES_NO_OPTION);
				if(result== JOptionPane.NO_OPTION)
				{
					dispose();
					return;
				}
			}

			if(lblContainsWhat.getText().equals("Embedded Message"))
			{
				String message= Steganograph.retrieveMessage(info, password);
				if(message!=null && !message.equals("#FAILED#"))
					new MessageDisplay(message, info.getFile().getName());
				else
				{
					message= Steganograph.getMessage();
					if(message!= null && message.equals("Incorrent Password"))
						JOptionPane.showMessageDialog(null, "Incorrect Password Specified!\nMake sure that CAPS LOCK is not on.", "Incorrect Password!", JOptionPane.WARNING_MESSAGE);
					else 
						JOptionPane.showMessageDialog(null, "Error!\n"+ Steganograph.getMessage(), "Oops, Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				boolean res= Steganograph.retrieveFile(info, password, false);
				if(!res)
				{
					String message= Steganograph.getMessage();
					if(message!= null)
						if(message.equals("Incorrent Password"))
							JOptionPane.showMessageDialog(null, "Incorrect Password Specified!\nMake sure that CAPS LOCK is not on.", "Incorrect Password!", JOptionPane.WARNING_MESSAGE);
						else if(message.equals("File Exists"))
							 {							
								result= JOptionPane.showConfirmDialog(null, "The data file '"+ info.getDataFile().getName()+ "' being retrieved already exists!\nWould you like to OVERWRITE it?", "Confirm OVERWRITE", JOptionPane.YES_NO_OPTION);
								if(result== JOptionPane.NO_OPTION)
								return;

								Steganograph.retrieveFile(info, password, true);
								showFile(info);
							 }
							 else
								JOptionPane.showMessageDialog(null, "Error!\n"+ Steganograph.getMessage(), "Oops, Error!", JOptionPane.ERROR_MESSAGE);
				}
				else
					showFile(info);
			}
		}
	}

	private void showFile(SteganoInformation info)
	{
		int result= JOptionPane.showConfirmDialog(null, "The data file '"+ info.getDataFile().getName()+ "' has been successfully retrieved as\n"+ info.getDataFile().getPath()+ "\n\nWould you like to open it now?", "Operation Successful", JOptionPane.YES_NO_OPTION);
		if(result== JOptionPane.YES_OPTION)
		{
			String osName= System.getProperty("os.name");
			if(osName.length()>=7)
				osName= osName.substring(0, 7);
			if(osName.compareToIgnoreCase("windows")== 0)
				try
				{
					Runtime.getRuntime().exec("explorer "+ info.getDataFile().getPath());
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Oops!!  Error!\n"+ ex, "Error!", JOptionPane.WARNING_MESSAGE);
				}
			else
				JOptionPane.showMessageDialog(null, "Sorry!\nI just discovered that you are not running a Windows operating system.\nI will not be able to open this file for you at this time.", "Non Windows OS!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

// Class to display retrieved message
class MessageDisplay extends JFrame
{
	public MessageDisplay(String message, String fileName)
	{
		super("Retrieved Message from file '"+ fileName+ "' - PS2 Steganography");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JTextArea secretMessage = new JTextArea(message, 8, 35);
		secretMessage.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		getContentPane().add(new JScrollPane(secretMessage));
		
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		int width= (int)(0.55* d.width);
		int height= (int)(0.6* d.height);
		setSize(width, height);
		setLocation((int)(d.width- 0.55*d.width)/2, (int)(d.height- 0.8*d.height)/2);
		setVisible(true);
	}
}