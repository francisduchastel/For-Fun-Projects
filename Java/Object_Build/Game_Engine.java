public class Game_Engine {


    public static void Main(String args[]) {

    }


    
    public int Update_Transition(int Transition_Value, boolean Transition_Apex, int[][] Map_Parts, int Map_Destination, int Spawn_Value, boolean Transitioning) {

        if (Transition_Apex == false) {
            Transition_Value = Clock.Timer_ticker(Transition_Value, 15, 256);

            

        } else {

            Transition_Value = Clock.Timer_ticker(Transition_Value, -15, 256);

        }


        return Transition_Value;

    }

    public int[] Update_Character(boolean Walking, int character_x, int Sideways_velocity, int character_y, int Friction, boolean Frictionless, int Upwards_velocity, String Jump_State, boolean grapple_active,
        int Gravity) {
            //character movement
        //sideways movement

        int[] values = new int[4];
        
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


        values[0] = character_x;
        values[1] = character_y;
        values[2] = Sideways_velocity;
        values[3] = Upwards_velocity;

        return values;

    }

    public int[][][] Update_Sword(int sword_state, int[][] hit_boxes, int Moving_Direction, int character_x, int character_y, int character_width, int sword_length, int sword_ready_length, 
        int character_height, int sword_width, int sword_timer, int swing_time, int ready_time, boolean frozen) {


        int[][][] values = new int[4][10][4];
        //sword state
        //sword timer
        //hit_boxes
        //frozen

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
 
        values[0][0][0] = sword_state;
        //sword state
        values[1][0][0] = sword_timer;
        //sword timer
        values[2] = hit_boxes;
        //hit_boxes
        if (frozen == true) {
            values[3][0][0] = 1;
        } else {
            values[3][0][0] = 0;
        }
        //frozen

        return values;

    }

    public int[] Update_Grapple(boolean grapple_active, boolean grapple_state, int grapple_x, int Screen_Width, int grapple_y, int Screen_height, int grapple_number, int grapple_size, 
    int character_x, int character_y, int grapple_speed, int Moving_Direction, int character_height, int character_width, int[][] Map_Parts) {


        int[] values = new int[5];
        //character_x
        //character_y
        //grapple_x
        //grapple_y
        //grapple_state
        //grapple_active

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

        values[0] = character_x;
        values[1] = character_y;
        values[2] = grapple_x;
        values[3] = grapple_y;
        if (grapple_state == true) {
            values[4] = 1;
        } else {
            values[4] = 0;
        }
        if (grapple_active == true) {
            values[5] = 1;
        } else {
            values[5] = 0;
        }


        return values;


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

    public int[] Update_Boomerang(boolean boomerang_active, int boomerang_x, int boomerang_y, int boomerang_speed, int boomerang_drag_directional, int character_x, int character_y, int character_height,
        int character_width, boolean boomerang_state, int boomerang_size, int boomerang_drag) {

        int[] values = new int[5];
        //boomerang_x
        //boomerang_y
        //boomerang_speed
        //boomerang_active
        //boomerang_state
        //boomerang_drag_directional
        

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

        //boomerang_x
        //boomerang_y
        //boomerang_speed
        //boomerang_active
        //boomerang_state
        //boomerang_drag_directional

        values[0] = boomerang_x;
        values[1] = boomerang_y;
        values[2] = boomerang_speed;
        if (boomerang_active == true) {
            values[3] = 1;
        } else {
            values[3] = 0;
        }
        if (boomerang_state == true) {
            values[4] = 1;
        } else {
            values[4] = 0;
        }
        values[5] = boomerang_drag_directional;

        return values;

    }

    public int[] Update_Collision(int[][] Map_Parts, int character_x, int character_y, int character_height, int character_width, boolean grapple_active, int grapple_x, int grapple_y, boolean grind_mode,
        int Sideways_velocity, int Moving_Direction, boolean Frictionless, String Character_Facing_Direction, int Upwards_velocity, String Jump_State, boolean Jumped, int grapple_number,
        int grapple_number_max, int Spawn_Value, boolean Transitioning, boolean grind_gotten, int Map_Destination, int grind_speed) {
        boolean Falling = true;

        int[] values = new int[18];
        //grapple_active
        //grapple_x
        //grapple_y
        //sideways_velocity
        //upwards_velocity
        //character_x
        //character_y
        //frictionless
        //moving_direction
        //character_facing_direction
        //jump_state
        //jumped
        //grapple_number
        //spawn_value
        //map_destination
        //transitioning
        //grind_mode
        //falling


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


        if (grapple_active == true) {
            values[0] = 1;
        } else {
            values[0] = 0;
        }
        //grapple_active
        values[1] = grapple_x;
        //grapple_x
        values[2] = grapple_y;
        //grapple_y
        values[3] = Sideways_velocity;
        //sideways_velocity
        values[4] = Upwards_velocity;
        //upwards_velocity
        values[5] = character_x;
        //character_x
        values[6] = character_y;
        //character_y
        if (Frictionless == true) {
            values[7] = 1;
        } else {
            values[7] = 0;
        }
        //frictionless
        values[8] = Moving_Direction;
        //moving_direction
        if (Character_Facing_Direction.equals("Right")) {
            values[9] = 1;
        } else if (Character_Facing_Direction.equals("Left")) {
            values[9] = 0;
        }
        //character_facing_direction
        if (Jump_State.equals("Jumping")) {
            values[10] = 1;
        } else if (Jump_State.equals("Grounded")) {
            values[10] = 0;
        }
        //jump_state
        if (Jumped == true) {
            values[11] = 1;
        } else {
            values[11] = 0;
        }
        //jumped
        values[12] = grapple_number;
        //grapple_number
        values[13] = Spawn_Value;
        //spawn_value
        values[14] = Map_Destination;
        //map_destination
        if (Transitioning == true) {
            values[15] = 1;
        } else {
            values[15] = 0;
        }
        //transitioning
        if (grind_mode == true) {
            values[16] = 1;
        } else {
            values[16] = 0;
        }
        //grind_mode
        if (Falling == true) {
            values[17] = 1;
        } else {
            values[17] = 0;
        }
        //falling


        return values;

    }

    public void Set_Grind_State(int Up_Movement, int Map_Parts_needed) {

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

    public void Update_Camera() {
        
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

}