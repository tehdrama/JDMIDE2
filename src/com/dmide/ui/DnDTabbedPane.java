package com.dmide.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class DnDTabbedPane extends JTabbedPane {
	  private static final int LINEWIDTH = 3;
	  private static final String NAME = "test";
	  private final GhostGlassPane glassPane = new GhostGlassPane();
	  private final Rectangle lineRect  = new Rectangle();
	  private final Color   lineColor = new Color(0, 100, 255);
	  private int dragTabIndex = -1;

	  private void clickArrowButton(String actionKey) {
	    ActionMap map = this.getActionMap();
	    if(map != null) {
	      Action action = map.get(actionKey);
	      if (action != null && action.isEnabled()) {
	        action.actionPerformed(new ActionEvent(
	            this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
	      }
	    }
	  }
	  private static Rectangle rBackward = new Rectangle();
	  private static Rectangle rForward  = new Rectangle();
	  private static int rwh = 20;
	  private static int buttonsize = 30;//XXX: magic number of scroll button size
	  private void autoScrollTest(Point glassPt) {
	    Rectangle r = this.getTabAreaBounds();
	    int tabPlacement = this.getTabPlacement();
	    if(tabPlacement==TOP || tabPlacement==BOTTOM) {
	      rBackward.setBounds(r.x, r.y, rwh, r.height);
	      rForward.setBounds(
	          r.x+r.width-rwh-buttonsize, r.y, rwh+buttonsize, r.height);
	    }else if(tabPlacement==LEFT || tabPlacement==RIGHT) {
	      rBackward.setBounds(r.x, r.y, r.width, rwh);
	      rForward.setBounds(
	          r.x, r.y+r.height-rwh-buttonsize, r.width, rwh+buttonsize);
	    }
	    rBackward = SwingUtilities.convertRectangle(
	        this.getParent(), rBackward, this.glassPane);
	    rForward  = SwingUtilities.convertRectangle(
	        this.getParent(), rForward,  this.glassPane);
	    if(rBackward.contains(glassPt)) {
	      //System.out.println(new java.util.Date() + "Backward");
	      this.clickArrowButton("scrollTabsBackwardAction");
	    }else if(rForward.contains(glassPt)) {
	      //System.out.println(new java.util.Date() + "Forward");
	      this.clickArrowButton("scrollTabsForwardAction");
	    }
	  }
	  public DnDTabbedPane() {
	    super();
	    final DragSourceListener dsl = new DragSourceListener() {
	      @Override public void dragEnter(DragSourceDragEvent e) {
	        e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
	      }
	      @Override public void dragExit(DragSourceEvent e) {
	        e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
	        DnDTabbedPane.this.lineRect.setRect(0,0,0,0);
	        DnDTabbedPane.this.glassPane.setPoint(new Point(-1000,-1000));
	        DnDTabbedPane.this.glassPane.repaint();
	      }
	      @Override public void dragOver(DragSourceDragEvent e) {
	        Point glassPt = e.getLocation();
	        SwingUtilities.convertPointFromScreen(glassPt, DnDTabbedPane.this.glassPane);
	        int targetIdx = DnDTabbedPane.this.getTargetTabIndex(glassPt);
	        //if(getTabAreaBounds().contains(tabPt) && targetIdx>=0 &&
	        if(DnDTabbedPane.this.getTabAreaBounds().contains(glassPt) && targetIdx>=0 &&
	           targetIdx!=DnDTabbedPane.this.dragTabIndex && targetIdx!=DnDTabbedPane.this.dragTabIndex+1) {
	          e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
	          DnDTabbedPane.this.glassPane.setCursor(DragSource.DefaultMoveDrop);
	        }else{
	          e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
	          DnDTabbedPane.this.glassPane.setCursor(DragSource.DefaultMoveNoDrop);
	        }
	      }
	      @Override public void dragDropEnd(DragSourceDropEvent e) {
	        DnDTabbedPane.this.lineRect.setRect(0,0,0,0);
	        DnDTabbedPane.this.dragTabIndex = -1;
	        DnDTabbedPane.this.glassPane.setVisible(false);
	        if(DnDTabbedPane.this.hasGhost()) {
	          DnDTabbedPane.this.glassPane.setVisible(false);
	          DnDTabbedPane.this.glassPane.setImage(null);
	        }
	      }
	      @Override public void dropActionChanged(DragSourceDragEvent e) {}
	    };
	    final Transferable t = new Transferable() {
	      private final DataFlavor FLAVOR = new DataFlavor(
	          DataFlavor.javaJVMLocalObjectMimeType, NAME);
	      @Override public Object getTransferData(DataFlavor flavor) {
	        return DnDTabbedPane.this;
	      }
	      @Override public DataFlavor[] getTransferDataFlavors() {
	        DataFlavor[] f = new DataFlavor[1];
	        f[0] = this.FLAVOR;
	        return f;
	      }
	      @Override public boolean isDataFlavorSupported(DataFlavor flavor) {
	        return flavor.getHumanPresentableName().equals(NAME);
	      }
	    };
	    final DragGestureListener dgl = new DragGestureListener() {
	      @Override public void dragGestureRecognized(DragGestureEvent e) {
	        if(DnDTabbedPane.this.getTabCount() <= 1) return;
	        Point tabPt = e.getDragOrigin();
	        DnDTabbedPane.this.dragTabIndex = DnDTabbedPane.this.indexAtLocation(tabPt.x, tabPt.y);
	        //"disabled tab problem".
	        if(DnDTabbedPane.this.dragTabIndex < 0 || !DnDTabbedPane.this.isEnabledAt(DnDTabbedPane.this.dragTabIndex)) return;
	        DnDTabbedPane.this.initGlassPane(e.getComponent(), e.getDragOrigin());
	        try{
	          e.startDrag(DragSource.DefaultMoveDrop, t, dsl);
	        }catch(InvalidDnDOperationException idoe) {
	          idoe.printStackTrace();
	        }
	      }
	    };
	    new DropTarget(this.glassPane, DnDConstants.ACTION_COPY_OR_MOVE,
	                   new CDropTargetListener(), true);
	    new DragSource().createDefaultDragGestureRecognizer(
	          this, DnDConstants.ACTION_COPY_OR_MOVE, dgl);
	  }

	  class CDropTargetListener implements DropTargetListener{
	    @Override public void dragEnter(DropTargetDragEvent e) {
	      if(this.isDragAcceptable(e)) e.acceptDrag(e.getDropAction());
	      else e.rejectDrag();
	    }
	    @Override public void dragExit(DropTargetEvent e) {}
	    @Override public void dropActionChanged(DropTargetDragEvent e) {}

	    private Point _glassPt = new Point();
	    @Override public void dragOver(final DropTargetDragEvent e) {
	      Point glassPt = e.getLocation();
	      if(DnDTabbedPane.this.getTabPlacement()==JTabbedPane.TOP ||
	         DnDTabbedPane.this.getTabPlacement()==JTabbedPane.BOTTOM) {
	        DnDTabbedPane.this.initTargetLeftRightLine(DnDTabbedPane.this.getTargetTabIndex(glassPt));
	      }else{
	        DnDTabbedPane.this.initTargetTopBottomLine(DnDTabbedPane.this.getTargetTabIndex(glassPt));
	      }
	      if(DnDTabbedPane.this.hasGhost()) {
	        DnDTabbedPane.this.glassPane.setPoint(glassPt);
	      }
	      if(!this._glassPt.equals(glassPt)) DnDTabbedPane.this.glassPane.repaint();
	      this._glassPt = glassPt;
	      DnDTabbedPane.this.autoScrollTest(glassPt);
	    }

	    @Override public void drop(DropTargetDropEvent e) {
	      if(this.isDropAcceptable(e)) {
	        DnDTabbedPane.this.convertTab(DnDTabbedPane.this.dragTabIndex, DnDTabbedPane.this.getTargetTabIndex(e.getLocation()));
	        e.dropComplete(true);
	      }else{
	        e.dropComplete(false);
	      }
	      DnDTabbedPane.this.repaint();
	    }
	    private boolean isDragAcceptable(DropTargetDragEvent e) {
	      Transferable t = e.getTransferable();
	      if(t==null) return false;
	      DataFlavor[] f = e.getCurrentDataFlavors();
	      if(t.isDataFlavorSupported(f[0]) && DnDTabbedPane.this.dragTabIndex>=0) {
	        return true;
	      }
	      return false;
	    }
	    private boolean isDropAcceptable(DropTargetDropEvent e) {
	      Transferable t = e.getTransferable();
	      if(t==null) return false;
	      DataFlavor[] f = t.getTransferDataFlavors();
	      if(t.isDataFlavorSupported(f[0]) && DnDTabbedPane.this.dragTabIndex>=0) {
	        return true;
	      }
	      return false;
	    }
	  }

	  private boolean hasGhost = true;
	  public void setPaintGhost(boolean flag) {
	    this.hasGhost = flag;
	  }
	  public boolean hasGhost() {
	    return this.hasGhost;
	  }
	  private boolean isPaintScrollArea = true;
	  public void setPaintScrollArea(boolean flag) {
	    this.isPaintScrollArea = flag;
	  }
	  public boolean isPaintScrollArea() {
	    return this.isPaintScrollArea;
	  }

	  private int getTargetTabIndex(Point glassPt) {
	    Point tabPt = SwingUtilities.convertPoint(
	        this.glassPane, glassPt, DnDTabbedPane.this);
	    boolean isTB = this.getTabPlacement()==JTabbedPane.TOP ||
	                   this.getTabPlacement()==JTabbedPane.BOTTOM;
	    for(int i=0;i < this.getTabCount();i++) {
	      Rectangle r = this.getBoundsAt(i);
	      if(isTB) r.setRect(r.x-r.width/2, r.y,  r.width, r.height);
	      else   r.setRect(r.x, r.y-r.height/2, r.width, r.height);
	      if(r.contains(tabPt)) return i;
	    }
	    Rectangle r = this.getBoundsAt(this.getTabCount()-1);
	    if(isTB) r.setRect(r.x+r.width/2, r.y,  r.width, r.height);
	    else   r.setRect(r.x, r.y+r.height/2, r.width, r.height);
	    return   r.contains(tabPt)?this.getTabCount():-1;
	  }
	  private void convertTab(int prev, int next) {
	    if(next < 0 || prev==next) {
	      return;
	    }
	    Component cmp = this.getComponentAt(prev);
	    Component tab = this.getTabComponentAt(prev);
	    String str  = this.getTitleAt(prev);
	    Icon icon   = this.getIconAt(prev);
	    String tip  = this.getToolTipTextAt(prev);
	    boolean flg   = this.isEnabledAt(prev);
	    int tgtindex  = prev>next ? next : next-1;
	    this.remove(prev);
	    this.insertTab(str, icon, cmp, tip, tgtindex);
	    this.setEnabledAt(tgtindex, flg);
	    //When you drag'n'drop a disabled tab, it finishes enabled and selected.
	    //pointed out by dlorde
	    if(flg) this.setSelectedIndex(tgtindex);

	    //I have a component in all tabs (jlabel with an X to close the tab)
	    //and when i move a tab the component disappear.
	    //pointed out by Daniel Dario Morales Salas
	    this.setTabComponentAt(tgtindex, tab);
	  }

	  private void initTargetLeftRightLine(int next) {
	    if(next < 0 || this.dragTabIndex==next || next-this.dragTabIndex==1) {
	      this.lineRect.setRect(0,0,0,0);
	    }else if(next==0) {
	      Rectangle r = SwingUtilities.convertRectangle(
	          this, this.getBoundsAt(0), this.glassPane);
	      this.lineRect.setRect(r.x-LINEWIDTH/2,r.y,LINEWIDTH,r.height);
	    }else{
	      Rectangle r = SwingUtilities.convertRectangle(
	          this, this.getBoundsAt(next-1), this.glassPane);
	      this.lineRect.setRect(r.x+r.width-LINEWIDTH/2,r.y,LINEWIDTH,r.height);
	    }
	  }
	  private void initTargetTopBottomLine(int next) {
	    if(next < 0 || this.dragTabIndex==next || next-this.dragTabIndex==1) {
	      this.lineRect.setRect(0,0,0,0);
	    }else if(next==0) {
	      Rectangle r = SwingUtilities.convertRectangle(
	          this, this.getBoundsAt(0), this.glassPane);
	      this.lineRect.setRect(r.x,r.y-LINEWIDTH/2,r.width,LINEWIDTH);
	    }else{
	      Rectangle r = SwingUtilities.convertRectangle(
	          this, this.getBoundsAt(next-1), this.glassPane);
	      this.lineRect.setRect(r.x,r.y+r.height-LINEWIDTH/2,r.width,LINEWIDTH);
	    }
	  }

	  private void initGlassPane(Component c, Point tabPt) {
	    this.getRootPane().setGlassPane(this.glassPane);
	    if(this.hasGhost()) {
	      Rectangle rect = this.getBoundsAt(this.dragTabIndex);
	      BufferedImage image = new BufferedImage(
	          c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
	      Graphics g = image.getGraphics();
	      c.paint(g);
	      rect.x = rect.x < 0?0:rect.x;
	      rect.y = rect.y < 0?0:rect.y;
	      image = image.getSubimage(rect.x,rect.y,rect.width,rect.height);
	      this.glassPane.setImage(image);
	    }
	    Point glassPt = SwingUtilities.convertPoint(c, tabPt, this.glassPane);
	    this.glassPane.setPoint(glassPt);
	    this.glassPane.setVisible(true);
	  }

	  private Rectangle getTabAreaBounds() {
	    Rectangle tabbedRect = this.getBounds();
	    //pointed out by daryl. NullPointerException: i.e. addTab("Tab",null)
	    //Rectangle compRect   = getSelectedComponent().getBounds();
	    Component comp = this.getSelectedComponent();
	    int idx = 0;
	    while(comp==null && idx < this.getTabCount()) comp = this.getComponentAt(idx++);
	    Rectangle compRect = (comp==null)?new Rectangle():comp.getBounds();
	    int tabPlacement = this.getTabPlacement();
	    if(tabPlacement==TOP) {
	      tabbedRect.height = tabbedRect.height - compRect.height;
	    }else if(tabPlacement==BOTTOM) {
	      tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
	      tabbedRect.height = tabbedRect.height - compRect.height;
	    }else if(tabPlacement==LEFT) {
	      tabbedRect.width = tabbedRect.width - compRect.width;
	    }else if(tabPlacement==RIGHT) {
	      tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
	      tabbedRect.width = tabbedRect.width - compRect.width;
	    }
	    tabbedRect.grow(2, 2);
	    return tabbedRect;
	  }
	  class GhostGlassPane extends JPanel {
	    private final AlphaComposite composite;
	    private Point location = new Point(0, 0);
	    private BufferedImage draggingGhost = null;
	    public GhostGlassPane() {
	      this.setOpaque(false);
	      this.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	      //http://bugs.sun.com/view_bug.do?bug_id=6700748
	      //setCursor(null);
	    }
	    public void setImage(BufferedImage draggingGhost) {
	      this.draggingGhost = draggingGhost;
	    }
	    public void setPoint(Point location) {
	      this.location = location;
	    }
	    @Override public void paintComponent(Graphics g) {
	      Graphics2D g2 = (Graphics2D) g;
	      g2.setComposite(this.composite);
	      if(DnDTabbedPane.this.isPaintScrollArea() && DnDTabbedPane.this.getTabLayoutPolicy()==SCROLL_TAB_LAYOUT) {
	        g2.setPaint(Color.RED);
	        g2.fill(rBackward);
	        g2.fill(rForward);
	      }
	      if(this.draggingGhost != null) {
	        double xx = this.location.getX() - (this.draggingGhost.getWidth(this) /2d);
	        double yy = this.location.getY() - (this.draggingGhost.getHeight(this)/2d);
	        g2.drawImage(this.draggingGhost, (int)xx, (int)yy , null);
	      }
	      if(DnDTabbedPane.this.dragTabIndex>=0) {
	        g2.setPaint(DnDTabbedPane.this.lineColor);
	        g2.fill(DnDTabbedPane.this.lineRect);
	      }
	    }
	  }
	}