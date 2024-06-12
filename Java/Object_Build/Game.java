
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Game extends JFrame implements KeyListener {
    
    Clock Time = new Clock();

    Painter Paint = new Painter();

    Game_Engine Engine = new Game_Engine();
    
    
    //character location values
    public static int character_x = 500;
    public static int character_y = 0;

    //character properties
    public static int character_width = 30;
    public static int character_height = 60;
    
    //Screen
    public static int Screen_Width = 1000;
    public static int Screen_height = 600;
    
    //physics constants
    public static int Gravity = 1;
    public static int Friction = 0;
    public static Boolean Frictionless = false;

    //character moving
    public static String Jump_State = "Grounded";
    public static int Upwards_velocity = 0;
    public static int Sideways_velocity = 0;
    public static Boolean Falling = false;
    public static Boolean Jumped = false;
    public static Boolean Walking = false;
    public static int Walk_Timer = 0;
    public static int Moving_Direction = 0; // Left = -1, Right = +1
    public static String Character_Facing_Direction = "Right";
    public static String Moving_State = "";
    public static int Walk_Speed = 7;
    public static Boolean frozen = false;

    //Grapple properties

    public static boolean grapple_active = false;
    public static boolean grapple_gotten = true;
    public static int grapple_x = 0;
    public static int grapple_y = 0;
    public static int grapple_size = 5;
    public static int grapple_speed = 10;
    public static int grapple_number = 0;
    public static int grapple_number_max = 2;
    //false = extending, true = retracting
    public static boolean grapple_state = false;

    // rang properties
    public static boolean boomerang_gotten = true;
    public static boolean boomerang_active = false;
    public static int boomerang_x = 0;
    public static int boomerang_y = 0;
    public static int boomerang_size = 5;
    public static int boomerang_speed_max = 25;
    public static int boomerang_speed = 0;
    public static int boomerang_drag = 1;
    public static int boomerang_drag_directional = 0;
    //false = extending, true = retracting
    public static boolean boomerang_state = false;


    //sword properties
    //0 = holstered, 1 = 1st ready, 2 = first swing, 3 = 2nd ready, 4 = 2nd swing
    public static int sword_state = 0;
    public static int sword_length = 80;
    public static int sword_width = 5;
    public static int sword_ready_length = 30;
    public static int swing_time = 30;
    public static int ready_time = 10;
    public static int sword_timer = 0;
    public static int swing_displacement_speed = 2;
    public static int swing_displacement_time = 40;
    public static int swing_displacement_timer = 0;
    public static int[][] hit_boxes = new int[10][4];


    // grind properties

    public static boolean grind_gotten = true;
    public static boolean grind_mode = false;
    public static int grind_speed = 10;


    //camera properties

    public static int Cam_velocity_vertical = 0;
    public static int Cam_velocity_horizontal = 0;
    public static Boolean Cam_Detached = false;


    public static int Timer_Value = 0;


    //Map Properties

    public static int Map_Destination = 100;

    public static boolean Transitioning = false;
    public static boolean Transition_Apex = false;
    public static int Transition_Value = 0;

    public static int Spawn_Value = 19;

    public static int[][] Map_Parts = new int[0][0];




    //Menu properties
    public static boolean In_Title = true;
    public static boolean In_Menu = false;
    public static int Button_Index_Vertical = 0;
    public static int Button_Index_Horizontal = 0;
    public static int Max_Button_Index_Vertical = 4;
    public static int Max_Button_Index_Horizontal = 1;


    public Game() {
        setTitle("A Bad Game");
        setSize(Screen_Width, Screen_height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        Map_Parts = Load_Map(Map_Destination);

        Load_Spawn(Map_Parts, Spawn_Value);


        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                

                if (In_Title == false) {

                    Paint.DrawMap(g, Map_Parts);

                    Paint.DrawGrapple(g, grapple_gotten, grapple_active, grapple_x, grapple_y, grapple_size, character_x, character_y, character_height, character_width);

                    Paint.DrawBoomerang(g, boomerang_active, boomerang_x, boomerang_y, boomerang_size);

                    if (sword_state != 0) {
                        Paint.DrawSword(g, hit_boxes);
                    }
                    
                    //

                    Walk_Timer = Time.Timer_ticker(Walk_Timer, 1, 11);

                    if (Walk_Timer == 0) {
                        Timer_Value = Time.Timer_ticker(Timer_Value, 1, 2);
                    }

                    Paint.DrawCharacter(g, Walk_Timer, Timer_Value, grind_mode, Sideways_velocity, Jump_State, Character_Facing_Direction, character_x, character_y);

                    //
    
                    Paint.DrawTransitions(g, Transitioning, Screen_height, Screen_Width, Transition_Value, Transition_Apex, Map_Parts, Map_Destination);

                    //

                } else if (In_Title == true) {


                    Paint.DrawTitle(g, Max_Button_Index_Horizontal, Max_Button_Index_Vertical, Button_Index_Horizontal, Button_Index_Vertical, Screen_Width, In_Title);

                }

             
                



            }
        };
        panel.setBackground(Color.WHITE);
        add(panel);

        startGameLoop();

        addKeyListener(this);
        setFocusable(true);
    }

   


    public void update() {


        if (In_Title == false) {

            if (frozen == false) {
                int[] values = Engine.Update_Character(Walking, character_x, Sideways_velocity, character_y, Friction, Frictionless, Upwards_velocity, Jump_State, grapple_active, Gravity);
                character_x = values[0];
                character_y = values[1];
                Sideways_velocity = values[2];
                Upwards_velocity = values[3];
            }
            

            if (grapple_gotten == true) {
                int[] values = Engine.Update_Grapple(grapple_active, grapple_state, grapple_x, Screen_Width, grapple_y, Screen_height, grapple_number, grapple_size, character_x, 
                character_y, grapple_speed, Moving_Direction, character_height, character_width, Map_Parts);

                character_x = values[0];
                character_y = values[1];
                grapple_x = values[2];
                grapple_y = values[3];
                if (values[4] == 1) {
                    grapple_state = true;
                } else {
                    grapple_state = false;
                }
                if (values[5] == 1) {
                    grapple_active = true;
                } else {
                    grapple_active = false;
                }

            }

            if (boomerang_gotten == true) {
                int[] values = Engine.Update_Boomerang(boomerang_active, boomerang_x, boomerang_y, boomerang_speed, boomerang_drag_directional, character_x, character_y, character_height, character_width, 
                boomerang_state, boomerang_size, boomerang_drag);

                boomerang_x = values[0];
                boomerang_y = values[1];
                boomerang_speed = values[2];
                if (values[3] == 1) {
                    boomerang_active = true;
                } else {
                    boomerang_active = false;
                }
                if (values[4] == 1) {
                    boomerang_state = true;
                } else {
                    boomerang_state = false;
                }
                boomerang_drag_directional = values[5];

            }

            int[][][] values = Engine.Update_Sword(sword_state, hit_boxes, Moving_Direction, character_x, character_y, character_width, sword_length, sword_ready_length, character_height, sword_width, 
                                sword_timer, swing_time, ready_time, frozen);

            sword_state = values[0][0][0];
            sword_timer = values[1][0][0];
            hit_boxes = values[2];
            if (values[3][0][0] == 1) {
                frozen = true;
            } else {
                frozen = false;
            }

    
            int[] values1 = Engine.Update_Collision(Map_Parts, character_x, character_y, character_height, character_width, grapple_active, grapple_x, grapple_y, grind_mode, Sideways_velocity, 
            Moving_Direction, Frictionless, Character_Facing_Direction, Upwards_velocity, Jump_State, Jumped, grapple_number, grapple_number_max, Spawn_Value, Transitioning, grind_gotten, 
            Map_Destination, grind_speed);

            if (values1[0] == 1) {
                grapple_active = true;
            } else {
                grapple_active = false;
            }
            //grapple_active
            grapple_x = values1[1];
            //grapple_x
            grapple_y = values1[2];
            //grapple_y
            Sideways_velocity = values1[3];
            //sideways_velocity
            Upwards_velocity = values1[4];
            //upwards_velocity
            character_x = values1[5];
            //character_x
            character_y = values1[6];
            //character_y
            if (values1[7] == 1) {
                Frictionless = true;
            } else {
                Frictionless = false;
            }
            //frictionless
            Moving_Direction = values1[8];
            //moving_direction
            if (values1[9] == 1) {
                Character_Facing_Direction = "Right";
            } else if (values1[9] == 0) {
                Character_Facing_Direction = "Left";
            }
            //character_facing_direction
            if (values1[10] == 1) {
                Jump_State = "Jumping";
            } else if (values1[10] == 0) {
                Jump_State = "Grounded";
            }
            //jump_state
            if (values1[11] == 1) {
                Jumped = true;
            } else {
                Jumped = false;
            }
            //jumped
            grapple_number = values1[12];
            //grapple_number
            Spawn_Value = values1[13];
            //spawn_value
            Map_Destination = values1[14];
            //map_destination
            if (values1[15] == 1) {
                Transitioning = true;
            } else {
                Transitioning = false;
            }
            //transitioning
            if (values1[16] == 1) {
                grind_mode = true;
            } else {
                grind_mode = false;
            }
            //grind_mode
            if (values1[17] == 1) {
                Falling = true;
            } else {
                Falling = false;
            }
            //falling
    
            Engine.Update_Camera();

            if (Transitioning == true) {
                Transition_Value = Engine.Update_Transition(Transition_Value, Transition_Apex, Map_Parts, Map_Destination, Spawn_Value, Transitioning);

                if (Transition_Value == 255) {
                    Transition_Apex = true;
        
                    Map_Parts = Load_Map(Map_Destination);
                    
                    Load_Spawn(Map_Parts, Spawn_Value);
        
        
                }

                if (Transition_Value == 0 && Transition_Apex == true) {

                    Transitioning = false;
                    Transition_Apex = false;
        
                }
            }
            
    

            repaint();

        }



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
            
            if (In_Title == false && In_Menu == false) {

                if (Jumped == false && grapple_active == false) {
                    Jump_State = "Jumping";
                    Jumped = true;
                    Upwards_velocity = -15;
                }

                if (grind_mode == true) {
                    grind_mode = false;
                }


            } else {

                Button_Index_Vertical--;

                Button_Index_Vertical %= Max_Button_Index_Vertical;

            }
            

        }

        if (keyCode == KeyEvent.VK_DOWN) {

            if (In_Title == false && In_Menu == false) {

            } else {

                Button_Index_Vertical++;

                Button_Index_Vertical %= Max_Button_Index_Vertical;

            }

        }

        if (keyCode == KeyEvent.VK_LEFT) {
            
            if (In_Title == false && In_Menu == false) {

                if (grapple_active == false) {
                    Sideways_velocity = -1 * Walk_Speed;
                    Moving_Direction = -1;
                    Character_Facing_Direction = "Left";
                }

                if (grind_mode == true) {
                    Sideways_velocity = Moving_Direction * grind_speed;
                }

            } else {

                Button_Index_Horizontal--;

                Button_Index_Horizontal %= Max_Button_Index_Horizontal;

            }
            
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            

            if (In_Title == false && In_Menu == false) {


                if (grapple_active == false) {
                    Sideways_velocity = Walk_Speed;
                    Moving_Direction = 1;
                    Character_Facing_Direction = "Right";
                }


                if (grind_mode == true) {
                    Sideways_velocity = Moving_Direction * grind_speed;
                }

            } else {

                
                Button_Index_Horizontal++;

                Button_Index_Horizontal %= Max_Button_Index_Horizontal;

            }
        }

        if (keyCode == KeyEvent.VK_SHIFT) {
            
            if (sword_state == 0) {

                sword_state = 1;
                sword_timer = ready_time;

                if (Jump_State.equals("Grounded")) {
                    frozen = true;
                }

            }

        }

        if (keyCode == KeyEvent.VK_SPACE) {

            if (grapple_number > 0 && grind_mode == false) {

                grapple_number--;

                grapple_active = true;
                grapple_state = false;
                grapple_x = character_x + 15;
                grapple_y = character_y + 30;
    
                Upwards_velocity = 0;
                Sideways_velocity = 0;
            }



        }

        if (keyCode == KeyEvent.VK_ENTER) {

            if (In_Title == true) {
                

                if (Button_Index_Vertical == 0) {
                    In_Title = false;
                } else if (Button_Index_Vertical == 1) {
                    In_Title = false;
                } else if (Button_Index_Vertical == 2) {
                    In_Title = false;
                } else {
                    System.exit(0);
                }


            }

        }

        if (keyCode == KeyEvent.VK_CONTROL) {

            if (boomerang_active == false) {
                boomerang_active = true;
                boomerang_x = character_x + (character_width / 2);
                boomerang_y = character_y + (character_height / 2);
    
                boomerang_speed = Moving_Direction * boomerang_speed_max;
                boomerang_drag_directional = Moving_Direction * boomerang_drag;
    
                boomerang_state = false;
            }



        }

        if (keyCode == KeyEvent.VK_ESCAPE) {

            if (In_Title == false && In_Menu == false) {

                In_Title = true;

            }

        }

        
        if (keyCode == KeyEvent.VK_NUMPAD2) {

            if (character_y >= 0 && character_y <= Screen_Width) {
                Cam_velocity_vertical = -5;
                Cam_Detached = true;
            }


        }

        if (keyCode == KeyEvent.VK_NUMPAD4) {

            if (character_y >= 0 && character_y <= Screen_Width) {
                Cam_velocity_horizontal = 5;
                Cam_Detached = true;
            }
        }

        if (keyCode == KeyEvent.VK_NUMPAD6) {

            if (character_x >= 0 && character_x <= Screen_Width) {
                Cam_velocity_horizontal = -5;
                Cam_Detached = true;
            }
        }

        if (keyCode == KeyEvent.VK_NUMPAD8) {

            if (character_y >= 0 && character_y <= Screen_Width) {
                Cam_velocity_vertical = 5;
                Cam_Detached = true;
            }
        }

 
       
        
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            
            if (In_Title == false && In_Menu == false) {
                Friction = 3;

            }
            
        }
        
        if (keyCode == KeyEvent.VK_RIGHT) {
            
            if (In_Title == false && In_Menu == false) {
                Friction = -3;
                
            }
            
            
        }

        if (keyCode == KeyEvent.VK_UP) {

            if (In_Title == false && In_Menu == false) {
                
                if (Upwards_velocity >= -5 && Upwards_velocity < 0) {
                    Upwards_velocity = 0;
                } else if (Upwards_velocity < -5) {
                    Upwards_velocity += 5;
                }
                
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


    public static int[][] Load_Map(int Map_to_go_to) {

    
        boomerang_active = false;


        String Map_Data = "";
        String Map_filepath = "Maps/Map" + Map_to_go_to + ".txt";


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

        int[][] Map_Parts = new int[Map_Size][6];
        
        for (int i = 0; i < Map_Size; i++) {

            String[] Current_Block_String = Blocks_String[i].split("[, ]+");

            int[] Block_Int = new int[6];

            for (int j = 0; j < 6; j++) {
                Block_Int[j] = Integer.valueOf(Current_Block_String[j]);
            }

            Map_Parts[i] = Block_Int;

        }

        return Map_Parts;

    }

    public void Load_Spawn(int[][] Map_Parts, int Spawn_Value) {


        for (int i = 0; i < Map_Parts.length; i++) {


            if (Map_Parts[i][4] == Spawn_Value) {

                character_x = Map_Parts[i][0];
                character_y = Map_Parts[i][1];

                Camera_Center(character_x, character_y);

                Upwards_velocity = 20;

                return;

            }

        }

        return;

    }

    public void Camera_Center(int character_x, int character_y) {

        int x_shift = Screen_Width/2 - character_x;
        int y_shift = Screen_height/2 - character_y;


        Move_Cam_Horizontal(x_shift);
        Move_Cam_Vertical(y_shift);

        return;

    }


    public static void main(String[] args) {


        
        
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
}