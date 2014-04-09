package downloader.ui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import downloader.object.Setting;
import downloader.object.Task;


/**
 * Dialog for user to fill in essential information to create a new task
 * @author Moon
 *
 */
public class NewTaskFrame extends JDialog implements ActionListener,FocusListener{

	
	private static final long serialVersionUID = 1L;

	//private JFileChooser filePath;
	
	private JTextField filePathText;
	private JTextField fileName;
	private JTextField url;
	
	private JLabel warningLabel;
	private JButton choosePath;
	private JButton ok;
	private JButton cancel;
	private JComboBox<String> threadCount;
	
	private MainFrame main;
	
	public NewTaskFrame(MainFrame main){
		super(main,"New Task",true);
		this.main=main;
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(400,200);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		
		
		
		//filePath = this.createFileChooser();	
		
		fileName = new JTextField();
		filePathText = new JTextField();
		filePathText.setText(Setting.getDefaultPath());
		threadCount = new JComboBox<String>();
		for(int i =1; i<17;i++)			
			threadCount.addItem(""+i);
		
		url = new JTextField();
		url.addFocusListener(this);
		url.addActionListener(this);
		warningLabel = new  JLabel(" ");
		
		choosePath = new JButton("Choose Path");
		choosePath.addActionListener(this);
		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton ("Cancel");
		cancel.addActionListener(this);
		
		
		JPanel labelPane = new JPanel(new GridLayout(4,1));
		labelPane.add(new JLabel("Http URL:"));
		labelPane.add(new JLabel("File Path:"));
		labelPane.add(new JLabel("File Name:"));
		labelPane.add(new JLabel("Thread Numbers:"));
		
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(ok);
		buttonPane.add(cancel);
		
		JPanel fileNamePane = new JPanel(new GridLayout(1,3));
		fileNamePane.add(fileName);
		fileNamePane.add(warningLabel);
		fileNamePane.add(choosePath);
		
		JPanel textPane = new JPanel(new GridLayout(4,1));
		textPane.add(url);
		textPane.add(filePathText);
		textPane.add(fileNamePane);
		textPane.add(threadCount);
		
		JPanel pane = new JPanel ();
		pane.setLayout(new BorderLayout());		
		
		pane.add(labelPane,BorderLayout.WEST);
		pane.add(textPane, BorderLayout.CENTER);
		pane.add(buttonPane,BorderLayout.SOUTH);
		
		
		JPanel mainPane = new JPanel(new BorderLayout());
		
		mainPane.add(new JLabel(" "),BorderLayout.NORTH);
		mainPane.add(new JLabel(" "),BorderLayout.SOUTH);
		mainPane.add(new JLabel("     "),BorderLayout.WEST);
		mainPane.add(new JLabel("     "),BorderLayout.EAST);
		mainPane.add(pane,BorderLayout.CENTER);
		
		this.setContentPane(mainPane);
		//this.setVisible(true);
		
		
	}
	
	/**
	 * Create a file chooser and set selection mode as directories only
	 * for user to choose folder to save the file
	 * @return created JFileChooser
	 */
	private JFileChooser createFileChooser(){
		JFileChooser filechooser= new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		filechooser.setDialogTitle("Choose New Path");
		filechooser.setFileHidingEnabled(true);
		//FileFilter f = filechooser.getFileFilter();
		
		return filechooser;
	}
	/**
	 * Create new task with information filled in the frame. 	 *
	 */
	private void createTask(){
		String url = this.url.getText();
		String filePath = this.filePathText.getText();
		String fileName = this.fileName.getText() ;
		int threadCount = Integer.parseInt(this.threadCount.getSelectedItem().toString());
		if("".equals(url)||url==null
				||"".equals(fileName) ||fileName==null
				||"".equals(filePath)||filePath==null){
			this.warningLabel.setText("Invalid information");
			
		}else{
			this.setVisible(false);			
			try {
				Task t;
				t = new Task(url, fileName ,filePath,  threadCount);
				main.getTaskList().addTask(t);
				t.beginDownload();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				this.warningLabel.setText("Invalid information");
				this.setVisible(true);
			}
			
			
		}	
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == choosePath){
			JFileChooser filePath = this.createFileChooser();
			int returnVal =filePath.showOpenDialog(this);
			if(returnVal==JFileChooser.APPROVE_OPTION)
				filePathText.setText(filePath.getSelectedFile().toString());
		}
		if (src == cancel) this.dispose();
		if (src == ok) createTask();	
		if (src== url) fileName.setText(getFileName(url.getText()));
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		Object src = e.getSource();
		if(src== url &&!"".equals(url)) fileName.setText(
				getFileName(url.getText()));
	}
	private String getFileName(String url){
		return url.substring(url.lastIndexOf('/')+1);
	}
	
	/**
	 * Sets this frame visible. 
	 */
	public void showThis(){
		this.fileName.setText("");
		this.filePathText.setText(Setting.getDefaultPath());
		this.url.setText("");
		this.warningLabel.setText("");
		this.setVisible(true);
	}
	
	/**
	 * Set this frame visible and fill blank according to the task.
	 * @param t Task
	 */
	public void addNewTask(Task t){
		this.url.setText(t.getURL().toString());
		this.fileName.setText(t.getFileName());		
		this.filePathText.setText(Setting.getDefaultPath());		
		this.warningLabel.setText("");
		this.setVisible(true);
	}
}
