package guigraph;

import gui.AnalyseToolBar;
import gui.GameBoard;
import gui.GameToolBar;
import gui.HelpFrame;
import guitrisentis.TriToolBar;
import guitrisentis.TrisentisBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import trisentis.Trisentis;

/**
 * This class describes the ToolBar for drawing graphs 
 * to create the game board and start the game.
 * In this subclass mainly the drawing options are implemented, 
 * they provide the possibility to
 * draw vertices, draw edges, move and delete them. 
 * If the graph should be added by some vertices or edges, 
 * the current game is ended automatically.
 * 
 * @author Irene Thesing
 *
 */
public class GraphToolBar extends GameToolBar{
	
	/**
	 * Id.
	 */
	private static final long serialVersionUID = -4537804866917703920L;

		
		/** 
		 * Creates a new GraphToolBar, where we can draw a new graph and start a game 
		 * as well as some other actions.
		 * 
		 * @param gameboard the board where the game takes place.
		 * 		 */
		public GraphToolBar(GraphBoard gameboard){
			super(gameboard);	
		}

		/* (non-Javadoc)
		 * @see gui.GameToolBar#initOptionPart()
		 */
		@Override
		protected void initOptionPart() {
			add(new JLabel("Malwerkzeuge:"));
			
			ButtonGroup butGroup = new ButtonGroup();
			
			JToggleButton move = new JToggleButton("");
			ImageIcon moveIcon = loadImage("select.gif");//new ImageIcon("bilder/select.gif");
			if (moveIcon != null)
				move.setIcon(moveIcon);
			else move.setText("Verschieben");
			add(move);
			butGroup.add(move);
			
			JToggleButton drawVertex = new JToggleButton("");
			ImageIcon drawIcon = loadImage("ellipse_klein.png");//new ImageIcon("bilder/ellipse_klein.png");
			if (drawIcon != null)
				drawVertex.setIcon(drawIcon);
			else drawVertex.setText("Knoten");
			add(drawVertex);
			butGroup.add(drawVertex);
			
			JToggleButton drawEdge = new JToggleButton("");
			if (loadImage("straight_klein.png")!= null)
				drawEdge.setIcon(loadImage("straight_klein.png"));
			else drawEdge.setText("Kante");
			add(drawEdge);
			butGroup.add(drawEdge);
			
			JToggleButton delete = new JToggleButton("");
			if (loadImage("delete.gif")!= null)
				delete.setIcon(loadImage("delete.gif"));
			else delete.setText("Löschen");
		//	ImageIcon deleteIcon = new ImageIcon("bilder/delete.gif");
			add(delete);
			butGroup.add(delete);
			
			final JToggleButton graphs = new JToggleButton("Spezielle Graphen");
		//	ImageIcon deleteIcon = new ImageIcon("bilder/delete.gif");
			add(graphs);
			butGroup.add(graphs);
			
			//mit Leben füllen
			
			move.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					gameboard.initMode(GraphBoard.MOVE_VERTEX_MODE);
					doAtAllDrawingOp();
				}
			});
			
			drawVertex.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					gameboard.initMode(GraphBoard.DRAW_VERTEX_MODE);
					doAtAllDrawingOp();
				}
			});
			
			drawEdge.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent act) {
					gameboard.initMode(GraphBoard.DRAW_EDGE_MODE);
					doAtAllDrawingOp();
				}
			});
			
			delete.addActionListener(new ActionListener(){
				//@Override
				public void actionPerformed(ActionEvent act) {
					gameboard.initMode(GraphBoard.DELETE_MODE);
					doAtAllDrawingOp();
				}
			});
			
			graphs.addActionListener(new ActionListener(){
				//@Override
				public void actionPerformed(ActionEvent act) {
					doAtAllDrawingOp();
					new SelectionFrame((GraphBoard)gameboard);
					graphs.setSelected(false);
				}
			});
			
			
			
			addSeparator();
		}
		
		/**
		 * Choosing all drawing operations: The analyse toolbar must be hidden and the gameboard again placed
		 * in the center of the main frame.
		 */
		private void doAtAllDrawingOp(){
			owner.removeAnalyse();
			owner.putCentral(gameboard);
		}

		@Override
		protected void additionalInputAction() {
			owner.putCentral(gameboard);
		}

		@Override
		protected AnalyseToolBar additionalStartAction() {
				owner.putCentral(gameboard);
				return new GraphAnalyseToolBar(owner);
		}

		@Override
		protected void changeToOtherGame() {
				Trisentis game = new Trisentis(4,4);
				TrisentisBoard board = new TrisentisBoard(game,GameBoard.SHOW_MODE);
				TriToolBar tools = new TriToolBar(board);
				tools.setOwner(owner);
				owner.putCentral(board);
				owner.setToolBar(tools);
				owner.setTitle("Trisentis spielen - Viel Spaß!");
				owner.repaint();
				owner.setVisible(true);
		}

		@Override
		protected int getSubclassConstant() {
			return HelpFrame.GRAPH;
		}		

		
		/**
		 * Loads an Icon for the buttons from the given path.
		 * 
		 * @param path the path where the Icon can be found.
		 * @return the stored Icon.
		 */
		private static ImageIcon loadImage(String path) {
			ImageIcon ret;
			Class<? extends GraphToolBar> c = GraphToolBar.class;
			ClassLoader cl = c.getClassLoader();
			URL uIcon = cl.getResource(path);
			if (uIcon != null) {
				ret = new ImageIcon(uIcon);
			} else {
				ret = null;
			}
			return ret;
		}
}
