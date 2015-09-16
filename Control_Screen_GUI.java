import java.util.*;
import javax.swing.*;
import java.awt.Font;
import java.util.concurrent.*;

// This class draws the control screen graphics to the screen. It contains the
// list of options and the canvas on which the control ball is drawn.

// Responsibilities: Creating the control screen from the list and ball.
// 					 Sending information about control screen actions to the
// 					 menu and the control ball.

public class Control_Screen_GUI 
{
	private Display_Window win;
	private Options_List controls_list;
	private Menu menu;
	private Control_Ball control_ball;
	private BlockingQueue<Queue_Action> queue;

	private Font font;
	private String highlight; 
	private int control_y;
	private ArrayList<String> control_options = new ArrayList<String>();

	private final int START = 0;

	private final String JUMP = "Jump:_Space", 
				   		 GRAV_UP = "Gravity_Up:_Up_Arrow",
				   		 GRAV_DOWN = "Gravity_Down:_Down_Arrow",
				   		 BACK = "Back";

	// Needs the menu in order to send messages back to it, the font to make
	// sure all text appears the same. Then creates a queue to communicate with
	// the control ball. It adds all of the necessary options to a list to form
	// the controls list and then later makes the menu using the Options_List
	// class. 
	Control_Screen_GUI(Menu menu)
	{
		this.menu = menu;
		this.win = menu.win();
		this.font = menu.font().deriveFont(20f);

		queue = new LinkedBlockingQueue<Queue_Action>();

		control_options.add(JUMP);
		control_options.add(GRAV_UP);
		control_options.add(GRAV_DOWN);
		control_options.add(BACK);
		
		controls_list = new Options_List(this.font);
		control_ball = new Control_Ball(this);
	}

	// This actually runs the control screen. First of all we position the
	// cursor so that the jump option at the top is selected, then we make the
	// list of options with jump highlighted. Finally we update the display with
	// this and tell the ball to start jumping.
	public void run()
	{
		control_y = START;
		Box options_box = make_options_screen(JUMP);
		win.update(options_box, null, null, this);
		queue.add(Queue_Action.JUMP);
		control_ball.animate();
	}

	// This passes to the options list class the task of making a scrolling list
	// of options to be selected using the ones we defined earlier.
	private Box make_options_screen(String highlight)
	{
		Box options_box = Box.createHorizontalBox();
		options_box.add(controls_list.create_list(control_options, highlight));
		options_box.add(control_ball);
		return(options_box);
	}

	// This updates the position in the menu depending on user interaction.
	// Depending on where we are we update the control ball queue to tell it
	// to do something different.
	public String update_y(int y)
	{
		control_y = y;
		highlight = update_controls(false);
		change_control_ball(highlight);
		return(highlight);
	}

	// This changes the control ball's behaviour. It adds a command to its queue
	// and then calls the function within the control ball to try and take the
	// action.

	// (Only public for testing.)
	public void change_control_ball(String highlight)
	{
		switch(highlight) {
			case JUMP: queue.add(Queue_Action.JUMP); break;
			case GRAV_UP : queue.add(Queue_Action.GRAV_UP); break;
			case GRAV_DOWN: queue.add(Queue_Action.GRAV_DOWN); break;
			default : queue.add(Queue_Action.NOTHING); break;
		}

		control_ball.animate();
	}

	// This updates the controls. If someone scrolls then it highlights the
	// latest option and then updates the display.

	// Only returns something for testing.
	private String update_controls(boolean testing)
	{
		highlight = controls_list.find_new_option(control_options, control_y);
		Box options_box = make_options_screen(highlight);
		if(!testing){ win.update(options_box, null, null, this); }
		return(highlight);
	}

	// If in the control class we sense that we are on the control screen and
	// that we have had space pressed then we pass back to here. which checks
	// what is selected on the menu. If it is back then we tell the menu that.
	public void register_space()
	{
		if(BACK.equals(highlight)){ menu.select("Back"); }
	}

	public String highlight()
	{
		return(highlight);
	}

	public BlockingQueue<Queue_Action> queue()
	{
		return(queue);
	}

}