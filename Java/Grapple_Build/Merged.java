import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.IOException;

public class Merged {
    public static void main(String[] args) {
        
        Scanner Input = new Scanner(System.in);

        System.out.println("What is the name of the .csv file you want converted?");

        String Map_filepath = "";
        Map_filepath += Input.nextLine();
        Map_filepath += ".csv";


        String Map_Name = "";

        System.out.println("What Should the Created File be called?");

        Map_Name += Input.nextLine() + ".txt";

        System.out.println("What Should the Scale be?");

        Double Scale = Input.nextDouble();

        Input.close();


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

                    Block_x = x_coord * 20;
                    Block_y = i * 20;


                    if (Lines[i].charAt(p) == 'B') {

                        Converted_Info += "" + Block_x + ", " + Block_y + ", 20, 20, " + "4, \n";


                    }

                    if (Lines[i].charAt(p) == 'C') {

                        Converted_Info += "" + Block_x + ", " + Block_y + ", 20, 20, " + "5, \n";

                    }

                    if (Lines[i].charAt(p) == 'D') {

                        Converted_Info += "" + Block_x + ", " + Block_y + ", 20, 20, " + "0, \n";

                    }

                    if (Lines[i].charAt(p) == 'S') {

                        Converted_Info += "" + Block_x + ", " + Block_y + ", 20, 20, " + "1" + Lines[i].charAt(p + 1) + ", \n";

                    }


                }

            }




        }

//4 = Down Transition
//5 = Up Trasition

//S# = Spawn point for Number

//0 Spawn

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


                    Converted_Info += "" + (Block_x) + ", " + Block_y + ", " + (int)(20 * Scale) + ", " + (int)(10 * Scale) + ", 1,\n";

                    Converted_Info += "" + (Block_x) + ", " + (int)(Block_y + (((height * 20) - 10) * Scale)) + ", " + (int)(20 * Scale) + ", " + (int)(10 * Scale) + ", 3,\n";

                    Converted_Info += "" + (int)(Block_x) + ", " + (int)(Block_y + (5 * Scale)) + ", " + (int)((20 * Scale)) + ", " + (int)( ((height * 20) - 15) * Scale)  +", 2,\n"; 



                }

            }


            

        }



        try {
            FileWriter myWriter = new FileWriter(Map_Name);
            myWriter.write(Converted_Info);
            myWriter.close();
        } catch (IOException e) {}





    }
}
