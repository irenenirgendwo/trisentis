package test;

import game.Game;
import game.GameGraph;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is used to solve the levels of a game called Crossflip,
 * found at hacker.org/crossflip.
 * It is a sigma+-game where with a move on one node 
 * the node itself and the nodes in the same row or column change their color.
 * It is also possible to have some free fields in the field, which are not part of the game.
 * 
 * @author Irene Thesing
 */
public class Crossflip{
	
	/**
	 * Characterizes the map which maps one character 
	 * in the input string to a number of the node
	 * in the corresponding graph.
	 */
	static HashMap<Integer,Integer> charPosToNode;
	
	/**
	 * Creates a sigma-game and the corresponding GameGraph from a String which encodes the Crossflip-game.
	 * The aim is to turn all lights on.
	 * 
	 * @param input the string where the game is coded. It has a form like "1110,2221",
	 * where a "," marks the beginning of a new row, a 2 marks that the field is not part of the game,
	 * a 1 marks an off-tourned light and a zero an on-turned light. 
	 * @return the game which simulates the Crossflip-game.
	 */
	public static Game makeGame(String input){
		int laenge = input.length();
		int numNodes=0;
		int rowLength = 0;
		int numRows = 1;
		boolean foundKomma = false;
		
		charPosToNode = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> charPosToRow = new HashMap<Integer,Integer>();
		//Knoten zählen und Zeilenlänge bestimmen
		for (int i=0;i< laenge;i++){
			if (input.charAt(i)=='1' || (input.charAt(i)=='0')){
				charPosToNode.put(i,numNodes);
				charPosToRow.put(i,numRows-1);
				numNodes ++;
			}
			if (!(input.charAt(i)==',') && !(foundKomma)) {
				rowLength ++;
			}
			if (input.charAt(i)==','){
				numRows ++;
				foundKomma = true;
			}
		}		
		//Adjazenzlisten
		HashMap<Integer,LinkedList<Integer>> adjList = new HashMap<Integer,LinkedList<Integer>>();
		//Startzustand für das Spiel
		boolean[] startState = new boolean[numNodes];
		
		//Alle Knoten durchgehen und Adjazenzlisten dazu erstellen
		for (int i=0;i< laenge;i++){
			if (!(input.charAt(i)==',' || input.charAt(i)=='2')){
				int actNode = charPosToNode.get(i);
				int row = charPosToRow.get(i);
				int col = i-(row*(rowLength+1));
		
				//benachbarte Knoten
				LinkedList<Integer> neighbournodes = new LinkedList<Integer>();
				
				//benachbarte Knoten suchen, erst nach oben (inklusive eigener Knoten)
				int newrow = row;
				while (newrow >= 0){
					if (input.charAt((newrow*(rowLength+1))+col)=='2')
						break;
					else {
						int neighbour = charPosToNode.get((newrow*(rowLength+1))+col);
						neighbournodes.add(neighbour);
					}
					newrow--;
				}
				//nach unten
				newrow = row+1;
				while (newrow < numRows){
					if (input.charAt((newrow*(rowLength+1))+col)=='2')
						break;
					else {
						int neighbour = charPosToNode.get((newrow*(rowLength+1))+col);
						neighbournodes.add(neighbour);
					}
					newrow++;;
				}
				
				//nach links
				int newcol = col-1;
				while (newcol >= 0){
					if (input.charAt((row*(rowLength+1))+newcol)=='2')
						break;
					else {
						int neighbour = charPosToNode.get((row*(rowLength+1))+newcol);
						neighbournodes.add(neighbour);
					}
					newcol--;
				}
				
				//nach rechts
				newcol = col+1;
				while (newcol < rowLength){
					if (input.charAt((row*(rowLength+1))+newcol)=='2')
						break;
					else {
						int neighbour = charPosToNode.get((row*(rowLength+1))+newcol);
						neighbournodes.add(neighbour);
					}
					newcol++;
				}
				
				//Adjazenzliste reinpacken
				adjList.put(actNode,neighbournodes);
				
				//ein und aus-Zustände in Startzustand speichern
				if (input.charAt(i)=='1')
					startState[actNode]=true;
				else if (input.charAt(i)=='0')
					startState[actNode] = false;
			}
		}
		
		//Adjazenzliste ist erstellt, jetzt der Graph
		GameGraph graph = new GameGraph(numNodes,adjList);	
		Game game = new Game(graph,startState);
		return game;	
	}
	
	
	/**
	 * This method solves the Crossflip game created by the given String
	 * by translating it into a #Game and solvint this game.
	 * 
	 * @param input the coded Crossflip game.
	 * @return the solution of the game, encoded by values 0 and 1 for all the nodes,
	 * 1 if it must be chosen, 0 if it must not be chosen and also 0 if the node is not part of the game
	 * so in the input coded with 2.
	 */
	public static String solveCrossflipp(String input){
		Game game = makeGame(input);
		boolean[] state = game.getState();
		boolean[] solution = game.getOneSolution(state);
		String solCode = "";
		for (int i=0;i<input.length();i++){
			if (input.charAt(i)=='0' || input.charAt(i)=='1'){
				if (solution[charPosToNode.get(i)])
					solCode +="1";
				else solCode +="0";
			} else if (input.charAt(i)=='2')
				solCode +="0";
			
		}
		return solCode;
	}
	
	/**
	 * Solves the Crossflip game, which is given coded as String in the second parameter of args
	 * and writes the coded solution in System.out.
	 * 
	 * @param args The arguments must be of the form --board <codedCrossflipGame> --level <levelnumber>.
	 */
	public static void main(String[] args){
		if (args.length != 4)
			throw new IllegalArgumentException("nicht genug argumente");
		String input = args[1];
		String output = solveCrossflipp(input);
		System.out.print(output);
	}

}
