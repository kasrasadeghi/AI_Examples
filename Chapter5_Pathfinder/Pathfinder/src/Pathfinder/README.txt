GraphEdgeTypes.h: "NavGraphEdge.enum { normal = 0, swim = 1 << 0, ..." normal and wim are both 0 ??
SparseGraph.h:     if (isNodePresent(from) && isNodePresent(from)) doesnot make sense
PriorityQueue.h: comments inside PriorityQLow.ReorderDownwards doesnot make sense
GraphAlgorithms.h: for (const Edge* pE=ConstEdgeItr.beg(); !ConstEdgeItr.end(); pE=ConstEdgeItr.nxt()) beg?? nxt??
GraphAlgorithms.h: double Priority = pE->Cost; // should be double Priority = pE->Cost();
HandyGraphFunctions.h: ShortestPaths[spt[nd]->From][target]= nd; // ->From should be ->From()
HandyGraphFunctions.h: nd = spt[nd]->From; //should be nd = spt[nd]->From();
pathfinder.h:   void MinSpanningTree(); //is declared, but not defined
pathfinder.cpp: if ( (x>m_iCellsX) || (y>(m_iCellsY-1)) ) return; //should be if ( (x>m_iCellsX -1) || (y>(m_iCellsY-1)) ) return;