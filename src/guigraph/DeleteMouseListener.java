package guigraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

/**
 * This is a mouse listener which deletes objects (vertices and edges) by clicking on them.
 * The only implemented method is #mouseClicked(MouseEvent e).
 * Then the node or edge is removed from the model.
 * At this time the graph is not connected to any game, so the game need not be updated.
 * 
 * @author Irene Thesing
 */
public class DeleteMouseListener implements MouseListener {
	

		/**
		 * The component, where the work is done and the graph object is placed.
		 */
		protected mxGraphComponent graphComponent;
		

		/**
		 * Creates a new mouse listener, which deletes vertices and edges by clicking on them.
		 * 
		 * @param graphComp component where something shall be deleted.
		 */
		public DeleteMouseListener(mxGraphComponent graphComp) {
			graphComponent = graphComp;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			Object cell = graphComponent.getCellAt(e.getX(), e.getY());
			graph.getModel().beginUpdate();
			try
			{
				if (graph.getModel().isVertex(cell)){
					Object[] cells = {cell};
					Object[] edges = graph.getAllEdges(cells);
					for (Object edge: edges)
						graph.getModel().remove(edge);
					graph.getModel().remove(cell);
					
				} else if (graph.getModel().isEdge(cell))
					graph.getModel().remove(cell);
				
			}
			finally
			{
				graph.getModel().endUpdate();
			}
			
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

	}
