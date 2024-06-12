
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
    
    
    //character location values
    public static int character_x = 500;
    public static int character_y = 0;

    //character properties
    public int character_width = 30;
    public int character_height = 60;
    
    //Screen
    public int Screen_Width = 1000;
    public int Screen_height = 600;
    
    //physics constants
    public int Gravity = 1;
    public int Friction = 0;
    public Boolean Frictionless = false;

    //character moving
    public String Jump_State = "Grounded";
    public int Upwards_velocity = 0;
    public int Sideways_velocity = 0;
    public Boolean Falling = false;
    public Boolean Jumped = false;
    public Boolean Walking = false;
    public int Walk_Timer = 0;
    public int Moving_Direction = 0; // Left = -1, Right = +1
    public String Character_Facing_Direction = "Right";
    public String Moving_State = "";
    public int Walk_Speed = 7;
    public Boolean frozen = false;

    //Grapple properties

    public boolean grapple_active = false;
    public boolean grapple_gotten = true;
    public int grapple_x = 0;
    public int grapple_y = 0;
    public int grapple_size = 5;
    public int grapple_speed = 10;
    public int grapple_number = 0;
    public int grapple_number_max = 2;
    //false = extending, true = retracting
    public boolean grapple_state = false;

    // rang properties
    public boolean boomerang_gotten = true;
    public static boolean boomerang_active = false;
    public int boomerang_x = 0;
    public int boomerang_y = 0;
    public int boomerang_size = 5;
    public int boomerang_speed_max = 25;
    public int boomerang_speed = 0;
    public int boomerang_drag = 1;
    public int boomerang_drag_directional = 0;
    //false = extending, true = retracting
    public boolean boomerang_state = false;


    //sword properties
    //0 = holstered, 1 = 1st ready, 2 = first swing, 3 = 2nd ready, 4 = 2nd swing
    public int sword_state = 0;
    public int sword_length = 80;
    public int sword_width = 5;
    public int sword_ready_length = 30;
    public int swing_time = 30;
    public int ready_time = 10;
    public int sword_timer = 0;
    public int swing_displacement_speed = 2;
    public int swing_displacement_time = 40;
    public int swing_displacement_timer = 0;
    public int[][] hit_boxes = new int[10][4];


    // grind properties

    public boolean grind_gotten = true;
    public boolean grind_mode = false;
    public int grind_speed = 10;


    //camera properties

    public int Cam_velocity_vertical = 0;
    public int Cam_velocity_horizontal = 0;
    public Boolean Cam_Detached = false;


    public int Timer_Value = 0;


    //Map Properties

    public int Map_Destination = 100;

    public static boolean Transitioning = false;
    public static boolean Transition_Apex = false;
    public static int Transition_Value = 0;

    public int Spawn_Value = 19;

    public int[][] Map_Parts = new int[0][0];




    //Menu properties
    public boolean In_Title = true;
    public boolean In_Menu = false;
    public int Button_Index_Vertical = 0;
    public int Button_Index_Horizontal = 0;
    public int Max_Button_Index_Vertical = 4;
    public int Max_Button_Index_Horizontal = 1;


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
                Update_Character();
            }
            

            if (grapple_gotten == true) {
                Update_Grapple();
            }

            if (boomerang_gotten == true) {
                Update_Boomerang();
            }

            Update_Sword();
    
            Update_Collision();
    
            Update_Camera();

            if (Transitioning == true) {
                Update_Transition();
            }
            
    

            repaint();

        }



    }

    public void Update_Transition() {

        if (Transition_Apex == false) {
            Transition_Value = Clock.Timer_ticker(Transition_Value, 15, 256);

            

        } else {

            Transition_Value = Clock.Timer_ticker(Transition_Value, -15, 256);

        }

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

    private void Update_Character() {
            //character movement
        //sideways movement
        
        if (Walking == true && Sideways_velocity != 0) {
            character_x += (Sideways_velocity/2);
        } else {
            character_x += Sideways_velocity;
        }
        
        if (Frictionless == false) {
            
            if ( (Sideways_velocity > 0 && Friction < 0) || (Sideways_velocity < 0 && Friction > 0)) {
            
                if (Sideways_velocity <= -Friction) {
                    Sideways_velocity = 0;
                } else {
                    Sideways_velocity += Friction;
                }
                
            } else {
                Friction = 0;
            }


        }
        
        //jumping

        if (Upwards_velocity > 20) {
            Upwards_velocity = 20;
        } else if (Upwards_velocity < -50) {
            Upwards_velocity = -50;
        }



        character_y += Upwards_velocity;

        if( Jump_State.equals("Jumping") && grapple_active == false) {
            Upwards_velocity += Gravity;
        } 

        return;

    }

    private void Update_Sword() {


        if (sword_state == 1 || sword_state == 3) {


            for (int i = 0; i < hit_boxes.length; i++) {

                if (Moving_Direction == -1) {
                    hit_boxes[i][0] = character_x + (character_width/2) + Moving_Direction * ((sword_ready_length/ hit_boxes.length) * (i + 1));
                    hit_boxes[i][1] = character_y + (character_height/2);
    
                    hit_boxes[i][2] = sword_length/hit_boxes.length;
                    hit_boxes[i][3] = sword_width; 
                } else {
                    hit_boxes[i][0] = character_x + (character_width/2) + Moving_Direction * ((sword_ready_length/ hit_boxes.length) * i);
                    hit_boxes[i][1] = character_y + (character_height/2);

                    hit_boxes[i][2] = sword_length/hit_boxes.length;
                    hit_boxes[i][3] = sword_width;

                }

            }

            sword_timer -= 1;

            if (sword_timer <= 0) {
                sword_state += 1;
                sword_timer = swing_time;
                hit_boxes = new int[10][4];
            }


        } 
        
        if (sword_state == 2) {

            for (int j = 0; j < hit_boxes.length; j++) {

                if (Moving_Direction == -1) {
                    hit_boxes[j][0] = character_x + (character_width/2) + Moving_Direction * ((sword_length/ hit_boxes.length) * (j + 1));
                    hit_boxes[j][1] = character_y + (character_height/2);
    
                    hit_boxes[j][2] = sword_length/hit_boxes.length;
                    hit_boxes[j][3] = sword_width; 
                } else {
                    hit_boxes[j][0] = character_x + (character_width/2) + Moving_Direction * ((sword_length/ hit_boxes.length) * j);
                    hit_boxes[j][1] = character_y + (character_height/2);

                    hit_boxes[j][2] = sword_length/hit_boxes.length;
                    hit_boxes[j][3] = sword_width;

                }



            }



            sword_timer -= 1;

            if (sword_timer <= 0) {
                sword_state = 0;
                sword_timer = ready_time;
                hit_boxes = new int[10][4];
                frozen = false;
            }

        }

    }

    private void Update_Grapple() {


        if (grapple_active == true && grapple_state == false) {

            if (Grapple_Collision() == true) {

                grapple_state = true;

            } else if ((grapple_x > Screen_Width || grapple_x < 0) || (grapple_y > Screen_height || grapple_y < 0) ) {
                
                grapple_active = false;

                grapple_number++;

                grapple_x = character_x + 15;
                grapple_y = character_y + 30;

                
            } else {

                grapple_x += (grapple_speed * Moving_Direction);

                grapple_y -= grapple_speed;

            }

        } else if (grapple_active == true && grapple_state == true) {

            character_x += (grapple_speed * Moving_Direction);

            character_y -= grapple_speed;

            if (CollisionCheck(character_x, character_y, character_width, character_height, grapple_x, grapple_y, grapple_size, grapple_size) == true) {

                grapple_active = false;
                grapple_state = false;

            }


        }


        return;


    }

    private boolean Grapple_Collision() {

        boolean Grapple_collides = false;


        for (int z = 0; z < Map_Parts.length; z++) {

            if (CollisionCheck(grapple_x, grapple_y, grapple_size, grapple_size, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {

                if (Map_Parts[z][4] == 1 || Map_Parts[z][4] == 2 || Map_Parts[z][4] == 3) {
                    
                    Grapple_collides = true;

                    return Grapple_collides;                    
                }



            }
        }

        return Grapple_collides;


    }

    private void Update_Boomerang() {

        if (boomerang_active == true) {

            boomerang_x += boomerang_speed;

            boomerang_speed -= boomerang_drag_directional;

            if (boomerang_speed > 25) {
                boomerang_speed = 25;
            } else if (boomerang_speed < -25) {
                boomerang_speed = -25;
            }
            
            if (boomerang_speed < 2 && boomerang_speed > -2) {
                boomerang_state = true;
            }

            if (boomerang_state == true && (boomerang_y > character_y + (character_height/2) || boomerang_y < character_y + + (character_height/2))) {

                if ((character_y + (character_height/2) - boomerang_y) / 10 > 10) {
                    boomerang_y += 10;
                } else if ((character_y + (character_height/2) - boomerang_y) / 10 < -10) {
                    boomerang_y += -10;
                } else {
                    boomerang_y += (character_y + (character_height/2) - boomerang_y) / 10;
                }
                

            }

        }

        if (boomerang_state == true && CollisionCheck(boomerang_x, boomerang_y, boomerang_size, boomerang_size, character_x, character_y, character_width, character_height)) {

            boomerang_active = false;

        }

        if (boomerang_state == true) {

            if (character_x > boomerang_x) {
                boomerang_drag_directional = boomerang_drag * -1;
            } else {
                boomerang_drag_directional = boomerang_drag;
            }

        }

    }

    private void Update_Collision() {
        Falling = true;


        for (int z = 0; z < Map_Parts.length; z++) {




            if (CollisionCheck(character_x, character_y, character_width, character_height, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                
                grapple_active = false;

                grapple_x = character_x + 15;
                grapple_y = character_y + 30;
                
                if (Map_Parts[z][4] == 2) { // wall
                    
                    if (grind_mode == false) {
                        Sideways_velocity = 0;

                        if (Moving_Direction == 1) {
                            character_x = Map_Parts[z][0] - character_width - 5; 
                        } else if (Moving_Direction == -1) {
                            character_x = Map_Parts[z][0] + Map_Parts[z][2] + 5;
                        }
    
                        if (Frictionless == true) {
                            Frictionless = false;
                        }

                    } else if (grind_mode == true) {
                        Moving_Direction *= -1;
                        Sideways_velocity *= -1;

                        if (Character_Facing_Direction.equals("Left")) {
                            Character_Facing_Direction = "Right";
                        } else if (Character_Facing_Direction.equals("Right")) {
                            Character_Facing_Direction = "Left";
                        }
                    }

                    
                } else if (Map_Parts[z][4] == 1) { // floor
                    
                    


                    if (CollisionCheck(character_x, character_y + 10, character_width, character_height - 10, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3]) == true) {
                        
                        if (Upwards_velocity > 0) {
                            Upwards_velocity = 0;
                        }

                        if (grind_mode == true) {
                            grind_mode = false;
                            Sideways_velocity = 0;
                        }
                        

                        if ((character_y + 60) - Map_Parts[z][1] <= character_height / 3 ) {
                            character_y = Map_Parts[z][1] - character_height;
                            Jump_State = "Grounded";
                            Jumped = false;
                            grapple_number = grapple_number_max;
                        }

                        if (Frictionless == true) {
                            Frictionless = false;
                        }
            
                        
                    }


                    

                } else if (Map_Parts[z][4] == 3) { // celing
                    
                    if (Map_Parts[z][0] < character_x && character_x < Map_Parts[z][0] + Map_Parts[z][3]) {
                        if (Upwards_velocity <= 2) {
                            Upwards_velocity = 2;
                            character_y = Map_Parts[z][1] + Map_Parts[z][3];
                        }
                    

                    }

                    if (Frictionless == true) {
                        Frictionless = false;
                    }
                    
                    
                } else if (Map_Parts[z][4] >= 10 && Map_Parts[z][4] < 20) { // spawn pointer

                    Spawn_Value = Map_Parts[z][4];
                    


                } else if (Map_Parts[z][4] == 4) { // transition marker

                    if (Transitioning == false) {
                        
                        Map_Destination = Map_Parts[z][5];
                        
                    }

                    if (Transitioning == false) {
                        Transitioning = true;
                    }

                } else if (Map_Parts[z][4] == 6 && grind_gotten == true) { // grind rails

                    if (Upwards_velocity > 0 && Map_Parts[z][5] == 1) {
                        Set_Grind_State(0, Map_Parts[z][1]);
                    } else if (Map_Parts[z][5] == 2) {
                        Set_Grind_State(Moving_Direction * grind_speed, Map_Parts[z][1]);
                    } else if (Map_Parts[z][5] == 3) {
                        Set_Grind_State(-1 * Moving_Direction * grind_speed, Map_Parts[z][1]);
                    }


                }


                

            }



            //falling check

            if (CollisionCheck(character_x, character_y + character_height - 5, character_width, 10, Map_Parts[z][0], Map_Parts[z][1], Map_Parts[z][2], Map_Parts[z][3])) {
                Falling = false;
            }



        }

        if (Falling == true) {
            Jump_State = "Jumping";
            Jumped = true;
        }

        return;

    }

    private void Set_Grind_State(int Up_Movement, int Map_Parts_needed) {

        grind_mode = true;
        Frictionless = true;
        Upwards_velocity = Up_Movement;
        Sideways_velocity = Moving_Direction * grind_speed;
        if (Up_Movement == 0) {
            character_y = Map_Parts_needed - character_height;
        }
        Jump_State = "Grounded";
        Jumped = false;
        grapple_number = grapple_number_max;

    }

    private void Update_Camera() {
        
        if (Cam_Detached == false) {

            if (character_x >= (Screen_Width/2)) {

                Cam_velocity_horizontal = ((Screen_Width/2) - character_x)/10;

            } else if (character_x <= (Screen_Width/2)) {

                Cam_velocity_horizontal = (Screen_Width/2 - character_x)/10;

            } else {

                Cam_velocity_horizontal = 0;

            }

            if (character_y >= Screen_height/2) {

                Cam_velocity_vertical = (Screen_height/2 - character_y)/10;

            } else if (character_y <= 290) {

                Cam_velocity_vertical = (Screen_height/2 - character_y)/10;

            } else {

                Cam_velocity_vertical = 0;

            }

        }


        Move_Cam_Horizontal(Cam_velocity_horizontal);
        Move_Cam_Vertical(Cam_velocity_vertical);

        return;
    }


    private void Move_Cam_Horizontal(int amount) {

        if (character_x >= 0 && character_x <= (Screen_Width - character_width - 12) || Cam_Detached == false) {
            character_x += amount;

            grapple_x += amount;

            boomerang_x += amount;
    
            for (int i = 0; i < Map_Parts.length; i++) {
    
    
                Map_Parts[i][0] += amount;
    
            }

        }



    }

    private void Move_Cam_Vertical(int amount) {

        if (character_y >= 0 && character_y <= (Screen_height - character_height - 40) || Cam_Detached == false) {
            character_y += amount;

            grapple_y += amount;
            
            boomerang_y += amount;
    
    
            for (int i = 0; i < Map_Parts.length; i++) {
    
    
                Map_Parts[i][1] += amount;
    
            }
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