import greenfoot.*;

public class MyWorld extends World
{
    public MyWorld()
    {    
        super(800, 900, 1); 
        prepare();
    }
    
    //GreenfootImage menu;
    GreenfootImage chat = new GreenfootImage("Background2.png");
    
    private void prepare()
    {
        setBackground(chat);
        
        Zwischenserver zwischenserver = new Zwischenserver();
        addObject(zwischenserver, getWidth() / 2, (getHeight() - 100) / 2);
        
        
        UserInput userinput = new UserInput();
        addObject(userinput, getWidth() / 2, getHeight() - 50);
        
        Client client = new Client("192.168.2.101", 6666);
        addObject(client, 0, 0);
    }
    
    /*private void openMenu() {
        setBackground(menu);
        
    }*/
}
