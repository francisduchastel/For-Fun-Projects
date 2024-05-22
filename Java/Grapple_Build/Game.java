
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Game extends JFrame implements KeyListener {
    //character location values
    private static int character_x = 100;
    private static int character_y = 0;

    //character properties
    private int character_width = 30;
    private int character_height = 60;
    
    //Screen
    private int Screen_Width = 500;
    private int Screen_height = 500;
    
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
    private int Walk_Timer = 0;
    private int Moving_Direction = 0;
    private String Character_Facing_Direction = "Right";
    private String Moving_State = "";

    //Grapple properties

    private boolean grapple_active = false;
    private boolean grapple_gotten = true;
    private int grapple_x = 0;
    private int grapple_y = 0;
    private int grapple_size = 5;
    private int grapple_speed = 10;
    //false = extending, true = retracting
    private boolean grapple_state = false;


    //camera properties

    private int Cam_velocity_vertical = 0;
    private int Cam_velocity_horizontal = 0;
    private Boolean Cam_Detached = false;


    private int Timer_Value = 0;


    //Map Properties

    private int Map_Type = 0;
    private int Map_Index = 0;

    private boolean Transitioning = false;
    private boolean Transition_Apex = false;
    private int Transition_Value = 0;

    private int Spawn_Value = 1;

    private int[][] Map_Parts = new int[0][0];


    public Game(int Inital_Map_Type, int Initial_Map_Index, int Inital_Spawn_Value) {
        setTitle("A Bad Game");
        setSize(Screen_Width, Screen_height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        Map_Parts = Load_Map(Inital_Map_Type, Initial_Map_Index);

        Load_Spawn(Map_Parts, Inital_Spawn_Value);


        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                

                //Draw character

                Walk_Timer += 1;

                if (Walk_Timer == 10) {
                    Walk_Timer = 0;
                    Timer_Value += 1;
                    Timer_Value %= 2;
                }


                if (Sideways_velocity != 0) {
                    Moving_State = "_Move" + (Timer_Value + 1);
                } else {
                    Moving_State = "";
                }

                String Character_Image = "Images/Character_" + Character_Facing_Direction + Moving_State;


                if (Jump_State.equals("Grounded") == false) {
                    Character_Image += "_Airbourn";
                }

                Character_Image += ".png";


                try {
                    File pathToFile = new File(Character_Image);
                    Image image = ImageIO.read(pathToFile);
                    g.drawImage(image, character_x, character_y, null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                


                //draw grapple

                if (grapple_gotten == true && grapple_active == true) {

                    drawSprite(g, grapple_x, grapple_y, grapple_size, grapple_size, Color.PINK);

                }


                
                //Draw map blocks
                
                
                

                for (int q = 0; q < Map_Parts.length; q++) {

                    if (Map_Parts[q][4] == 1) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.black);
                    } else if (Map_Parts[q][4] == 2) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.gray);
                    } else if (Map_Parts[q][4] == 3) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.lightGray);
                    } else if (Map_Parts[q][4] == 4) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.RED);
                    } else if (Map_Parts[q][4] == 5) {
                        drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.BLUE);
                    }
    

                }


                //map transition

                if (Transitioning == true) {

                    drawSprite(g, 0, 0, Screen_Width, Screen_height, new Color(0, 0, 0, Transition_Value));

                    if (Transition_Apex == false) {
                        Transition_Value += 15;

                        

                    } else {

                        Transition_Value -= 15;

                    }

                    if (Transition_Value == 255) {
                        Transition_Apex = true;

                        Map_Parts = Load_Map(Map_Type, Map_Index);
                        
                        Load_Spawn(Map_Parts, Spawn_Value);


                    }


                    if (Transition_Value == 0 && Transition_Apex == true) {

                        Transitioning = false;
                        Transition_Apex = false;

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


        Update_Character();

        if (grapple_gotten == true) {
            Update_Grapple();
        }
        


        Update_Collision();


        Update_Camera();


        


        

        


        repaint();
    }

    private void Update_Character() {
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

        if( Jump_State.equals("Jumping") && grapple_active == false) {
            Upwards_velocity += Gravity;
        }

        return;

    }

    private void Update_Grapple() {


        if (grapple_active == true && grapple_state == false) {

            if (Grapple_Collision() == true) {

                grapple_state = true;

            } else if ((grapple_x > Screen_Width || grapple_x < 0) || (grapple_y > Screen_height || grapple_y < 0) ) {
                
                grapple_active = false;

                grapple_x = character_x + 15;
                grapple_y = character_y + 30;

                
            } else {

                grapple_x -= (grapple_speed * Moving_Direction);

                grapple_y -= grapple_speed;

            }

        } else if (grapple_active == true && grapple_state == true) {

            character_x -= (grapple_speed * Moving_Direction);

            character_y -= grapple_speed;

            if (CollisionCheck(character_x, character_y, character_width, character_height, grapple_x, grapple_y, grapple_size, grapple_size) == true) {

                grapple_active = false;
                grapple_state = false;

            }


        }


    }

    private boolean Grapple_Collision() {

        boolean Grapple_collides = false;


        for (int z = 0; z < Map_Parts.length; z++) {

            if (CollisionCheck(grapple_x, grapple_y, grapple_size, grapple_size, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {

                Grapple_collides = true;

                return Grapple_collides;

            }
        }

        return Grapple_collides;


    }

    private void Update_Collision() {
        Falling = true;


        for (int z = 0; z < Map_Parts.length; z++) {

            if (Map_Parts[z].length != 5) {
                z = Map_Parts.length + 1;
                break;
            } else {



                if (CollisionCheck(character_x, character_y, character_width, character_height, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                    
                    grapple_active = false;

                    grapple_x = character_x + 15;
                    grapple_y = character_y + 30;
                    
                    if (Map_Parts[z][4] == 2) {
                        Sideways_velocity = 0;

                        if (Moving_Direction == -1) {
                            character_x = Map_Parts[z][0] - character_width - 5; 
                        } else if (Moving_Direction == 1) {
                            character_x = Map_Parts[z][0] + Map_Parts[z][2] + 5;
                        }
                        
                    } else if (Map_Parts[z][4] == 1) {
                        
                        


                        if (CollisionCheck(character_x, character_y + 10, character_width, character_height - 10, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                            
                            if (Upwards_velocity > 0) {
                                Upwards_velocity = 0;
                            }

                            if ((character_y + 60) - Map_Parts[z][1] <= character_height / 3 ) {
                                character_y = Map_Parts[z][1] - character_height;
                                Jump_State = "Grounded";
                                Jumped = false;
                            }

                            
                
                            
                        }


                        

                    } else if (Map_Parts[z][4] == 3) {
                        
                        if (Map_Parts[z][0] < character_x && character_x < Map_Parts[z][0] + Map_Parts[z][3]) {
                            if (Upwards_velocity <= 2) {
                                Upwards_velocity = 2;
                                character_y = Map_Parts[z][1] + Map_Parts[z][3];
                            }
                        

                        }

                        
                        
                        
                    } else if (Map_Parts[z][4] == 4 || Map_Parts[z][4] == 5) {


                        //moving maps

                        if (Map_Parts[z][4] == 4 && Transitioning == false) {
                            Map_Index -= 1;
                        } else if (Map_Parts[z][4] == 5 && Transitioning == false) {
                            Map_Index += 1;
                        }

                        if (Transitioning == false) {
                            Transitioning = true;
                        }


                    } else if (Map_Parts[z][4] >= 10) {

                        Spawn_Value = Map_Parts[z][4] % 10;

                    }


                    

                }


            }


            //falling check

            if (CollisionCheck(character_x, character_y, character_width, character_height + 5, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                Falling = false;
            }



        }

        if (Falling == true) {
            Jump_State = "Jumping";
        }

        return;

    }

    private void Update_Camera() {
        
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

            } else if (character_y <= 290) {

                Cam_velocity_vertical = (250 - character_y)/10;

            } else {

                Cam_velocity_vertical = 0;

            }

        }


        Move_Cam_Horizontal(Cam_velocity_horizontal);
        Move_Cam_Vertical(Cam_velocity_vertical);

        return;
    }


    private void Move_Cam_Horizontal(int amount) {

        character_x += amount;

        grapple_x += amount;

        for (int i = 0; i < Map_Parts.length; i++) {


            Map_Parts[i][0] += amount;

        }

    }

    private void Move_Cam_Vertical(int amount) {

        character_y += amount;

        grapple_y += amount;


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
            
            if (grapple_active == false) {
                Sideways_velocity = -10;
                Moving_Direction = 1;
                Character_Facing_Direction = "Left";
            }
            
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            
            if (grapple_active == false) {
                Sideways_velocity = 10;
                Moving_Direction = -1;
                Character_Facing_Direction = "Right";
            }
            
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            
            Walking = true;

        }

        if (keyCode == KeyEvent.VK_SPACE) {

            grapple_active = true;
            grapple_state = false;
            grapple_x = character_x + 15;
            grapple_y = character_y + 30;

            Upwards_velocity = 0;
            Sideways_velocity = 0;

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


    public int[][] Load_Map(int Map_Type, int Map_index) {

        String Map_Data = "";
        String Map_filepath = "Maps/Map" + Map_Type + "" + Map_index + ".txt";


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
        
        for (int i = 0; i < Map_Size; i++) {

            String[] Current_Block_String = Blocks_String[i].split("[, ]+");

            int[] Block_Int = new int[5];

            for (int j = 0; j < 5; j++) {
                Block_Int[j] = Integer.valueOf(Current_Block_String[j]);
            }

            Map_Parts[i] = Block_Int;

        }

        return Map_Parts;

    }

    public void Load_Spawn(int[][] Map_Parts, int Spawn_Value) {


        for (int i = 0; i < Map_Parts.length; i++) {


            if (Map_Parts[i][4] >= 10 && Map_Parts[i][4] % 10 == Spawn_Value) {

                character_x = Map_Parts[i][0];
                character_y = Map_Parts[i][1];

                Camera_Center(character_x, character_y);

                return;

            }

        }

        return;

    }

    public void Camera_Center(int character_x, int character_y) {

        int x_shift = 250 - character_x;
        int y_shift = 250 - character_y;


        Move_Cam_Horizontal(x_shift);
        Move_Cam_Vertical(y_shift);

        return;

    }


    public static void main(String[] args) {
        

       int Inital_Map_Type = 0;
       int Initial_Map_Index = 1;
       int Inital_Spawn_Value = 1;


        
        
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(Inital_Map_Type, Initial_Map_Index, Inital_Spawn_Value);
            game.setVisible(true);
        });
    }
}

