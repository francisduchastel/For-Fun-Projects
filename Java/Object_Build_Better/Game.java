
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Game extends JFrame implements KeyListener {
    
    static Archive A = new Archive();

    Clock Time = new Clock();

    Painter Paint = new Painter(A);

    Game_Engine Engine = new Game_Engine(A);

    Listener Listen = new Listener();

    
    
    
    


    public Game() {
        setTitle("A Bad Game");
        setSize(A.g_Screen_Width(), A.g_Screen_height());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        A.s_Map_Parts(Load_Map(A.g_Map_Destination()));

        Load_Spawn(A.g_Map_Parts(), A.g_Spawn_Value());


        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                

                if (A.g_In_Title() == false) {

                    Paint.DrawMap(g);

                    Paint.DrawGrapple(g);

                    Paint.DrawBoomerang(g);

                    if (A.g_sword_state() != 0) {
                        Paint.DrawSword(g);
                    }
                    
                    //

                    A.s_Walk_Timer(Time.Timer_ticker(A.g_Walk_Timer(), 1, 11));

                    if (A.g_Walk_Timer() == 0) {
                        A.s_Timer_Value(Time.Timer_ticker(A.g_Timer_Value(), 1, 2));
                    }

                    Paint.DrawCharacter(g);

                    //
    
                    Paint.DrawTransitions(g);

                    //

                } else if (A.g_In_Title() == true) {

                    Paint.DrawTitle(g);

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

        if (A.g_In_Title() == false) {

            if (A.g_frozen() == false) {
                Engine.Update_Character();
            }
            

            if (A.g_grapple_gotten() == true) {
                Engine.Update_Grapple();

            }

            if (A.g_boomerang_gotten() == true) {
                Engine.Update_Boomerang();


            }

            Engine.Update_Sword();

    
            Engine.Update_Collision();

    
            Engine.Update_Camera();

            if (Transitioning == true) {
                Engine.Update_Transition();

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
        
        int[] values = Listen.ButtonPressed(keyCode, In_Title, In_Menu, Jumped, grapple_active, grind_mode, Button_Index_Vertical, Button_Index_Horizontal, Max_Button_Index_Horizontal, 
        Max_Button_Index_Vertical, Sideways_velocity, Walk_Speed, Moving_Direction, grind_speed, sword_state, ready_time, Jump_State, frozen, grapple_number, grapple_state, boomerang_active, 
        boomerang_speed_max, boomerang_drag, character_x, character_y, Screen_height, Screen_Width, character_height, character_width, Upwards_velocity, Character_Facing_Direction, 
        sword_timer, grapple_x, grapple_y, boomerang_x, boomerang_y, boomerang_speed, boomerang_drag_directional, boomerang_state, Cam_velocity_horizontal, Cam_velocity_vertical, Cam_Detached,
        Friction);
 
        if (values[0] == 1) {
            Jump_State = "Jumping";
        } else if (values[0] == 0) {
            Jump_State = "Grounded";
        }
        if (values[1] == 1) {
            Jumped = true;
        } else {
            Jumped = false;
        }
        Upwards_velocity = values[2];
        Sideways_velocity = values[3];
        if (values[4] == 1) {
            grind_mode = true;
        } else {
            grind_mode = false;
        }
        Button_Index_Vertical = values[5];
        Button_Index_Horizontal = values[6];
        Moving_Direction = values[7];
        if (values[8] == 1) {
            Character_Facing_Direction = "Right";
        } else if (values[8] == 0) {
            Character_Facing_Direction = "Left";
        }
        sword_state = values[9];
        sword_timer = values[10];
        if (values[11] == 1) {
            frozen = true;
        } else {
            frozen = false;
        }
        grapple_number = values[12];
        if (values[13] == 1) {
            grapple_active = true;
        } else {
            grapple_active = false;
        }
        if (values[14] == 1) {
            grapple_state = true;
        } else {
            grapple_state = false;
        }
        grapple_x = values[15];
        grapple_y = values[16];
        if (values[17] == 1) {
            In_Title = true;
        } else {
            In_Title = false;
        }
        boomerang_x = values[18];
        boomerang_y = values[19];
        if (values[20] == 1) {
            boomerang_state = true;
        } else {
            boomerang_state = false;
        }
        boomerang_speed = values[21];
        boomerang_drag_directional = values[22];
        if (values[23] == 1) {
            boomerang_active = true;
        } else {
            boomerang_active = false;
        }
        Cam_velocity_vertical = values[24];
        Cam_velocity_horizontal = values[25];
        if (values[26] == 1) {
            Cam_Detached = true;
        } else {
            Cam_Detached = false;
        }

        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        int[] values = Listen.ButtonReleased(keyCode, In_Title, In_Menu, Friction, Upwards_velocity, Cam_Detached);

        Friction = values[0];
        Upwards_velocity = values[1];
        if (values[2] == 1) {
            Cam_Detached = true;
        } else {
            Cam_Detached = false;
        }

        
    }


    public static int[][] Load_Map(int Map_to_go_to) {

    
        A.s_boomerang_active(false);


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

                Engine.Camera_Center(character_x, character_y, Screen_Width, Screen_height, character_height, character_width, Cam_Detached, grapple_x, grapple_y, boomerang_x, boomerang_y, Map_Parts);


                Upwards_velocity = 20;

                return;

            }

        }

        return;

    }

    


    public static void main(String[] args) {


        Archive A = new Archive();


        A.s_character_x(500);
        A.s_character_y(0);

        //character properties
        A.s_character_width(30);
        A.s_character_height(60);
        
        //Screen
        A.s_Screen_Width(1000);
        A.s_Screen_height(600);
        
        //physics constants
        A.s_Gravity(1);
        A.s_Friction(0);
        A.s_Frictionless(false);

        //character moving
        A.s_Jump_State("Grounded");
        A.s_Upwards_velocity(0);
        A.s_Sideways_velocity(0);
        A.s_Falling(false);
        A.s_Jumped(false);
        A.s_Walk_Timer(0);
        A.s_Moving_Direction(0); // Left(-1, Right(+1
        A.s_Character_Facing_Direction("Right");
        A.s_Moving_State("");
        A.s_Walk_Speed(7);
        A.s_frozen(false);

        //Grapple properties

        A.s_grapple_active(false);
        A.s_grapple_gotten(true);
        A.s_grapple_x(0);
        A.s_grapple_y(0);
        A.s_grapple_size(5);
        A.s_grapple_speed(10);
        A.s_grapple_number(0);
        A.s_grapple_number_max(2);
        //false(extending, true(retracting
        A.s_grapple_state(false);

        // rang properties
        A.s_boomerang_gotten(true);
        A.s_boomerang_active(false);
        A.s_boomerang_x(0);
        A.s_boomerang_y(0);
        A.s_boomerang_size(5);
        A.s_boomerang_speed_max(25);
        A.s_boomerang_speed(0);
        A.s_boomerang_drag(1);
        A.s_boomerang_drag_directional(0);
        //false(extending, true(retracting
        A.s_boomerang_state(false);


        //sword properties
        //0(holstered, 1(1st ready, 2(first swing, 3(2nd ready, 4(2nd swing
        A.s_sword_state(0);
        A.s_sword_length(80);
        A.s_sword_width(5);
        A.s_sword_ready_length(30);
        A.s_swing_time(30);
        A.s_ready_time(10);
        A.s_sword_timer(0);
        A.s_swing_displacement_speed(2);
        A.s_swing_displacement_time(40);
        A.s_swing_displacement_timer(0);
        A.s_hit_boxes(new int[10][4]);


        // grind properties

        A.s_grind_gotten(true);
        A.s_grind_mode(false);
        A.s_grind_speed(10);


        //camera properties

        A.s_Cam_velocity_vertical(0);
        A.s_Cam_velocity_horizontal(0);
        A.s_Cam_Detached(false);


        A.s_Timer_Value(0);


        //Map Properties

        A.s_Map_Destination(100);

        A.s_Transitioning(false);
        A.s_Transition_Apex(false);
        A.s_Transition_Value(0);

        A.s_Spawn_Value(19);

        A.s_Map_Parts(new int[0][0]);

        //Menu properties
        A.s_In_Title(true);
        A.s_In_Menu(false);
        A.s_Button_Index_Vertical(0);
        A.s_Button_Index_Horizontal(0);
        A.s_Max_Button_Index_Vertical(4);
        A.s_Max_Button_Index_Horizontal(1);
            
        
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }



}

    