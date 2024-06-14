import java.util.Map;

public class Game_Engine {


    public Game_Engine(Archive Arc) {
        this.A = Arc;
    }

    Archive A;
    
    public void Update_Transition() {

        if (A.g_Transition_Apex() == false) {
            A.s_Transition_Value(Clock.Timer_ticker(A.g_Transition_Value(), 15, 256));

            

        } else {

            A.s_Transition_Value(Clock.Timer_ticker(A.g_Transition_Value(), -15, 256));

        }

    }

    public void Update_Character() {

        
        A.s_character_x(A.g_character_x() + A.g_Sideways_velocity());
        
        if (A.g_Frictionless() == false) {
            
            if ( (A.g_Sideways_velocity() > 0 && A.g_Friction() < 0) || (A.g_Sideways_velocity() < 0 && A.g_Friction() > 0)) {
            
                if (A.g_Sideways_velocity() <= -A.g_Friction()) {
                    A.s_Sideways_velocity(0);
                } else {
                    A.s_Sideways_velocity(A.g_Sideways_velocity() + A.g_Friction());
                }
                
            } else {
                A.s_Friction(0);
            }


        }
        
        //jumping

        if (A.g_Upwards_velocity() > 20) {
            A.s_Upwards_velocity(20);
        } else if (A.g_Upwards_velocity() < -50) {
            A.s_Upwards_velocity(-50);
        }



        A.s_character_y( A.g_character_y() + A.g_Upwards_velocity());

        if( A.g_Jump_State().equals("Jumping") && A.g_grapple_active() == false) {
            A.s_Upwards_velocity(A.g_Upwards_velocity() + A.g_Gravity());
        } 

    }

    public void Update_Sword() {

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

    public void Update_Grapple() {


        if (grapple_active == true && grapple_state == false) {

            if (Grapple_Collision(Map_Parts, grapple_x, grapple_y, grapple_size) == true) {

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


    }

    public boolean Grapple_Collision(int[][] Map_Parts, int grapple_x, int grapple_y, int grapple_size) {

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

    public void Update_Boomerang() {

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

    public int[] Update_Collision() {
        
        boolean Falling = true;

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

                        grind_mode = true;
                        Frictionless = true;
                        Upwards_velocity = 0;
                        Sideways_velocity = Moving_Direction * grind_speed;
                        character_y = Map_Parts[z][1] - character_height;
                        Jump_State = "Grounded";
                        Jumped = false;
                        grapple_number = grapple_number_max;


                    } else if (Map_Parts[z][5] == 2) {

                        grind_mode = true;
                        Frictionless = true;
                        Upwards_velocity = Moving_Direction * grind_speed;
                        Sideways_velocity = Moving_Direction * grind_speed;
                        character_y = Map_Parts[z][1] - character_height;
                        Jump_State = "Grounded";
                        Jumped = false;
                        grapple_number = grapple_number_max;


                    } else if (Map_Parts[z][5] == 3) {

                        grind_mode = true;
                        Frictionless = true;
                        Upwards_velocity = -1 * Moving_Direction * grind_speed;
                        Sideways_velocity = Moving_Direction * grind_speed;
                        character_y = Map_Parts[z][1] - character_height;
                        Jump_State = "Grounded";
                        Jumped = false;
                        grapple_number = grapple_number_max;


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


    }


    public int[][][] Update_Camera() {
        
        int Cam_velocity_horizontal = 0;
        int Cam_velocity_vertical = 0;


        if (Cam_Detached == false) {

            

            if (character_x >= (Screen_Width/2)) {

                Cam_velocity_horizontal = ((Screen_Width/2) - character_x)/10;

            } else if (character_x <= (Screen_Width/2)) {

                Cam_velocity_horizontal = (Screen_Width/2 - character_x)/10;

            }

            if (character_y >= Screen_height/2) {

                Cam_velocity_vertical = (Screen_height/2 - character_y)/10;

            } else if (character_y <= 290) {

                Cam_velocity_vertical = (Screen_height/2 - character_y)/10;

            }

        }


        Move_Cam_Horizontal(Cam_velocity_horizontal, character_x, Screen_Width, character_width, Cam_Detached, grapple_x, boomerang_x, Map_Parts);

        
        Move_Cam_Vertical(Cam_velocity_vertical, character_y, Screen_height, character_height, Cam_Detached, grapple_y, boomerang_y, Map_Parts);

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

    private void Move_Cam_Horizontal(int amount, int character_x, int Screen_Width, int character_width, boolean Cam_Detached, int grapple_x, int boomerang_x, int[][] Map_Parts) {


        if (character_x >= 0 && character_x <= (Screen_Width - character_width - 12) || Cam_Detached == false) {
            character_x += amount;

            grapple_x += amount;

            boomerang_x += amount;
    
            for (int i = 0; i < Map_Parts.length; i++) {
    
    
                Map_Parts[i][0] += amount;
    
            }

        }


    }

    private void Move_Cam_Vertical(int amount, int character_y, int Screen_height, int character_height, boolean Cam_Detached, int grapple_y, int boomerang_y, int[][] Map_Parts) {


        if (character_y >= 0 && character_y <= (Screen_height - character_height - 40) || Cam_Detached == false) {
            character_y += amount;

            grapple_y += amount;
            
            boomerang_y += amount;
    
    
            for (int i = 0; i < Map_Parts.length; i++) {
    
    
                Map_Parts[i][1] += amount;
    
            }
        }

    }

    public int[][][] Camera_Center() {

        int x_shift = Screen_Width/2 - character_x;
        int y_shift = Screen_height/2 - character_y;


        Move_Cam_Horizontal(x_shift, character_x, Screen_Width, character_width, Cam_Detached, grapple_x, boomerang_x, Map_Parts);

        Move_Cam_Vertical(y_shift, character_y, Screen_height, character_height, Cam_Detached, grapple_y, boomerang_y, Map_Parts);



    }

}