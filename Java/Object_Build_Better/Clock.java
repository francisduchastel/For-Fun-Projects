public class Clock {

    public static void Main(String args[]) {




    }

    public static int Timer_ticker(int timer_value, int timer_tick, int Max_timer) {


        timer_value += timer_tick;

        timer_value %= Max_timer;

        return timer_value;

    }

}