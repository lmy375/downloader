package downloader.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import downloader.Downloader;
import downloader.object.Task;
import downloader.ui.taskList.TaskList;



/**
 * Button bar show on the top of main frame.
 * @author Moon
 *
 */
public class ButtonBar extends JToolBar implements ActionListener{
	
	private JButton newTask;
	private JButton pause;
	private JButton restart;
	private JButton stop;
	private JButton remove;
	private JButton share;
	private JButton inbox;
	
	
	private MainFrame main;
	
	public ButtonBar(MainFrame main){
		super();
		this.main= main;
		
		this.init();
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		/**
		 * set layout
		 */
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(newTask);
		this.add(pause);
		this.add(restart);
		this.add(stop);
		this.add(remove);
		this.add(share);
		this.add(inbox);
		
		//this.setVisible(true);
		/**
		 * add ActionListener
		 */
		newTask.addActionListener(this);
		pause.addActionListener(this);
		restart.addActionListener(this);
		stop.addActionListener(this);
		remove.addActionListener(this);
		share.addActionListener(this);
		inbox.addActionListener(this);
	}
	
	/**
	 * initialize the buttons in pane
	 */
	private void init(){
		ImageIcon newTaskImg = new ImageIcon("resources/images/New.png");
		ImageIcon pauseImg = new ImageIcon("resources/images/Pause.png");
		ImageIcon resumeImg = new ImageIcon ("resources/images/Begin.png");
		ImageIcon stopImg = new ImageIcon("resources/images/Stop.png");
		ImageIcon removeImg = new ImageIcon("resources/images/Remove.gif");
		ImageIcon shareImg = new ImageIcon("resources/images/Share.png");
		ImageIcon inboxImg = new ImageIcon("resources/images/Inbox.png");
		
		newTask = new JButton(newTaskImg);
		newTask.setToolTipText("New Task");
		pause = new JButton (pauseImg);
		pause.setToolTipText("Pause");
		restart = new JButton (resumeImg);
		restart.setToolTipText("Restart");
		stop = new JButton(stopImg);
		stop.setToolTipText("Stop");
		remove = new JButton(removeImg);
		remove.setToolTipText("Remove");
		share = new JButton(shareImg);
		share.setToolTipText("Share");
		inbox = new JButton(inboxImg);
		inbox.setToolTipText("Inbox");
		
	}
	/**
s	 * Action performance of buttons
	 */
	
	public void actionPerformed(ActionEvent event){
		Object src = event.getSource();
		if(src == newTask)	main.showNewTaskFrame();
		if(src == pause){
			TaskList list = main.getTaskList();
			int rowIndex = list.getSelectedRow();
			if(rowIndex>=list.getRowCount()||rowIndex<0) return;
			Task t= (Task) main.getTaskList().getValueAt(rowIndex, 2);
			t.pause();
		}
		if(src == stop){
			TaskList list = main.getTaskList();
			int rowIndex = list.getSelectedRow();
			if(rowIndex>=list.getRowCount()||rowIndex<0) return;
			Task t= (Task) main.getTaskList().getValueAt(rowIndex, 2);
			if(!(t.getCurrentStatus()==Task.FINISHED)&&
				!(t.getCurrentStatus()==Task.MERGING)){
				int response = JOptionPane.showConfirmDialog(null, "Stop the Task ?");
				if(response == JOptionPane.YES_OPTION){
					t.stop();
				}
			}
		}
		if(src == remove){
			TaskList list = main.getTaskList();
			DefaultTableModel model = (DefaultTableModel) list.getModel();
			int rowIndex = list.getSelectedRow();
			if(rowIndex>=list.getRowCount()||rowIndex<0) return;
			int response = JOptionPane.showConfirmDialog(null, "Remove the Task ?");
			
			if(response ==JOptionPane.YES_OPTION){
				Task t= (Task) main.getTaskList().getValueAt(rowIndex, 2);			
				t.stop();
				t.clearData();
				model.removeRow(rowIndex);
			}
			
		}
		if(src == restart){
			TaskList list = main.getTaskList();
			int rowIndex = list.getSelectedRow();
			if(rowIndex>=list.getRowCount()||rowIndex<0) return;
			
			int response = JOptionPane.showConfirmDialog(null, "Restart the Task ?");
			
			if (response==JOptionPane.YES_OPTION){			
				Task t= (Task) main.getTaskList().getValueAt(rowIndex, 2);
				t.restart();
			}
		}
		if(src== share){
			
			TaskList list = main.getTaskList();
			int rowIndex = list.getSelectedRow();
			if(rowIndex>=list.getRowCount()||rowIndex<0) return;
			Task t= (Task) main.getTaskList().getValueAt(rowIndex, 2);
			new ShareFrame(t, main);
		}
		if(src == inbox){
			new ReceiverFrame(main.getRequestListener().getMessages(), main);
		}
		
	}
}
