package help;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
 
/**
 * This ToolBar shows a Button when not all elements fit on the ToolBar.
 * After pressing the Button the not fitting Buttons are shown
 * and can be clicked.
 *  
 * Original source code is from:
 * http://www.java-forum.org/codeschnipsel-u-projekte/124394-jtoolbar-passt-groesse.html
 * 
 * @author amunra
 */
public class NewJToolBar extends JToolBar implements ActionListener, FocusListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The additional Button and its features.
     * 
     * @author amunra
     */
    private class NextAction extends JToggleButton {
        /**
		 * Java id.
		 */
		private static final long serialVersionUID = 5239576295574373047L;
		
		/**The frame which is shown by clicking on the button.*/
		private JFrame win;
 
		/**Creates a new Button which does the drop down menue for the not fitting buttons.*/
        public NextAction() {
 
            super();
            final ActionListener verschwinde = new ActionListener() {
                
                //  @Override
                  public void actionPerformed(final ActionEvent e) {
                      open.setVisible(false);
                      open.dispose();
                      NextAction.this.setSelected(false);
                  }

              };
            this.setAction(new Action() {
 
           //     @Override
                public void actionPerformed(final ActionEvent e) {
 
                    if (!NextAction.this.isSelected()) {
                        open.setVisible(false);
                        open.dispose();
                    } else {
                    	
 
                        open = new JFrame();
                        win = open;
                        open.setSize(200, 300);
                        open.setUndecorated(true);
                        Container c = open.getContentPane();
                        /*
                         * Wenn die Toolbar Horizontal angeordnet ist werden die
                         * zusätzlichen Button Vertical angeordnet sonst
                         * horizontal
                         */
                        if (NewJToolBar.this.getOrientation() == JToolBar.HORIZONTAL) {
                            open.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
                        } else {
                            open.setLayout(new BoxLayout(c, BoxLayout.LINE_AXIS));
                        }
                        int width = 0;
                        int height = 0;
                        //für Buttons zusätzlich hinzufügen, dass fenster verschwindet bei klick
                        for (int i = visibleIndex; i < objects.size(); i++) {
                            if (objects.get(i) instanceof JButton) {
                            	boolean exists = false;
                            	for (ActionListener acter : ((JButton)objects.get(i)).getActionListeners())
                            		if (acter == verschwinde)
                            			exists = true;
                            	if (!exists)
                            		((JButton) objects.get(i)).addActionListener(verschwinde);
                                
                            }
                            c.add(objects.get(i));
                            
 
                            if (width < objects.get(i).getPreferredSize().getWidth()) {
                                width = (int) objects.get(i).getPreferredSize().getWidth();
                            }
                            if (height < objects.get(i).getPreferredSize().getHeight()) {
                                height = (int) objects.get(i).getPreferredSize().getHeight();
                            }
                        }
 
                        if (NewJToolBar.this.getOrientation() == JToolBar.HORIZONTAL) {
                            open.setSize((int) (width * 1.3), height
                                    * (objects.size() - visibleIndex));
                            open.setLocation(objects.get(visibleIndex - 1).getLocationOnScreen().x
                                    + objects.get(visibleIndex - 1).getWidth() - open.getWidth(),
                                    objects.get(visibleIndex - 1).getLocationOnScreen().y
                                            + objects.get(visibleIndex - 1).getHeight());
                        } else {
                            open.setSize((width * (objects.size() - visibleIndex)),
                                    (int) (height * 1.3));
                            open.setLocation(objects.get(visibleIndex - 1).getLocationOnScreen().x
                                    + objects.get(visibleIndex - 1).getWidth(),
                                    objects.get(visibleIndex - 1).getLocationOnScreen().y
 
                            );
                        }
                        open.setVisible(true);
                        open.invalidate();
                        open.repaint();
                        open.validate();
                        /*
                         * Berechnung der Position und größe des Fensters
                         */
                        if (NewJToolBar.this.getOrientation() == JToolBar.HORIZONTAL) {
                            open.setSize(width, height * (objects.size() - visibleIndex));
                            open.setLocation(objects.get(visibleIndex - 1).getLocationOnScreen().x
                                    + objects.get(visibleIndex - 1).getWidth() - open.getWidth(),
                                    objects.get(visibleIndex - 1).getLocationOnScreen().y
                                            + objects.get(visibleIndex - 1).getHeight());
                        } else {
                            open.setSize((width * (objects.size() - visibleIndex)), height);
                            open.setLocation(objects.get(visibleIndex - 1).getLocationOnScreen().x
                                    + objects.get(visibleIndex - 1).getWidth(),
                                    objects.get(visibleIndex - 1).getLocationOnScreen().y
 
                            );
                        }
                        open.setFocusable(true);
                        open.addFocusListener(new FocusListener() {
 
                         //   @Override
                            public void focusGained(final FocusEvent e) {
                                // TODO Auto-generated method stub
 
                            }
 
                       //     @Override
                            public void focusLost(final FocusEvent e) {
                             /*   open.setVisible(false);
                                open.dispose();
                                NextAction.this.setSelected(false);
                                System.out.println("focus Lost");*/
                            }
 
                        });
 
                        open.invalidate();
                        open.repaint();
                        open.validate();
 
                    }
                }
 
            //    @Override
                public void addPropertyChangeListener(final PropertyChangeListener listener) {
 
                }
 
             //   @Override
                public Object getValue(final String key) {
                    if (key.equals(Action.NAME)) return ">>";
                    return null;
                }
 
              //  @Override
                public boolean isEnabled() {
                    return true;
                }
 
               // @Override
                public void putValue(final String key, final Object value) {
 
                }
 
              //  @Override
                public void removePropertyChangeListener(final PropertyChangeListener listener) {
 
                }
 
              // @Override
                public void setEnabled(final boolean b) {
 
                }
            });
        }
 
        @Override
        protected void finalize() {
            win.setVisible(false);
            win.dispose();
        }
    }
 
    /**
     * Window for the not fitting Buttons.
     */
    JFrame open = new JFrame();
    /**The Button which is added if the toolbar does not fit.*/
    private NextAction current = null;
 
    /**The objects on the toolbar.*/
    final private ArrayList<Component> objects = new ArrayList<Component>();
 
    int visibleIndex = 0;
    int width = 0;
    int height = 0;
 
    /**
     * Creates a new JToolBar with the additional feature.
     */
    public NewJToolBar() {
        super();
    }
 
    /**
     * Creates a new JToolBar with the additional feature and the given name.
     * 
     * @param name name of the toolbar.
     */
    public NewJToolBar(final String name) {
        super(name);
    }
 
    /**
     * Creates a new JToolBar with the additional feature and the given orientation.
     * 
     * @param horizontal the orientation of the toolbar.
     */
    public NewJToolBar(int horizontal) {
		super(horizontal);
	}

