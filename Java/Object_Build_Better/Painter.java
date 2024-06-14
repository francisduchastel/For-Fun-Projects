import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.*;


public class Painter {

    static Archive A = new Archive();

    public void DrawTitle(Graphics g) {


        

        A.s_Max_Button_Index_Vertical(4);

        if (A.g_Button_Index_Horizontal() < 0) {
            A.s_Button_Index_Horizontal(A.g_Max_Button_Index_Horizontal() - 1);
        }

        if (A.g_Button_Index_Vertical() < 0) {
            A.s_Button_Index_Vertical(A.g_Max_Button_Index_Vertical() - 1);
        }




        Draw_Image(A.g_Screen_Width()/3, 150, "Images/Title/Save1.png", g);

        Draw_Image(A.g_Screen_Width()/3, 225, "Images/Title/Save2.png", g);

        Draw_Image(A.g_Screen_Width()/3, 300, "Images/Title/Save3.png", g);

        Draw_Image(A.g_Screen_Width()/3, 375, "Images/Title/Exit.png", g);

        Draw_Menu_Selection(g);

    }

    public void Draw_Menu_Selection(Graphics g) {
        //true for Title, false for Inv


        if (A.g_In_Title() == true) {

            drawSprite(g, A.g_Screen_Width()/3, 150 + (A.g_Button_Index_Vertical() * 75), 20, 4, Color.BLACK);
            drawSprite(g, A.g_Screen_Width()/3, 150 + (A.g_Button_Index_Vertical() * 75), 4, 20, Color.BLACK);

            drawSprite(g, A.g_Screen_Width()/3 + 280, 150 + (A.g_Button_Index_Vertical() * 75), 20, 4, Color.BLACK);
            drawSprite(g, A.g_Screen_Width()/3 + 296, 150 + (A.g_Button_Index_Vertical() * 75), 4, 20, Color.BLACK);

            drawSprite(g, A.g_Screen_Width()/3, 196 + (A.g_Button_Index_Vertical() * 75), 20, 4, Color.BLACK);
            drawSprite(g, A.g_Screen_Width()/3, 180 + (A.g_Button_Index_Vertical() * 75), 4, 20, Color.BLACK);

            drawSprite(g, A.g_Screen_Width()/3 + 280, 196 + (A.g_Button_Index_Vertical() * 75), 20, 4, Color.BLACK);
            drawSprite(g, A.g_Screen_Width()/3 + 296, 180 + (A.g_Button_Index_Vertical() * 75), 4, 20, Color.BLACK);


        }


    }

    public void DrawCharacter(Graphics g) {

        //Draw character

        String Moving_State;

        if (A.g_grind_mode() == false) {
            if (A.g_Sideways_velocity() != 0) {
                Moving_State = "_Move" + (A.g_Timer_Value() + 1);
            } else {
                Moving_State = "";
            }            
        } else {
            Moving_State = "_Grind";
        }



        String Character_Image = "Images/Character_" + A.g_Character_Facing_Direction() + Moving_State;


        if (A.g_Jump_State().equals("Grounded") == false) {
            Character_Image += "_Airbourn";
        }

        Character_Image += ".png";

        Draw_Image(A.g_character_x(), A.g_character_y(), Character_Image, g);
        

    }

    public void DrawSword(Graphics g) {

        int [][] hit_boxes = A.g_hit_boxes();

        for (int i = 0; i < hit_boxes.length; i++) {

            drawSprite(g, hit_boxes[i][0], hit_boxes[i][1], hit_boxes[i][2], hit_boxes[i][3], Color.RED);

        }


    }

    public void DrawGrapple(Graphics g) {


        if (A.g_grapple_gotten() == true && A.g_grapple_active() == true) {

            drawSprite(g, A.g_grapple_x(), A.g_grapple_y(), A.g_grapple_size(), A.g_grapple_size(), Color.PINK);

        }



        if (A.g_grapple_active() == true && (A.g_grapple_x() > A.g_character_x() + A.g_character_width() || A.g_grapple_x() < A.g_character_x()) && 
            (A.g_grapple_y() < A.g_character_y() || A.g_grapple_y() > A.g_character_y() + A.g_character_height())) {
            boolean done = false;
            int chain_x = A.g_grapple_x() + 2;
            int chain_y = A.g_grapple_y() + 2;


            while (done == false) {

                if (chain_x < A.g_character_x()) {
                    chain_x++;
                } else if (chain_x > A.g_character_x()) {
                    chain_x--;
                } else {
                    done = true;
                }

                if (chain_y < A.g_character_y()) {
                    chain_y++;
                } else if (chain_y > A.g_character_y()) {
                    chain_y--;
                } else {
                    done = true;
                }

                drawSprite(g, chain_x, chain_y, 1, 1, new Color(50, 50, 50, 200) );


            }

        }



    }

    public void DrawBoomerang(Graphics g) {

        if (A.g_boomerang_active() == true) {
            drawSprite(g, A.g_boomerang_x(), A.g_boomerang_y(), A.g_boomerang_size(), A.g_boomerang_size(), Color.GREEN);  
        }
        

    }

    public void DrawMap(Graphics g) {


        int[][] Map_Parts = A.g_Map_Parts();

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

    public void DrawTransitions(Graphics g) {

        if (A.g_Transitioning() == true) {


            drawSprite(g, 0, 0, A.g_Screen_Width(), A.g_Screen_height(), new Color(0, 0, 0, A.g_Transition_Value()));


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