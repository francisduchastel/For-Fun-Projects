
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class Game extends JFrame implements KeyListener {
    //character location values
    private int character_x = 100;
    private int character_y = 300;
    
    //projectile values
    private int projectile_x_start;
    private int projectile_x = 10;
    private int projectile_y = -20;
    private int projectile_speed = 15;
    private int projectile_direction = 1;
    private int MOVE_SPEED = 0;
    private int projectile_state = 0;
    private int projectile_dim = 10;

    //sword values
    private int sword_length = 0;
    private int max_sword_length = 50;
    private int sword_hand = 10; //10 = right, 0 = left
    private int sword_x = character_x + 5 + sword_hand;
    private int sword_y = character_y + 5;
    private int retract_speed = 10;
    private int sword_state = 0; //0 = not being used, 1 = extending, 2 = retracting
    
    
    //physics constants
    private int Gravity = 1;
    private int Friction = 0;

    //character moving
    private String Jump_State = "Grounded";
    private int Upwards_velocity = 0;
    private int Sideways_velocity = 0;

    //walking enemy
    private Boolean Enemy_Exist = false;
    private int Enemy_MoveSpeed = 3;
    private int Enemy_X = 100;
    private int Enemy_Y = -50;


    //flying enemy
    private Boolean Enemy_Fly_Exist = false;
    private int Enemy_Fly_MoveSpeed = 3;
    private int Enemy_X_fly = 150;
    private int Enemy_Y_fly = -50;
    private int Enemy_Fly_Target_Height = 250;
    private int Enemy_Fly_Target_Height_Distance = 0;

    //random number generator
    private Random Rand = new Random();

    //UI
    private int Score_Amount = 0;
    private int Selection_x = 20;
    private int Selection_y = 333;

    public Game() {
        setTitle("A Bad Game");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //projectile
                drawSprite(g, projectile_x, projectile_y, projectile_dim, projectile_dim, Color.BLACK);
                
                //sword
                drawSprite(g, sword_x, sword_y, sword_length, 5, Color.LIGHT_GRAY);
                
                
                //character
                drawSprite(g, character_x, character_y, 20, 20, Color.BLUE);

                
                //enemy walk
                drawSprite(g, Enemy_X, Enemy_Y, 20, 20, Color.MAGENTA);

                //enemy fly
                drawSprite(g, Enemy_X_fly, Enemy_Y_fly, 20, 20, Color.PINK);


                //floor
                drawSprite(g, 0, 320, 400, 75, Color.GRAY);

                    //UI

                //selection
                drawSprite(g, Selection_x, Selection_y, 20, 20, Color.white);

                //small projectile
                drawSprite(g, 25, 338, 10, 10, Color.BLACK);

                //big projectile
                drawSprite(g, 50, 336, 14, 14, Color.BLACK);

                //score
                drawSprite(g, 390, 0, Score_Amount, 20, Color.GREEN);
            }
        };
        panel.setBackground(Color.WHITE);
        add(panel);

        startGameLoop();

        addKeyListener(this);
        setFocusable(true);
    }

    private void drawSprite(Graphics g, int Start_X, int Start_Y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(Start_X, Start_Y, width, height);
    }

    private void update() {
            //projectile

        //moving
        projectile_x += MOVE_SPEED;

        if (projectile_y < 0) {
            MOVE_SPEED = projectile_direction * projectile_speed;


            if (projectile_state == 0) {
                projectile_dim = 10;

                projectile_speed = 15;

            } else if (projectile_state == 1) {
                projectile_dim = 14;

                projectile_speed = 7;
            }


        }


        //despawning after moving 200 units
        if (projectile_x - projectile_x_start >= 200 || projectile_x - projectile_x_start <= -200) {
            projectile_x = 20;
            projectile_y = -20;
        }


            //walking enemy

        //spawing
        if (Enemy_Exist == false) {
            Enemy_X = 400;
            Enemy_Y = 300;
            Enemy_Exist = true;
        }

        //moving if spawned

        if (Enemy_X < 50) {
            Enemy_MoveSpeed = 1;
        } else {
            Enemy_MoveSpeed = 3;
        }

        if (Enemy_Y > 0) {
            Enemy_X -= Enemy_MoveSpeed;
        }

        //reaching end of screen
        if (Enemy_X < 0) {
            Die();
        }


            //flying enemy

        //spawing
        if (Enemy_Fly_Exist == false && Score_Amount <= -50) {
            Enemy_X_fly = 400;
            Enemy_Y_fly = 250;
            Enemy_Fly_Exist = true;

            Enemy_Fly_Target_Height = Rand.nextInt(99) + 200;
        }


        //moving

        if (Enemy_X_fly < 50) {
            Enemy_Fly_MoveSpeed = 1;
        } else {
            Enemy_Fly_MoveSpeed = 3;
        }

        if (Enemy_Y_fly > 0) {
            Enemy_X_fly -= Enemy_Fly_MoveSpeed;

            if (Enemy_Y_fly != Enemy_Fly_Target_Height) {
                Enemy_Fly_Target_Height_Distance = Enemy_Fly_Target_Height - Enemy_Y_fly;

                Enemy_Y_fly += Enemy_Fly_Target_Height_Distance / 25;
            }
        }

        

        //reaching end of screen
        if (Enemy_X_fly < 0) {
            Die();
        }

        //Randomizing Target Height
        if (Enemy_X_fly == 400 || Enemy_X_fly == 350 || Enemy_X_fly == 300 || Enemy_X_fly == 250 || Enemy_X_fly == 200 || Enemy_X_fly == 150 || Enemy_X_fly == 100) {
            Enemy_Fly_Target_Height = Rand.nextInt(99) + 200;
        }


            //character movement
        //sideways movement
        
        if (character_x >= 0 && character_x <= 370) {
            character_x += Sideways_velocity;
        }

        if (character_x < 0) {
            character_x = 0;
        } else if (character_x > 370) {
            character_x = 370;
        }

        if ( (Sideways_velocity > 0 && Friction < 0) || (Sideways_velocity < 0 && Friction > 0)) {
            
            if (Sideways_velocity <= -Friction) {
                Sideways_velocity = 0;
            } else {
                Sideways_velocity += Friction;
            }
            
        } else {
            Friction = 0;
        }

        
        //jumping
        
        if (character_y > 300) {
            character_y = 300;
            Jump_State = "Grounded";
            Upwards_velocity = 0;
        }

        character_y += Upwards_velocity;

        if( character_y < 300 || Upwards_velocity != 0) {
            Upwards_velocity += Gravity;
        }


            //sword
        //extending
        if (sword_state == 1) {

            if (sword_length < max_sword_length) {
                sword_length += retract_speed;
            } else if (sword_length == max_sword_length) {
                sword_state = 2;
            } else if (sword_length > max_sword_length) {
                sword_length -= retract_speed;
            }
        } else if (sword_state == 2) {

            if (sword_length > 0) {
                sword_length -= retract_speed;
            } else if (sword_length == 0) {
                sword_state = 0;
            } else if (sword_length < 0) {
                sword_length += retract_speed;
            }

        }

        //movement
        sword_x = character_x + 5 + sword_hand;
        sword_y = character_y + 5;


        //collision logic

        if (CollisionCheck(character_x, character_y, 20, 20, Enemy_X, Enemy_Y, 20, 20)) {
            Die();
        }

        if (CollisionCheck(character_x, character_y, 20, 20, Enemy_X_fly, Enemy_Y_fly, 20, 20)) {
            Die();
        }

        //projectile collision logic

        if (CollisionCheck(projectile_x, projectile_y, projectile_dim, projectile_dim, Enemy_X, Enemy_Y, 20, 20)) {

            Enemy_Exist = false;
            Enemy_X = 100;
            Enemy_Y = -50;

            projectile_x = 20;
            projectile_y = -20;

            Score_Amount -= 10;
        }

        if (CollisionCheck(projectile_x, projectile_y, projectile_dim, projectile_dim, Enemy_X_fly, Enemy_Y_fly, 20, 20)) {

            Enemy_Fly_Exist = false;
            Enemy_X_fly = 150;
            Enemy_Y_fly = -50;

            projectile_x = 20;
            projectile_y = -20;

            Score_Amount -= 20;
        }

        //sword collision logic

        if (CollisionCheck(sword_x, sword_y, sword_length, 5, Enemy_X, Enemy_Y, 20, 20)) {
            Enemy_Exist = false;
            Enemy_X = 100;
            Enemy_Y = -50;

            Score_Amount -= 10;
        } 
        
        if (CollisionCheck(sword_x, sword_y, sword_length, 5, Enemy_X_fly, Enemy_Y_fly, 20, 20)) {
            Enemy_Fly_Exist = false;
            Enemy_X_fly = 150;
            Enemy_Y_fly = -50;

            Score_Amount -= 20;

        }


        repaint();
    }


    //obj coords stored as NE, NW, SW, SE

    private Boolean CollisionCheck(int obj1_x, int obj1_y, int obj1_width, int obj1_height, int obj2_x, int obj2_y, int obj2_width, int obj2_height) {

        Boolean Does_Collide = false;

        Boolean Negative_width = false;
        int x_check = 0;
        int y_check = 0;

        if (obj1_width < 0) {
            Negative_width = true;
            obj1_width *= -1;
        }

        for (int i = 0; i < obj1_width; i++) {

            for (int p = 0; p < obj1_height; p++) {

                if (Negative_width == false) {
                    x_check = obj1_x + i;
                    y_check = obj1_y + p;
                } else if (Negative_width == true) {
                    x_check = obj1_x - i;
                    y_check = obj1_y + p;
                }
                

                if (x_check >= obj2_x && x_check <= obj2_x + obj2_width && y_check >= obj2_y && y_check <= obj2_y + obj2_height ) {

                    Does_Collide = true;

                    return Does_Collide;

                }

            }

        }


        return Does_Collide;
    }

    private void startGameLoop() {
        // Create a timer to update the game periodically
        Timer timer = new Timer(30, e -> update());
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void Die() {

        character_x = 0;
        character_y = 0;
        Jump_State = "Jumping";
        Upwards_velocity = 0;
        Sideways_velocity = 0;

        sword_length = 0;
        sword_state = 0;

        Enemy_Exist = false;
        Enemy_X = 100;
        Enemy_Y = -50;

        Enemy_Fly_Exist = false;
        Enemy_X_fly = 150;
        Enemy_Y_fly = -50;

        Score_Amount = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_UP) {
            if (Jump_State.equals("Grounded")) {
                    Jump_State = "Jumping";
                    Upwards_velocity = -15;
                }
        }

        if (keyCode == KeyEvent.VK_LEFT) {
            Sideways_velocity = -10;
            
            projectile_direction = -1;
            
            sword_hand = 0;
            max_sword_length = -50;

            if (sword_length > 0) {
                sword_length *= -1;
            }
            
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            Sideways_velocity = 10;
            
            projectile_direction = 1;

            sword_hand = 10;
            max_sword_length = 50;
            
            if (sword_length < 0) {
                sword_length *= -1;
            }
        
        }

        if (keyCode == KeyEvent.VK_SPACE) {

            if (projectile_y < 0) {
                    projectile_x = character_x + 5;
                    projectile_y = character_y + 5;

                    projectile_x_start = projectile_x;
            }

        }

        if (keyCode == KeyEvent.VK_CONTROL) {
            if (sword_state == 0) {
                sword_state = 1;
            }

        }

        if (keyCode == KeyEvent.VK_SHIFT) {

            if (projectile_state == 0) {
                projectile_state = 1;
                Selection_x = 47;
            } else {
                projectile_state = 0;
                Selection_x = 20;
            }

        }
       
        
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            Friction = 3;
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            Friction = -3;
        }

        if (keyCode == KeyEvent.VK_UP) {

            if (Upwards_velocity >= -5 && Upwards_velocity < 0) {
                Upwards_velocity = 0;
            } else if (Upwards_velocity < -5) {
                Upwards_velocity += 5;
            }

        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
}

