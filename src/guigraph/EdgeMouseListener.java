package guigraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

/**
 * This is a mouse listener which draws an edge between the vertex, 
 * where the mouse is clicked and the one, where it is released.
 * An edge which is from the vertex to itself is not allowed.
 * The needed methods are #mousePressed(MouseEvent e) and #mouseReleased(MouseEvent e).
 * 
 * When this MouseListener is active, the graph is not connected to any game. So the game need not be updated.
 * 
 * 
 * @author Irene Thesing
 */
public class EdgeMouseListener implements MouseListener {
	
		/**
		 * The component, where the vertices and edges are placed.
		 */
		protected mxGraphComponent graphComponent;
		
		/**To save the start vertex of the edge.*/
		Object startVertex = null;

		/**
		 * Creates a new mouse listener which draws edges. If the mouse is pressed,
		 * the vertex which is on this place is remembered and if it the mouse is released,
		 * an edge between both vertices is drawn.
		 * 
		 * @param graphComp component, where the vertices are and the edges shall be.
		 */
		public EdgeMouseListener(mxGraphComponent graphComp) {
			graphComponent = graphComp;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			Object cell = graphComponent.getCellAt(e.getX(), e.getY());
			if (graph.getModel().isVertex(cell))
				startVertex = cell;
			else startVertex  = null;		
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			Object destinationVertex = graphComponent.getCellAt(e.getX(), e.getY());
			if (graph.getModel().isVertex(destinationVertex) && startVertex != null && destinationVertex !=startVertex){
				graph.getModel().beginUpdate();
				try
				{
					graph.insertEdge(graph.getDefaultParent(), null, "",
							startVertex,destinationVertex);
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {	}

	}