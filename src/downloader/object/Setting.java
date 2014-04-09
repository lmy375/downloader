package downloader.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * Class with only static variable and static functions. Storing current
 * setting and change current setting.
 * When program starts the setting will be loaded ( if failed , set default
 * setting ), and save setting before program ends. 
 * @author Moon
 *
 */
public class Setting {
	
	
	private static String defaultPath;
	private static String lookAndFeel;	
	
	private static int portNumber;
	private static int testTime;	
	
	public static boolean startRequestListenerWhenStarts;
	public static boolean startTasksWhenStarts;
	
	
	/**
	 * Sets default setting for program. 
	 * UI: SystemLookAndFeel
	 * DefaultPath: C:\\Users\\Moon\\Desktop\\New Folder
	 * Connection test time : 10
	 * Port number: 8888
	 * Start Request Listener When Starts : true;
	 * Start Tasks When Starts : false;
	 */
	public static void setDefaultSetting() {
		lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		defaultPath = "C:\\Users\\Moon\\Desktop\\New Folder";
		File f = new File(Setting.defaultPath);
		if(!f.exists()) 
			if(!f.mkdirs())
					JOptionPane.showMessageDialog(null, "Default path does not exist. Set a new one in Options->Setting.");
		testTime = 10;
		portNumber = 8888;
		startRequestListenerWhenStarts= true;
		startTasksWhenStarts= false;
	}
	
	/**
	 * Saves current setting to file "config.dat".
	 */
	public static void saveSetting(){
		try {
			FileOutputStream fos = new FileOutputStream("config.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Setting.defaultPath);
			File f = new File(Setting.defaultPath);
			if(!f.exists()) 
				if(!f.mkdirs())
						JOptionPane.showMessageDialog(null, "Default path does not exist. Set a new one in Options->Setting.");
			oos.writeObject(Setting.lookAndFeel);
			oos.writeObject(Setting.portNumber);
			oos.writeObject(Setting.testTime);
			oos.writeBoolean(startRequestListenerWhenStarts);
			oos.writeBoolean(startTasksWhenStarts);
			oos.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads file "config.dat" to current setting. 
	 * If "config.dat" does not exist , set default setting.
	 */
	public static void loadSetting(){
		
			try {
				FileInputStream fis = new FileInputStream("config.dat");
				ObjectInputStream ois = new ObjectInputStream(fis);
				Setting.defaultPath= (String) ois.readObject();
				Setting.lookAndFeel= (String) ois.readObject();
				Setting.portNumber= (int) ois.readObject();
				Setting.testTime= (int) ois.readObject();
				Setting.startRequestListenerWhenStarts= ois.readBoolean();
				Setting.startTasksWhenStarts= ois.readBoolean();
				ois.close();
				fis.close();
				File f = new File(Setting.defaultPath);
				if(!f.exists()) 
					if(!f.mkdirs())
							JOptionPane.showMessageDialog(null, "Default path does not exist. Set a new one in Options->Setting.");
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Setting.setDefaultSetting();
			}
			
		
	}

	
	/**
	 * Gets connection test times number which means how many times 
	 * program will try while encountering connection failure.
	 * @return test times number.
	 */
	public static int getTestTime(){
		return testTime;
	}
	/**
	 * Sets connection test times number.
	 * @param time  :  test times number set by user.
	 */
	public static void setTestTime(int time){
		testTime = time;
	}
	/**
	 * Gets current path which is set automatically in the new task frame.
	 * @return current default path.
	 */
	public static String getDefaultPath() {
		return defaultPath;
	}
	
	
	/**
	 * Sets default path.
	 * @param path   file path set by user
	 */
	public static void setDefaultPath(String path){
		defaultPath = path;
	}
	
	/**
	 * Gets current UI
	 * @return Current UI of setting.
	 */
	public static String getDefaultLookAndFeel(){
		return Setting.lookAndFeel;
	}
	/**
	 * Sets default UI
	 * @param laf The LookAndFeel to be set
	 */
	public static void setDefaultLookAndFeel(String laf){
		Setting.lookAndFeel= laf;
	}
	
	/**
	 * Gets current port number
	 * @return Current port number
	 */
	public static int getPortNumber(){
		return portNumber;
	}
	/**
	 * Sets default port number
	 * @param port Number to set
	 */
	public static void setPortNumber(String port){
		try{
			int portNum = Integer.parseInt(port);
			if(portNum<1024||portNum>65535) {
				JOptionPane.showMessageDialog(null,"Out of Range 1024~ 65535.");
				return;
			}else{
				portNumber = portNum;
				JOptionPane.showMessageDialog(null, "Success. Restart to apply setting.");
			}
		}catch (NumberFormatException nfe){
			JOptionPane.showMessageDialog(null, "Invalid Number.");
			return;
		}
		
		
	}

	
}
