/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Charie Mae
 */
public class DFA_MP2 {
  /**
   * @param args the command line arguments
   * @throws java.io.FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    File file = new File("mp2.out");
    file.createNewFile();
    FileWriter fw = new FileWriter(file);
    FileInputStream read_input = new FileInputStream("mp2.in");
    BufferedReader br = new BufferedReader(new InputStreamReader(read_input));
    String instruction;

    while ((instruction = br.readLine()) != null){
     execute(instruction, fw);
    }    
    fw.flush();
    fw.close();
  }
 
  public static boolean validState(ArrayList <Character> east){
    return !((east.contains('R') && east.contains('L') || east.contains('R') && east.contains('C') ||
              east.contains('N') && east.contains('L') || east.contains('N') && east.contains('C')) &&
              east.size() == 2 || east.contains('L') && east.contains('C') && east.contains('R') &&
              east.size() == 3);
  }
  public static void toEast(ArrayList <Character> east, char character){
    /*If character is not a man, then we add will let the man accompany the character
      to cross the river from westbank to eastbank. If character is the man, then only
      the man will cross the riverbank.
    */   
    if(character != 'N'){ 
      east.add('N');
    }
    east.add(character); 
  }
  public static void toWest(ArrayList <Character> east, char character){
    /*Remove the existing character on eastbank when it is going in the other side of the river.
      If character is not found on eastbank (east arrayList), then it is implied that is is on the west.
    */
    if(character != 'N'){
      east.remove(east.indexOf('N'));
    }
    east.remove(east.indexOf(character));
  }
  public static void execute(String instruction, FileWriter fw) throws IOException{
    ArrayList <Character> east = new ArrayList<>();
    
    /*'int c' holds the index of the desired character.
      Even = going to eastbank | Odd = going to westbank
    */
    int c = 0; 

    if(instruction.charAt(0) != 'N' && instruction.charAt(0) != 'L' && instruction.charAt(0) != 'C'){ 
     
      for (;c < instruction.length(); c ++){
        char character = instruction.charAt(c);
        
        
        if (c % 2 == 0){    // if going to eastbank
          /* If character you want to transfer is already at the eastbank, then the instruction is invalid.*/
          if (east.contains(character) == true){
            break;
          }
          toEast(east, character);       
        
        } else{             // else if going to westbank
          /*If character you want to remove from the eastbank is not found, then it means it
            is already at the westbank. Therefore, your instruction is invalid.
          */
          if (east.contains(character) == false){
            break;
          }
          toWest(east,character);
        }
        
        /** break from loop is state is not valid. */
        if(validState(east) == false){
          fw.write("NG ");
          break;
        }
      }
      if(east.isEmpty() || c <instruction.length()){
        fw.write("NG ");
      }
      if(east.size() == 4){
        fw.write("OK ");
        System.out.println("OK");
      }
      
    } else{
      /** instructions that starts with the following are invalid:
        N = man crossing first
        L = lion and man crosses first (invalid because rabbit will eat carrot)
        C = carrot and man crosses first (invalid because lion will eat rabbit)
      */
      fw.write("NG ");
    }
  }    
}