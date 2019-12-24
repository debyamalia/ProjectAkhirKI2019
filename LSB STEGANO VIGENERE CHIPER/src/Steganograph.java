//package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.zip.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Steganograph
{
    // Three letters indicate:Uncompressed/Compressed Encrypted/Unencrypted Message/File
	public static final byte UUM= 0;
	public static final byte UUF= 1;
	public static final byte UEM= 2;
	public static final byte UEF= 3;
	public static final byte CUM= 4;
	public static final byte CUF= 5;
	public static final byte CEM= 6;
	public static final byte CEF= 7;

	private static Cipher cipher;

	private static SecretKeySpec spec;
	private static String message;
	private static AboutFrame about= new AboutFrame();

    //This byte stores the features being used by the file
    private static byte features;
	private static int i, j, messageSize, tempInt;
	private static short compressionRatio= 0;
	private static byte byteArrayIn[], tempByte[];

	private Steganograph()
	{
		System.out.println("PS2 Steganography file is "+" ready!");
	}

	public static String getMessage()
	{
		return message;
	}

	// Embeds a message into a Master file
	public static boolean embedMessage(File masterFile, File outputFile, String msg, int compression, String password)
	{
		if(msg==null||msg.length()<1)
		{
			message= "Message is empty";
			return false;
		}
		
		if(compression!= -1)
		{
			// Make sure that the compression is a valid numerical between 0 and 10
			if(compression<0)	compression= 0;
			if(compression>9)	compression= 9;

			if(password==null)	features= CUM;
			else				features= CEM;
		}
		else
		{
			if(password==null)	features= UUM;
			else				features= UEM;
		}

		try
		{
			// Convert message into a character array
			byte[] messageArray= msg.getBytes();
			messageSize= messageArray.length;
			
			BufferedImage bfimage = ImageIO.read(masterFile);
			
			int imageWidth = bfimage.getWidth(), imageHeight = bfimage.getHeight();
			int imageSize = imageWidth * imageHeight;
			
			if(messageSize * 8 + 48 > imageSize) 
            {
		       message="Message is too long for the chosen image.";
			   return false;
	        }
			
			// Write 1 byte for features
			embedByte(bfimage, features, 0, 0);

			// Compress the message if required
			if(features== CUM || features== CEM)
			{
				ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
				ZipOutputStream zOut= new ZipOutputStream(arrayOutputStream);
				ZipEntry entry= new ZipEntry("MESSAGE");
				zOut.setLevel(compression);
				zOut.putNextEntry(entry);
			    zOut.write(messageArray, 0, messageSize);
				zOut.closeEntry();
				zOut.finish();
				zOut.close();
				// Get the compressed message byte array
				messageArray= arrayOutputStream.toByteArray();
				compressionRatio= (short) ((double)messageArray.length / (double)messageSize * 100.0);
				messageSize= messageArray.length;
			}
			else
				compressionRatio=0;
			
			// Embed 1 byte compression ratio into the output file
			embedByte(bfimage,(byte)compressionRatio, 8, 0);

			// Encrypt the message if required
			if(features== UEM || features== CEM)
			{
				MessageDigest md=MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes(StandardCharsets.UTF_8));
				byte[] mdbyte=md.digest();
				byte[] key=new byte[mdbyte.length/4];
				
				for(int i=0;i<key.length;i++)
					key[i]=mdbyte[i];

			//	for(int i=0;i<key.length;i++)
					System.out.println(key.toString());
				
				Cipher cipher= Cipher.getInstance("DES");
				SecretKeySpec spec= new SecretKeySpec(key, "DES");
				cipher.init(Cipher.ENCRYPT_MODE, spec);
				messageArray= cipher.doFinal(messageArray);
				messageSize= messageArray.length;
				
				System.out.println("Cipher");
//				for(int i=0;i<messageArray.length;i++)
					System.out.println(messageArray.toString());
			}

			// Convert the 32 bit message size into byte array
			tempByte= new byte[4];
			for(i=24, j=0; i>=0; i-=8, j++)
			{
				tempInt= messageSize;
				tempInt>>= i;
				tempInt&= 0x000000FF;
				tempByte[j]= (byte) tempInt;
			}
			
			// Embed 4 byte messageSize array into the master file
			for(int i=0; i<tempByte.length; i++)
				embedByte(bfimage, tempByte[i], i*8+16, 0);

			// Embed the message
			for(int i=0; i<messageArray.length; i++)
				embedByte(bfimage, messageArray[i], i*8+48, 0);	
			
			ImageIO.write(bfimage,"png",outputFile);		 
		}
		catch(EOFException e)
		{
		}
		catch(Exception e)
		{
			message= "Oops!!\nError: "+ e.toString();
			e.printStackTrace();
			return false;
		}

		message= "Message embedded successfully in file '"+ outputFile.getName()+ "'.";
		return true;
	}

    // Retrieves an embedded message from a Master file
	public static String retrieveMessage(SteganoInformation info, String password)
	{
		String messg= null;
		features= info.getFeatures();
		
		try
		{
			messageSize= info.getDataLength();

			if(messageSize<=0)
			{	message= "Unexpected size of message: 0.";
				return("#FAILED#");
			}

			byte[] messageArray= new byte[messageSize];
			messageArray=info.getData();

			//Decrypt the message if required
			if(features== CEM || features== UEM)
			{		
				MessageDigest md=MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes(StandardCharsets.UTF_8));
				byte[] mdbyte=md.digest();
				byte[] key=new byte[mdbyte.length/4];
				
				for(int i=0;i<key.length;i++)
					key[i]=mdbyte[i];

				cipher= Cipher.getInstance("DES");
				spec= new SecretKeySpec(key, "DES");
				cipher.init(Cipher.DECRYPT_MODE, spec);
				try
				{
					messageArray= cipher.doFinal(messageArray);
				}
				catch(Exception bp)
				{
					message= "Incorrent Password";
					bp.printStackTrace();
					return "#FAILED#";
				}
				messageSize= messageArray.length;
			}
			
			// Uncompress the message if required
			if(features== CUM || features== CEM)
			{
				ByteArrayOutputStream by= new ByteArrayOutputStream();
				DataOutputStream out= new DataOutputStream(by);
				ZipInputStream zipIn= new ZipInputStream(new ByteArrayInputStream(messageArray));
				zipIn.getNextEntry();
				byteArrayIn= new byte[1024];
				while((tempInt= zipIn.read(byteArrayIn, 0, 1024))!= -1)
					out.write(byteArrayIn, 0, tempInt);
				zipIn.close();
				out.close();
				messageArray= by.toByteArray();
				messageSize= messageArray.length;
			}
			messg= new String((messageArray));
		}
		catch(Exception e)
		{
			message= "Oops!!\n Error: "+ e;
			e.printStackTrace();
			return("#FAILED#");
		}
		
		message= "Message Size: "+ messageSize+ " B";
		return messg;
	}

	// Embeds a file into a Master file
	public static boolean embedFile(File masterFile, File outputFile, File dataFile, int compression, String password)
	{
     	messageSize= (int) dataFile.length();

		if(compression!= 0)
		{
			// Make sure that the compression is a valid numerical between 0 and 9
			if(compression<0)		compression=0;
			if(compression>9)		compression=9;

			if(password== null)	features= CUF;
			else				features= CEF;
		}
		else
		{
			if(password== null)	features= UUF;
			else				features= UEF;
		}
		
		try
		{
			// Read the data bytes into fileArray
			byte []fileArray= new byte[messageSize];
			DataInputStream in= new DataInputStream(new FileInputStream(dataFile));
			in.read(fileArray, 0, messageSize);
			in.close();
						
			BufferedImage bfimage = ImageIO.read(masterFile);
						
			int imageWidth = bfimage.getWidth(), imageHeight = bfimage.getHeight();
			int imageSize = imageWidth * imageHeight;
			messageSize= fileArray.length;	
			
			if(messageSize * 8 + 48 > imageSize) 
	        {
			   message="File is too big for the chosen image.";
			   return false;
			}
						
			// Write 1 byte for features
			embedByte(bfimage, features, 0, 0);

			// Compress the message if required
			if(features== CUF || features== CEF)
			{
				ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
				ZipOutputStream zOut= new ZipOutputStream(arrayOutputStream);
				ZipEntry entry= new ZipEntry(dataFile.getName());
				zOut.setLevel(compression);
				zOut.putNextEntry(entry);
				zOut.write(fileArray, 0, messageSize);
				zOut.closeEntry();
				zOut.finish();
				zOut.close();
				// Get the compressed message byte array
				fileArray= arrayOutputStream.toByteArray();
				compressionRatio= (short) ((double)fileArray.length / (double)messageSize * 100.0);
				messageSize= fileArray.length;
			}
			else
				compressionRatio=0;
			
			// Embed 1 byte compression ratio into the output file
			embedByte(bfimage,(byte)compressionRatio, 8, 0);

			// Encrypt the message if required
			if(features== UEF || features== CEF)
			{
				MessageDigest md=MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes(StandardCharsets.UTF_8));
				byte[] mdbyte=md.digest();
				byte[] key=new byte[mdbyte.length/4];
				
				for(int i=0;i<key.length;i++)
					key[i]=mdbyte[i];

				Cipher cipher= Cipher.getInstance("DES");
				SecretKeySpec spec= new SecretKeySpec(key, "DES");
				cipher.init(Cipher.ENCRYPT_MODE, spec);
				fileArray= cipher.doFinal(fileArray);
				messageSize= fileArray.length;
			}
	
			// Convert the 32 bit message size into byte array
			tempByte= new byte[4];
			for(i=24, j=0; i>=0; i-=8, j++)
			{
				tempInt= messageSize;
				tempInt>>= i;
				tempInt&= 0x000000FF;
				tempByte[j]= (byte) tempInt;
			}
									
			// Embed 4 byte messageSize array into the master file
			for(int i=0; i<tempByte.length; i++)
				embedByte(bfimage, tempByte[i], i*8+16, 0);

			// Embed the message
			for(int i=0; i<fileArray.length; i++)
				embedByte(bfimage, fileArray[i], i*8+48, 0);	
			
			ImageIO.write(bfimage,"png",outputFile);		 
		}
		catch(EOFException e)
		{
		}
		catch(Exception e)
		{
			message= "Oops!!\nError: "+ e.toString();
			e.printStackTrace();
			return false;
		}
		message= "File '"+ dataFile.getName()+ "' embedded successfully in file '"+ outputFile.getName()+ "'.";
		return true;
	}
	
	// Retrieves an embedded file from a Master file
	public static boolean retrieveFile(SteganoInformation info, String password, boolean overwrite)
	{
		File dataFile= null;
		features= info.getFeatures();

		try
		{
			messageSize= info.getDataLength();

			byte[] fileArray= new byte[messageSize];
			fileArray=info.getData();
			
			if(messageSize<=0)
			{
				message= "Unexpected size of Embedded File: 0.";
				return false;
			}
		
			//Decrypt the file if required
			if(features== CEF || features== UEF)
			{
				MessageDigest md=MessageDigest.getInstance("SHA-256");
				md.update(password.getBytes(StandardCharsets.UTF_8));
				byte[] mdbyte=md.digest();
				byte[] key=new byte[mdbyte.length/4];
				
				for(int i=0;i<key.length;i++)
					key[i]=mdbyte[i];
				
				cipher= Cipher.getInstance("DES");
				spec= new SecretKeySpec(key, "DES");
				cipher.init(Cipher.DECRYPT_MODE, spec);
				try
				{
					fileArray= cipher.doFinal(fileArray);
				}
				catch(Exception bp)
				{
					message= "Incorrect Password";
					bp.printStackTrace();
					return false;
				}
				messageSize= fileArray.length;
			}

			// Uncompress the file if required
			if(features== CUF || features== CEF)
			{
				ByteArrayOutputStream by= new ByteArrayOutputStream();
				DataOutputStream out= new DataOutputStream(by);
				ZipInputStream zipIn= new ZipInputStream(new ByteArrayInputStream(fileArray));
				ZipEntry entry= zipIn.getNextEntry();
				dataFile= new File(entry.getName());
				byteArrayIn= new byte[1024];
				while((tempInt= zipIn.read(byteArrayIn, 0, 1024))!= -1)
					out.write(byteArrayIn, 0, tempInt);
				zipIn.close();
				out.close();
				fileArray= by.toByteArray();
				messageSize= fileArray.length;
			}
			
			info.setDataFile(dataFile);
			if(dataFile.exists() && !overwrite)
			{
				message= "File Exists";
				return false;
			}
			
			DataOutputStream out= new DataOutputStream(new FileOutputStream(dataFile));
			out.write(fileArray, 0, fileArray.length);
			out.close();			
		}
		catch(Exception e)
		{
			message= "Oops!!\n Error: "+ e;
			e.printStackTrace();
			return false;
		}

		message= "Retrieved file size: "+ messageSize+ " B";
		return true;
	}

	// Method used to write bytes into the output array
	private static void embedByte(BufferedImage img, byte b, int start, int storageBit) 
	{
	    int maxX = img.getWidth(), maxY = img.getHeight(); 
	    int startX = start/maxY, startY = start - startX*maxY, count=0;
	    for(int i=startX; i<maxX && count<8; i++) 
	    {
	    	for(int j=startY; j<maxY && count<8; j++) 
	    	{
		       int rgb = img.getRGB(i, j), bit = getBitValue(b, count);
		       rgb = setBitValue(rgb, storageBit, bit);
		       img.setRGB(i, j, rgb);
		       count++;
		       if(j==maxY-1)
		           startY = 0;
		    }
        }
    }
	
	private static int getBitValue(int n, int location)
	{
	    int v = n & (int) Math.round(Math.pow(2, location));
	    return v==0?0:1;
	}
		 
	private static int setBitValue(int n, int location, int bit)
	{
	    int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
	    if(bv == bit)
	       return n;
	    if(bv == 0 && bit == 1)
	       n |= toggle;
	    else if(bv == 1 && bit == 0)
	       n ^= toggle;
	    return n;
	}

	public static void showAboutDialog()
	{
		about.setDisplayed(true);
	}

	public static void updateUserInterface()
	{
		SwingUtilities.updateComponentTreeUI(about);
	}

	private static class AboutFrame extends JFrame implements ActionListener
	{
		private JButton btnClose;
		public AboutFrame()
		{
			setTitle("About PS2 Steganography");
		
			JLabel background = new JLabel(new ImageIcon(getClass().getResource("About.jpg")));
			setContentPane(background);
			setLayout(new GridBagLayout());
			
			ImageIcon img = new ImageIcon(getClass().getResource("logo.png"));
			setIconImage(img.getImage());
			
			btnClose= new MyJButton("Close");
			btnClose.addActionListener(this);
			
			JPanel panelButtons= new JPanel();
			panelButtons.setLayout(new GridBagLayout());
			panelButtons.add(btnClose);

			Container tempPanel= getContentPane();
			tempPanel.setLayout(new BorderLayout());
			tempPanel.add(panelButtons,BorderLayout.SOUTH);

			Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
			setSize((int) (d.width*0.513), (int) (d.height*0.4557));
			setLocation((int)(d.width*0.2562),(int)(d.height*0.2604));
			setResizable(false);
		}
		
		public void setDisplayed(boolean choice)
		{
			setVisible(choice);
		}

		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()== btnClose)			
				setVisible(false);
		}
	} 	// End of Class AboutFrame
}	// End of Class Steganograph


