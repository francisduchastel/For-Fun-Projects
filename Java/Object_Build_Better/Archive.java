


public class Archive {


    //character location values
    public static int character_x;
    public static int character_y;

    //character properties
    public static int character_width;
    public static int character_height;
    
    //Screen
    public static int Screen_Width;
    public static int Screen_height;
    
    //physics constants
    public static int Gravity;
    public static int Friction;
    public static Boolean Frictionless;

    //character moving
    public static String Jump_State;
    public static int Upwards_velocity;
    public static int Sideways_velocity;
    public static Boolean Falling;
    public static Boolean Jumped;
    public static int Walk_Timer;
    public static int Moving_Direction;
    public static String Character_Facing_Direction;
    public static String Moving_State;
    public static int Walk_Speed;
    public static Boolean frozen;

    //Grapple properties

    public static boolean grapple_active;
    public static boolean grapple_gotten;
    public static int grapple_x;
    public static int grapple_y;
    public static int grapple_size;
    public static int grapple_speed;
    public static int grapple_number;
    public static int grapple_number_max;
    //false = extending, true = retracting
    public static boolean grapple_state;

    // rang properties
    public static boolean boomerang_gotten;
    public static boolean boomerang_active;
    public static int boomerang_x;
    public static int boomerang_y;
    public static int boomerang_size;
    public static int boomerang_speed_max;
    public static int boomerang_speed;
    public static int boomerang_drag;
    public static int boomerang_drag_directional;
    //false = extending, true = retracting
    public static boolean boomerang_state;


    //sword properties
    //0 = holstered, 1 = 1st ready, 2 = first swing, 3 = 2nd ready, 4 = 2nd swing
    public static int sword_state;
    public static int sword_length;
    public static int sword_width;
    public static int sword_ready_length;
    public static int swing_time;
    public static int ready_time;
    public static int sword_timer;
    public static int swing_displacement_speed;
    public static int swing_displacement_time;
    public static int swing_displacement_timer;
    public static int[][] hit_boxes;


    // grind properties

    public static boolean grind_gotten;
    public static boolean grind_mode;
    public static int grind_speed;


    //camera properties

    public static int Cam_velocity_vertical;
    public static int Cam_velocity_horizontal;
    public static Boolean Cam_Detached;


    public static int Timer_Value;


    //Map Properties

    public static int Map_Destination;

    public static boolean Transitioning;
    public static boolean Transition_Apex;
    public static int Transition_Value;

    public static int Spawn_Value;

    public static int[][] Map_Parts;

    //Menu properties
    public static boolean In_Title;
    public static boolean In_Menu;
    public static int Button_Index_Vertical;
    public static int Button_Index_Horizontal;
    public static int Max_Button_Index_Vertical;
    public static int Max_Button_Index_Horizontal;





    //character location values
    public static int g_character_x() {
        return character_x;
    }
    public static void s_character_x(int x) {
        character_x = x;
    }


    public static int g_character_y() {
        return character_y;
    }
    public static void s_character_y(int y) {
        character_y = y;
    }

    //character properties
    public static int g_character_width() {
        return character_width;
    }
    public static void s_character_width(int w) {
        character_width = w;
    }
    public static int g_character_height() {
        return character_height;
    }
    public static void s_character_height(int h) {
        character_height = h;
    }
    
    //Screen
    public static int g_Screen_Width() {
        return Screen_Width;
    }
    public static void s_Screen_Width(int w) {
        Screen_Width = w;
    }
    public static int g_Screen_height() {
        return Screen_height;
    }
    public static void s_Screen_height(int h) {
        Screen_height = h;
    }

    //physics constants
    public static int g_Gravity() {
        return Gravity;
    }
    public static void s_Gravity(int g) {
        Gravity = g;
    }
    public static int g_Friction() {
        return Friction;
    }
    public static void s_Friction(int f) {
        Friction = f;
    }
    public static Boolean g_Frictionless() {
        return Frictionless;
    }
    public static void s_Frictionless(boolean f) {
        Frictionless = f;
    }

