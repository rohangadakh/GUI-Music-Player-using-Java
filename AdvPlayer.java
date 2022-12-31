import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.sql.*;

public class AdvPlayer extends JFrame implements ActionListener
{

int i = 1, plyed = 1;

	// to store current position
	Long currentFrame;
	Clip clip;

	// current status of clip;
	String status, songName;

	AudioInputStream audioInputStream;

	JLabel sgnmlbl;
	JButton psbtn, plybtn, stbtn, exbtn, rsbtn, nxtbtn, prevbtn;
	int cnt = 1;

	ImageIcon stop = new ImageIcon("stop.png"); // image path
	ImageIcon resume = new ImageIcon("resume.png"); // image path
	ImageIcon pause = new ImageIcon("pause.png"); // image path
		
	AdvPlayer()
	{
		setLayout(null);
		

		plybtn = new JButton("Play");
		rsbtn = new JButton(resume);
		psbtn = new JButton(pause);
		stbtn = new JButton(stop);
		exbtn = new JButton();
		nxtbtn = new JButton("Next");
		prevbtn = new JButton("Previous");
		sgnmlbl = new JLabel("Music");		

		add(plybtn);
		add(rsbtn);
		add(psbtn);
		add(stbtn);
		add(exbtn);
		add(nxtbtn);
		add(sgnmlbl);
		add(prevbtn);
	
		plybtn.addActionListener(this);
		stbtn.addActionListener(this);
		rsbtn.addActionListener(this);
		psbtn.addActionListener(this);
		exbtn.addActionListener(this);
		nxtbtn.addActionListener(this);
		prevbtn.addActionListener(this);

		plybtn.setBounds(20,300,50,50);
		rsbtn.setBounds(70,300,50,50);
		psbtn.setBounds(120,300,50,50);
		stbtn.setBounds(170,300,50,50);
		exbtn.setBounds(220,300,50,50);
		nxtbtn.setBounds(20,270,100,25);
		prevbtn.setBounds(170,270,100,25);
		sgnmlbl.setBounds(20,200,200,70);
		
		sgnmlbl.setForeground(Color.white);
		 sgnmlbl.setFont(new Font("Verdana", Font.PLAIN, 20));
  		 sgnmlbl.setPreferredSize(new Dimension(250, 100));
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection con = DriverManager.getConnection("jdbc:odbc:DSNMusic");
			System.out.println("Connection Successful");

			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from Table1";
			ResultSet rs = st.executeQuery(sql);
		
			String filePath = "";

			if(ae.getSource()==plybtn)
			{	
			rs.absolute(i);
			filePath = rs.getString(2);
			songName = rs.getString(1);	
			
			con.close();
			
				if(cnt==1)
				{		
				
				sgnmlbl.setText(songName);

				// create AudioInputStream object
				audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

				// create clip reference
				clip = AudioSystem.getClip();
		
				// open audioInputStream to the clip
				clip.open(audioInputStream);
		
				clip.loop(Clip.LOOP_CONTINUOUSLY);	
				cnt++;
				i++;
				}
			}
			else if(ae.getSource()==stbtn)
			{
				if(this.clip==null)
				{
					System.out.println("Null");
				}
				else
				{
					currentFrame = 0L;
					clip.stop();
					clip.close();
					cnt =1;
				}
			}
			else if(ae.getSource()==rsbtn)
			{
				if(this.clip==null)
				{
					System.out.println("Null");
				}
				else
				{
					//start the clip
					clip.start();
					status = "play";
				}
			}	
			else if(ae.getSource()==psbtn)
			{
				if(this.clip==null)
				{
					System.out.println("Null");
				}
				else
				{	
					currentFrame = 0L;
					clip.stop();
				}
			}	
			else if(ae.getSource()==exbtn)
			{
				System.out.println("In Exit Button");
				dispose();
				con.close();
			}
			else if(ae.getSource()==nxtbtn)
			{
				currentFrame = 0L;
				clip.stop();
				
				if(rs.absolute(i) && i!=1)
				{	
					filePath = rs.getString(2);
					songName = rs.getString(1);	
					sgnmlbl.setText(songName);				

					System.out.println("Song path : " + filePath);

					// create AudioInputStream object
					audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

					// create clip reference
					clip = AudioSystem.getClip();
				
					// open audioInputStream to the clip
					clip.open(audioInputStream);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					
					i++;
				} 
			}
			else if(ae.getSource()==prevbtn)
			{
					currentFrame = 0L;
					clip.stop();
				
				if(rs.absolute(i) && i!=1)
				{	
					filePath = rs.getString(2);
					songName = rs.getString(1);	
					sgnmlbl.setText(songName);				

					System.out.println("Song path : " + filePath);

					// create AudioInputStream object
					audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

					// create clip reference
					clip = AudioSystem.getClip();
				
					// open audioInputStream to the clip
					clip.open(audioInputStream);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					
					i--;

				}
				else 
				{
					rs.absolute(1);
					cnt = 1;
				}	
			}	
			else
			{
				System.out.println("Else in ActionPerformed");
			}
		} catch(Exception e) { System.out.println(e); }
	}
	
	public static void main(String args[])
	{
		AdvPlayer obj = new AdvPlayer();
		obj.setTitle("GUI AdvMusicPlayer");
		obj.getContentPane().setBackground(Color.BLACK);
		obj.setSize(305,407);
		obj.setVisible(true);
	}
}
