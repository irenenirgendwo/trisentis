package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * This class is only to show the help text for using the program.
 * 
 * @author Irene Thesing
 */
public class HelpFrame extends JFrame {

	/**
	 * Generated id.
	 */
	private static final long serialVersionUID = 7744684220646471300L;
	
	/**
	 * Used when the Trisentis game is shown.
	 */
	public static int TRISENTIS = 42;

	/**
	 * Used when the game on an arbitrary graph is shown.
	 */
	public static int GRAPH = 17;
	
	
	/**
	 * Shows a new Frame where the help text is shown. Which text ist shown depends on 
	 * the type of the game, which is a trisentis game or a sigma game on an arbitrary graph.
	 * 
	 * @param gameType which game is played at the moment, TRISENTIS or GRAPH.
	 */
	public HelpFrame(int gameType){
		super("Hilfe");
		this.setLayout(new GridLayout(3,1));
		JPanel gameStart = new JPanel();
		gameStart.setLayout(new BorderLayout());
		gameStart.add(new JLabel("Spiel starten und spielen"),BorderLayout.NORTH);
		JTextArea starttext = new JTextArea("Ein Spiel starten Sie mit einem Klick auf Spielstart. " +
				"Wählen Sie dann ein Feld an, so " +
				"verändern alle Nachbarfelder die Farbe. " +
				"Gewonnen haben Sie, wenn Sie alle Felder eingefärbt haben.");
		starttext.setLineWrap(true);
		starttext.setWrapStyleWord(true);
		//starttext.setEnabled(false);
		//starttext.setForeground(Color.black);
		gameStart.add(starttext,BorderLayout.CENTER);
		this.add(gameStart);
		
		JPanel gameProp = new JPanel();
		gameProp.setLayout(new BorderLayout());
		gameProp.add(new JLabel("Spieleigenschaften einstellen"),BorderLayout.NORTH);
		if (gameType == TRISENTIS){
			JTextArea proptext = new JTextArea("In der oberen Leiste können sie die Anzahl der Zeilen und Spalten einstellen " +
					"und mit anderer Zeilen- und Spaltenzahl ein neues Spiel starten. " +
					"Mit der Wahl von Diagonalnachbarschaften können Sie einstellen, ob Nachbarschaften " +
					"nur über Kanten oder auch über Ecken gelten sollen. Das Spiel ist einfacher " +
					"ohne Diagonalnachbarschaften.\n" +
					"Mittels dem Feld Eingabe können Sie eine Starteingabe für das Trisentis-Spiel machen. " +
					"Wenn Sie mit dieser Eingabe fertig sind und spielen wollen, klicken Sie auf den Button " +
					"unter dem Spielfeld um das Spiel mit der Eingabe zu starten.\n" +
					"Mit Anwahl der Protokollanzeige wird beim Spielen zeitgleich ein Protokoll der " +
					"gemachten Züge angezeigt.");
			proptext.setLineWrap(true);
			proptext.setWrapStyleWord(true);
	//		proptext.setEnabled(false);
			gameProp.add(proptext,BorderLayout.CENTER);
		} else if (gameType == GRAPH){
			JTextArea proptext = new JTextArea("In der oberen Leiste können Sie mit Hilfe der Malwerkzeuge " +
					"Knoten malen, Kanten zwischen den Knoten ziehen, Knoten verschieben oder Elemente löschen. " +
					"Damit können Sie beliebige Graphen eingeben. Außerdem können über die Schaltfläche " +
					"Spezielle Graphen einige interessante Graphen automatisch erzeugt werden.\n" +
					"Mittels dem Feld Eingabe können Sie eine Starteingabe für das sigma-Spiel machen. " +
					"Wenn Sie mit dieser Eingabe fertig sind und spielen wollen, klicken Sie auf den Button " +
					"unter dem Spielfeld oder den Spielstart-Button um das Spiel mit der Eingabe zu starten.\n" +
					"Mit Anwahl der Protokollanzeige wird beim Spielen zeitgleich ein Protokoll der " +
					"gemachten Züge angezeigt.");
			proptext.setLineWrap(true);
			proptext.setWrapStyleWord(true);
	//		proptext.setEnabled(false);
			gameProp.add(proptext,BorderLayout.CENTER);
		}
		this.add(gameProp);
		
		JPanel analyse = new JPanel();
		analyse.setLayout(new BorderLayout());
		analyse.add(new JLabel("Spieleigenschaften einstellen"),BorderLayout.NORTH);
			JTextArea anatext = new JTextArea("Um das Spiel analysieren zu können, starten Sie bitte zunächst ein Spiel. " +
					"Dann erscheint unterhalb des Spielfelds eine Analyseleiste. Auf dieser können Sie wählen, " +
					"was sie sich anzeigen lassen möchten, beispielsweise eine Basis der Kernelemente. " +
					"In der darauf folgenden Anzeige können Sie Kernelemente auf lineare Unabhängigkeit prüfen, " +
					"addieren und weitere Analysen anstellen lassen.");
			anatext.setLineWrap(true);
			anatext.setWrapStyleWord(true);
		//	anatext.setEnabled(false);
			analyse.add(anatext,BorderLayout.CENTER);
		this.add(analyse);
		
		this.setSize(800,600);
		this.setVisible(true);
	}
	
	
	

}