//	@Override
    public void actionPerformed(final ActionEvent e) {
        if (current != null && open.isVisible()) {
            open.setVisible(false);
            open.dispose();
            current.setSelected(false);
        }
 
    }
 
    @Override
    public Component add(final Component comp) {
 
        if (comp instanceof JButton) {
            ((JButton) comp).addActionListener(this);
        //    System.out.println("button action listener hinzugefügt");
        }
        Component b = super.add(comp);
        width += b.getPreferredSize().getWidth();
        objects.add(b);
        return b;
 
    }
 
   // @Override
    public void focusGained(final FocusEvent e) {
 
    }
 
 //   @Override
    public void focusLost(final FocusEvent e) {
        if (open != null && open.isVisible()) {
            this.open.setVisible(false);
            open.dispose();
        }
 
    }
 
    @Override
    public void paint(final Graphics g) {
        super.paint(g);
 
        int currentWidth = (int) this.getSize().getWidth();
        int currentHeight = (int) this.getSize().getHeight();
        int importantValue = this.getOrientation() == JToolBar.HORIZONTAL ? currentWidth
                : currentHeight;
        int check = this.getOrientation() == JToolBar.HORIZONTAL ? width : height;
        if (check == importantValue) return;
        width = currentWidth;
        height = currentHeight;
        removeAll();
        int tempwidth = 0;
        /*
         * Überprüfung ob alle Buttons angezeigt werden können.
         */
        for (int i = 0; i < objects.size(); i++) {
            double checkValue = this.getOrientation() == JToolBar.HORIZONTAL ? objects.get(i)
                    .getX() + objects.get(i).getWidth() * 1.5 : objects.get(i).getY()
                    + objects.get(i).getHeight() * 1.5;
            if (checkValue > importantValue) {
 
                visibleIndex = i;
                if (open != null && open.isVisible()) {
                    open.setVisible(false);
                    open.dispose();
                }
                current = new NextAction();
                super.add(current);
                validate();
                break;
            }
            super.add(objects.get(i));
            tempwidth += objects.get(i).getPreferredSize().getWidth();
 
        }
 
        super.paint(g);
 
    }
}

