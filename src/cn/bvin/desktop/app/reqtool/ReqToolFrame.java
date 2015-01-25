package cn.bvin.desktop.app.reqtool;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


public class ReqToolFrame extends JFrame {

	private static final String HTTP_PREF = "http://";
	
	private JPanel contentPane;
	private JTextField tfRequestUrl;
	private JButton btExecute;
	private JSplitPane splitPane;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JTable table;
	private String[] requestParamsHeader = {"Name","Value"};
	private String[][] requestParams;

	private RequestWorker worker;
	private JTextArea taFormatResponces;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReqToolFrame frame = new ReqToolFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ReqToolFrame() {
		
		worker = new RequestWorker();
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image image=tk.createImage("icon.png");
		setIconImage(image);
		setTitle("请求调试工具 Alpha版         			by bvin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		tfRequestUrl = new JTextField();
		tfRequestUrl.setColumns(10);
		
		btExecute = new JButton("执行");
		btExecute.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String url = tfRequestUrl.getText();
				if (url!=null&&url.trim().length()>0) {//有输入字符
					if (url.contains(HTTP_PREF)){//包含http前缀
						if (!url.startsWith(HTTP_PREF)) {//如果不是http打头就截取出来
							url = url.substring(url.indexOf(HTTP_PREF));//去除前面非url的字符串
						}
						tfRequestUrl.setText(url);
						String params = url.substring(url.indexOf("?")+1);
						String[] paramMap  = params.split("&");
						requestParams = new String[paramMap.length][];
						for (int i = 0; i < paramMap.length; i++) {
							String queryString = paramMap[i];
							String[] nameAndValue = queryString.split("=");
							requestParams[i] = nameAndValue;
						}
						DefaultTableModel dataModel = new DefaultTableModel(requestParams, requestParamsHeader);
						table.setModel(dataModel);
						((AbstractTableModel)table.getModel()).fireTableDataChanged();
						((AbstractTableModel)table.getModel()).fireTableStructureChanged();
						worker.url(url).execute();
					}else {
					}
				}else {
					
				}
				
			}
		});
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(tfRequestUrl, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btExecute))
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfRequestUrl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btExecute))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
		);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 100));
		splitPane.setLeftComponent(panel);
		
		lblNewLabel = new JLabel("参数表");
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		
		table = new JTable();
		DefaultTableModel dataModel = new DefaultTableModel(requestParams, requestParamsHeader);
		table.setModel(dataModel);
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		
		
		String[] columnNames = {"Name","Value"};   //列名
		String [][]tableVales={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}}; //数据
		DefaultTableModel  tableModel = new DefaultTableModel(tableVales, columnNames);
		
		panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("响应");
		
		taFormatResponces = new JTextArea();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(taFormatResponces, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
						.addComponent(lblNewLabel_1))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(4)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(taFormatResponces, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
	}
	
	private String syncRequest(String requestUrl) {
		HttpURLConnection connection = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();  
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			int read;
			char[] buff = new char[1024];
			while ((read = reader.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return jsonResults.toString(); 
	    
	}
	
	class RequestWorker extends SwingWorker<String, String>{
		
		private String url;
		
		@Override
		protected String doInBackground() throws Exception {
			return syncRequest(url);
		}

		@Override
		protected void process(List<String> chunks) {
			super.process(chunks);
		}
		
		
		@Override
		protected void done() {
			super.done();
			try {
				String result = get();
				result = JsonFomat.format(result);
				taFormatResponces.setText(result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		public RequestWorker url(String url) {
			this.url = url;
			return this;
		}
		
	}
}
