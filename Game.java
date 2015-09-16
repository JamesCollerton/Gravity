import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Font;

// This is the game class and is responsible for running the main part of the
// game. It communicates game related messages to the rest of the program and
// sets up the game screen with the score, level, game field etc.

// Responsibilities: Running main game.
// 					 Holding game field.
// 					 Holding score and level.
// 					 Running game over.
//  				 Communicating between main game and other components.

public class Game
{
	private Display_Window win;
	private Game_Field game_field;
	private Info_Box score, level;
	private Game_Audio audio;

	private Box main_menu_box;
	private Font font;
	private boolean game_over;

	private final int WIDTH = Game_Const.CANV_W,
					  HEIGHT = Game_Const.CANV_H,
					  SCORE_DEL = 150,
					  LEVEL_DEL = 15000;

	// Takes in the font so it is consistent and window so it can be updated
	// with the game once it is created.
	Game(Menu menu)
	{
		this.font = menu.font().deriveFont(20f);
		this.win = menu.win();
		this.audio = menu.game_audio();

		game_over = false;
	}

	// We make the layout of the game and put it to screen.
	public void run()
	{
		main_menu_box = make_layout();
		win.update(main_menu_box, null, this, null);
	}

	// Here we create a blank box at the top, then add the game field, then
	// add the bottom box with the level and score in.
	private Box make_layout()
	{
		Box menu_box = Box.createVerticalBox();
		JPanel top_box = create_blank_box();
		game_field = new Game_Field(this);
		JPanel bottom_box = create_bottom_box();
		menu_box.add(top_box);
		menu_box.add(game_field);
		menu_box.add(bottom_box);
		return(menu_box);
	}

	// This creates a blank box a quarter of the height of the screen so that
	// the components can be spaced out nicely.

	// (Only public for testing.)
	public JPanel create_blank_box()
	{
		JPanel blank_box = new JPanel();
		blank_box.setPreferredSize(new Dimension(WIDTH, HEIGHT / 4));
		blank_box.setBackground(Color.BLACK);

		return(blank_box);
	}

	// This is the bottom box, we create the two info boxes with the score and
	// level in, add them and then return the box to be put to screen.

	// (Only public for testing.)
	public JPanel create_bottom_box()
	{
		JPanel score_level = create_blank_box();
		score = new Info_Box(font, "Score:", JTextField.LEFT, SCORE_DEL);
		level = new Info_Box(font, "Level:", JTextField.RIGHT, LEVEL_DEL);
		score_level.add(score);
		score_level.add(level);

		return(score_level);
	}

	// When we are told it is game over we create a game over screen using a
	// blank box and the game over class. We feed the score and the level back
	// so that we can print them to screen and update the display window.
	public void game_over()
	{
		audio.game_over();
		Box game_over_box = Box.createVerticalBox();
		JPanel top_box = create_blank_box();
		Game_Over game_over = new Game_Over(font, score.value(), level.value());
		game_over_box.add(top_box);
		game_over_box.add(game_over);
		win.update(game_over_box, null, null, null);
	}

	// Each of these is used to communicate to the game field that we want the
	// ball to do the necessary action.
	public void jump()
	{
		game_field.jump();
	}

	public void grav_up()
	{
		game_field.grav_up();
	}

	public void grav_down()
	{
		game_field.grav_down();
	}

}