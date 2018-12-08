// O. Bittel;
// 18.10.2011

package Aufgabe3;


import Aufgabe2.*;
import Aufgabe3.sim.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 27.01.2015
 */
public class ShortestPath<V> {

    SYSimulation sim = null;

    Map<V, Double> dist = new HashMap<>(); // Distanz für jeden Knoten
    Map<V, V> pred = new HashMap<>(); // Vorgänger für jeden Knoten
    V start;
    V end;
    DirectedGraph<V> dg = null;
    Heuristic<V> h = null;

    /**
     * Berechnet im Graph g kürzeste Wege nach dem A*-Verfahren.
     * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
     * Wird h = null gewählt, dann ist das Verfahren identisch mit dem Dijkstra-Verfahren.
     *
     * @param g Gerichteter Graph
     * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
     *          dem Dijkstra-Verfahren gesucht.
     */
    public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
        this.dg = g;
        this.h = h;
    }

    /**
     * Diese Methode sollte nur verwendet werden,
     * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
     * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
     * <p>
     * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
     * <p><blockquote><pre>
     *    if (sim != null)
     *       sim.visitStation((Integer) v, Color.blue);
     * </pre></blockquote>
     *
     * @param sim SYSimulation-Objekt.
     */
    public void setSimulator(SYSimulation sim) {
        this.sim = sim;
    }

    /**
     * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
     * <p>
     * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
     * der als nächstes aus der Kandidatenliste besucht wird, animiert.
     *
     * @param s Startknoten
     * @param g Zielknoten
     */
    public void searchShortestPath(V s, V g) {
        start = s;
        end = g;
        for (V v : dg.getVertexSet())
            dist.put(v, Double.POSITIVE_INFINITY);

        LinkedList<V> SettledNodes = new LinkedList<>();
        LinkedList<V> UnSettledNodes = new LinkedList<>();

        UnSettledNodes.add(s);
        dist.put(s, 0.0);

        while (!UnSettledNodes.isEmpty()) {
            V smallestNode = UnSettledNodes.getFirst();
            for (V v : UnSettledNodes) {
                if (dist.get(v) < dist.get(smallestNode))
                    smallestNode = v;
            }
            UnSettledNodes.remove(smallestNode);
            SettledNodes.add(smallestNode);

            for (V succ : dg.getSuccessorVertexSet(smallestNode)) {
                if (!SettledNodes.contains(succ)) {
                    double edgeDistance = dg.getWeight(smallestNode, succ);
                    double newDistance = dist.get(smallestNode) + edgeDistance;
                    if (dist.get(succ) > newDistance) {
                        dist.replace(succ, newDistance);
                        UnSettledNodes.add(succ);
                        pred.put(succ, smallestNode);
                    }
                }
            }

        }
    }


    /**
     * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return kürzester Weg als Liste von Knoten.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public List<V> getShortestPath() {
        LinkedList<V> path = new LinkedList<>();
        V selected = end;
        while (selected != start) {
            path.add(selected);
            selected = pred.get(selected);
        }
        path.add(start);
        LinkedList<V> result = new LinkedList<>();
        while (path.size() > 0) {
            result.add(path.removeLast());
        }
        return result;
    }

    /**
     * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return Länge eines kürzesten Weges.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public double getDistance() {
        // ...
        return dist.get(end);
    }


}