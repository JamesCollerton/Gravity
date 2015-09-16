import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

// This is used to run all of the tests for the program and just rattles through
// them one by one. They are very self explanatory and all of them have a 
// message printed above them to say what is going on anyway, so I won't comment
// too heavily.

public class Testing_Suite_Functions
{
	private int MENU = 0, CONTROLS = 1, CONTROL_BALL = 2, MENU_LOGO = 3,
				GAME = 4, GAME_FIELD = 5;

	Display_Window test_win = new Display_Window();
	Menu test_menu = new Menu(test_win);
	Menu_GUI test_menu_gui = new Menu_GUI(test_menu);
	Controls test_controls = new Controls();
	Options_List test_list = new Options_List(test_menu.font());
	Control_Screen_GUI test_control_screen = new Control_Screen_GUI(test_menu);
	Control_Ball test_cntrl_ball = new Control_Ball(test_control_screen);
	Menu_Logo test_menu_logo = new Menu_Logo();
	Game test_game = new Game(test_menu);
	Game_Field test_game_field = new Game_Field(test_game);

	Test_Functions test = new Test_Functions();	

	public void run()
	{

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting menu initialised correctly.\n\n");
		menu_initialisation_test();

		System.out.printf("\n\nTesting menu GUI initialised correctly.\n\n");
		menu_gui_initialisation_test();

		System.out.printf("\n\nTesting font initialisation.\n\n");
		menu_gui_font_initialise();

		System.out.printf("\n\nTesting creation of menu screen box.\n\n");
		menu_gui_box_init_test();

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting creation of option button, New Game.\n\n");
		menu_gui_button_init_test("New Game");

		System.out.printf("\n\nTesting creation of option button, Controls.\n\n");
		menu_gui_button_init_test("Controls");

		System.out.printf("\n\nTesting creation of option button, Exit.\n\n");
		menu_gui_button_init_test("Exit");

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting update menu, move to Exit.\n\n");
		menu_gui_move_cursor(2, "Exit");

		System.out.printf("\n\nTesting update menu, move to Controls.\n\n");
		menu_gui_move_cursor(1, "Controls");

		System.out.printf("\n\nTesting update menu, move to New Game.\n\n");
		menu_gui_move_cursor(0, "New_Game");

		System.out.printf("\n\nTesting update menu, move to Exit.\n\n");
		menu_gui_move_cursor(5, "Exit");

		System.out.printf("\n\nTesting update menu, move to Controls.\n\n");
		menu_gui_move_cursor(4, "Controls");

		System.out.printf("\n\nTesting update menu, move to New_Game.\n\n");
		menu_gui_move_cursor(6, "New_Game");

		System.out.printf("\n\nTesting update menu, move to Exit.\n\n");
		menu_gui_move_cursor(-1, "Exit");

		System.out.printf("\n\nTesting update menu, move to Controls.\n\n");
		menu_gui_move_cursor(-2, "Controls");

		System.out.printf("\n\nTesting update menu, move to New Game.\n\n");
		menu_gui_move_cursor(-3, "New_Game");

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting update controls, move to Jump:_Space.\n\n");
		control_gui_move_cursor(-4, "Jump:_Space");

		System.out.printf("\n\nTesting update controls, move to Gravity Up.\n\n");
		control_gui_move_cursor(-3, "Gravity_Up:_Up_Arrow");

		System.out.printf("\n\nTesting update controls, move to Gravity Down.\n\n");
		control_gui_move_cursor(-2, "Gravity_Down:_Down_Arrow");

		System.out.printf("\n\nTesting update controls, move to Back.\n\n");
		control_gui_move_cursor(-1, "Back");

		System.out.printf("\n\nTesting update controls, move to Jump:_Space.\n\n");
		control_gui_move_cursor(0, "Jump:_Space");

		System.out.printf("\n\nTesting update controls, move to Gravity Up.\n\n");
		control_gui_move_cursor(1, "Gravity_Up:_Up_Arrow");

		System.out.printf("\n\nTesting update controls, move to Gravity Down.\n\n");
		control_gui_move_cursor(2, "Gravity_Down:_Down_Arrow");

		System.out.printf("\n\nTesting update controls, move to Back.\n\n");
		control_gui_move_cursor(3, "Back");

		System.out.printf("\n\nTesting update controls, move to Jump:_Space.\n\n");
		control_gui_move_cursor(4, "Jump:_Space");

		System.out.printf("\n\nTesting update controls, move to Gravity Up.\n\n");
		control_gui_move_cursor(5, "Gravity_Up:_Up_Arrow");

		System.out.printf("\n\nTesting update controls, move to Gravity Down.\n\n");
		control_gui_move_cursor(6, "Gravity_Down:_Down_Arrow");

		System.out.printf("\n\nTesting update controls, move to Back.\n\n");
		control_gui_move_cursor(7, "Back");

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting the controls update object, Menu.\n\n");
		controls_update_object(Game_State.MENU, test_control_screen, test_menu_gui);

		System.out.printf("\n\nTesting the controls update object, Controls.\n\n");
		controls_update_object(Game_State.CONTROLS, test_control_screen, null);

		System.out.printf("\n\nTesting the controls update object, Menu.\n\n");
		controls_update_object(Game_State.MENU, null, test_menu_gui);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting the menu ball x calculations.\n\n");
		menu_x_calc(1, 30, 31);

		System.out.printf("\n\nTesting the menu ball x calculations.\n\n");
		menu_x_calc(700, 30, 670);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting the menu ball y calculations.\n\n");
		menu_y_calc(1, 30, 31);

		System.out.printf("\n\nTesting the menu ball y calculations.\n\n");
		menu_y_calc(161, 30, 160);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting the menu ball vy calculations.\n\n");
		menu_vy_calc(161, 20);

		System.out.printf("\n\nTesting the menu ball vy calculations.\n\n");
		menu_vy_calc(9, 10);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting the options list naming function.\n\n");
		options_rename("Hello_My_Name_is_James", "Hello My Name is James");

		System.out.printf("\n\nTesting the options list naming function.\n\n");
		options_rename("(__)", "(  )");

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting controls back.\n\n");
		controls_back();

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting making the game layout.\n\n");
		game_layout();

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting adding obstacles, obstacle 5.\n\n");
		add_obstacle(5, 10);

		System.out.printf("\n\nTesting adding obstacles, obstacle 10.\n\n");
		add_obstacle(10, 5);

		System.out.printf("\n\nTesting adding obstacles, obstacle 31.\n\n");
		add_obstacle(31, 2);

		System.out.printf("\n\nTesting adding obstacles, obstacle 51.\n\n");
		add_obstacle(51, 1);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting powerups, double size.\n\n");
		activate_powerup(0);

		System.out.printf("\n\nTesting powerups, double speed.\n\n");
		activate_powerup(1);

		System.out.printf("\n\nTesting powerups, invincible.\n\n");
		activate_powerup(2);

		///////////////////////////////////////////////////////////////////////

		System.out.printf("\n\nTesting collision, obstacle passed.\n\n");
		collision(0, 20, 20, false, false, false);

		System.out.printf("\n\nTesting collision, obstacle directly on top.\n\n");
		collision(0, 175, 0, false, false, true);

		System.out.printf("\n\nTesting collision, ball just over obstacle.\n\n");
		collision(50, 175, 0, false, false, false);

		System.out.printf("\n\nTesting collision, ball and obstacle at top.\n\n");
		collision(265, 175, 265, false, false, true);

		System.out.printf("\n\nTesting collision, collided in center screen.\n\n");
		collision(100, 175, 100, false, false, true);

		System.out.printf("\n\nTesting collision, double size.\n\n");
		collision(50, 175, 0, false, true, true);

		System.out.printf("\n\nTesting collision, double size at bottom.\n\n");
		collision(245, 175, 265, false, true, true);

		///////////////////////////////////////////////////////////////////////

		test.results();
	}