    //character moving
    public static String g_Jump_State() {
        return Jump_State;
    }
    public static void s_Jump_State(String s) {
        Jump_State = s;
    }
    public static int g_Upwards_velocity() {
        return Upwards_velocity;
    }
    public static void s_Upwards_velocity(int v) {
        Upwards_velocity = v;
    }
    public static int g_Sideways_velocity() {
        return Sideways_velocity;
    }
    public static void s_Sideways_velocity(int v) {
        Sideways_velocity = v;
    }
    public static Boolean g_Falling() {
        return Falling;
    }
    public static void s_Falling(boolean f) {
        Falling = f;
    }
    public static Boolean g_Jumped() {
        return Jumped;
    }
    public static void s_Jumped(boolean j) {
        Jumped = j;
    }
    public static int g_Walk_Timer() {
        return Walk_Timer;
    }
    public static void s_Walk_Timer(int t) {
        Walk_Timer = t;
    }
    public static int g_Moving_Direction() {
        return Moving_Direction;
    }
    public static void s_Moving_Direction(int d) {
        Moving_Direction = d;
    }
    public static String g_Character_Facing_Direction() {
        return Character_Facing_Direction;
    }
    public static void s_Character_Facing_Direction(String d) {
        Character_Facing_Direction = d;
    }
    public static String g_Moving_State() {
        return Moving_State;
    }
    public static void s_Moving_State(String s) {
        Moving_State = s;
    }
    public static int g_Walk_Speed() {
        return Walk_Speed;
    }
    public static void s_Walk_Speed(int w) {
        Walk_Speed = w;
    }
    public static Boolean g_frozen() {
        return frozen;
    }
    public static void s_frozen(boolean f) {
        frozen = f;
    }

    //Grapple properties

    public static boolean g_grapple_active() {
        return grapple_active;
    }
    public static void s_grapple_active(boolean g) {
        grapple_active = g;
    }
    public static boolean g_grapple_gotten() {
        return grapple_gotten;
    }
    public static void s_grapple_gotten(boolean g) {
        grapple_gotten = g;
    }
    public static int g_grapple_x() {
        return grapple_x;
    }
    public static void s_grapple_x(int g) {
        grapple_x = g;
    }
    public static int g_grapple_y() {
        return grapple_y;
    }
    public static void s_grapple_y(int g) {
        grapple_y = g;
    }
    public static int g_grapple_size() {
        return grapple_size;
    }
    public static void s_grapple_size(int s) {
        grapple_size = s;
    }
    public static int g_grapple_speed() {
        return grapple_speed;
    }
    public static void s_grapple_speed(int s) {
        grapple_speed = s;
    }
    public static int g_grapple_number() {
        return grapple_number;
    }
    public static void s_grapple_number(int g) {
        grapple_number = g;
    }
    public static int g_grapple_number_max() {
        return grapple_number_max;
    }
    public static void s_grapple_number_max(int g) {
        grapple_number_max = g;
    }
    public static boolean g_grapple_state() {
        return grapple_state;
    }
    public static void s_grapple_state(boolean g) {
        grapple_state = g;
    }

    // rang properties
    public static boolean g_boomerang_gotten() {
        return boomerang_gotten;
    }
    public static void s_boomerang_gotten(boolean g) {
        boomerang_gotten = g;
    }
    public static boolean g_boomerang_active() {
        return boomerang_active;
    }
    public static void s_boomerang_active(boolean b) {
        boomerang_active = b;
    }
    public static int g_boomerang_x() {
        return boomerang_x;
    }
    public static void s_boomerang_x(int b) {
        boomerang_x = b;
    }
    public static int g_boomerang_y() {
        return boomerang_y;
    }
    public static void s_boomerang_y(int b) {
        boomerang_y = b;
    }
    public static int g_boomerang_size() {
        return boomerang_size;
    }
    public static void s_boomerang_size(int s) {
        boomerang_size = s;
    }
    public static int g_boomerang_speed_max() {
        return boomerang_speed_max;
    }
    public static void s_boomerang_speed_max(int s) {
        boomerang_speed_max = s;
    }
    public static int g_boomerang_speed() {
        return boomerang_speed;
    }
    public static void s_boomerang_speed(int b) {
        boomerang_speed = b;
    }
    public static int g_boomerang_drag() {
        return boomerang_drag;
    }
    public static void s_boomerang_drag(int d) {
        boomerang_drag = d;
    }
    public static int g_boomerang_drag_directional() {
        return boomerang_drag_directional;
    }
    public static void s_boomerang_drag_directional(int d) {
        boomerang_drag_directional = d;
    }
    public static boolean g_boomerang_state() {
        return boomerang_state;
    }
    public static void s_boomerang_state(boolean b) {
        boomerang_state = b;
    }


    //sword properties

