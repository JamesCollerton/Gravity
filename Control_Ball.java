import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

// This class creates the control ball as seen on the controls screen. It sets
// up three threads, one doing a jumping motion, one doing a gravity up and one
// doing a gravity down. Each of these is then painted to screen depending on
// what the user is highlighting.

// Responsibilities: Creating the control ball canvas.
//                   Animating the control ball.
//                   Painting the control ball to screen.

class Control_Ball extends JPanel {

    private int jump_y, up_y, down_y;
    private boolean jump_selected, up_selected, down_selected;
    private Control_Screen_GUI cntrl_screen;
    private BlockingQueue<Queue_Action> queue;

    // Constants used in defining the physics for the ball.
    private final int GRAVITY = 1, 
                      BOUNCE = 1, 
                      RADIUS = 50,
                      JUMP_VY = 20,
                      X_COORD = 125,
                      Y_BOTTOM = 350,
                      Y_TOP = 150, 
                      CANVAS_WIDTH = 300, 
                      CANVAS_HEIGHT = 650, 
                      SLEEP = 30,
                      STOP = 800;

    // We set the size of the canvas and the colour scheme. The control screen
    // is linked as we can use it to get hold of the queue for communicating
    // between threads. 
    Control_Ball(Control_Screen_GUI cntrl_screen) 
    {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        this.cntrl_screen = cntrl_screen;
        this.queue = cntrl_screen.queue();

        jump();
        gravity_change();
    }

    // This function is called whenever we want to change what is shown by the
    // control ball. A queue is set up in order to communicate between the 
    // different threads. A different function elsewhere will add an indicator
    // to the queue depending on what is selected on the menu.

    // (Only returns something for testing.)
    public void animate()
    {
        Queue_Action action = Queue_Action.NOTHING;

        try { action = queue.take(); }
        catch (Exception err) { throw new Error("\n\nControl queue error.\n\n"); }

        if(action == Queue_Action.JUMP){selected(true, false, false);}
        else if(action == Queue_Action.GRAV_UP){selected(false, true, false);}
        else if(action == Queue_Action.GRAV_DOWN){selected(false, false, true);}
        else{selected(false,false, false);}

    }

    // This thread is looped continuously and plays the jump animation. The
    // ball moves under gravity. It is only displayed when we have that jump
    // is selected in the menu.
    public void jump() 
    {
        Thread thread = new Thread() {
            public void run() {

                while(true){
                    jump_y = Y_BOTTOM - 1;
                    int vy = JUMP_VY;

                    while(jump_y <= Y_BOTTOM){
                        animate_sleep(SLEEP);
                        jump_y -= vy;
                        vy -= GRAVITY;
                        repaint();
                    }
                }
            }
        };
        thread.start();
    }

    // This is the gravity change animation. We have the two sets of 'y' values
    // up_y and down_y, both of which represent the ball moving to the top of
    // the screen or bottom of the screen.
    public void gravity_change() 
    {
        Thread thread = new Thread() {
            public void run() {

                while(true){
                    up_y = Y_BOTTOM;
                    int up_vy = 0;
                    down_y = Y_TOP;
                    int down_vy = 0;

                        while( up_y > Y_TOP ){
                            animate_sleep(SLEEP);
                            up_y -= up_vy;
                            up_vy += GRAVITY;
                            down_y += down_vy;   
                            down_vy += GRAVITY;
                            repaint();
                        }

                    animate_sleep(STOP);
                }
            }
        };
        thread.start();
    }

    // Short function to make the thread sleep so the animation doesn't occur
    // too quickly.
    private void animate_sleep(int length)
    {
        try { Thread.sleep(length); }
        catch (InterruptedException e){ 
            throw new Error("\n\nThread sleep error\n\n"); 
        }
    }

    // The overwritten paint component that draws the ball to screen depending
    // on what option has been selected from the queue.
    public void paintComponent(Graphics g0) {

        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        RenderingHints rh = new RenderingHints(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(rh);
        if(y() != 0){ g.fillOval(X_COORD, y(), RADIUS, RADIUS); }
        
    }

    // This is essentially a switch used to swap between the different states.
    public void selected(boolean jump, boolean up, boolean down)
    {
        jump_selected = jump;
        up_selected = up;
        down_selected = down;
    }

    // Just for testing, returns y so we can look for the correct movement in
    // tests. Also lets us set y so we can know where the ball starts moving
    // from.
    public int y()
    {
        if(jump_selected){ return(jump_y); }
        else if(up_selected){ return(up_y);}
        else if(down_selected){ return(down_y); }
        return(0);
    }

    public void jump_y(int new_y)
    {
        jump_y = new_y;
    }
}
