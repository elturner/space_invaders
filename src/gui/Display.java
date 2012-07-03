package gui;
	
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
public class Display extends JPanel
{
	private BufferedImage myImage;
	private Graphics myBuffer;
	private int wide;
	private int tall;
	
	 public Display(int x, int y)
	{
		setSize(x, y);
		setMaximumSize(new Dimension(x, y));
		setMinimumSize(new Dimension(x, y));
		wide = x;
		tall = y;
	
		myImage = new BufferedImage(wide, tall, 
				BufferedImage.TYPE_INT_RGB);	 
		myBuffer = myImage.getGraphics();
	}
	 public void paintComponent(Graphics g)
	{
		g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
	}
	 public Graphics draw()
	{
		return myBuffer;
	}
	 public int width()
	{
		return wide;
	}
	 public int height()
	{
		return tall;
	}	
}
