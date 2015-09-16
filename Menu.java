import java.util.*;
import java.io.*;
import java.awt.Font;
import javax.swing.*;
import java.util.concurrent.*;

// The menu class is responsible for starting and stopping all of the different
// sections of the game. When the program begins it passes to here and the
// Menu GUI is started up. Then when we want to move into the controls screen
// a message is passed here and the controls screen is started up from here.
// The game is also started from this class.

// Responsibilities: Flow control for the program.
//  				 Starting the Menu GUI, Control Screen GUI and Game running.
// 					 Holding the main font for the program.

public class Menu 
{
	private Display_Window win;
	private Menu_GUI menu_gui;
	private Control_Screen_GUI cntrl_gui;
	private Game game;
	private Game_Audio game_audio = new Game_Audio();

	// Used as a default font if the other one doesn't load.
	private Font font = new Font("Serif", Font.ITALIC, 10);
	private ArrayList<String> menu_options = new ArrayList<String>();
	
	private final String NEW_GAME = "New_Game",
						 CONTROLS = "Controls",
						 EXIT = "Exit",
						 BACK = "Back";

	// We take in the display window composed in the main class as this will
	// be used in order to put graphics to screen. We then create a set of 
	// options that can be chosen within the menu and initialise the font, as
	// well as the Menu GUI, the Controls GUI and the Game that can be called 
    // from this part of the program. Finally we start the backing track.
	Menu(Display_Window win)
	{
		this.win = win;
		menu_options.add(NEW_GAME);
		menu_options.add(CONTROLS);
		menu_options.add(EXIT);
		initialise_font();
		menu_gui = new Menu_GUI(this);
		cntrl_gui = new Control_Screen_GUI(this);
		game = new Game(this);
	}

	// We take in the font file in order to create a new font here.

	// (Only returns font for testing.)
	public Font initialise_font()
	{
        try{
            InputStream myStream = new FileInputStream("Fonts/Slapstick.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, myStream);
            font = font.deriveFont(50f);
        }catch(Exception err){ err.printStackTrace();}

        return(font);
    }

    // Once we have create the menu fields we want to run the Menu GUI so the
    // user has something to interact with visually.
	public void run()
	{
		menu_gui.run();
	}

	// Different parts of the program communicate back to this class to tell it
	// what to launch into next. This deals with these commands, running the
	// required part. 
	public void select(String selected_option)
	{
		switch(selected_option){
			case EXIT: System.exit(0); break;
			case CONTROLS: cntrl_gui.run(); break;
			case NEW_GAME: game.run(); break;
			case BACK: run();
			default: break;
		}
	}

	// These are used to be accessed by other classes.
	public ArrayList<String> menu_options()
	{
		return(menu_options);
	}

	public Display_Window win()
	{
		return(win);
	}

	public Font font()
	{
		return(font);
	}

	public Game_Audio game_audio()
	{
		return(game_audio);
	}

}