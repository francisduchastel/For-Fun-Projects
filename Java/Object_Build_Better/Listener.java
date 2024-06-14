
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Listener {

    public static void main(String[] args) {


        
    }


    public int[] ButtonPressed(int keyCode, boolean In_Title, boolean In_Menu, boolean Jumped, boolean grapple_active, boolean grind_mode, int Button_Index_Vertical, int Button_Index_Horizontal,
    int Max_Button_Index_Horizontal, int Max_Button_Index_Vertical, int Sideways_velocity, int Walk_Speed, int Moving_Direction, int grind_speed, int sword_state, int ready_time, String Jump_State,
    boolean frozen, int grapple_number, boolean grapple_state, boolean boomerang_active, int boomerang_speed_max, int boomerang_drag, int character_x, int character_y, int Screen_height, 
    int Screen_Width, int character_height, int character_width, int Upwards_velocity, String Character_Facing_Direction, int sword_timer, int grapple_x, int grapple_y, int boomerang_x,
    int boomerang_y, int boomerang_speed, int boomerang_drag_directional, boolean boomerang_state, int Cam_velocity_horizontal, int Cam_velocity_vertical, boolean Cam_Detached, int Friction) {


        int[] values = new int[28];
        
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
            
            if (sword_state == 0 && grind_mode == false) {

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

            if (In_Title == false && In_Menu == false && Sideways_velocity == 0 && Upwards_velocity == 0) {

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

            if (character_x >= 0 && character_x <= Screen_height) {
                Cam_velocity_horizontal = 5;
                Cam_Detached = true;
            }
        }

        if (keyCode == KeyEvent.VK_NUMPAD6) {

            if (character_x >= 0 && character_x <= Screen_height) {
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


        if (Jump_State.equals("Jumping")) {
            values[0] = 1;
        } else if (Jump_State.equals("Grounded")) {
            values[0] = 0;
        }
        if (Jumped == true) {
            values[1] = 1;
        } else {
            values[1] = 0;
        }
        values[2] = Upwards_velocity;
        values[3] = Sideways_velocity;
        if (grind_mode == true) {
            values[4] = 1;
        } else {
            values[4] = 0;
        }
        values[5] = Button_Index_Vertical;
        values[6] = Button_Index_Horizontal;
        values[7] = Moving_Direction;
        if (Character_Facing_Direction.equals("Right")) {
            values[8] = 1;
        } else if (Character_Facing_Direction.equals("Left")) {
            values[8] = 0;
        }
        values[9] = sword_state;
        values[10] = sword_timer;
        if (frozen == true) {
            values[11] = 1;
        } else {
            values[11] = 0;
        }
        values[12] = grapple_number;
        if (grapple_active == true) {
            values[13] = 1;
        } else {
            values[13] = 0;
        }
        if (grapple_state == true) {
            values[14] = 1;
        } else {
            values[14] = 0;
        }
        values[15] = grapple_x;
        values[16] = grapple_y;
        if (In_Title == true) {
            values[17] = 1;
        } else {
            values[17] = 0;
        }
        values[18] = boomerang_x;
        values[19] = boomerang_y;
        if (boomerang_state == true) {
            values[20] = 1;
        } else {
            values[20] = 0;
        }
        values[21] = boomerang_speed;
        values[22] = boomerang_drag_directional;
        if (boomerang_active == true) {
            values[23] = 1;
        } else {
            values[23] = 0;
        }
        values[24] = Cam_velocity_vertical;
        values[25] = Cam_velocity_horizontal;
        if (Cam_Detached == true) {
            values[26] = 1;
        } else {
            values[26] = 0;
        }
        values[27] = Friction;



        return values;

    }

    public int[] ButtonReleased(int keyCode, boolean In_Title, boolean In_Menu, int Friction, int Upwards_velocity, boolean Cam_Detached) {


        int[] values = new int[3];

        
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

    
        values[0] = Friction;
        values[1] = Upwards_velocity;
        if (Cam_Detached == true) {
            values[2] = 1;
        } else {
            values[2] = 0;
        }


        return values;


    }

}