//package src;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;   
import java.awt.event.*;   
import java.io.*;      
   
public class BackEndHandler extends Thread   
{   
    public static final short EMBED_MESSAGE=    0;   
    public static final short EMBED_FILE=       1;   
    public static final short RETRIEVE_MESSAGE= 2;   
    public static final short RETRIEVE_FILE=    3;   
    public static final short EDIT_MASTER=      4;   
   
    private short operation;   
    private JFileChooser fileChooser, DataFileChooser;   
    private File masterFile, dataFile, outputFile;   
    private int result, result2;   
   
    public BackEndHandler(WindowAdapter client, short operation)   
    {   
        this.operation= operation;   
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Corbel",Font.BOLD,14)));
        
        // Setup file chooser   
        fileChooser= new JFileChooser("./");   
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);   
        fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);   
        setFileChooserFont(fileChooser.getComponents());
        fileChooser.setAccessory(new FilePreviewer(fileChooser));  
        
        DataFileChooser= new JFileChooser("./");   
        DataFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);   
        DataFileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);   
        setFileChooserFont(DataFileChooser.getComponents());
        DataFileChooser.setAccessory(new FilePreviewer(fileChooser));  
   
        // Create and set the file filter   
        MyFileFilter filter1= new MyFileFilter(new String[]{"bmp", "jpg", "gif", "tif", "png", "jpeg"}, "Picture files");   
        MyFileFilter filter2= new MyFileFilter(new String[]{"mp3", "wav", "ram", "wma"}, "Audio files");   
        MyFileFilter filter3= new MyFileFilter(new String[]{"mpg", "wmv", "dat", "mkv", "flv", "mp4"}, "Video files");   
        fileChooser.addChoosableFileFilter(filter1);  
        fileChooser.setAcceptAllFileFilterUsed(false);
        DataFileChooser.addChoosableFileFilter(filter1);
        DataFileChooser.addChoosableFileFilter(filter2);   
        DataFileChooser.addChoosableFileFilter(filter3);         
    }   
    
    public void setFileChooserFont(Component[] comp)
    {
        for(int x = 0; x < comp.length; x++)
        {
           if(comp[x] instanceof Container) setFileChooserFont(((Container)comp[x]).getComponents());
              try{comp[x].setFont(new Font("Corbel", Font.BOLD, 14));}
              catch(Exception e){}//do nothing
        }
    }
   
    public void run()   
    {              
        if(!chooseMasterFile()) return;   
           
        if(operation== EMBED_FILE)   
            if(!chooseDataFile()) return;  
        
        if(operation== EMBED_MESSAGE || operation== EMBED_FILE)   
            if(!chooseOutputFile()) return;   
    
        SteganoInformation steg;   
        switch(operation)   
        {   
            case EMBED_MESSAGE :  new EmbedMessageGUI(this);  break;   
            case EMBED_FILE    :  new EmbedFileGUI(this);     break;   
            case RETRIEVE_MESSAGE:   
                    steg= new SteganoInformation(masterFile);      
                    if(!steg.isValid())   
                       JOptionPane.showMessageDialog(null, "File '"+ masterFile.getName()+    
                           "' does not contain any message or file\nembedded using Steganography!", "Invalid Steganography file!", JOptionPane.WARNING_MESSAGE);   
                    else   
                       new PreRetrieveGUI(steg, PreRetrieveGUI.RETRIEVE_MESSAGE);   
                    break;   
            case RETRIEVE_FILE:   
                    steg= new SteganoInformation(masterFile);     
                    if(!steg.isValid())   
                       JOptionPane.showMessageDialog(null, "File '"+ masterFile.getName()+    
                           "' does not contain any message or file\nembedded using Steganography!", "Invalid Steganograph file!", JOptionPane.WARNING_MESSAGE);   
                    else   
                       new PreRetrieveGUI(steg, PreRetrieveGUI.RETRIEVE_FILE);                    
        }   
    }   
   
    // Method for choosing input file   
    public boolean chooseMasterFile()   
    {  
        do   
        {   result= fileChooser.showDialog(null, "Select Master File");   
            if(result== JFileChooser.APPROVE_OPTION)   
            {   
                masterFile= fileChooser.getSelectedFile();   
                if(!checkFileExistency(masterFile))   
                    continue;   
                else   
                    break;   
            }   
        } while(result!= JFileChooser.CANCEL_OPTION);   
        if(result== JFileChooser.CANCEL_OPTION)  return false;   
        else                                     return true;   
    }   
   
    // Method for choosing data file   
    public boolean chooseDataFile()   
    {   
        do   
        {   result= DataFileChooser.showDialog(null, "Select Data File");   
            if(result== JFileChooser.APPROVE_OPTION)   
            {   
                dataFile= DataFileChooser.getSelectedFile();   
                if(!checkFileExistency(dataFile))   
                        continue;   
                else   
                        break;   
            }   
        } while(result!= JFileChooser.CANCEL_OPTION);   
        if(result== JFileChooser.CANCEL_OPTION) return false;   
        else                                    return true;   
    }   

    // Method for choosing output file   
    public boolean chooseOutputFile()   
    {   
        int result;   
        do   
        {   
            File previousFile= DataFileChooser.getSelectedFile();   
            result= DataFileChooser.showDialog(null, "Select Output File");   
            if(result== JFileChooser.APPROVE_OPTION)   
            {   
                outputFile= DataFileChooser.getSelectedFile();   
                if(outputFile.exists())   
                {   
                    result2= JOptionPane.showConfirmDialog(null, "File "+ outputFile.getName()+ " already exists!\nWould you like to OVERWRITE it?", "File already exists!", JOptionPane.YES_NO_OPTION);   
                    if(result2== JOptionPane.NO_OPTION)   
                    {   
                        if(previousFile!= null)   
                            DataFileChooser.setSelectedFile(previousFile);   
                        continue;   
                    }   
                }   
                break;   
            }   
        } while(result!= JFileChooser.CANCEL_OPTION);   
   
        if(result== JFileChooser.CANCEL_OPTION)  return false;   
        else                                     return true;   
    }
    
    // Accessor methods   
    public File getMasterFile() { return masterFile; }   
    public File getOutputFile() { return outputFile; }   
    public File getDataFile()   { return dataFile;   }   
   
    // Mutator methods   
    public void setMasterFile(File file)    { masterFile= file; }   
    public void setOutputFile(File file)    { outputFile= file; }   
    public void setDataFile(File file)      { dataFile  = file; }   
   
    // Checks whether given file actually exists   
    private boolean checkFileExistency(File file)   
    {   
        if(!file.exists())   
        {   
            JOptionPane.showMessageDialog(null, "File "+ file.getName()+ " does not exist!", "Invalid file!", JOptionPane.ERROR_MESSAGE);   
            return false;   
        }              
        return true;   
    }     
   
 /*   private void showInfo(SteganoInformation steg)   
    {   
        Object message[]= new Object[3];   
        message[0]= new MyJLabel("This is an Encrypted Zone.", new Font("Corbel", Font.BOLD, 13), Color.red, Color.gray);   
        message[1]= new MyJLabel("Please enter the password to continue..");   
        JPasswordField pass= new JPasswordField(17);   
        message[2]= pass;   
   
        String options[]= {"Retrieve Now", "Cancel"};   
        int result= JOptionPane.showOptionDialog(null, message, "Encrypted Zone"   
              , JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);   
   
        if(result== 1)  return;   
   
        String password= new String(pass.getPassword());   
        if(password.length()<8)   
            JOptionPane.showMessageDialog(null, "That is an incorrect password!", "Invalid Password", JOptionPane.OK_OPTION);   
        else   
        {   int fileSize= (int) steg.getFile().length();   
            byte[] byteArray= new byte[fileSize];   
            try   
            {   
                DataInputStream in= new DataInputStream(new FileInputStream(steg.getFile()));   
                in.read(byteArray, 0, fileSize);   
                in.close();   
   
                Cipher cipher= Cipher.getInstance("DES");   
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password.substring(0, 8).getBytes(), "DES"));   
   
                byteArray= cipher.doFinal(byteArray);   
            }   
            catch(Exception e)  { return; }   
   
            JFrame frame= new JFrame("Enjoy the ester egg...");   
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   
            frame.getContentPane().add(new JScrollPane(new JLabel(new ImageIcon(byteArray))));   
            frame.setBackground(Color.white);   
   
            Dimension d= Toolkit.getDefaultToolkit().getScreenSize();   
            frame.setSize(d.width, d.height/2);   
            frame.setVisible(true);   
        }   
    } 
    */  
}  