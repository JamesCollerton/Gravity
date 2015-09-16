import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.util.concurrent.*;

// This class creates obstacles for the game field. Each of the obstacles
// is given a time to sleep before moving (their speed), access to the queue
// from the game field in order to update itself, something telling it if
// it is a powerup or not and whether it should be double the regular size.
// The game field then holds a list of these obstacles to be drawn to screen.

// Responsibilities: Holding all the information that defines an obstacle.
//                   Giving access to it to the game field to draw it.

public class Obstacle
{
    private final int CANVAS_WIDTH = Game_Const.CANV_W, 
                  	  CANVAS_HEIGHT = Game_Const.CANV_H,
                      TOP = 275,
                      WIDTH = 25,
                      END = -40,
                      SLEEP = 10,
                      MOVE_INCR = 1,
                      SPEED_SLEEP = 3;

    private int x, y, width, sleep;
    private boolean game_over;
    private boolean power_up;
    private BlockingQueue<Queue_Action> queue;
    private Game_Field game_field;
    private Random randomGenerator = new Random();

    // Here we set how quickly the obstacle moves, the queue, if it is a
    // powerup, and its size. It also needs access to the game field in order
    // to tell it to look for updates.
	Obstacle(Game_Field game_field, int sleep, BlockingQueue<Queue_Action> queue,
             boolean power_up, boolean double_size)
	{
        this.game_field = game_field;
        this.sleep = sleep;
        this.queue = queue;
        this.power_up = power_up;

        x = CANVAS_WIDTH;
        game_over = false;

        if(double_size){ width = 2 * WIDTH; }
        else{ width = WIDTH;} 

        if(randomGenerator.nextInt(2) == 0){ y = 0; }
        else if(double_size){ y = TOP - WIDTH; }
        else{ y = TOP; }

        move();
	}

    // This moves the object along and tells the game field to try and paint
    // it and to look for collisions.
    private void move()
    {
        Thread thread = new Thread() {
            public void run() {

                while(x > END && !game_over){ 
                    x -= MOVE_INCR; 
                    queue.add(Queue_Action.PAINT);
                    game_field.obstacles_interact();
                    queue.add(Queue_Action.DETECT_COLLISION);
                    game_field.obstacles_interact(); 
                    animate_sleep(sleep); 
                }

            }
        };
        thread.start();
    }

    // Sleeps the thread for the allotted time.
    private void animate_sleep(int length)
    {
        try { Thread.sleep(length); }
        catch (InterruptedException e){ 
            throw new Error("\n\nThread sleep error\n\n"); 
        }
    }

    // Used to swap the game over status. When it is game over we stop
    // the obstacles moving.
    public void switch_game_over(boolean new_game_over)
    {
        game_over = new_game_over;
    }

    // Used by the game field classes to access the obstacle information.
    public int x()
    {
        return(x);
    }

    public int y()
    {
        return(y);
    }

    public int width()
    {
        return(width);
    }

    public void double_w()
    {
        width = 2 * WIDTH;
        if(y == TOP){ y = TOP - WIDTH; }
    }

    public void double_s()
    {
        sleep = SPEED_SLEEP;
    }

    public boolean power_up()
    {
        return(power_up);
    }

    public void reduce_size()
    {
        width = WIDTH;
        if(y == TOP - WIDTH){ y = TOP; }
    }

    // Only for testing, used to check that the obstacles have been set
    // up correctly and to set values for consitent testing.
    public int sleep()
    {
        return(sleep);
    }

    public void change_x(int new_x)
    {
        x = new_x;
    }

    public void change_y(int new_y)
    {
        y = new_y;
    }
}