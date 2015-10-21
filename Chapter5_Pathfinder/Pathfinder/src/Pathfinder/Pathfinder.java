/**
 *  Desc:   class enabling users to create simple environments consisting
 *         of different terrain types and then to use various search algorithms
 *          to find paths through them
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package Pathfinder;

import static Pathfinder.constants.*;
import static common.windows.*;
import common.D2.Vector2D;
import static common.D2.Vector2D.*;
import common.Graph.AStarHeuristicPolicies.Heuristic_Euclid;
import common.Graph.GraphAlghorithms.Graph_SearchAStar;
import common.Graph.GraphAlghorithms.Graph_SearchBFS;
import common.Graph.GraphAlghorithms.Graph_SearchDFS;
import common.Graph.GraphAlghorithms.Graph_SearchDijkstra;
import static common.Graph.HandyGraphFunctions.*;
import common.Graph.GraphEdgeTypes.GraphEdge;
import common.Graph.GraphNodeTypes.NavGraphNode;
import common.Graph.SparseGraph;
import common.Time.PrecisionTimer;
import common.misc.Cgdi;
import static common.misc.Cgdi.gdi;
import static common.misc.Stream_Utility_function.ttos;
import static common.misc.utils.MaxDouble;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Pathfinder {

    //size of the main Window (rect.right is width, rect.bottom is height)
    private RECT rect;
    public enum brush_type {

        normal(0),
        obstacle(1),
        water(2),
        mud(3),
        source(4),
        target(5);
        private int id;

        brush_type(int id) {
            this.id = id;
        }

        public static brush_type convert(int value) {
            return brush_type.class.getEnumConstants()[value];
        }

        public int toInteger() {
            return id;
        }
    };

    public enum algorithm_type {

        non,
        search_astar,
        search_bfs,
        search_dfs,
        search_dijkstra
    };
    //the terrain type of each cell
    private ArrayList<Integer> m_TerrainType = new ArrayList<Integer>();
    //this vector will store any path returned from a graph search
    private List<Integer> m_Path = new LinkedList<Integer>();

    //create a typedef for the graph type
    private class NavGraph extends SparseGraph<NavGraphNode<Object>, GraphEdge> {

        public NavGraph(boolean digraph) {
            super(digraph);
        }
    }
    private NavGraph m_pGraph;
    //this vector of edges is used to store any subtree returned from 
    //any of the graph algorithms (such as an SPT)
    private List<GraphEdge> m_SubTree = new ArrayList<GraphEdge>();
    //the total cost of the path from target to source
    private double m_dCostToTarget;
    //the currently selected algorithm
    private algorithm_type m_CurrentAlgorithm;
    //the current terrain brush
    private brush_type m_CurrentTerrainBrush;
    //the dimensions of the cells
    private double m_dCellWidth;
    private double m_dCellHeight;
    //number of cells vertically and horizontally
    private int m_iCellsX,
            m_iCellsY;
    //local record of the client area
    private int m_icxClient,
            m_icyClient;
    //the indices of the source and target cells
    private int m_iSourceCell,
            m_iTargetCell;
    //flags to indicate if the start and finish points have been added
    private boolean m_bStart,
            m_bFinish;
    //should the graph (nodes and GraphEdges) be rendered?
    private boolean m_bShowGraph;
    //should the tile outlines be rendered
    private boolean m_bShowTiles;
    //holds the time taken for the most currently used algorithm to
    //complete
    private double m_dTimeTaken;

    /**
     * this calls the appropriate algorithm
     */
    private void UpdateAlgorithm() {
        //update any current algorithm
        switch (m_CurrentAlgorithm) {
            case non:
                break;

            case search_dfs:
                CreatePathDFS();
                break;

            case search_bfs:
                CreatePathBFS();
                break;

            case search_dijkstra:
                CreatePathDijkstra();
                break;

            case search_astar:
                CreatePathAStar();
                break;

            default:
                break;
        }
    }

    private void UpdateGraphFromBrush(brush_type brush, int CellIndex) {
        UpdateGraphFromBrush(brush.toInteger(), CellIndex);
    }

    /**
     * helper function for PaintTerrain 
     * given a brush and a node index, this method updates the graph appropriately
     * (by removing/adding nodes or changing the costs of the node's edges)
     */
    private void UpdateGraphFromBrush(int brush, int CellIndex) {
        //set the terrain type in the terrain index
        m_TerrainType.set(CellIndex, brush);

        //if current brush is an obstacle then this node must be removed
        //from the graph
        if (brush == 1) {
            m_pGraph.RemoveNode(CellIndex);
        } else {
            //make the node active again if it is currently inactive
            if (!m_pGraph.isNodePresent(CellIndex)) {
                int y = CellIndex / m_iCellsY;
                int x = CellIndex - (y * m_iCellsY);
                m_pGraph.AddNode(new NavGraphNode<Object>(CellIndex, new Vector2D(x * m_dCellWidth + m_dCellWidth / 2.0,
                        y * m_dCellHeight + m_dCellHeight / 2.0)));

                GraphHelper_AddAllNeighboursToGridNode(m_pGraph, y, x, m_iCellsX, m_iCellsY);
            }

            //set the edge costs in the graph
            WeightNavGraphNodeEdges(m_pGraph, CellIndex, GetTerrainCost(brush_type.convert(brush)));
        }
    }

    private String GetNameOfCurrentSearchAlgorithm() {
        switch (m_CurrentAlgorithm) {
            case non:
                return "";
            case search_astar:
                return "A Star";
            case search_bfs:
                return "Breadth First";
            case search_dfs:
                return "Depth First";
            case search_dijkstra:
                return "Dijkstras";
            default:
                return "UNKNOWN!";
        }
    }

    public Pathfinder(RECT rect) {
        this.rect = rect;
        m_bStart = false;
        m_bFinish = false;
        m_bShowGraph = false;
        m_bShowTiles = true;
        m_dCellWidth = 0;
        m_dCellHeight = 0;
        m_iCellsX = 0;
        m_iCellsY = 0;
        m_dTimeTaken = 0.0;
        m_CurrentTerrainBrush = brush_type.normal;
        m_iSourceCell = 0;
        m_iTargetCell = 0;
        m_icxClient = 0;
        m_icyClient = 0;
        m_dCostToTarget = 0.0;
        m_pGraph = null;
    }

    @Override
    protected void finalize() throws Throwable {
        m_pGraph = null;
    }

    public void CreateGraph(int CellsUp, int CellsAcross) {
        m_icxClient = (int) rect.right;
        m_icyClient = (int) rect.bottom - InfoWindowHeight;

        //initialize the terrain vector with normal terrain
        m_TerrainType.clear();
        m_TerrainType.addAll(Collections.nCopies(CellsUp * CellsAcross, brush_type.normal.toInteger()));

        m_iCellsX = CellsAcross;
        m_iCellsY = CellsUp;
        m_dCellWidth = (double) m_icxClient / (double) CellsAcross;
        m_dCellHeight = (double) m_icyClient / (double) CellsUp;

        //delete any old graph
        m_pGraph = null;

        //create the graph
        m_pGraph = new NavGraph(false);//not a digraph

        GraphHelper_CreateGrid(m_pGraph, m_icxClient, m_icyClient, CellsUp, CellsAcross);

        //initialize source and target indexes to mid top and bottom of grid 
        m_iTargetCell = PointToIndex(VectorToPOINTS(new Vector2D(m_icxClient / 2, m_dCellHeight * 2)));
        m_iSourceCell = PointToIndex(VectorToPOINTS(new Vector2D(m_icxClient / 2, m_icyClient - m_dCellHeight * 2)));

        m_Path.clear();
        m_SubTree.clear();

        m_CurrentAlgorithm = algorithm_type.non;
        m_dTimeTaken = 0;
        //System.out.println(m_pGraph.Save(System.out));
    }

    public void Render() {
        gdi.TransparentText();

        //render all the cells
        for (int nd = 0; nd < m_pGraph.NumNodes(); ++nd) {
            int left = (int) (m_pGraph.GetNode(nd).Pos().x - m_dCellWidth / 2.0);
            int top = (int) (m_pGraph.GetNode(nd).Pos().y - m_dCellHeight / 2.0);
            int right = (int) (1 + m_pGraph.GetNode(nd).Pos().x + m_dCellWidth / 2.0);
            int bottom = (int) (1 + m_pGraph.GetNode(nd).Pos().y + m_dCellHeight / 2.0);

            gdi.GreyPen();
            switch (m_TerrainType.get(nd)) {
                case 0:
                    gdi.WhiteBrush();
                    if (!m_bShowTiles) {
                        gdi.WhitePen();
                    }
                    break;

                case 1:
                    gdi.BlackBrush();
                    if (!m_bShowTiles) {
                        gdi.BlackPen();
                    }
                    break;

                case 2:
                    gdi.LightBlueBrush();
                    if (!m_bShowTiles) {
                        gdi.LightBluePen();
                    }
                    break;

                case 3:
                    gdi.BrownBrush();
                    if (!m_bShowTiles) {
                        gdi.BrownPen();
                    }
                    break;

                default:
                    gdi.WhiteBrush();
                    if (!m_bShowTiles) {
                        gdi.WhitePen();
                    }
                    break;

            }//end switch

            if (nd == m_iTargetCell) {
                gdi.RedBrush();
                if (!m_bShowTiles) {
                    gdi.RedPen();
                }
            }

            if (nd == m_iSourceCell) {
                gdi.GreenBrush();
                if (!m_bShowTiles) {
                    gdi.GreenPen();
                }
            }

            gdi.Rect(left, top, right, bottom);

            if (nd == m_iTargetCell) {
                gdi.ThickBlackPen();
                gdi.Cross(new Vector2D(m_pGraph.GetNode(nd).Pos().x, m_pGraph.GetNode(nd).Pos().y),
                        (int) ((m_dCellWidth * 0.6) / 2.0));
            }

            if (nd == m_iSourceCell) {
                gdi.ThickBlackPen();
                gdi.HollowBrush();
                gdi.Rect(left + 5, top + 5, right - 6, bottom - 6);
            }

            //render dots at the corners of the cells
            gdi.DrawDot(left, top, new Color(0, 0, 0));
            gdi.DrawDot(right - 1, top, new Color(0, 0, 0));
            gdi.DrawDot(left, bottom - 1, new Color(0, 0, 0));
            gdi.DrawDot(right - 1, bottom - 1, new Color(0, 0, 0));
        }
        //draw the graph nodes and edges if rqd
        if (m_bShowGraph) {
            GraphHelper_DrawUsingGDI(m_pGraph, Cgdi.light_grey, false);  //false = don't draw node IDs
        }

        //draw any tree retrieved from the algorithms
        gdi.RedPen();

        for (int e = 0; e < m_SubTree.size(); ++e) {
            if (m_SubTree.get(e) != null) {
                Vector2D from = m_pGraph.GetNode(m_SubTree.get(e).From()).Pos();
                Vector2D to = m_pGraph.GetNode(m_SubTree.get(e).To()).Pos();

                gdi.Line(from, to);
            }
        }

        //draw the path (if any)  
        if (m_Path.size() > 0) {
            gdi.ThickBluePen();

            ListIterator<Integer> it = m_Path.listIterator();
            Integer prew = it.next();

            while (it.hasNext()) {
                Integer nxt = it.next();
                gdi.Line(m_pGraph.GetNode(prew).Pos(), m_pGraph.GetNode(nxt).Pos());
                prew = nxt;
            }
        }

        if (m_dTimeTaken != 0.0) {
            //draw time taken to complete algorithm
            String time = ttos(m_dTimeTaken, 8);
            String s = "Time Elapsed for " + GetNameOfCurrentSearchAlgorithm() + " is " + time;
            gdi.TextAtPos(1, m_icyClient + 3, s);
        }

        //display the total path cost if appropriate
        if (m_CurrentAlgorithm == algorithm_type.search_astar || m_CurrentAlgorithm == algorithm_type.search_dijkstra) {
            gdi.TextAtPos(m_icxClient - 110, m_icyClient + 3, "Cost is " + ttos(m_dCostToTarget));
        }
    }

    /**
     * this either changes the terrain at position p to whatever the current
     * terrain brush is set to, or it adjusts the source/target cell
     */
    public void PaintTerrain(POINTS p) {
        //convert p to an index into the graph
        int x = (int) ((double) (p.x) / (double) m_dCellWidth);
        int y = (int) ((double) (p.y) / (double) m_dCellHeight);

        //make sure the values are legal
        if ((x > m_iCellsX - 1) || (y > (m_iCellsY - 1)) || x <  0 || y < 0) {
            return;
        }

        //reset path and tree records
        m_SubTree.clear();
        m_Path.clear();

        //if the current terrain brush is set to either source or target we
        //should change the appropriate node
        if ((m_CurrentTerrainBrush == brush_type.source) || (m_CurrentTerrainBrush == brush_type.target)) {
            switch (m_CurrentTerrainBrush) {
                case source:

                    m_iSourceCell = y * m_iCellsX + x;
                    break;

                case target:

                    m_iTargetCell = y * m_iCellsX + x;
                    break;

            }//end switch
        } //otherwise, change the terrain at the current mouse position
        else {
            UpdateGraphFromBrush(m_CurrentTerrainBrush, y * m_iCellsX + x);
        }

        //update any currently selected algorithm
        UpdateAlgorithm();
    }

    //the algorithms
    /**
     * uses DFS to find a path between the start and target cells.
     * Stores the path as a series of node indexes in m_Path.
     */
    public void CreatePathDFS() {
        //set current algorithm
        m_CurrentAlgorithm = algorithm_type.search_dfs;

        //clear any existing path
        m_Path.clear();
        m_SubTree.clear();

        //create and start a timer
        PrecisionTimer timer = new PrecisionTimer();
        timer.Start();

        //do the search
        Graph_SearchDFS<NavGraph> DFS = new Graph_SearchDFS<NavGraph>(m_pGraph, m_iSourceCell, m_iTargetCell);

        //record the time taken  
        m_dTimeTaken = timer.TimeElapsed();

        //now grab the path (if one has been found)
        if (DFS.Found()) {
            m_Path = DFS.GetPathToTarget();
        }

        m_SubTree = (List<GraphEdge>) DFS.GetSearchTree();

        m_dCostToTarget = 0.0;
    }

    /**
     * uses BFS to find a path between the start and target cells.
     * Stores the path as a series of node indexes in m_Path.
     */
    public void CreatePathBFS() {
        //set current algorithm
        m_CurrentAlgorithm = algorithm_type.search_bfs;

        //clear any existing path
        m_Path.clear();
        m_SubTree.clear();

        //create and start a timer
        PrecisionTimer timer = new PrecisionTimer();
        timer.Start();

        //do the search
        Graph_SearchBFS<NavGraph> BFS = new Graph_SearchBFS<NavGraph>(m_pGraph, m_iSourceCell, m_iTargetCell);

        //record the time taken  
        m_dTimeTaken = timer.TimeElapsed();

        //now grab the path (if one has been found)
        if (BFS.Found()) {
            m_Path = BFS.GetPathToTarget();
        }

        m_SubTree = BFS.GetSearchTree();

        m_dCostToTarget = 0.0;
    }

    /**
     * creates a path from m_iSourceCell to m_iTargetCell using Dijkstra's algorithm
     */
    public void CreatePathDijkstra() {
        //set current algorithm
        m_CurrentAlgorithm = algorithm_type.search_dijkstra;

        //create and start a timer
        PrecisionTimer timer = new PrecisionTimer();
        timer.Start();

        Graph_SearchDijkstra<NavGraph> djk = new Graph_SearchDijkstra<NavGraph>(m_pGraph, m_iSourceCell, m_iTargetCell);

        //record the time taken  
        m_dTimeTaken = timer.TimeElapsed();

        m_Path = djk.GetPathToTarget();

        m_SubTree = djk.GetSPT();

        m_dCostToTarget = djk.GetCostToTarget();
    }

    public void CreatePathAStar() {
        //set current algorithm
        m_CurrentAlgorithm = algorithm_type.search_astar;

        //create and start a timer
        PrecisionTimer timer = new PrecisionTimer();
        timer.Start();

        //create a couple of typedefs so the code will sit comfortably on the page   
        //typedef Graph_SearchAStar<NavGraph, Heuristic_Euclid> AStarSearch;

        //create an instance of the A* search using the Euclidean heuristic
        Graph_SearchAStar<NavGraph, Heuristic_Euclid> AStar =
                new Graph_SearchAStar<NavGraph, Heuristic_Euclid>(m_pGraph, new Heuristic_Euclid(), m_iSourceCell, m_iTargetCell);


        //record the time taken  
        m_dTimeTaken = timer.TimeElapsed();

        m_Path = AStar.GetPathToTarget();

        m_SubTree = AStar.GetSPT();

        m_dCostToTarget = AStar.GetCostToTarget();

    }

    //public void MinSpanningTree();
    //if m_bShowGraph is true the graph will be rendered
    public void ToggleShowGraph() {
        m_bShowGraph = !m_bShowGraph;
    }

    public void SwitchGraphOn() {
        m_bShowGraph = true;
    }

    public void SwitchGraphOff() {
        m_bShowGraph = false;
    }

    public boolean isShowGraphOn() {
        return m_bShowGraph;
    }

    public void ToggleShowTiles() {
        m_bShowTiles = !m_bShowTiles;
    }

    public void SwitchTilesOn() {
        m_bShowTiles = true;
    }

    public void SwitchTilesOff() {
        m_bShowTiles = false;
    }

    public boolean isShowTilesOn() {
        return m_bShowTiles;
    }

    public void ChangeBrush(final brush_type NewBrush) {
        m_CurrentTerrainBrush = NewBrush;
    }

    public void ChangeSource(final int cell) {
        m_iSourceCell = cell;
    }

    public void ChangeTarget(final int cell) {
        m_iTargetCell = cell;
    }

    /**
     * converts a POINTS to an index into the graph. Returns -1 if p
     * is invalid
     */
    public int PointToIndex(POINTS p) {
        //convert p to an index into the graph
        int x = (int) ((double) (p.x) / m_dCellWidth);
        int y = (int) ((double) (p.y) / m_dCellHeight);

        //make sure the values are legal
        if ((x > m_iCellsX) || (y > m_iCellsY)) {
            return -1;
        }

        return y * m_iCellsX + x;
    }

    /**
     * returns the cost of the terrain represented by the current brush type
     */
    public double GetTerrainCost(final brush_type brush) {
        final double cost_normal = 1.0;
        final double cost_water = 2.0;
        final double cost_mud = 1.5;

        switch (brush) {
            case normal:
                return cost_normal;
            case water:
                return cost_water;
            case mud:
                return cost_mud;
            default:
                return MaxDouble;
        }
    }

    public void Save(String FileName) {
        FileOutputStream save;
        try {
            save = new FileOutputStream(FileName);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Pathfinder::Save< bad file >", ex);
        }

        //save the size of the grid
        PrintStream ps = new PrintStream(save);
        ps.println(m_iCellsX);
        ps.println(m_iCellsY);

        //save the terrain
        for (int t = 0; t < m_TerrainType.size(); ++t) {
            if (t == m_iSourceCell) {
                ps.println(brush_type.source.toInteger());
            } else if (t == m_iTargetCell) {
                ps.println(brush_type.target.toInteger());
            } else {
                ps.println(m_TerrainType.get(t));
            }
        }
    }

//-------------------------------- Load ---------------------------------------
//-----------------------------------------------------------------------------
    public void Load(String FileName) {
        InputStream load;
        try {
            load = new FileInputStream(FileName);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Pathfinder::Save< bad file >", ex);
        }
        Scanner buffer = new Scanner(load);
        //load the size of the grid
        m_iCellsX = buffer.nextInt();
        m_iCellsY = buffer.nextInt();

        //create a graph of the correct size
        CreateGraph(m_iCellsY, m_iCellsX);

        int terrain;

        //save the terrain
        for (int t = 0; t < m_iCellsX * m_iCellsY; ++t) {
            terrain = buffer.nextInt();

            if (terrain == brush_type.source.toInteger()) {
                m_iSourceCell = t;
            } else if (terrain == brush_type.target.toInteger()) {
                m_iTargetCell = t;
            } else {
                m_TerrainType.set(t, terrain);
                UpdateGraphFromBrush(terrain, t);
            }
        }
    }
}
