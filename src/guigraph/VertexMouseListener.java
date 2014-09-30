package guigraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

/**
 * This is a MouseListener, which creates vertices at the place, where the user clicks on the given component.
 * At this time the graph is not connected to any game, so the game need not be updated.
 *  
 * @author Irene Thesing
 */
public class VertexMouseListener implements MouseListener {
	
		/**The style to draw vertices is set here, the #mxGraph-packet recommends a style as a String.
		 * The vertices are painted as ellipse with blue color.*/
		private String styleVertex = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE
			+ ";perimeter=ellipsePerimeter;fillcolor=blue"; 
		
		
		/**
		 * The component on which we want to draw vertices.
		 */
		private mxGraphComponent graphComponent;
		
		/**
		 * Creates a new MouseListener, which draws vertices, where is clicked.
		 * 
		 * @param graphComp component, where the vertices shall be.
		 */
		public VertexMouseListener(mxGraphComponent graphComp) {
			graphComponent = graphComp;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			mxGraph graph= graphComponent.getGraph();
			graph.getModel().beginUpdate();
			try
			{
				graph.insertVertex(graph.getDefaultParent(), null, "",
						e.getX()-10,e.getY()-10,30,30,styleVertex);
			}
			finally
			{
				graph.getModel().endUpdate();
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
		}
		
		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

	}
