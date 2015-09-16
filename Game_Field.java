import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.util.concurrent.*;

// This class actually holds the game map, the white rectangle we see in the
// center of the screen. It controls drawing all of the objects on that map
// excluding the ball and handling the collisions that happen there.

// Responsibilities: Holding the game map.
// 					 Drawing the map and obstacles on it.
// 					 Handling collision and the results of collision.

public class Game_Field extends JPanel
{

	private final int WIDTH = Game_Const.CANV_W,
					  HEIGHT = Game_Const.CANV_H,
					  OBS_SLEEP = 2000,
					  POWER_UP_GAP = 3,
					  POWER_UP_WAIT = 4,
					  DOUBLE_SIZE = 0,
					  DOUBLE_SPEED = 1,
					  INVINCIBLE = 2,
					  END = -39;

	// Indicators to tell us whether powerups are on or not or if the game has
	// ended. 
	private boolean double_size,
					double_speed,
					invincible,
					powered_up,
					game_over;

	// Counters to be used to count how long powerups have been on or how many
	// obstacles we have on the field. More obstacles means they go quicker,
	// and after a while we want to turn powerups off.
	private int double_size_counter, 
				double_speed_counter,
				invincible_counter,
				obstacle_counter,
				obs_sleep = 800,
				power_up_counter = 0;

	private BlockingQueue<Queue_Action> queue;
	private BlockingQueue<Queue_Action> obstacle_queue;
	private Game_Ball ball;
	private Game game;
	private Random randomGenerator = new Random();

	// List of all obstacles on the field.
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