// Class to obtain information about a Steganograph file
class SteganoInformation
{
	private File file;
	private File dataFile= null;
	private String starter="nid";
	private String version;
	private byte features;
	private short compressionRatio;
	private int dataLength, temp;

	private byte name[], data[];
	private int inputMarker, i, j;
	
	private BufferedImage bfimage;

	// Accessor methods
	public File getFile() { return file; }
	public int getInputMarker() { return inputMarker; }
	public File getDataFile() { return dataFile; }
	public String getVersion() { return version; }
	public byte   getFeatures() { return features; }
	public short getCompressionRatio() { return compressionRatio; }
	public int   getDataLength()	{ return dataLength; }
	public byte[] getData() { return data; }

	// Mutator methods
	public void setDataFile(File dataFile)
	{
		this.dataFile= dataFile;
	}

	public static char[] byteToCharArray(byte[] bytes)
	{
		int size= bytes.length, i;
		char []chars= new char[size];
		for(i=0; i<size; i++)
		{
			bytes[i]&= 0x7F;
			chars[i]= (char) bytes[i];
		}
		return chars;
	}
	
	private byte extractByte(BufferedImage img, int start, int storageBit)
	{
	    int maxX = img.getWidth(), maxY = img.getHeight(); 
	    int startX = start/maxY, startY = start - startX*maxY, count=0;
	    byte b = 0;
	    for(int i=startX; i<maxX && count<8; i++) 
	    {
	       for(int j=startY; j<maxY && count<8; j++) 
	       {
	          int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
	          b = (byte)setBitValue(b, count, bit);
	          count++;
	          if(j==maxY-1) 
	        	 startY = 0;
	        }
	    }
	    return b;
	}
	
