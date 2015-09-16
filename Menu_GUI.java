import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;

// This class displays the menu to the screen. The previous menu class is the
// flow control for the program and determines what goes on. This class is to
// put the information to the screen so the user can see it.

// Responsibilities: Creating the main menu as seen on the opening screen.
// 					 Keeping that updated as the user continues to interact
// 					 with it.

public class Menu_GUI 
{
	private Menu menu;
	private Font font;
	private Options_List menu_options;
	private Menu_Logo menu_logo = new Menu_Logo();
	private Game_Audio game_audio;

	private String highlighted_option;
	private int menu_y = 0;
	private boolean menu_on = true, backing_playing = false;

	// We have the menu included so that we can communicate back to it how
	// the user interacts with the GUI. We also set up an options list as
	// we will need this to create the set of options seen on the menu screen.
	Menu_GUI(Menu menu)
	{
		this.menu = menu;
		this.font = menu.font();
		this.game_audio = menu.game_audio();
		menu_options = new Options_List(this.font);
	}

	// This runs the Menu GUI. When we boot up we set the highlighted option
	// to be the first one on the list. We make the rest of the menu screen and
	// then update the display window. Finally we set the menu logo ball to
	// bounce around the screen.
	public void run()
	{
		highlighted_option = menu.menu_options().get(0);
		Box menu_box = make_menu_screen(highlighted_option);
		menu.win().update(menu_box, this, null, null);
		if(menu_on){ menu_logo.bounce(); }
		if(!backing_playing){ game_audio.playSound(); backing_playing = true; }
	}

	// Used to make the menu screen. It adds the menu logo and then creates a
	// list of options and adds it to the bottom.

	// (Only public for testing.)
	public Box make_menu_screen(String highlight)
	{
		Box menu_box = Box.createVerticalBox();
		menu_box.add(menu_logo);
		menu_box.add(menu_options.create_list(menu.menu_options(), highlight));
        return(menu_box);
	}

	// This updates the current position in the menu.

	// (Only returns something for testing.)
	public String update_y(int y)
	{
		menu_y = y;
		String testing_string = update_menu(false);
		return(testing_string);
	}

	// This updates the menu depending on what is now highlighted. When the
	// player presses up or down on the menu then this is registered, the
	// position in the menu incremented or decremented and the highlighted
	// option changed. The menu window is then updated with the new menu
	// screen.

	// (Only returns something for testing.)
	private String update_menu(boolean testing)
	{
		highlighted_option = menu_options.find_new_option(menu.menu_options(), menu_y);

		Box menu_box = make_menu_screen(highlighted_option);
		if(!testing){ menu.win().update(menu_box, this, null, null); }

		return(highlighted_option);
	}

	// If space is pressed we pass back to the Menu class to control what we
	// want to happen with the program.
	public void register_space()
	{
		menu_on = false;
		menu_logo.menu_on(false);
		menu.select(highlighted_option);
	}

	public Menu menu()
	{
		return(menu);
	}

	public void set_backing_playing(boolean new_backing)
	{
		backing_playing = new_backing;
	}

	// Only for testing, returns what is currently highlighted to make sure
	// scrolling works.
	public String highlighted_option()
	{
		return(highlighted_option);
	}
}