	// Set all of the properties for the field and then create queues so we
	// can pass onto different threads. We initialise all the indicators to 
	// false as nothing has happened yet.
	Game_Field(Game game)
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT / 2));
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);

		queue = new LinkedBlockingQueue<Queue_Action>();
		obstacle_queue = new LinkedBlockingQueue<Queue_Action>();
		ball = new Game_Ball(queue);

		game_over = false;
		double_speed = false;
		double_size = false;
		invincible = false;
		powered_up = false;

		this.game = game;

		add(ball);
		create_obstacles();
	}

	// We set all of the counters to zero. At one point I did try putting all
	// of the counters in a dictionary, but that ended up being more complex 
	// than just keeping them all seperate. Then while the game is not finished
	// we increment all the counters and add an obstacle after every alotted 
	// time.
	public void create_obstacles()
	{
		Thread thread = new Thread() {
            public void run() {

            obstacle_counter = 0; 
            double_size_counter = 0;
            double_speed_counter = 0;
            invincible_counter = 0;

                while(!game_over){
                	increment_counters();
                    add_obstacle(obstacle_counter);
                    animate_sleep(obs_sleep);
                    check_powerups();
                }

            }
        };
        thread.start();
	}

	// Add one to every counter each time an obstacle is released.
	private void increment_counters()
	{
		++obstacle_counter;
    	++double_size_counter;
    	++double_speed_counter;
    	++invincible_counter; 
	}

	// This is used to turn off the powerups if they have been on too long.
	private void check_powerups()
	{
		if(double_size_counter == POWER_UP_WAIT){ 
        	double_size = false; 
        	powered_up = false;
        	reduce_sizes(); 
        }

        if(double_speed_counter == POWER_UP_WAIT){ 
        	double_speed = false; 
        	powered_up = false; 
        }

        if(invincible_counter == POWER_UP_WAIT){ 
        	invincible = false; 
        	ball.invincible_switch(false);
			powered_up = false; 
		}
	}

	// Goes through all of the obstacles asking them to reduce their size.
	private void reduce_sizes()
	{
		for(Obstacle obstacle: obstacles){ obstacle.reduce_size(); }
	}

	// This adds an obstacle. It creates an array of waiting times before the
	// next obstacle is added and an array of speeds at which the seperate 
	// obstacles travel at. Then if we are in a later stage of the game 
	// objects travel faster and appear more often.
	public void add_obstacle(int obstacle_count)
	{
		Obstacle obstacle;

		int[] waits = {2000, 1300, 800, 300};
		int[] speeds = {10, 5, 2, 1};

		if(double_speed){
			for(int i = 0; i < waits.length; ++i){ waits[i] /= 2; speeds[i] /= 2;}
		}

		if(obstacle_count < 10){ obstacle = single_obstacle(speeds[0], waits[0]); }
		else if(obstacle_count < 30){ obstacle = single_obstacle(speeds[1], waits[1]); }
		else if(obstacle_count < 50){ obstacle = single_obstacle(speeds[2], waits[2]);}
		else{ obstacle = single_obstacle(speeds[3], waits[3]); }

        obstacles.add(obstacle);
	}

	// This creates a single obstacle. If we have had the set number of
	// obstacles without powerups then the next obstacle we create will be
	// a powerup. We also update the sleep time until the next obstacle
	// is created.
	private Obstacle single_obstacle(int sleep, int new_obs_sleep)
	{
		Obstacle obstacle;
		boolean is_power_up = false;

		if( ++power_up_counter % POWER_UP_GAP == 0){ is_power_up = true; }

		obstacle = new Obstacle(this, sleep, obstacle_queue, 
								is_power_up, double_size); 
		obs_sleep = new_obs_sleep;

		return(obstacle);
	}

	private void animate_sleep(int length)
    {
        try { Thread.sleep(length); }
        catch (InterruptedException e){ 
            throw new Error("\n\nThread sleep error\n\n"); 
        }
    }

    // All of the above are to set the game ball taking the commands as needed.
	public void jump()
	{
		queue.add(Queue_Action.JUMP);
		ball.animate();
	}

	public void grav_up()
	{
		queue.add(Queue_Action.GRAV_UP);
		ball.animate();
	}

	public void grav_down()
	{
		queue.add(Queue_Action.GRAV_DOWN);
		ball.animate();
	}

	// This is called from the obstacle class. Each of the obstacles has access
	// to a queue from this class and they can add on actions to be taken,
	// such as asking to be painted to screen after moving or looking for
	// collisions. This function takes items off the queue and begins to update
	// the game field.
	public void obstacles_interact()
	{
		Queue_Action action = Queue_Action.NOTHING;

        try { action = obstacle_queue.take(); }
        catch (Exception err) { throw new Error("\n\nGame Queue Error\n\n"); }

        if( action == Queue_Action.PAINT ){ repaint(); }
        if( action == Queue_Action.DETECT_COLLISION){ collision_detection(); }
	}

	// This detects if anything has collided. It goes through all of the
	// objects and looks for the coordinates clashing. If they are detected
	// to have clashed then it finds out if the obstacle is a power up, or if
	// it's game over.
	private void collision_detection()
	{
		boolean power_up_on = false;

		for(Obstacle obstacle : obstacles){
			if(horizontal_collision(obstacle) && vertical_collision(obstacle)){
				if(obstacle.power_up()){ power_up_on = true; } 
				else if(!invincible) { game_over = true; }
			}
		}

		if(power_up_on){ power_up_activate(false, 0); powered_up = true; }
		if(game_over){ game_over(); }
	}

	// This activates power ups, randomly choosing one from the list. It goes
	// through all the current obstacles and applies the powerup and then
	// sets indicators so that the next obstacles to be generated also have
	// the powerup supplied. Finally it resets the counter for the powerup to
	// zero so that after a fixed amount of time it can be switched off again.

	// (Only public for testing.)
	public void power_up_activate(boolean testing, int test)
	{
		int power_up_type = randomGenerator.nextInt(3);

		if(testing){ power_up_type = test; }

		if(!powered_up){
			switch(power_up_type) {
				case DOUBLE_SIZE: 
					for(Obstacle obstacle : obstacles){ obstacle.double_w(); }
					double_size = true;
					double_size_counter = 0;
					break;
				case DOUBLE_SPEED:
					for(Obstacle obstacle : obstacles){ obstacle.double_s(); }
					double_speed = true;
					double_speed_counter = 0;
					break;
				case INVINCIBLE:
					invincible = true;
					ball.invincible_switch(true);
					invincible_counter = 0;
					break;
				default:
					break;
			}
		}
	}

	// This is the game over sequence. It goes through all of the existing
	// obstacles telling them that it's game over so they stop adding updates
	// to the queue, then runs the game over sequence from the menu.
	private void game_over()
	{
		for(Obstacle obstacle: obstacles){ obstacle.switch_game_over(true); }
		game.game_over();
	}

	// Used to detect if the ball has collided in terms of the horizontal field.

	// (Only public for testing.)
	public boolean horizontal_collision(Obstacle obstacle)
	{
		if( (ball.x() + (ball.r() / 2) + (obstacle.width() / 2) > obstacle.x() ) &&
			(ball.x() - (ball.r() / 2) - (obstacle.width() / 2) < obstacle.x() ) ){
			return(true);
		}
		return(false);
	}

	// Used to detect if the ball has collided in terms of the vertical field.
	// The increment is because the ball and the field are drawn on slightly
	// different size canvases.

	// (Only public for testing.)
	public boolean vertical_collision(Obstacle obstacle)
	{
		int increment;

		if(double_size){ increment = -15; }
		else{ increment = 10; }

		if( (ball.y() + increment + (ball.r() / 2) + (obstacle.width() / 2) > 
			obstacle.y() ) &&
			(ball.y() + increment - (ball.r() / 2) - (obstacle.width() / 2) < 
			obstacle.y() ) ){
			return(true);
		}
		return(false);
	}

	// Used to paint the obstacles to screen.
	public void paintComponent(Graphics g0) {

        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0;

        RenderingHints rh = new RenderingHints(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(rh);

        for(int i = 0; i < obstacles.size(); ++i){ colour_obstacle(i, g); }
    }
	
	// Obstacles are represented by black squares, powerups by red balls. In
	// order to speed up garbage collection and free up memory, once the 
	// obstacle has left the screen it is removed from the list.
    public void colour_obstacle(int i, Graphics2D g){

    	    if(obstacles.get(i).power_up()){
        		g.setColor(Color.RED);
        		g.fillOval(obstacles.get(i).x(), 
	        			   obstacles.get(i).y(), 
	        			   obstacles.get(i).width(),
	        			   obstacles.get(i).width());
        	}
        	else{
        		g.setColor(Color.BLACK);
	        	g.fillRect(obstacles.get(i).x(), 
	        			   obstacles.get(i).y(), 
	        			   obstacles.get(i).width(),
	        			   obstacles.get(i).width());
	        }

        	if(obstacles.get(i).x() < END){ obstacles.remove(i); }
    }

    // All of the below are used only for testing and are to return values
    // for checking or to set up components of the class so tests return 
    // regular results.

    // Only for testing.
    public ArrayList<Obstacle> obstacles()
    {
    	return(obstacles);
    }

    public boolean double_size()
    {
    	return(double_size);
    }

    public boolean double_speed()
    {
    	return(double_speed);
    }
		
    public boolean invincible()
    {
    	return(invincible);
    }

    public Game_Ball ball()
    {
    	return(ball);
    }

    public void change_ball_y(int new_y)
    {
    	ball.change_y(new_y);
    }
}