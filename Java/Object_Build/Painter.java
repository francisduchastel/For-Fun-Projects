import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.*;


public class Painter {

    public static void main(String args[]) {

    }


        public void DrawTitle(Graphics g, int Max_Button_Index_Horizontal, int Max_Button_Index_Vertical, int Button_Index_Horizontal, int Button_Index_Vertical, int Screen_Width, Boolean In_Title) {

        Max_Button_Index_Horizontal = 1;
        Max_Button_Index_Vertical = 4;

        if (Button_Index_Horizontal < 0) {
            Button_Index_Horizontal = Max_Button_Index_Horizontal - 1;
        }

        if (Button_Index_Vertical < 0) {
            Button_Index_Vertical = Max_Button_Index_Vertical - 1;
        }




        Draw_Image(Screen_Width/3, 150, "Images/Title/Save1.png", g);

        Draw_Image(Screen_Width/3, 225, "Images/Title/Save2.png", g);

        Draw_Image(Screen_Width/3, 300, "Images/Title/Save3.png", g);

        Draw_Image(Screen_Width/3, 375, "Images/Title/Exit.png", g);

        Draw_Menu_Selection(g, In_Title, Screen_Width, Button_Index_Horizontal, Button_Index_Vertical);

    }

    public void Draw_Menu_Selection(Graphics g, boolean In_Title, int Screen_Width, int Button_Index_Horizontal, int Button_Index_Vertical) {
        //true for Title, false for Inv

        if (In_Title == true) {

            drawSprite(g, Screen_Width/3, 150 + (Button_Index_Vertical * 75), 20, 4, Color.BLACK);
            drawSprite(g, Screen_Width/3, 150 + (Button_Index_Vertical * 75), 4, 20, Color.BLACK);

            drawSprite(g, Screen_Width/3 + 280, 150 + (Button_Index_Vertical * 75), 20, 4, Color.BLACK);
            drawSprite(g, Screen_Width/3 + 296, 150 + (Button_Index_Vertical * 75), 4, 20, Color.BLACK);

            drawSprite(g, Screen_Width/3, 196 + (Button_Index_Vertical * 75), 20, 4, Color.BLACK);
            drawSprite(g, Screen_Width/3, 180 + (Button_Index_Vertical * 75), 4, 20, Color.BLACK);

            drawSprite(g, Screen_Width/3 + 280, 196 + (Button_Index_Vertical * 75), 20, 4, Color.BLACK);
            drawSprite(g, Screen_Width/3 + 296, 180 + (Button_Index_Vertical * 75), 4, 20, Color.BLACK);


        }


    }

    public void DrawCharacter(Graphics g, int Walk_Timer, int Timer_Value, boolean grind_mode, int Sideways_velocity, String Jump_State, String Character_Facing_Direction, int character_x, int character_y) {

        //Draw character

        String Moving_State;

        if (grind_mode == false) {
            if (Sideways_velocity != 0) {
                Moving_State = "_Move" + (Timer_Value + 1);
            } else {
                Moving_State = "";
            }            
        } else {
            Moving_State = "_Grind";
        }



        String Character_Image = "Images/Character_" + Character_Facing_Direction + Moving_State;


        if (Jump_State.equals("Grounded") == false) {
            Character_Image += "_Airbourn";
        }

        Character_Image += ".png";

        Draw_Image(character_x, character_y, Character_Image, g);
        

    }

    public void DrawSword(Graphics g, int[][] hit_boxes) {


        for (int i = 0; i < hit_boxes.length; i++) {

            drawSprite(g, hit_boxes[i][0], hit_boxes[i][1], hit_boxes[i][2], hit_boxes[i][3], Color.RED);

        }


    }

    public void DrawGrapple(Graphics g, boolean grapple_gotten, boolean grapple_active, int grapple_x, int grapple_y, int grapple_size, int character_x, int character_y, int character_height,
        int character_width) {


        if (grapple_gotten == true && grapple_active == true) {

            drawSprite(g, grapple_x, grapple_y, grapple_size, grapple_size, Color.PINK);

        }



        if (grapple_active == true && (grapple_x > character_x + character_width || grapple_x < character_x) && (grapple_y < character_y || grapple_y > character_y + character_height)) {
            boolean done = false;
            int chain_x = grapple_x + 2;
            int chain_y = grapple_y + 2;


            while (done == false) {

                if (chain_x < character_x) {
                    chain_x++;
                } else if (chain_x > character_x) {
                    chain_x--;
                } else {
                    done = true;
                }

                if (chain_y < character_y) {
                    chain_y++;
                } else if (chain_y > character_y) {
                    chain_y--;
                } else {
                    done = true;
                }

                drawSprite(g, chain_x, chain_y, 1, 1, new Color(50, 50, 50, 200) );


            }

        }



    }

    public void DrawBoomerang(Graphics g, boolean boomerang_active, int boomerang_x, int boomerang_y, int boomerang_size) {

        if (boomerang_active == true) {
            drawSprite(g, boomerang_x, boomerang_y, boomerang_size, boomerang_size, Color.GREEN);  
        }
        

    }

    public void DrawMap(Graphics g, int[][] Map_Parts) {

        for (int q = 0; q < Map_Parts.length; q++) {

            if (Map_Parts[q][4] == 1) {
                drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.black);
            } else if (Map_Parts[q][4] == 2) {
                drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.gray);
            } else if (Map_Parts[q][4] == 3) {
                drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.lightGray);
            } else if (Map_Parts[q][4] == 0) {
                
                for (int i = 0; i < 175; i++) {
                    drawSprite(g, Map_Parts[q][0] + i, Map_Parts[q][1], Map_Parts[q][2] / 2, Map_Parts[q][3], new Color(0, 0, 0, 175 - i));
                }
            } else if (Map_Parts[q][4] == 5) {

                for (int j = 174; j >= 0; j--) {
                    drawSprite(g, Map_Parts[q][0] + 10 - j, Map_Parts[q][1], Map_Parts[q][2] / 2, Map_Parts[q][3], new Color(0, 0, 0, 175 -  j));
                }

            } else if (Map_Parts[q][4] == 6) {
                drawSprite(g, Map_Parts[q][0], Map_Parts[q][1], Map_Parts[q][2], Map_Parts[q][3], Color.gray);

            }


        }


    }

    public void DrawTransitions(Graphics g, boolean Transitioning, int Screen_height, int Screen_Width, int Transition_Value, boolean Transition_Apex, int[][] Map_Parts, int Map_Destination) {

        if (Transitioning == true) {


            drawSprite(g, 0, 0, Screen_Width, Screen_height, new Color(0, 0, 0, Transition_Value));


        }

    }

    public void drawSprite(Graphics g, int Start_X, int Start_Y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(Start_X, Start_Y, width, height);
    }

    public void Draw_Image(int x, int y, String Image_Path, Graphics g) {

        try {
            File pathToFile = new File(Image_Path);
            Image image = ImageIO.read(pathToFile);
            g.drawImage(image, x, y, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}