	private void menu_initialisation_test()
	{
		test.is(test_menu.menu_options().get(0), 
				"New_Game", 
				"New Game passed", MENU);
		test.is(test_menu.menu_options().get(1), 
				"Controls", 
				"Options passed", MENU);
		test.is(test_menu.menu_options().get(2), 
				"Exit", 
				"Exit passed", MENU);
	}

	private void menu_gui_initialisation_test()
	{
		test.is_not(test_menu_gui.menu(), 
					null, 
					"Menu GUI initialisation passed", MENU);
	}

	private void menu_gui_font_initialise()
	{
		test.is_not(test_menu.initialise_font(), 
					null, 
					"Menu GUI font setting passed", MENU);
	}

	private void menu_gui_box_init_test()
	{
		test.is_not(test_menu_gui.make_menu_screen("New_Game"), 
					null, 
					"Menu GUI box initialisation passed", MENU);
	}

	private void menu_gui_button_init_test(String arg)
	{
		test.is_not(test_list.make_option(arg, arg), 
					null, 
					"Created " + arg + " button." , MENU);
	}

	private void menu_gui_move_cursor(int new_y, String expected_result)
	{
		test.is(test_menu_gui.update_y(new_y), 
				expected_result, 
				"Cursor moved to " + expected_result  , MENU);
	}

