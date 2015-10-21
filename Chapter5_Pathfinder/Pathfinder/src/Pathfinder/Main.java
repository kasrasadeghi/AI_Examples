/**
 * @author Petr (http://www.sallyx.org/)
 */
package Pathfinder;

import javax.swing.JComponent;
import javax.swing.ButtonGroup;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import common.misc.CppToJava;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Point;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import java.awt.Cursor;
import static Pathfinder.constants.*;
import static Pathfinder.resource.*;
import Pathfinder.toolbar.MyMenuBar;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static common.misc.Cgdi.gdi;
import static common.misc.WindowUtils.*;
import static common.windows.*;

public class Main {

//--------------------------------- Globals ------------------------------
//
//------------------------------------------------------------------------
    static final String g_szApplicationName = "PathFinder";
    //final String g_szWindowClassName = "MyWindowClass";
    // because of game resize (g_Pathfinder could be null for a while)
    static Lock PathfinderLock = new ReentrantLock();
    static Pathfinder g_Pathfinder;
    final static Window window = new Window(g_szApplicationName);
    //global toolbar handle
    static JComponent g_hwndToolbar;
    //used to create the back buffer
    static BufferedImage buffer;
    static Graphics2D hdcBackBuffer;
    //these hold the dimensions of the client window area
    static int cxClient;
    static int cyClient;
    //to grab filenames
    static String[] szFileName = new String[MAX_PATH];
    static String[] szTitleName = new String[MAX_PATH];
    static int CurrentSearchButton = 0;

    public static void HandleMenuItems(int ID, MyMenuBar menu) {
        switch (ID) {
            case ID_BUTTON_STOP:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.target);

                break;

            case ID_BUTTON_START:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.source);

                break;

            case ID_BUTTON_OBSTACLE:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.obstacle);

                break;

            case ID_BUTTON_WATER:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.water);

                break;

            case ID_BUTTON_MUD:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.mud);

                break;

            case ID_BUTTON_NORMAL:

                g_Pathfinder.ChangeBrush(Pathfinder.brush_type.normal);

                break;

            case ID_BUTTON_DFS:

                g_Pathfinder.CreatePathDFS();
                CurrentSearchButton = ID_BUTTON_DFS;

                break;

            case ID_BUTTON_BFS:

                g_Pathfinder.CreatePathBFS();
                CurrentSearchButton = ID_BUTTON_BFS;

                break;

            case ID_BUTTON_DIJKSTRA:

                g_Pathfinder.CreatePathDijkstra();
                CurrentSearchButton = ID_BUTTON_DIJKSTRA;

                break;

            case ID_BUTTON_ASTAR:

                g_Pathfinder.CreatePathAStar();
                CurrentSearchButton = ID_BUTTON_ASTAR;

                break;

            case ID_MENU_LOAD:
            
            JFileChooser FileLoadDialog = new JFileChooser();
            FileLoadDialog.setFileFilter(new FileNameExtensionFilter("pathfinder files (*.map)", "map"));
            if(FileLoadDialog.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
                g_Pathfinder.Load(FileLoadDialog.getSelectedFile().toString());
            }
            
            /*
            //uncheck the current search toolbar button
            SendMessage(g_hwndToolbar,
            TB_CHECKBUTTON,
            (WPARAM) CurrentSearchButton,
            (LPARAM) false);
             */
            
            break;

            case ID_MENU_SAVEAS:
            
            JFileChooser FileSaveDialog = new JFileChooser();
            FileSaveDialog.setFileFilter(new FileNameExtensionFilter("pathfinder files (*.map)", "map"));
            if(FileSaveDialog.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
                String szTitleName = FileSaveDialog.getSelectedFile().toString();
                if(!szTitleName.matches("^.*\\.(?i)map$")) {
                    szTitleName = szTitleName+".map";
                }
                g_Pathfinder.Save(szTitleName);
            }
            
            break;

            case ID_MENU_NEW:
            
            //create the graph
            g_Pathfinder.CreateGraph(NumCellsX, NumCellsY);
            
            //uncheck the current search toolbar button
            /*
            SendMessage(g_hwndToolbar,
            TB_CHECKBUTTON,
            (WPARAM) CurrentSearchButton,
            (LPARAM) false);
            */
            break;

            case IDM_VIEW_GRAPH:
                g_Pathfinder.ToggleShowGraph();
                CheckMenuItemAppropriately(menu, IDM_VIEW_GRAPH, g_Pathfinder.isShowGraphOn());
            break;
            
            case IDM_VIEW_TILES:
                g_Pathfinder.ToggleShowTiles();
                CheckMenuItemAppropriately(menu, IDM_VIEW_TILES, g_Pathfinder.isShowTilesOn());
        }//end switch
        window.repaint();
    }

