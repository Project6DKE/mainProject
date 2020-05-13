package MainProject;

import readingOfFunctions.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import javax.swing.border.EmptyBorder;
import java.io.IOException;

public class UI {
	private JFrame frame;
	private JPanel panel, mainMenu, designn, importP, play;
	
	PuttingSimulator s;
	
	Font myFont= new Font("SansSerif", Font.BOLD, 30);
	Font myFont1= new Font("SansSerif", Font.BOLD, 60);
	Color myColor= new Color(210,210,210);
	
	public UI() {
		this("Golf",1500,1000);
	}
	
	public UI(String title, int x, int y) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
		frame= new JFrame();
		frame.setTitle(title);
		frame.setSize(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel= new JPanel() { 
			protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        int w = getWidth();
	        int h = getHeight();
	        Color color1 = new Color(100,0,255);
	        Color color2 = new Color(150,0,150);
	        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
	        g2d.setPaint(gp);
	        g2d.fillRect(0, 0, w, h);
	    }};
		frame.add(panel);
		setUpMainMenu();
		
		refresh();
			}};
			SwingUtilities.invokeLater(r);
	}
	
	
	
	public void setUpMainMenu() {
		mainMenu= new JPanel();
		mainMenu.setOpaque(false);
		mainMenu.setBorder(new EmptyBorder(frame.getHeight()/3, frame.getWidth()/5, frame.getHeight()/4, frame.getWidth()/4));
		
		JLabel TitleL = new JLabel("Group 6 Project 1-2 ");
		TitleL.setHorizontalAlignment(SwingConstants.LEADING);
		TitleL.setBorder(null);
		TitleL.setFont(myFont1);
		TitleL.setForeground(new Color(255,255,255));
		
		JButton button1 = new JButton("Play");
		button1.setContentAreaFilled(false);
		button1.setHorizontalAlignment(SwingConstants.LEADING);
		button1.setBorder(null);
		button1.setFont(myFont);
		button1.setForeground(myColor);
		
		JButton button2 = new JButton("Import Course and Play");
		button2.setContentAreaFilled(false);
		button2.setHorizontalAlignment(SwingConstants.LEADING);
		button2.setBorder(null);
		button2.setFont(myFont);
		button2.setForeground(myColor);
		
		JButton button3 = new JButton("Create a Course and Play");
		button3.setContentAreaFilled(false);
		button3.setHorizontalAlignment(SwingConstants.LEADING);
		button3.setBorder(null);
		button3.setFont(myFont);
		button3.setForeground(myColor);
		
		
		JPanel menuPanel = new JPanel(new GridLayout(0, 1, 20, 20));
		menuPanel.setOpaque(false);
		menuPanel.add(TitleL);
		menuPanel.add(button1);
		menuPanel.add(button2);
		menuPanel.add(button3);
		mainMenu.add(menuPanel);
		panel.add(mainMenu);
		
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Function2d height= new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
				
				Vector2d flag = new Vector2d(0,3);
				Vector2d start = new Vector2d(0,0);
				
				double g,m,mu,vmax,tol;
				g=9.81;m=45.93/1000;mu=0.131;vmax=3;tol=0.02;
				
				PuttingCourse course = new PuttingCourse(height,flag, start, mu, vmax,tol,g,m );
				
				
				panel.remove(mainMenu);
				playGame(course);
				refresh();
			}
			
		});
		
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.remove(mainMenu);
				
				setUpImportWindow();
				refresh();
			}
			
		});
		
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.remove(mainMenu);
				setUpCourseWindow();
				refresh();
			}
			
		});
	}
	
	public void setUpImportWindow() {
		importP= new JPanel(new BorderLayout());
		importP.setOpaque(false);
		JPanel menuPanel = new JPanel(new GridLayout(0, 1, 20, 20));
		JLabel fromatL = new JLabel("<html>File has to be in this Format:<br>g = 1<br>m = 2<br>mu = 3<br>vmax = 4<br>tol = 5<br>startX =  6<br>startY =  7<br>goalX = 8<br>goalY = 9"
				+ "<br>height =-0.01 * x + 0.003 * x ^ 2 + 0.04 * y<br>ballposX = 10<br>baLlposY = 11<br>stroke = 12<br>shot = 13<br>velocity = 14<br>direction = 15</html>");
		fromatL.setHorizontalAlignment(SwingConstants.LEADING);
		fromatL.setBorder(null);
		fromatL.setFont(myFont);
		fromatL.setForeground(myColor);
		importP.add(fromatL, BorderLayout.NORTH);//fromatL.setBounds(0, 0, 120, frame.getHeight());
		
		//importP.setBorder(new EmptyBorder(frame.getHeight()/3, frame.getWidth()/4, frame.getHeight()/4, frame.getWidth()/4));
		
		JLabel filePL = new JLabel("File Path: ");
		filePL.setHorizontalAlignment(SwingConstants.LEADING);
		filePL.setBorder(null);
		filePL.setFont(myFont1);
		filePL.setForeground(new Color(255,255,255));
		
		JTextField filePathT = new JTextField();
		
		filePL.setLabelFor(filePathT);
		
		JButton button3 = new JButton("Import and Play");
		button3.setContentAreaFilled(false);
		button3.setHorizontalAlignment(SwingConstants.LEADING);
//		button3.setBorder(null);
		button3.setFont(myFont);
		button3.setForeground(Color.WHITE);
		
		
		menuPanel.setOpaque(false);
		menuPanel.add(filePL);
		menuPanel.add(filePathT);
		menuPanel.add(button3);
		importP.add(menuPanel,BorderLayout.CENTER);
		
		
		panel.add(importP);
		
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub	
				FunctionReader xh;
				try {
					xh = new FunctionReader(filePathT.getText());
					PuttingCourse course1 = xh.get_Course();
					panel.remove(importP);
					playGame(course1);
					refresh();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "File cant be found");	
				}
			}
			
		});
	}
	
	public void setUpCourseWindow() {
		JPanel design= new JPanel();
		
		designn = new JPanel();designn.setOpaque(false);
		designn.setLayout(new BorderLayout());
		
		design.setLayout(new GridLayout(5,4,40,60));
		panel.setLayout(null);
		
		JLabel gL= new JLabel("g =");design.add(gL);gL.setFont(myFont);gL.setForeground(Color.WHITE);
		JTextField gT = new JTextField();design.add(gT);
		
		JLabel mL= new JLabel("m =");design.add(mL);mL.setFont(myFont);mL.setForeground(Color.WHITE);
		JTextField mT = new JTextField();design.add(mT);

		JLabel muL= new JLabel("mu =");design.add(muL);muL.setFont(myFont);muL.setForeground(Color.WHITE);
		JTextField muT = new JTextField();design.add(muT);

		JLabel vmaxL= new JLabel("vmax =");design.add(vmaxL);vmaxL.setFont(myFont);vmaxL.setForeground(Color.WHITE);
		JTextField vmaxT = new JTextField();design.add(vmaxT);

		JLabel tolL= new JLabel("tol =");design.add(tolL);tolL.setFont(myFont);tolL.setForeground(Color.WHITE);
		JTextField tolT = new JTextField();design.add(tolT);

		JLabel startxL= new JLabel("startX =");design.add(startxL);startxL.setFont(myFont);startxL.setForeground(Color.WHITE);
		JTextField startxT = new JTextField();design.add(startxT);

		JLabel startyL= new JLabel("startY =");design.add(startyL);startyL.setFont(myFont);startyL.setForeground(Color.WHITE);
		JTextField startyT = new JTextField();design.add(startyT);

		JLabel goalxL= new JLabel("goalX =");design.add(goalxL);goalxL.setFont(myFont);goalxL.setForeground(Color.WHITE);
		JTextField goalxT = new JTextField();design.add(goalxT);

		JLabel goalyL= new JLabel("goalY =");design.add(goalyL);goalyL.setFont(myFont);goalyL.setForeground(Color.WHITE);
		JTextField goalyT = new JTextField();design.add(goalyT);

		JLabel hL= new JLabel("height =");design.add(hL);hL.setFont(myFont);hL.setForeground(Color.WHITE);
		JTextField hT = new JTextField();design.add(hT);
		
		JButton createB = new JButton("Create");
		createB.setContentAreaFilled(false);
		createB.setHorizontalAlignment(SwingConstants.LEADING);
//		createB.setBorder(null);
		createB.setFont(myFont);
		createB.setForeground(Color.WHITE);
		
		
		design.setOpaque(false);
		designn.add(design,BorderLayout.CENTER);designn.add(createB);createB.setBounds(frame.getWidth()/2-50, frame.getHeight()-100, 100, 100);
		designn.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		panel.add(designn);design.setBounds(100, 25, frame.getWidth()-250, frame.getHeight()-250);
	
		createB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.print("I was pressed");
				try {
					double g,m,mu,vmax,tol,sX,sY,gX,gY;
					String he=hT.getText();
					g=Double.valueOf(gT.getText());
					m=Double.valueOf(mT.getText());
					mu=Double.valueOf(muT.getText());
					vmax=Double.valueOf(vmaxT.getText());
					tol=Double.valueOf(tolT.getText());
					sX=Double.valueOf(startxT.getText());
					sY=Double.valueOf(startyT.getText());
					gX=Double.valueOf(goalxT.getText());
					gY=Double.valueOf(goalyT.getText());
					
					PuttingCourse course= new PuttingCourse(new FunctionH((he)),new Vector2d(sX, sY),new Vector2d(gX, gY),mu,vmax,tol,g,m );
					
					panel.remove(designn);
					playGame(course);
					refresh();
				}
				catch(Exception exx) {
					JOptionPane.showMessageDialog(null, "Error! Please make sure everything is in the correct format");
					//exx.printStackTrace();
				}
			}
			
		});
	}
	
	public void playGame(PuttingCourse course) {
		play= new JPanel();frame.remove(panel);
		JPanel rightSide= new JPanel();rightSide.setBounds(0, 0, frame.getWidth()/4, frame.getHeight());rightSide.setLayout(null);
		JPanel leftSide= new JPanel();leftSide.setBounds(frame.getWidth()/4,0, frame.getWidth()-frame.getWidth()/4, frame.getHeight());
		frame.add(rightSide);
		frame.add(leftSide);
		rightSide.setBackground(Color.blue);
		leftSide.setBackground(Color.GREEN);
		
		EulerSolver engine= new EulerSolver();
		s= new PuttingSimulator(course,engine);
		
		JLabel titel_L = new JLabel(course.toString());titel_L.setFont(myFont);titel_L.setForeground(Color.WHITE);
		rightSide.add(titel_L);titel_L.setBounds(10,10,rightSide.getWidth(),rightSide.getHeight());
		
		//f.add(play);
	}
	
	
	public void refresh() {
		frame.setVisible(false);frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new UI();
	}
}
