import javax.swing.*;
import java.awt.*;

// This class controls the ball that appears on the menu screen and bounces
// around. Although it might be tempting to say that it would be better to
// put this ball, the control ball and the game ball all in one class, I don't
// actually think that they're all that similar. This one has a horizontal
// velocity and I reversed gravity so it looks cool as it bounces around, the
// control ball is designed to just do one thing at a time and the game ball
// is linked to user input. The only real similarities are the physics used
// to drive them.

// Moving away from that, this class is used to control the menu ball and 
// drawing it to screen.

// Responsibilities: Create canvas for menu ball.
//                   Control the ball's bounce.
//                   Draw the ball to screen.

class Menu_Logo extends JPanel {

    private int x = 250, y = 125, vx = 1, vy = 1;
    private boolean x_back = false, grav_back = false, menu_on = true;
    private Thread thread;

    // Constants used for driving the ball's motion.
    private int GRAVITY = 1, 
                BOUNCE = 1, 
                RADIUS = 50,
                MAX_VELOC = 20, 
                MIN_VELOC = 10, 
                X_L_EDGE = 20,
                X_R_EDGE = 700, 
                Y_BOTTOM = 160, 
                CANVAS_WIDTH = Game_Const.CANV_W, 
                CANVAS_HEIGHT = Game_Const.CANV_H / 2, 
                SLEEP = 30;

    Menu_Logo() 
    {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }

    // We set up a new thread for the ball to bounce on. This is so that it
    // can do so without interrupting the rest of the program. We set the ball
    // bouncing once and it continues to do so on its own thread so when we 
    // come back to it it is still going.
    public void bounce() 
    {
        Thread thread = new Thread() {
            public void run() {

                while(true){

                    try { Thread.sleep(SLEEP); }
                    catch (InterruptedException e) { 
                        throw new Error("\n\nError sleeping thread.\n\n"); 
                    }

                    x_calculations();
                    y_calculations();
                    velocity_calculations();

                    repaint();

                }
            }
        };
        thread.start();   
        
    }

    // These calculate the x movements. When we reach an edge we bounce off it
    // and begin to move back the opposite way.
    public void x_calculations()
    {
        if( x < X_R_EDGE && !x_back){ x += vx; } 
        else if( x == X_R_EDGE ){ x_back = true; x -= vx;}
        else if( x < X_L_EDGE ){ x_back = false;}
        else if( x < X_R_EDGE && x_back ){ x -= vx; }
    }

    // These calculate the y movements. We add on the velocity of the ball as
    // it moves and then when it hits the floor we include an elasticity 
    // component.
    public void y_calculations()
    {
        if(y <= Y_BOTTOM){ y += vy; }
        else if(y > Y_BOTTOM){ y = Y_BOTTOM; vy *= -BOUNCE; }
    }

    // These are used to calculate the velocity of the particle. It increases 
    // until we reach an upper limit, then we set it equal to the upper limit
    // and reverse it, which stops the ball bouncing away. Once it has reached
    // a lower limit we turn gravity backwards again and once more start 
    // increasing the velocity.

    // The overall effect of this is the ball gathers speed up until a point and
    // then loses speed, continually cycling this.
    public void velocity_calculations()
    {
        if(vy <= MAX_VELOC && !grav_back){ vy += GRAVITY; }
        else if(vy > MAX_VELOC && !grav_back){vy = MAX_VELOC; grav_back = true;}
        else if(vy < MIN_VELOC && grav_back){ vy = MIN_VELOC; grav_back = false; }
        else if(vy <= MAX_VELOC && grav_back){ vy -= GRAVITY; }
    }

    // Used to draw the ball to screen.
    public void paintComponent(Graphics g0) {

        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        RenderingHints rh = new RenderingHints(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(rh);
        g.fillOval(x, y, RADIUS, RADIUS);
        
    }

    // Used by various other classes.
    public void menu_on(boolean on_off)
    {
        menu_on = on_off;
    }

    // Just for testing.
    public void set_x(int new_x)
    {
        x = new_x;
    }

    public void set_vx(int new_vx)
    {
        vx = new_vx;
    }

    public int x()
    {
        return(x);
    }

    // Just for testing, used to set values so results are consistent in
    // testing and to return them to be checked.
    public void set_y(int new_y)
    {
        y = new_y;
    }

    public void set_vy(int new_vy)
    {
        vy = new_vy;
    }

    public int y()
    {
        return(y);
    }

    public int vy()
    {
        return(vy);
    }
}