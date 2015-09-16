// This is used to enumerate all the possible actions that can be put onto
// our blocking queues between threads.

public enum Queue_Action
{
    JUMP,
    GRAV_UP,
    GRAV_DOWN,
    PAINT,
    DETECT_COLLISION,
    NOTHING,
}