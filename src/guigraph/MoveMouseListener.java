package guigraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

/**
 * This is a MouseListener, which moves chosen vertices to a place, where the mouse is released.
 * The vertex is chosen, when the mouse is pressed.
 * When this mouse listener is active, the game is not yet started (or enden by choosing this option)
 * and so no connection to a game must be stored or updated.
 *  
 * @author Irene Thesing
 */
public class MoveMouseListener implements MouseListener,MouseMotionListener {	
		
		/**
		 * Component, where all nodes and edges are on.
		 */
		protected mxGraphComponent graphComponent;
		
		/**The vertex which shall be moved.*/
		private Object vertex = null;
		
		/** The x-value from the original vertex position.*/
		private int xstart = 0;
		/** The y-value from the original vertex position.*/
		private int ystart = 0;

		/**
		 * Creates a new mouse listener, which moves vertices.
		 * By pressing the mouse, a vertex is selected. When releasing the mouse the node is drawn on the new position.
		 * It is already drawn on the move, so while dragging the mouse, the change can be seen.
		 * 
		 * @param graphComp component where everything takes place.
		 */
		public MoveMouseListener(mxGraphComponent graphComp) {
			graphComponent = graphComp;
		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			Object cell = graphComponent.getCellAt(e.getX(), e.getY());
			if (graph.getModel().isVertex(cell)){
				vertex = cell;
				xstart = e.getX();
				ystart = e.getY();
			}
			else vertex  = null;		
		}

		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
		//	Object destinationVertex = graphComp.getCellAt(e.getX(), e.getY());
			if (//!graph.getModel().isVertex(destinationVertex) && 
					vertex != null){
				Object[] cells = {vertex};
				graph.getModel().beginUpdate();
				try
				{
					graph.moveCells(cells,e.getX()-xstart,e.getY()-ystart);
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			}

		public void mouseDragged(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			if (
					vertex != null){
				Object[] cells = {vertex};
				graph.getModel().beginUpdate();
				try
				{
					graph.moveCells(cells,e.getX()-xstart,e.getY()-ystart);
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			if (
					vertex != null){
				Object[] cells = {vertex};
				graph.getModel().beginUpdate();
				try
				{
					graph.moveCells(cells,e.getX()-xstart,e.getY()-ystart);
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
		}

	}
