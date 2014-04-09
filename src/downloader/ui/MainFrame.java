package downloader.ui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import downloader.object.DownloadInfo;
import downloader.object.Setting;
import downloader.object.Task;
import downloader.object.share.RequestListener;
import downloader.ui.taskList.TaskList;


/**
 * Main frame of the program.
 * @author Moon
 *
 */
public class MainFrame extends JFrame implements WindowListener{
	
	private MenuBar menu;
	private ButtonBar buttonBar;
	private TaskList taskList;
	private NewTaskFrame newTaskFrame;
	
	private RequestListener requestListener;
	
	int closingFlag;
	
	public MainFrame(){
		super("Downloader");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationByPlatform(true);
		setDefaultLookAndFeelDecorated(true);
		
		try{
			UIManager.setLookAndFeel(Setting.getDefaultLookAndFeel());
		}catch(Exception e){
			
		}
		
		
		menu = new MenuBar(this);
		this.setJMenuBar(menu);
		buttonBar = new ButtonBar(this);
		//navigationTree = new NavigationTree();
		taskList = new TaskList(this);
		newTaskFrame = new NewTaskFrame(this);
		
		Container mainPane = this.getContentPane();
		mainPane.setLayout(new BorderLayout());
		//mainPane.add(new JScrollPane(navigationTree),BorderLayout.WEST);
		
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		centerPane.add(buttonBar,BorderLayout.NORTH);
		centerPane.add(new JScrollPane(taskList),BorderLayout.CENTER);
		
		mainPane.add(centerPane,BorderLayout.CENTER);
		
	
		
		this.addWindowListener(this);
		this.setSize(600,450);
		this.setVisible(true);
		
		/**
		 * Initial the request listener. 
		 */
		requestListener = new RequestListener(this);
		
		
	}
	
	public void showNewTaskFrame(){
		this.newTaskFrame.showThis();
	}
	public TaskList getTaskList(){
		return this.taskList;
	}
	public NewTaskFrame getNewTaskFrame(){
		return this.newTaskFrame;
	}	
	public RequestListener getRequestListener(){
		return this.requestListener;
	}

	
	
	
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
		if(Setting.startRequestListenerWhenStarts) this.requestListener.startServer();
		DownloadInfo.loadTaskInfo(taskList);
		
		for(int i = 0 ;i< taskList.getRowCount();i++){
			Task t= (Task) getTaskList().getValueAt(i, 2);
			switch (t.getCurrentStatus()){
			case Task.PAUSED: if(Setting.startTasksWhenStarts) t.resumeDownload(); break;
			case Task.MERGING: t.merge();break;
			}
		}
		
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Setting.saveSetting();
		
		this.closingFlag = DownloadInfo.saveTasksInfo(taskList);
		this.requestListener.closeServer();
		// TODO Auto-generated method stub	
		if(this.closingFlag==0){
			this.dispose();
			System.exit(0);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