//----------------------------- CreateToolBar ----------------------------
//------------------------------------------------------------------------
    static JComponent CreateToolBar(Window hwndParent) {

        final int NumButtons = 11;

        Box hwndToolBar = Box.createHorizontalBox();
        //hwndToolBar.setLayout(new BoxLayout(hwndToolBar,BoxLayout.X_AXIS));

        //create the buttons
        ButtonGroup group1 = new ButtonGroup();
        Component[] button = new Component[NumButtons];
        button[0] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar1.gif")), ID_BUTTON_STOP);
        button[1] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar2.gif")), ID_BUTTON_START);
        button[2] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar3.gif")), ID_BUTTON_OBSTACLE);
        button[3] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar4.gif")), ID_BUTTON_MUD);
        button[4] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar5.gif")), ID_BUTTON_WATER);
        button[5] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar6.gif")), ID_BUTTON_NORMAL);        
        for(int i = 0; i <= 5; i++)
            group1.add((IcoCheckBox) button[i]);

        //this creates a separater
        button[6] = Box.createHorizontalGlue();
        
        ButtonGroup group2 = new ButtonGroup();
        button[7] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar7.gif")), ID_BUTTON_DFS);
        button[8] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar8.gif")), ID_BUTTON_BFS);
        button[9] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar9.gif")), ID_BUTTON_DIJKSTRA);
        button[10] = new IcoCheckBox((LoadIcon("/Pathfinder/resource/toolbar10.gif")), ID_BUTTON_ASTAR);
        for(int i = 7; i <= 10; i++)
            group1.add((IcoCheckBox) button[i]);
        //add the buttons to the toolbar
        //SendMessage(hwndToolBar, TB_ADDBUTTONS, (WPARAM)NumButtons, (LPARAM)(LPTBBUTTON)&button);
        for (int i = 0; i < NumButtons; i++) {
            hwndToolBar.add(button[i]);
        }
        hwndParent.addButtons(hwndToolBar);

        return hwndToolBar;
    }

    public static void main(String[] args) throws InterruptedException {

        window.setIconImage(LoadIcon("/Pathfinder/icon1.png"));
        window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        buffer = new BufferedImage(ClientWidth, ClientHeight, BufferedImage.TYPE_INT_RGB);
        hdcBackBuffer = buffer.createGraphics();
        //these hold the dimensions of the client window area
        cxClient = buffer.getWidth();
        cyClient = buffer.getHeight();
        //seed random number generator
        common.misc.utils.setSeed(0);

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        //Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        window.setResizable(false);

        toolbar.MyMenuBar menu = toolbar.createMenu(IDR_MENU1);
        window.setJMenuBar(menu);

        //create the toolbar
        g_hwndToolbar = CreateToolBar(window);

        g_Pathfinder = new Pathfinder(new RECT(0, 0, cxClient, cyClient));

        //create the graph
        g_Pathfinder.CreateGraph(NumCellsX, NumCellsY);

        final JPanel panel = new JPanel() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                gdi.StartDrawing(hdcBackBuffer);
                //fill our backbuffer with white
                gdi.fillRect(Color.WHITE, 0, 0, WindowWidth, WindowHeight);
                PathfinderLock.lock();
                g_Pathfinder.Render();
                PathfinderLock.unlock();
                gdi.StopDrawing(hdcBackBuffer);
                g.drawImage(buffer, 0, 0, null);
            }
        };
        panel.setSize(ClientWidth, ClientHeight);
        panel.setPreferredSize(new Dimension(ClientWidth, ClientHeight));
        window.add(panel);
        window.pack();

        int y = center.y - window.getHeight() / 2;
        window.setLocation(center.x - window.getWidth() / 2, y >= 0 ? y : 0);

        window.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                CppToJava.keyCache.released(e);
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case 'g':
                    case 'G':
                        g_Pathfinder.ToggleShowGraph();
                        break;
                    case 't':
                    case 'T':
                        g_Pathfinder.ToggleShowTiles();
                        break;

                }//end switch
                window.repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                CppToJava.keyCache.pressed(e);
            }
        });

        window.addComponentListener(new ComponentAdapter() {

            @Override //has the user resized the client area?
            public void componentResized(ComponentEvent e) {
                //if so we need to update our variables so that any drawing
                //we do using cxClient and cyClient is scaled accordingly
                cxClient = e.getComponent().getBounds().width;
                cyClient = e.getComponent().getBounds().height;
                //now to resize the backbuffer accordingly. 
                buffer = new BufferedImage(cxClient, cyClient, BufferedImage.TYPE_INT_RGB);
                hdcBackBuffer = buffer.createGraphics();
            }
        });

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    PathfinderLock.lock();
                    g_Pathfinder.PaintTerrain(new POINTS(e.getPoint()));
                    window.repaint();
                    PathfinderLock.unlock();
                }
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                PathfinderLock.lock();
                g_Pathfinder.PaintTerrain(new POINTS(e.getPoint()));
                window.repaint();
                PathfinderLock.lock();
            }
        });

        CheckMenuItemAppropriately(menu, IDM_VIEW_TILES, g_Pathfinder.isShowTilesOn());
        CheckMenuItemAppropriately(menu, IDM_VIEW_GRAPH, g_Pathfinder.isShowGraphOn());

        //make the window visible
        window.setVisible(true);
    }
}