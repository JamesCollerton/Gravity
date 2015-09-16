import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

// This class is designed to hold the game ball. It takes in arguments depending
// on user commands and animates the ball accordingly.

// Responsibilities: Holding the game ball.
//                   Registering commands and making the ball react.
//                   Drawing the ball to screen.
//                   Feeding information on the ball's coordinates back for
//                   collision.

public class Game_Ball extends JPanel
{
    private int jump_y, switch_y, switch_vy;
    private boolean jump_running, gravity_up, grav_switch_running, invincible;
    private BlockingQueue<Queue_Action> queue;
    private Game_Audio audio = new Game_Audio();

    // These constants are used in animating the ball.
    private final int X_COORD = 175,
                      GRAVITY = 1, 
                      BOUNCE = 1, 
                      RADIUS = 25,
                      DEF_BOTTOM = 265,
                      DEF_TOP = 0, 
                      CANVAS_WIDTH = Game_Const.CANV_W, 
                      CANVAS_HEIGHT = Game_Const.CANV_H,
                      JUMP_VY = 15, 
                      SLEEP = 30;

    // We take in a queue in order to communicate with the game ball thread.
    // We set the size of the canvas to cover the field and set the background
    // to transparent to see throught it. At the minute nothing is happening and
    // we are on the bottom of the screen (confusingly the top coordinates wise)
    // so set the indicators accordingly. 
    Game_Ball(BlockingQueue<Queue_Action> queue) 
    {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT / 2));
        setForeground(Color.BLACK);
        setOpaque(false);

        grav_switch_running = false;
        gravity_up = false;
        invincible = false;

        this.queue = queue;

        repaint();
    }

    // Once an action has been added to the queue we call this function in order
    // to make the program look for something. Depending on what is there we
    // take an action.
    public void animate()
    {
        Queue_Action action = Queue_Action.NOTHING;

        try { action = queue.take(); }
        catch (Exception err) { throw new Error("\n\nGame Queue Error\n\n"); }

        if( action == Queue_Action.JUMP && !busy() ){ jump(); }
        else if( ( (action == Queue_Action.GRAV_UP && !gravity_up)    || 
                   (action == Queue_Action.GRAV_DOWN && gravity_up) ) && 
                   !busy() ){ gravity_switch(); }
    }

    // We start a new thread in order to allow everything else to continue 
    // running as we do the animation. We tell the rest of the program that
    // the jump is running so that we can't do another command and then start
    // the animation. 
    private void jump() 
    {
        jump_running = true;
        audio.jump();

        Thread thread = new Thread() {
            public void run() {

                reset_pos();
                int vy = JUMP_VY;

                jump_animation(vy);

                reset_pos();
                repaint();
                jump_running = false;

            }
        };
        thread.start();
    }

    // We animate the ball under gravity depending on whether we are at the 
    // top or bottom of the screen.
    private void jump_animation(int vy)
    {
        while( jump_condition() ){
            animate_sleep(SLEEP);

            if(gravity_up){ jump_y += vy; }
            else{ jump_y -= vy; }

            vy -= GRAVITY;
            repaint();
        }
    }

    // We reset the postion of the ball to the top or bottom of the screen to
    // allow for collision and so the game looks consistent.
    private void reset_pos()
    {
        if(gravity_up){ jump_y = DEF_TOP; }
        else{ jump_y = DEF_BOTTOM; }
    }

    // This tells us when we need to stop jumping depending on where we are, top
    // or bottom.
    private boolean jump_condition()
    {
        if(gravity_up){ if(jump_y >= DEF_TOP){ return(true); } }
        else{ if(jump_y <= DEF_BOTTOM){ return(true); } }

        return(false);
    }

    // This controls the gravity switch operator. It works in a very similar
    // way to the jump function and so I won't comment it too much.
    private void gravity_switch() 
    {
        grav_switch_running = true;
        gravity_audio();

        Thread thread = new Thread() {
            public void run() {

                set_switch_pos();
                int switch_vy = 0;

                switch_animation(switch_vy);

                end_switch_pos();
                repaint();
                grav_switch_running = false;
                gravity_up = !gravity_up;

            }
        };
        thread.start();
    }

    private void gravity_audio()
    {
        if(gravity_up){ audio.grav_down(); }
        else{ audio.grav_up(); }
    }

    private void set_switch_pos()
    {
        if(gravity_up){ switch_y = DEF_TOP; }
        else{ switch_y = DEF_BOTTOM; }
    }

    private void switch_animation(int switch_vy)
    {
        while( switch_condition() ){
            animate_sleep(SLEEP);
            if(gravity_up){ switch_y += switch_vy; }
            else{ switch_y -= switch_vy; }   
            switch_vy += GRAVITY;
            repaint();
        }
    }

    private void end_switch_pos()
    {
        if(gravity_up){ switch_y = DEF_BOTTOM; }
        else{ switch_y = DEF_TOP; }
    }

    private boolean switch_condition()
    {
        if(gravity_up){ if(switch_y <= DEF_BOTTOM){ return(true); } }
        else{ if( switch_y > DEF_TOP ){ return(true); } }

        return(false);
    }

    // Used to sleep threads so that animations remain smooth.
    private void animate_sleep(int length)
    {
        try { Thread.sleep(length); }
        catch (InterruptedException e){ 
            throw new Error("\n\nThread sleep error\n\n"); 
        }
    }

    // These are used to tell the rest of the program where the ball coordinates
    // are.
    public int x()
    {
        return(X_COORD);
    }

    public int y()
    {
        if(jump_running){ return(jump_y); }
        else if(grav_switch_running){ return(switch_y); }
        else if(gravity_up){ return(DEF_TOP); }
        else if(!gravity_up){ return(DEF_BOTTOM); }

        return(DEF_BOTTOM);
    }

    // Telling the game the ball radius.
    public int r()
    {
        return(RADIUS);
    }

    // Used to switch the ball's invincibility on and off.
    public void invincible_switch(boolean on_off)
    {
        invincible = on_off;
    }

    // How we make the ball appear on screen. If the player is invincible their
    // ball is green.
    public void paintComponent(Graphics g0) {

        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0;

        RenderingHints rh = new RenderingHints(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(rh);
        if(invincible){ g.setColor(Color.GREEN); }
        g.fillOval(X_COORD, y(), RADIUS, RADIUS);

    }

    // This is used to say if the ball is in the middle of an animation or not.
    // If it is then we can't start another one.
    private boolean busy()
    {
        if(!jump_running && !grav_switch_running){ return(false); }
        return(true);
    }

    // The two below are used to check if the ball has been set to invincible
    // and to set a y component for testing. By setting jump running it means
    // that collision can be detected anywhere.

    // (Only for testing.)
    public boolean invincible()
    {
        return(invincible);
    }

    public void change_y(int new_y)
    {
        jump_running = true;
        jump_y = new_y;
    }

}