	private void control_gui_move_cursor(int new_y, String expected_result)
	{
		test.is(test_control_screen.update_y(new_y), 
				expected_result, 
				"Cursor moved to " + expected_result, CONTROLS);
	}

	private void controls_update_object(Game_State state, 
										Control_Screen_GUI test_control_screen,
										Menu_GUI test_menu_gui)
	{
		test_controls.update_object(test_menu_gui, null, test_control_screen);
		test.is(test_controls.state(), 
				state, 
				"Control state updated " + state, CONTROLS);
	}

	private void menu_x_calc(int start, int vx, int end)
	{
		test_menu_logo.set_x(start);
		test_menu_logo.set_vx(vx);
		test_menu_logo.x_calculations();
		test.is(test_menu_logo.x(), 
				end, 
				"Menu x calculation " + start, MENU_LOGO);
	}

	private void menu_y_calc(int start, int vy, int end)
	{
		test_menu_logo.set_y(start);
		test_menu_logo.set_vy(vy);
		test_menu_logo.y_calculations();
		test.is(test_menu_logo.y(), 
				end, 
				"Menu y calculation " + start, MENU_LOGO);
	}

	private void menu_vy_calc(int start, int end)
	{
		test_menu_logo.set_vy(start);
		test_menu_logo.velocity_calculations();
		test.is(test_menu_logo.vy(), 
				end, 
				"Menu vy calculation " + start, MENU_LOGO);
	}

	private void options_rename(String start, String end)
	{
		test.is(test_list.adjust_text(start), 
				end, 
				"Options rename " + start, MENU_LOGO);
	}

	private void controls_back()
	{
		test_control_screen.register_space();
		test.is(test_menu_gui.highlighted_option(), 
				"New_Game", 
				"Back to menu working.", CONTROLS);
	}

	private void game_layout()
	{
		JPanel test_box = test_game.create_blank_box();
		test.is(test_box.getPreferredSize(), 
				new Dimension(800, 150), 
				"Blank box correct.", GAME);
		test_box = test_game.create_bottom_box();
		test.is(test_box.getPreferredSize(), 
				new Dimension(800, 150), 
				"Bottom panel correct.", GAME);
	}

	private void add_obstacle(int obstacle_count, int expected_sleep)
	{
		int start_size = test_game_field.obstacles().size();
		test_game_field.add_obstacle(obstacle_count);
		int end_size = test_game_field.obstacles().size();
		test.is(start_size, 
				end_size - 1, 
				"Obstacle " + obstacle_count + " added.", GAME_FIELD);
		test.is(test_game_field.obstacles().get(test_game_field.obstacles().size() - 1).sleep(), 
				expected_sleep, 
				"Sleep " + expected_sleep + " registered.", GAME_FIELD);
	}

	private void activate_powerup(int powerup)
	{
		test_game_field.power_up_activate(true, powerup);
		switch(powerup) 
		{
			case 0:
				test.is(test_game_field.double_size(), 
						true, 
						"Double size registered.", GAME_FIELD);
				for(Obstacle obstacle: test_game_field.obstacles() ){
					test.is(obstacle.width(), 
							50, 
							"Obstacle size doubled.", GAME_FIELD);
				}
				break;
			case 1:
				test.is(test_game_field.double_speed(), 
						true, 
						"Double speed registered.", GAME_FIELD);
				for(Obstacle obstacle: test_game_field.obstacles() ){
					test.is(obstacle.sleep(), 
							3, 
							"Obstacle speed doubled.", GAME_FIELD);
				}
				break;
			case 2:
				test.is(test_game_field.invincible(), 
						true, 
						"Invincible registered.", GAME_FIELD);
				test.is(test_game_field.ball().invincible(), 
						true, 
						"Obstacle size doubled.", GAME_FIELD);
				break;
		}
	}

	private void collision(int ball_y, int obs_x, int obs_y, 
						   boolean powerup, boolean double_size,
						   boolean collision_expected)
	{
		BlockingQueue<Queue_Action> queue = new LinkedBlockingQueue<Queue_Action>();
		boolean collided = false;

		Obstacle test_obstacle = new Obstacle(test_game_field,
											  20,
											  queue,
											  powerup,
											  double_size);

		test_obstacle.change_x(obs_x);
		test_obstacle.change_y(obs_y);
		test_game_field.change_ball_y(ball_y);

		if(test_game_field.horizontal_collision(test_obstacle) && 
		   test_game_field.vertical_collision(test_obstacle)){
			collided = true;
		}

		test.is(collided, 
				collision_expected, 
				"Collision " + ball_y + ", " + obs_x + ", "
				+ obs_y + " test passed.", GAME_FIELD);
	}

}