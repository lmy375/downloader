package downloader.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Menu bar on the top of the frame.
 * @author Moon
 *
 */
public class MenuBar extends JMenuBar implements ActionListener{
	
	private JMenu file;
	private JMenu options;
	private JMenu help;
	
	
	private JMenuItem newTask;
	private JMenuItem exit;
	
	private JMenuItem setting;
	
	private JMenuItem downloaderHelp;
	private JMenuItem about;
	private MainFrame main;
	
	public MenuBar(MainFrame main){
		super();
		this.main = main;
		file= new JMenu("File");
		newTask = new JMenuItem("New Task");
		newTask.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		file.add(newTask);
		file.add(exit);		
		
		options = new JMenu("Options");		
		setting = new JMenuItem("Setting");
		setting.addActionListener(this);
		options.add(setting);
		
		help = new JMenu("Help");
		downloaderHelp = new JMenuItem("Downloader Help");
		downloaderHelp.addActionListener(this);
		about = new JMenuItem("About Downloader");
		about.addActionListener(this);
		help.add(downloaderHelp);
		help.add(about);

		this.add(file);
		this.add(options);
		this.add(help);
	}
	
	
	public void actionPerformed(ActionEvent ae){
		Object src = ae.getSource();
		if(src == newTask) main.showNewTaskFrame();
		if(src == exit) System.exit(0);
		if(src == setting) new OptionsFrame(main);
		if(src == downloaderHelp) JOptionPane.showMessageDialog(null, "" +
				"\n	   1. This is a simple downloader programmed by Java which" +
				"\n       contains two major functions , Download and Share." +
				"\n	   2. Most functions depends on buttons in the Button Bar." +
				"\n       Here are tips about all buttons." +
				"\n       New Task: To start a new task, in which the URL, file"+
				"\n                 name , file path and thread number is required. "+
				"\n       Pause   : To pause a running task or resume a paused" +
				"\n                 task." +
				"\n       Restart : To restart selected task  no matter  what " +
				"\n                 status task is in , running ,  paused  or" +
				"\n                 stoped." +
				"\n       Stop    : To stop a task." +
				"\n       Remove  : To remove selected task from the list with" +
				"\n                 file in the disk remaining." +
				"\n       Share   : Share selected task with your friends.  IP" +
				"\n                 and port number is required." +
				"\n       Inbox   : Open inbox in which shows requests from your" +
				"\n                 friend. Handle requests in three ways." +
				"\n                 Accept  : Receive data from addresser's PC." +
				"\n                 Download: Download yourself using the URL." +
				"\n                 Refuse  : Refused the request." +
				"\n    3. All setting can be changed in Options->Setting. Setting" +
				"\n       file saved as \"config.dat\", download information saved"+
				"\n       as \"tasks.info\" in the disk.", 
				"Help", JOptionPane.PLAIN_MESSAGE);
		if(src== about) JOptionPane.showMessageDialog(null,"" +
				"\n       Downloader 1.0 Copyright 2012-2013" +
				"\n       Moon , SCSE 1106 , BUAA" +
				"\n       Personal projects as assignment for JAVA course." +
				"\n       Icons picked from simulator Mars 4.2. ", "About", JOptionPane.WARNING_MESSAGE);
	}
}
