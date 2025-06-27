package Analizador_Semantico;

import Generacion_Codigo.CodeGenerator;
import Parseo.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LectorNana {
    public String lector(String filePath) {
        try {
            //lee el archivo
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                return data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        LectorNana lectorNana = new LectorNana();

        String input = "C:\\Users\\Ariana\\Documents\\upb\\quinto-semestre\\tercer-modulo\\compilacion\\Compilación-código\\Analizador_Semantico\\input.nana";

        //llama a la clase parser, la cuál implementa todos los analizadores
        System.out.println(lectorNana.lector(input));
        Parser parser = new Parser(lectorNana.lector(input));
        parser.Parseo();

    }
}
