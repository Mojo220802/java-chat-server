import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    public MyWorld()
    {    
        super(800, 600, 1); 
        setup();
    }
    
    private void setup() {
        Server server = new Server();
        addObject(server, 0, 0);
        
        int max_clients = 10;
        for(int i = 0; i < max_clients / 2; i++) {
            Watchman watchman = new Watchman(i);
            addObject(watchman, getWidth() / (max_clients / 2) * (i) + (getWidth() / (max_clients / 2)) / 2, getHeight() / 4);
        }
        
        for(int i = max_clients / 2; i < max_clients; i++) {
            Watchman watchman = new Watchman(i);
            addObject(watchman, (getWidth() / (max_clients / 2) * (i - (max_clients / 2))) + (getWidth() / (max_clients / 2)) / 2, getHeight() / 4 * 3);
        }
    }
}