	private int getBitValue(int n, int location) 
	{
	    int v = n & (int) Math.round(Math.pow(2, location));
	    return v==0?0:1;
	}
		 
	private int setBitValue(int n, int location, int bit) 
	{
	    int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
	    if(bv == bit)
	       return n;
	    if(bv == 0 && bit == 1)
	       n |= toggle;
	    else if(bv == 1 && bit == 0)
	       n ^= toggle;
	    return n;
	}

	public SteganoInformation(File file)
	{
		this.file= file;

		if(!file.exists())
		{
			starter= null;
			return;
		}
				
		try
		{
			bfimage=ImageIO.read(file);
		}
		catch(Exception e)
		{
			starter= null;
			return;
		}

		// Obtain the features
		features=extractByte(bfimage,0,0);		

		// Obtain the compression ratio
		name= new byte[1];
		name[0]=extractByte(bfimage, 8, 0);
		name[0]&= 0x7F;
		compressionRatio= name[0];

		// Obtain the data length
		name= new byte[4];
		for(int i=0;i<4;i++)
			name[i]=extractByte(bfimage, i*8+16, 0);
		dataLength= 0;
		for(i=24,j=0; i>=0; i-=8,j++)
		{
			temp= name[j];
			temp&= 0x000000FF;
			temp<<= i;
			dataLength|= temp;
		}
		
		if(dataLength<0 || dataLength>file.length())
		{
			starter= "Invalid";
			return;
		}
		else
			starter= "nid";
		
		//Obtain the data
		data= new byte[dataLength];
		for(int i=0;i<dataLength;i++)
			data[i]=extractByte(bfimage, i*8+48, 0);
	}

	public boolean isValid()
	{
		if(starter.equals("nid"))
		{	
			return true;
		}
		else
			return false;
	}
}