    public static int g_sword_state() {
        return sword_state;
    }
    public static void s_sword_state(int s) {
        sword_state = s;
    }
    public static int g_sword_length() {
        return sword_length;
    }
    public static void s_sword_length(int l) {
        sword_length = l;
    }
    public static int g_sword_width() {
        return sword_width;
    }
    public static void s_sword_width(int w) {
        sword_width = w;
    }
    public static int g_sword_ready_length() {
        return sword_ready_length;
    }
    public static void s_sword_ready_length(int r) {
        sword_ready_length = r;
    }
    public static int g_swing_time() {
        return swing_time;
    }
    public static void s_swing_time(int s) {
        swing_time = s;
    }
    public static int g_ready_time() {
        return ready_time;
    }
    public static void s_ready_time(int r) {
        ready_time = r;
    }
    public static int g_sword_timer() {
        return sword_timer;
    }
    public static void s_sword_timer(int s) {
        sword_timer = s;
    }
    public static int g_swing_displacement_speed() {
        return swing_displacement_speed;
    }
    public static void s_swing_displacement_speed(int d) {
        swing_displacement_speed = d;
    }
    public static int g_swing_displacement_time() {
        return swing_displacement_time;
    }
    public static void s_swing_displacement_time(int d) {
        swing_displacement_time = d;
    }
    public static int g_swing_displacement_timer() {
        return swing_displacement_timer;
    }
    public static void s_swing_displacement_timer(int s) {
        swing_displacement_timer = s;
    }
    public static int[][] g_hit_boxes() {
        return hit_boxes;
    }
    public static void s_hit_boxes(int[][] h) {
        hit_boxes = h;
    }


    // grind properties

    public static boolean g_grind_gotten() {
        return grind_gotten;
    }
    public static void s_grind_gotten(boolean g) {
        grind_gotten = g;
    }
    public static boolean g_grind_mode() {
        return grind_mode;
    }
    public static void s_grind_mode(boolean m) {
        grind_mode = m;
    }
    public static int g_grind_speed() {
        return grind_speed;
    }
    public static void s_grind_speed(int g) {
        grind_speed = g;
    }

    //camera properties

    public static int g_Cam_velocity_vertical() {
        return Cam_velocity_vertical;
    }
    public static void s_Cam_velocity_vertical(int v) {
        Cam_velocity_vertical = v;
    }
    public static int g_Cam_velocity_horizontal() {
        return Cam_velocity_horizontal;
    }
    public static void s_Cam_velocity_horizontal(int v) {
        Cam_velocity_horizontal = v;
    }
    public static Boolean g_Cam_Detached() {
        return Cam_Detached;
    }
    public static void s_Cam_Detached(boolean d) {
        Cam_Detached = d;
    }


    public static int g_Timer_Value() {
        return Timer_Value;
    }
    public static void s_Timer_Value(int t) {
        Timer_Value = t;
    }


    //Map Properties

    public static int g_Map_Destination() {
        return Map_Destination;
    }
    public static void s_Map_Destination(int d) {
        Map_Destination = d;
    }

    public static boolean g_Transitioning() {
        return Transitioning;
    }
    public static void s_Transitioning(boolean t) {
        Transitioning = t;
    }
    public static boolean g_Transition_Apex() {
        return Transition_Apex;
    }
    public static void s_Transition_Apex(boolean a) {
        Transition_Apex = a;
    }
    public static int g_Transition_Value() {
        return Transition_Value;
    }
    public static void s_Transition_Value(int v) {
        Transition_Value = v;
    }

    public static int g_Spawn_Value() {
        return Spawn_Value;
    }
    public static void s_Spawn_Value(int s) {
        Spawn_Value = s;
    }

    public static int[][] g_Map_Parts() {
        return Map_Parts;
    }
    public static void s_Map_Parts(int[][] m) {
        Map_Parts = m;
    }

    //Menu properties
    public static boolean g_In_Title() {
        return In_Title;
    }
    public static void s_In_Title(boolean t) {
        In_Title = t;
    }
    public static boolean g_In_Menu() {
        return In_Menu;
    }
    public static void s_In_Menu(boolean m) {
        In_Menu = m;
    }
    public static int g_Button_Index_Vertical() {
        return Button_Index_Vertical;
    }
    public static void s_Button_Index_Vertical(int i) {
        Button_Index_Vertical = i;
    }
    public static int g_Button_Index_Horizontal() {
        return Button_Index_Horizontal;
    }
    public static void s_Button_Index_Horizontal(int i) {
        Button_Index_Horizontal = i;
    }
    public static int g_Max_Button_Index_Vertical() {
        return Max_Button_Index_Vertical;
    }
    public static void s_Max_Button_Index_Vertical(int v) {
        Max_Button_Index_Vertical = v;
    }
    public static int g_Max_Button_Index_Horizontal() {
        return Max_Button_Index_Horizontal;
    }
    public static void s_Max_Button_Index_Horizontal(int h) {
        Max_Button_Index_Horizontal = h;
    }






}



