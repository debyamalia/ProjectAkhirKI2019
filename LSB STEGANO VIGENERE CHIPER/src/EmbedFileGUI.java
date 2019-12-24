//package src;

import javax.swing.*;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import java.awt.*;
import java.awt.event.*;
//import java.io.File;
import java.util.Hashtable;

public class EmbedFileGUI extends JFrame implements ActionListener, ItemListener
{
	BackEndHandler client;

	private MyJLabel lblMaster, lblOutput, lblData; 
	private MyJLabel lblMasterSize, lblDataSize;
	private MyJLabel lblMasterFileSize, lblDataFileSize;
	private MyJLabel lblCompression, lblPassword, lblPassword1, low, high;
	private MyJCheckBox chkCompress, chkEncrypt;
	private MyJSlider	sliderCompression;
	private JPasswordField txtPassword;
	private MyJTextField txtMasterFile, txtOutputFile, txtDataFile;
	private MyJButton btnGo, btnCancel;
	private MyJButton btnChangeMasterFile, btnChangeOutputFile, btnChangeDataFile;

	public EmbedFileGUI(BackEndHandler client)
	{
		super("Embedding File - PS2 Steganography");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		UIManager.put("Label.disabledForeground", new ColorUIResource(Color.BLACK));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Corbel",Font.BOLD,14)));
		this.client= client;

		JLabel background = new JLabel(new ImageIcon(getClass().getResource("computer2.jpg")));
		setContentPane(background);
		setLayout(new GridBagLayout());
		
		ImageIcon img = new ImageIcon(getClass().getResource("logo.png"));
		setIconImage(img.getImage());
		
		Font arialFont= new Font("Corbel", Font.PLAIN, 13);
		lblMaster= new MyJLabel("Master File : ");
		lblOutput= new MyJLabel("Output File : ");
		lblData=   new MyJLabel("Data File   : ");
		lblMasterSize= new MyJLabel("Size : ");
		lblDataSize=   new MyJLabel("Size : ");
		
		txtMasterFile= new MyJTextField(client.getMasterFile().getName(), 13, Color.YELLOW, Color.DARK_GRAY);
		txtMasterFile.setEditable(false);
		txtMasterFile.setMargin(new Insets(1,1,1,1));
		lblMasterFileSize= new MyJLabel(""+ client.getMasterFile().length()/1024+ " Kb", arialFont, Color.YELLOW, Color.DARK_GRAY);

		txtOutputFile= new MyJTextField(client.getOutputFile().getName(), 13, Color.YELLOW, Color.DARK_GRAY);
		txtOutputFile.setEditable(false);
		txtOutputFile.setMargin(new Insets(1,1,1,1));
		txtDataFile= new MyJTextField(client.getDataFile().getName(), 13, Color.YELLOW, Color.DARK_GRAY);
		txtDataFile.setEditable(false);
		txtDataFile.setMargin(new Insets(1,1,1,1));
		lblDataFileSize= new MyJLabel(""+ client.getDataFile().length()/1024+ " Kb", arialFont, Color.YELLOW, Color.DARK_GRAY);

		btnChangeMasterFile= new MyJButton("Change");
		btnChangeOutputFile= new MyJButton("Change");
		btnChangeDataFile=	 new MyJButton("Change");
		btnGo    = new MyJButton("   Proceed    ");
		btnCancel= new MyJButton("    Close     ");

		lblCompression= new MyJLabel("(Compression Level)", arialFont, Color.CYAN, Color.lightGray);
		lblPassword= new MyJLabel("Password ", arialFont, Color.CYAN, Color.lightGray);
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
		
		chkCompress.addItemListener(this);
		chkEncrypt.addItemListener(this);
		lblCompression.setEnabled(false);
		sliderCompression.setEnabled(false);		
		lblPassword.setEnabled(false);
		lblPassword1.setEnabled(false);
		txtPassword.setEnabled(false);

		// Setup panel file1
		MyJPanel file1= new MyJPanel();
		new BoxLayout(file1, BoxLayout.X_AXIS);
		file1.add(lblMaster);
		file1.add(txtMasterFile);
		file1.add(lblMasterSize);
		file1.add(lblMasterFileSize);
		file1.add(btnChangeMasterFile);

		// Setup panel file2
		MyJPanel file2= new MyJPanel();
		new BoxLayout(file2, BoxLayout.X_AXIS);
		file2.add(lblData);
		file2.add(txtDataFile);
		file2.add(lblDataSize);
		file2.add(lblDataFileSize);
		file2.add(btnChangeDataFile);
		
		// Setup panel file3
		MyJPanel file3= new MyJPanel();
		new BoxLayout(file3, BoxLayout.X_AXIS);
		file3.add(lblOutput);
		file3.add(txtOutputFile);
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(new JLabel("  "));
		file3.add(btnChangeOutputFile);

		MyJPanel panelFiles= new MyJPanel();
		GridBagLayout gbl= new GridBagLayout();
		GridBagConstraints gbc= new GridBagConstraints();
		panelFiles.setLayout(gbl);
		gbc.insets=new Insets(2,2,2,2);
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.anchor= GridBagConstraints.WEST;	gbc.gridx=1; gbc.gridy= 1;
		panelFiles.add(file1,gbc);
		gbc.gridy= 2;		
		panelFiles.add(file2,gbc);
		gbc.gridy= 3;		
		panelFiles.add(file3,gbc);
		panelFiles=UtilityOperations.createBorderedPanel(panelFiles, " Files : ");
		
		low=new MyJLabel("Low", arialFont, Color.CYAN, Color.lightGray);
		high=new MyJLabel("High", arialFont, Color.CYAN, Color.lightGray);
		low.setEnabled(false);
		high.setEnabled(false);
		
		// Setup features panel 1
		MyJPanel panelFeatures1a= new MyJPanel();
		new BoxLayout(panelFeatures1a, BoxLayout.X_AXIS);
		panelFeatures1a.add(chkCompress);
		panelFeatures1a.add(lblCompression);

		MyJPanel panelFeatures1b= new MyJPanel();
		new BoxLayout(panelFeatures1b, BoxLayout.X_AXIS);
		panelFeatures1b.add(low);
		panelFeatures1b.add(sliderCompression);
		panelFeatures1b.add(high);

		MyJPanel panelFeatures1= new MyJPanel();
		gbl= new GridBagLayout();
		panelFeatures1.setLayout(gbl);
		gbc.fill=GridBagConstraints.NONE;
		gbc.gridx= 1;	gbc.gridy= 1;	
		panelFeatures1.add(panelFeatures1a,gbc);
		gbc.gridy= 2;	
		panelFeatures1.add(panelFeatures1b,gbc);
		panelFeatures1= UtilityOperations.createBorderedPanel(panelFeatures1, " Compression : ");

		// Setup features panel 2
		MyJPanel panelFeatures2a= new MyJPanel();
		panelFeatures2a.setLayout(gbl);
		gbc.insets=new Insets(2,2,2,2);
		gbc.gridx=1; gbc.gridy=1;
		panelFeatures2a.add(chkEncrypt,gbc);
		gbc.gridy=2;
		panelFeatures2a.add(lblPassword,gbc);
		gbc.gridx=2;
		panelFeatures2a.add(txtPassword,gbc);
		
		MyJPanel panelFeatures2b= new MyJPanel();
		panelFeatures2b.add(lblPassword1);
		
		MyJPanel panelFeatures2=new MyJPanel();
		gbl= new GridBagLayout();
		panelFeatures2.setLayout(gbl);
		gbc.gridx= 1;	gbc.gridy= 1;	
		panelFeatures2.add(panelFeatures2a,gbc);
		gbc.gridy= 2;	
		panelFeatures2.add(panelFeatures2b,gbc);
		panelFeatures2= UtilityOperations.createBorderedPanel(panelFeatures2, " Encryption : ");

		MyJPanel panelFeatures= new MyJPanel();
		gbl= new GridBagLayout();
		panelFeatures.setLayout(gbl);
		gbc.anchor= GridBagConstraints.WEST;	gbc.gridx=1; gbc.gridy= 1;
		panelFeatures.add(panelFeatures1,gbc);
		gbc.gridy= 2;		
		panelFeatures.add(panelFeatures2,gbc);

		// Setup the buttons panel
		MyJPanel panelButtons= new MyJPanel();
		gbl= new GridBagLayout();
		panelButtons.setLayout(gbl);
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		gbc.insets=new Insets(2,2,2,2);
		gbc.gridx=1; gbc.gridy= 1;
		panelButtons.add(new JLabel(" "),gbc);
		gbc.gridy=2;
		panelButtons.add(btnGo,gbc);
		gbc.gridy= 3;		
		panelButtons.add(btnCancel,gbc);

		MyJPanel mainPanel1= new MyJPanel();
		gbl=new GridBagLayout();
		gbc=new GridBagConstraints();
		mainPanel1.setLayout(gbl);
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		gbc.gridx=1; gbc.gridy=1;
		mainPanel1.add(panelFiles,gbc);
		gbc.gridx=2;
		mainPanel1.add(new JLabel(" "),gbc);
		gbc.gridx=3;
		mainPanel1.add(panelFeatures,gbc);
		
		MyJPanel mainPanel2= new MyJPanel();
		mainPanel2.add(panelButtons,gbc);
		
		MyJPanel mainPanel=new MyJPanel();
		mainPanel.setLayout(gbl);
		gbc.gridx=1; gbc.gridy=1; 
		mainPanel.add(mainPanel1,gbc);
		gbc.gridy=2;
		mainPanel.add(mainPanel2,gbc);
		mainPanel.setPreferredSize(new Dimension(850,370));
		mainPanel= UtilityOperations.createBorderedPanel(mainPanel, "");
				
		Container tempPanel=getContentPane();
	    tempPanel.add(mainPanel);

		btnChangeMasterFile.addActionListener(this);
		btnChangeOutputFile.addActionListener(this);
		btnChangeDataFile.addActionListener(this);
		btnGo.addActionListener(this);
		btnCancel.addActionListener(this);		

		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)(d.width*0.644), (int)(d.height*0.6));
		setLocation((int)(d.width- 0.65*d.width)/2, (int)(d.height- 0.7*d.height)/2);
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

		if(source== btnChangeDataFile)
		{
			client.chooseDataFile();
			txtDataFile.setText(client.getDataFile().getName());
			lblDataFileSize.setText(""+ client.getDataFile().length()/1024+ "Kb");
		}

		if(source== btnCancel)
			dispose();

		if(source== btnGo)
		{
			int compression= sliderCompression.getValue();
			String password= null;

			if(chkEncrypt.isSelected())
			{
				password= new String(txtPassword.getPassword());
				if(password.length()<8)
				{
					JOptionPane.showMessageDialog(this, "Password needs to have atleast 8 characters!", "Invalid password!", JOptionPane.WARNING_MESSAGE);
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

			if(Steganograph.embedFile(client.getMasterFile(), client.getOutputFile(), client.getDataFile(), compression, password))
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
		if(e.getSource()== chkEncrypt)
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
}