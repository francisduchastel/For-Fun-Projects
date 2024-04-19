
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game extends JFrame implements KeyListener {
    //character location values
    private int character_x = 100;
    private int character_y = 300;

    //chaaracter properties
    private int character_width = 20;
    private int character_height = 20;
    private int Moving_Direction = 0;
    
    
    private int[][] Map_Parts = new int[0][0];
    
    //physics constants
    private int Gravity = 1;
    private int Friction = 0;

    //character moving
    private String Jump_State = "Grounded";
    private int Upwards_velocity = 0;
    private int Sideways_velocity = 0;
    private Boolean Falling = false;
    private Boolean Jumped = false;
    private Boolean Walking = false;

    //camera properties

    private int Cam_velocity_vertical = 0;
    private int Cam_velocity_horizontal = 0;
    private Boolean Cam_Detached = false;



    public Game(int[][] Map_Parts_passed, int[] Spawn_coords_passed) {
        setTitle("A Bad Game");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Map_Parts = Map_Parts_passed;
        character_x = Spawn_coords_passed[0];
        character_y = Spawn_coords_passed[1];

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                
                //character
                drawSprite(g, character_x, character_y, character_width, character_height, Color.BLUE);

                
                //map blocks
                
                
                

                for (int q = 0; q < Map_Parts.length; q++) {

                    if (Map_Parts[q][4] == 1) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.black);
                    } else if (Map_Parts[q][4] == 2) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.gray);
                    } else if (Map_Parts[q][4] == 3) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.lightGray);
                    }
    

                }

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

            //character movement
        //sideways movement
        
        if (Walking == true && Sideways_velocity != 0) {
            character_x += (Sideways_velocity/2);
        } else {
            character_x += Sideways_velocity;
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

        if (Upwards_velocity > 20) {
            Upwards_velocity = 20;
        } else if (Upwards_velocity < -20) {
            Upwards_velocity = -20;
        }

        character_y += Upwards_velocity;

        if( Jump_State.equals("Jumping")) {
            Upwards_velocity += Gravity;
        }



        //block collision

        Falling = true;


        for (int z = 0; z < Map_Parts.length; z++) {

            if (Map_Parts[z].length != 5) {
                z = Map_Parts.length + 1;
                break;
            } else {



                if (CollisionCheck(character_x, character_y, character_width, character_height, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                    
                    
                    if (Map_Parts[z][4] == 2) {
                        Sideways_velocity = 0;

                        if (Moving_Direction == -1) {
                        character_x = Map_Parts[z][0] - character_width; 
                        } else if (Moving_Direction == 1) {
                            character_x = Map_Parts[z][0] + Map_Parts[z][2];
                        }
                        
                    } else if (Map_Parts[z][4] == 1) {
                        
                        


                        if (CollisionCheck(character_x, character_y + 10, character_width, character_height - 10, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                            
                            if (Upwards_velocity > 0) {
                                Upwards_velocity = 0;
                            }

                            character_y = Map_Parts[z][1] - character_height;
                            Jump_State = "Grounded";
                            Jumped = false;
                        }


                        

                    } else if (Map_Parts[z][4] == 3) {
                        
                        if (Upwards_velocity <= 2) {
                            Upwards_velocity = 2;
                            character_y = Map_Parts[z][1] + Map_Parts[z][3];
                        }
                        
                        
                        
                    }


                    //falling check

                    if (CollisionCheck(character_x, character_y, character_width, character_height + 5, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                        Falling = false;
                    }

                }


            }




        }

        if (Falling == true) {
            Jump_State = "Jumping";
        }

        


        // Camera Movements

        


        if (Cam_Detached == false) {

            if (character_x >= 260) {

                Cam_velocity_horizontal = (260 - character_x)/10;

            } else if (character_x <= 220) {

                Cam_velocity_horizontal = (220 - character_x)/10;

            } else {

                Cam_velocity_horizontal = 0;

            }

            if (character_y >= 350) {

                Cam_velocity_vertical = (350 - character_y)/10;

            } else if (character_y <= 250) {

                Cam_velocity_vertical = (250 - character_y)/10;

            } else {

                Cam_velocity_vertical = 0;

            }

        }


        Move_Cam_Horizontal(Cam_velocity_horizontal);
        Move_Cam_Vertical(Cam_velocity_vertical);


        

        


        repaint();
    }


    private void Move_Cam_Horizontal(int amount) {

        character_x += amount;

        for (int i = 0; i < Map_Parts.length; i++) {


            Map_Parts[i][0] += amount;

        }

    }

    private void Move_Cam_Vertical(int amount) {

        character_y += amount;


        for (int i = 0; i < Map_Parts.length; i++) {


            Map_Parts[i][1] += amount;

        }

    }


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

   

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_UP) {
            if (Jumped == false) {
                Jump_State = "Jumping";
                Jumped = true;
                Upwards_velocity = -15;
            }
        }

        if (keyCode == KeyEvent.VK_LEFT) {
            Sideways_velocity = -10;
            Moving_Direction = 1;

        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            Sideways_velocity = 10;
            Moving_Direction = -1;

        
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            
            Walking = true;

        }

        
        if (keyCode == KeyEvent.VK_NUMPAD2) {

            Cam_velocity_vertical = -5;
            Cam_Detached = true;

        }

        if (keyCode == KeyEvent.VK_NUMPAD4) {

            Cam_velocity_horizontal = 5;
            Cam_Detached = true;

        }

        if (keyCode == KeyEvent.VK_NUMPAD6) {

            Cam_velocity_horizontal = -5;
            Cam_Detached = true;

        }

        if (keyCode == KeyEvent.VK_NUMPAD8) {

            Cam_velocity_vertical = 5;
            Cam_Detached = true;

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

        if (keyCode == KeyEvent.VK_SHIFT) {
            
            Walking = false;

        }

        if (keyCode == KeyEvent.VK_NUMPAD2) {

            Cam_Detached = false;

        }

        if (keyCode == KeyEvent.VK_NUMPAD4) {

            Cam_Detached = false;

        }

        if (keyCode == KeyEvent.VK_NUMPAD6) {

            Cam_Detached = false;

        }

        if (keyCode == KeyEvent.VK_NUMPAD8) {

            Cam_Detached = false;

        }

        
    }

    public static void main(String[] args) {
        
        //map values
          
        String Map_Data = "";
        String Map_filepath = "Map.txt";


        try {
            // try to open the file and extract its contents
            Scanner scn = new Scanner(new File(Map_filepath));
            while (scn.hasNextLine()) {
            String line = scn.nextLine();
            Map_Data += line + "\n"; // nextLine() removes line breaks, so add them back in
            }
            scn.close(); // be nice and close the Scanner
        }
        catch (FileNotFoundException e) {
        }

        String[] Blocks_String = Map_Data.split("\n");
        int Map_Size = Blocks_String.length;

        int[][] Map_Parts = new int[Map_Size][5];
        
        for (int i = 1; i < Map_Size; i++) {

            String[] Current_Block_String = Blocks_String[i].split("[, ]+");

            int[] Block_Int = new int[5];

            for (int j = 0; j < 5; j++) {
                Block_Int[j] = Integer.valueOf(Current_Block_String[j]);
            }

            Map_Parts[i] = Block_Int;

        }


        String[] Spawn = Blocks_String[0].split("[, ]+");

        int Spawn_x = Integer.valueOf(Spawn[0]);
        int Spawn_y = Integer.valueOf(Spawn[1]);
        int[] Spawn_coords = new int[2];
        Spawn_coords[0] = Spawn_x;
        Spawn_coords[1] = Spawn_y;

        
        
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(Map_Parts, Spawn_coords);
            game.setVisible(true);
        });
    }
}

