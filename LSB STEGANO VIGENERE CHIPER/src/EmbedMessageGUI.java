//package src;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class EmbedMessageGUI extends JFrame implements ActionListener, ItemListener
{	
	static BackEndHandler client;

	private MyJLabel lblMaster, lblOutput, lblMasterSize, lblMessage;;
	private MyJLabel lblMasterFileSize;
	private MyJLabel lblCompression, lblPassword, lblPassword1, low, high;
	private MyJCheckBox chkCompress, chkEncrypt;
	private MyJSlider   sliderCompression;
	private JPasswordField txtPassword;
	private MyJTextField txtMasterFile, txtOutputFile;
	private JTextArea txtMessage;
	private JScrollPane scrollPane;
	private MyJButton btnGo, btnCancel, btnChangeMasterFile, btnChangeOutputFile;

	public EmbedMessageGUI(BackEndHandler client)
	{
		super("Embedding Message - PS2 Steganography");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		UIManager.put("Label.disabledForeground", new ColorUIResource(Color.BLACK));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Corbel",Font.BOLD,14)));
		this.client= client;
		
		JLabel background = new JLabel(new ImageIcon(getClass().getResource("computer2.jpg")));
		setContentPane(background);
		setLayout(new GridBagLayout());
		
		ImageIcon img = new ImageIcon(getClass().getResource("logo.png"));
		setIconImage(img.getImage());
		
		Font arialFont= new Font("Corbel", Font.BOLD, 13);

		lblMaster= new MyJLabel("Master File :");
		lblOutput= new MyJLabel("Output File :");
		lblMasterSize= new MyJLabel("Size :");
		txtMasterFile= new MyJTextField(client.getMasterFile().getName(), 13, Color.YELLOW, Color.DARK_GRAY);
		txtMasterFile.setEditable(false);
		lblMasterFileSize= new MyJLabel(""+ client.getMasterFile().length()/1024+ " Kb", arialFont, Color.YELLOW, Color.DARK_GRAY);

		txtOutputFile= new MyJTextField(client.getOutputFile().getName(), 13, Color.YELLOW, Color.DARK_GRAY);
		txtOutputFile.setEditable(false);
		lblMessage= new MyJLabel("Message : ", arialFont, Color.YELLOW, Color.DARK_GRAY);
		txtMessage= new JTextArea(8,45);
		txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		scrollPane= new JScrollPane(txtMessage);

		btnChangeMasterFile= new MyJButton("Change");
		btnChangeOutputFile= new MyJButton("Change");
		btnGo= new MyJButton("Proceed");
		btnCancel= new MyJButton("Close");

		// Setup panelFiles
		MyJPanel panelFiles= new MyJPanel();
		GridBagLayout gbl= new GridBagLayout();
		GridBagConstraints gbc= new GridBagConstraints();
		panelFiles.setLayout(gbl);
        gbc.insets=new Insets(6,4,8,4);
        gbc.gridx=0; gbc.gridy=1;
        panelFiles.add(new JLabel("  "),gbc);
		gbc.gridx= 1;	gbc.gridy= 1;	
		panelFiles.add(lblMaster,gbc);
		gbc.gridx= 2;	
		panelFiles.add(txtMasterFile,gbc);
		gbc.gridx= 3;	
		panelFiles.add(lblMasterSize,gbc);
		gbc.gridx= 4;	
		panelFiles.add(lblMasterFileSize,gbc);
		gbc.gridx= 5;	
		panelFiles.add(new MyJLabel("          "),gbc);
		gbc.gridx= 6;	
		panelFiles.add(lblOutput,gbc);
		gbc.gridx= 7;	
		panelFiles.add(txtOutputFile,gbc);
		gbc.gridx= 8;	
		panelFiles.add(new JLabel("  "),gbc);
		gbc.gridx= 9;	
		panelFiles.add(new JLabel("  "),gbc);

		gbc.gridx= 2;	gbc.gridy= 2;	
		gbc.insets=new Insets(2,2,6,2);
		panelFiles.add(btnChangeMasterFile,gbc);
		gbc.gridx= 7;	
		panelFiles.add(btnChangeOutputFile,gbc);
		panelFiles=UtilityOperations.createBorderedPanel(panelFiles, " Files : ");

		// Setup panelFeatures
		lblCompression= new MyJLabel("(Compression level)", arialFont, Color.CYAN, Color.lightGray);
		lblPassword= new MyJLabel("Password : ", arialFont, Color.CYAN, Color.lightGray);
		lblPassword1=new MyJLabel("(Minimum 8 characters)", arialFont, Color.CYAN, Color.lightGray);
		
		chkCompress= new MyJCheckBox("Compress");
		chkEncrypt= new MyJCheckBox("Encrypt");
		
		sliderCompression= new MyJSlider(0, 10, 5);
		sliderCompression.setPaintTicks(true);
		sliderCompression.setPaintLabels(true);
		sliderCompression.setSnapToTicks(true);
		sliderCompression.setMajorTickSpacing(1);

		Hashtable<Integer, JLabel> h= new Hashtable<Integer, JLabel>();
		h.put(new Integer(0), new MyJLabel("0".toString()));
		h.put(new Integer(5), new MyJLabel("5".toString()));
		h.put(new Integer(10), new MyJLabel("10".toString()));
		sliderCompression.setLabelTable(h);
		
		txtPassword= new JPasswordField(17);
		low=new MyJLabel("Low", arialFont, Color.CYAN, Color.lightGray);
		high=new MyJLabel("High", arialFont, Color.CYAN, Color.lightGray);
		
		chkCompress.addItemListener(this);
		chkEncrypt.addItemListener(this);
		lblCompression.setEnabled(false);
		sliderCompression.setEnabled(false);
		lblPassword.setEnabled(false);
		lblPassword1.setEnabled(false);
		txtPassword.setEnabled(false);
		low.setEnabled(false);
		high.setEnabled(false);

		MyJPanel panelCompression1= new MyJPanel();
		new BoxLayout(panelCompression1, BoxLayout.X_AXIS);
		panelCompression1.add(chkCompress);
		panelCompression1.add(lblCompression);

		MyJPanel panelCompression2= new MyJPanel();
		new BoxLayout(panelCompression2, BoxLayout.X_AXIS);		
		panelCompression2.add(low);
		panelCompression2.add(sliderCompression);
		panelCompression2.add(high);

		MyJPanel panelCompression= new MyJPanel();
		gbl= new GridBagLayout();
		panelCompression.setLayout(gbl);
		gbc.insets=new Insets(2,2,2,2);
		gbc.gridx= 1; gbc.gridy=1; 
		panelCompression.add(panelCompression1,gbc);
		gbc.gridy= 2; 
		panelCompression.add(panelCompression2,gbc);
		panelCompression= UtilityOperations.createBorderedPanel(panelCompression, " Compression : ");

		MyJPanel panelEncryption1= new MyJPanel();
		panelEncryption1.setLayout(gbl);
		gbc.insets=new Insets(4,4,4,4);
		gbc.gridx=1; gbc.gridy=1;
		panelEncryption1.add(chkEncrypt,gbc);
		gbc.gridy=2;
		panelEncryption1.add(lblPassword,gbc);
		gbc.gridx=2;
		panelEncryption1.add(txtPassword,gbc);
		
		MyJPanel panelEncryption2= new MyJPanel();
		panelEncryption2.add(lblPassword1);
		
		MyJPanel panelEncryption=new MyJPanel();
		gbl= new GridBagLayout();
		panelEncryption.setLayout(gbl);
		gbc.insets=new Insets(2,2,2,2);
		gbc.gridx= 1;	gbc.gridy= 1;	
		panelEncryption.add(panelEncryption1,gbc);
		gbc.gridy= 2;	
		panelEncryption.add(panelEncryption2,gbc);
		panelEncryption= UtilityOperations.createBorderedPanel(panelEncryption, " Encryption : ");		
		
		MyJPanel panelFeatures= new MyJPanel();
		new BoxLayout(panelFeatures, BoxLayout.X_AXIS);
		panelFeatures.add(panelCompression);
		panelFeatures.add(panelEncryption);
		
		// Setup panelText
		MyJPanel panelText= new MyJPanel();
		gbl= new GridBagLayout();		
		panelText.setLayout(gbl);
		gbc.gridy= 2;	gbc.anchor= GridBagConstraints.WEST;	
		panelText.add(lblMessage,gbc);
		gbc.gridy= 3;	gbc.anchor= GridBagConstraints.CENTER;	
		panelText.add(scrollPane,gbc);

		// Setup panelButtons
		MyJPanel panelButtons= new MyJPanel();
		gbl= new GridBagLayout();
		panelButtons.setLayout(gbl);
		gbc.insets=new Insets(2,2,2,2);
		gbc.anchor= GridBagConstraints.CENTER;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridx= 1; gbc.gridy= 1;	
		panelButtons.add(btnGo,gbc);
		gbc.gridy= 2;
		panelButtons.add(btnCancel,gbc);

		MyJPanel panelLower= new MyJPanel();
		new BoxLayout(panelLower, BoxLayout.X_AXIS);
		panelLower.add(panelText);
		panelLower.add(panelButtons);		

		MyJPanel mainPanel= new MyJPanel();
		new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(new MyJLabel("  "));
		mainPanel.add(panelFiles);
		mainPanel.add(panelFeatures);
		mainPanel.add(panelLower);
		mainPanel.setPreferredSize(new Dimension(800,450));
		mainPanel= UtilityOperations.createBorderedPanel(mainPanel, "");
		
		Container tempPanel=getContentPane();
	    tempPanel.add(mainPanel);

		btnChangeMasterFile.addActionListener(this);
		btnChangeOutputFile.addActionListener(this);
		btnGo.addActionListener(this);
		btnCancel.addActionListener(this);

		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		int width= (int)(0.644* d.width);
		int height= (int)(0.7* d.height);
		setSize(width, height);
		setLocation((int)(d.width- 0.65*d.width)/2, (int)(d.height- 0.8*d.height)/2);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source= e.getSource();

		if(source== btnChangeMasterFile)
		{
			client.chooseMasterFile();
			txtMasterFile.setText(client.getMasterFile().getName());
			lblMasterFileSize.setText(""+ client.getMasterFile().length()/1024+ "Kb");
		}

		if(source== btnChangeOutputFile)
		{
			client.chooseOutputFile();
			txtOutputFile.setText(client.getOutputFile().getName());
		}

		if(source== btnCancel)
			dispose();

		if(source== btnGo)
		{
			int compression= -1;
			String password= null;

			if(txtMessage.getText().length()<1)
			{
				JOptionPane.showMessageDialog(this, "Please enter the message\nYou can also paste the message on clipboard using\nCtrl+V combination.", "Empty message!", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if(chkCompress.isSelected())
				compression= sliderCompression.getValue();

			if(chkEncrypt.isSelected())
			{
				password= new String(txtPassword.getPassword());
				if(password.length()<8)
				{
					JOptionPane.showMessageDialog(this, "Password needs to be a minimum of 8 Characters!", "Invalid password!", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			if(client.getOutputFile().exists())
			{
				int result2= JOptionPane.showConfirmDialog(null, "File "+ client.getOutputFile().getName()+ " already exists!\nWould you like to OVERWRITE it?", "File already exists!", JOptionPane.YES_NO_OPTION);
				if(!(result2== JOptionPane.YES_OPTION))
				if(!client.chooseOutputFile())
					return;
			}

			if(Steganograph.embedMessage(client.getMasterFile(), client.getOutputFile(), txtMessage.getText(), compression, password))
				JOptionPane.showMessageDialog(this, Steganograph.getMessage(), "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, Steganograph.getMessage(), "Operation Unsuccessful", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void itemStateChanged(ItemEvent e)
	{
		if(e.getSource()== chkCompress)
		{
			if(chkCompress.isSelected())
			{
				lblCompression.setEnabled(true);
				sliderCompression.setEnabled(true);
				low.setEnabled(true);
				high.setEnabled(true);
			}
			else
			{
				lblCompression.setEnabled(false);
				sliderCompression.setEnabled(false);
				low.setEnabled(false);
				high.setEnabled(false);
			}
		}
		else
		{
			if(chkEncrypt.isSelected())
			{
				lblPassword.setEnabled(true);
				lblPassword1.setEnabled(true);
				txtPassword.setEnabled(true);
			}
			else
			{
				lblPassword.setEnabled(false);
				lblPassword1.setEnabled(false);
				txtPassword.setEnabled(false);
			}
		}
	}
	public static void main(String args[])
	{
		new EmbedMessageGUI(client);
	}
}