import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Font;

// The info box class is used to create the boxes that contain the level and
// score information. It takes in a metric and then sets a thread running that
// continually counts up the number as the game progresses, finally giving
// an overall score.

// Responsibilities: Holding the value regarding the score or level.
// 					 Incrementing that value at the proper time.
// 					 Giving access to that value to the rest of the program.

public class Info_Box extends JTextField
{
	private Font font;
	private int count, alignment, sleep_time;
	private String text;

	// When constructed we need the font, the string that tells us what
	// we're counting, how we want to align the box and the amount of
	// time we want to spend before the count ticks over.
	Info_Box(Font font, String info_string, int alignment, int sleep_time)
	{
		count = 0;
		this.font = font;
		this.text = info_string;
		this.alignment = alignment;
		this.sleep_time = sleep_time;
		create_info_box();
		count(); 
	}

	// We set the text in the box to be the name of what we're counting and the
	// current count (0). Then we sort out colours and borders etc.
	private void create_info_box()
	{
		setText(text + " " + count);
		setFont(font);
		setEditable(false);
        setHorizontalAlignment(alignment);
        setColumns(15);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder());
	}

	// When the game is started we create threads for each of the counters so
	// they can continually keep counting as the game progresses.
	private void count()
	{
		Thread thread = new Thread() {
            public void run() {

            	while(true){
	            	animate_sleep(sleep_time);
	            	++count;
					setText(text + " " + count);
				}

            }
        };
        thread.start();
	}

	// This returns whatever's being displayed so it can be printed to screen
	// at game over.
	public String value()
	{
		return(text + " " + count);
	}

	// Used to sleep the thread for the required time before the count ticks
	// over.
	private void animate_sleep(int length)
    {
        try { Thread.sleep(length); }
        catch (InterruptedException e) { 
        	throw new Error("\n\nThread sleep error\n\n"); 
        }
    }

}