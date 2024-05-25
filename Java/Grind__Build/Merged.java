import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.IOException;

public class Merged {
    public static void main(String[] args) {

        Scanner Input = new Scanner(System.in);

        boolean flag = false;

        boolean done = false;
        String Map_filepath = "";

        while (done == false) {
            

            if (flag == true) {
                Input.nextLine();
            }
            

            System.out.println("What is the name of the .csv file you want converted?");

            Map_filepath = Input.nextLine();
            Map_filepath += ".csv";


            String Map_Name = "";

            System.out.println("What Should the Created File be called?");

            Map_Name += Input.nextLine() + ".txt";

            System.out.println("What Should the Scale be?");

            Double Scale = Input.nextDouble();



            String Map_Data = "";

            String Converted_Info = "";

            int Block_x = 0;
            int Block_y = 0;
            


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

            Boolean[][] Grid = new Boolean[1000][1000];

            for (int a = 0; a < 1000; a++) {
                for (int b = 0; b < 1000; b++) {

                    Grid[a][b] = false;

                }
            }
            
            



            String[] Lines = Map_Data.split("\n");

            for (int i = 0; i < Lines.length; i++) {
                
                


                int x_coord = 0;

                for (int p = 0; p < Lines[i].length(); p++) {

                    if (Lines[i].charAt(p) == ',') {

                        x_coord++;


                    } else {

                        if (Lines[i].charAt(p) == 'A') {

                            Grid[x_coord][i] = true;

                        }

                        Block_x = (int)(x_coord * 20 * Scale);
                        Block_y = (int)(i * 20 * Scale);


                        if (Lines[i].charAt(p) == 'S') {

                            Converted_Info += "" + Block_x + ", " + Block_y + ", " + (int) (20 * Scale) + ", " + (int) (20 * Scale) + ", " + Lines[i].charAt(p + 1) + Lines[i].charAt(p + 2) + ", 0, \n";


                        }

                        if (Lines[i].charAt(p) == 'M') {

                            Converted_Info += "" + Block_x + ", " + Block_y + ", " + (int) (20 * Scale) + ", " + (int) (20 * Scale) + ", 4, " + Lines[i].charAt(p + 1) + Lines[i].charAt(p + 2) + Lines[i].charAt(p + 3) + ", \n";

                        }

                        if (Lines[i].charAt(p) == 'R') {

                            Converted_Info += "" + Block_x + ", " + Block_y + ", " + (int) (20 * Scale) + ", " + (int) (20 * Scale) + ", " + "0, 0, \n";


                        }

                        if (Lines[i].charAt(p) == 'L') {

                            Converted_Info += "" + Block_x + ", " + Block_y + ", " + (int) (20 * Scale) + ", " + (int) (20 * Scale) + ", " + "5, 0, \n";


                        }

                        if (Lines[i].charAt(p) == '-') {

                            Converted_Info += "" + Block_x + ", " + Block_y + ", " + (int) (20 * Scale) + ", " + (int) (5 * Scale) + ", " + "6, 0, \n";
                        }


                    }

                }




            }

    //S10 - 19 = Spawn Flags (determines which entrances connect) (10 - 19)

    //M### = Tranisition trigger, brings to Map### (4, ###)

    //-# = grind rail (6, #) second number tells what type to draw

    //R - Shadow source going right (0)
    //L - Shadow source going left (5)

    //1 = Floor
    //2 = Wall
    //3 = Celing
            

            for (int z = 0; z < 1000; z++) {

                for (int y = 0; y < 1000; y++) {

                    int height = 0;
                    
                    if (Grid[z][y] == true) {

                        

                        height++;

                        while (Grid[z][y + height] == true) {

                            Grid[z][y + height] = false;

                            height++;

                        }


                        Block_x = (int)((z * 20) * Scale);
                        Block_y = (int)((y * 20) * Scale);


                        //creates floor, wall, celing for block
                        Converted_Info += "" + (Block_x) + ", " + Block_y + ", " + (int)(20 * Scale) + ", " + (int)(10 * Scale) + ", 1, 0, \n";

                        Converted_Info += "" + (Block_x) + ", " + (int)(Block_y + (((height * 20) - 10) * Scale)) + ", " + (int)(20 * Scale) + ", " + (int)(10 * Scale) + ", 3, 0, \n";

                        Converted_Info += "" + (Block_x) + ", " + (int)(Block_y + (5 * Scale)) + ", " + (int)((20 * Scale)) + ", " + (int)( ((height * 20) - 15) * Scale)  +", 2, 0, \n"; 



                    }

                }


                

            }



            try {
                FileWriter myWriter = new FileWriter(Map_Name);
                myWriter.write(Converted_Info);
                myWriter.close();
            } catch (IOException e) {}



            System.out.println("Type 1 to make another conversion");


            if (Input.nextInt() != 1) {
                done = true;
            }

            flag = true;
            
        }

        Input.close();


    }
}
