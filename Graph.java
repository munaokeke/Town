package cmsc204Assign6;
/**
 * Programmer : Muna Okeke
*/
import java.util.*;


public class Graph implements GraphInterface<Town, Road> {

    HashSet<Town> towns;
    HashSet<Road> roads;
    HashMap<Town, Integer> distances = new HashMap<>();
    HashMap<Town, Town> nodesBefore = new HashMap<>();


    /**
     * Standard constructor
     */
    public Graph() {
        towns = new HashSet<>();
        roads = new HashSet<>();
    }


    @Override
    public Road getEdge(Town sourceVertex, Town destinationVertex) {

        if (sourceVertex == null || destinationVertex == null) {
            return null;
        }

        for (Road road : roads) {
            if (road.sourceTown.equals(sourceVertex) && road.destinationTown.equals(destinationVertex)) {
                return road;
            } else if (road.sourceTown.equals(destinationVertex) && road.destinationTown.equals(sourceVertex)) {
                return road;
            }
        }

        return null;
    }

    @Override
    public Road addEdge(Town sourceVertex, Town destinationVertex, int weight, String description) {

        if (towns.contains(sourceVertex) == false || towns.contains(destinationVertex) == false) {
            throw new IllegalArgumentException("Towns not in map. Create towns first");
        }

        if (sourceVertex == null || destinationVertex == null) {
            throw new NullPointerException("Towns not given. Input information for towns");
        }

        Road newRoad = new Road(sourceVertex, destinationVertex, weight, description);
        roads.add(newRoad);

        for (Town town: towns) {
            if (town.equals(sourceVertex))
                town.addToAdjacentTowns(destinationVertex);
            else if (town.equals(destinationVertex))
                town.addToAdjacentTowns(sourceVertex);
        }

        return newRoad;
    }

    @Override
    public boolean addVertex(Town town) {
        if (town == null) {
            throw new NullPointerException("Town not given. Input information for town");
        }

        if (towns.contains(town)) {
            return false;
        } else {
            towns.add(town);
            return true;
        }

    }


    @Override
    public boolean containsEdge(Town sourceVertex, Town destinationVertex) {

        if (getEdge(sourceVertex, destinationVertex) == null)
            return false;

        else
            return true;
    }

 
    @Override
    public boolean containsVertex(Town town) {
        return towns.contains(town);
    }


    @Override
    public Set<Road> edgeSet() {
        return roads;
    }


    @Override
    public Set<Road> edgesOf(Town vertex) {

        HashSet<Road> roadsOf = new HashSet<>();

        for (Road road : roads) {
            if (road.contains(vertex))
                roadsOf.add(road);
        }
        return roadsOf;
    }


    @Override
    public Road removeEdge(Town sourceVertex, Town destinationVertex, int weight, String description) {

        if (towns.contains(sourceVertex) == false || towns.contains(destinationVertex) == false) {
            throw new IllegalArgumentException("Towns not in map. Create towns first");
        }

        if (sourceVertex == null || destinationVertex == null) {
            throw new NullPointerException("Towns not given. Input information for towns");
        }

        Road newRoad = new Road(sourceVertex, destinationVertex, weight, description);

        roads.remove(getEdge(sourceVertex, destinationVertex));
        newRoad.sourceTown.removeFromAdjacentTowns(destinationVertex);
        newRoad.destinationTown.removeFromAdjacentTowns(sourceVertex);

        return newRoad;


    }


    @Override
    public boolean removeVertex(Town town) {
        if (town == null) {
            throw new NullPointerException("Town not given. Input information for town");
        }

        if (!(towns.contains(town))) {
            return false;
        } else {
            towns.remove(town);
            return true;
        }
    }


    @Override
    public Set<Town> vertexSet() {
        return towns;
    }


    @Override
    public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex) {



        dijkstraShortestPath(sourceVertex);

        ArrayList<String> arrayList = new ArrayList<>();


        Town node = destinationVertex;



        while (!(arrayList.contains(sourceVertex.name))) {

            Town stepTown = nodesBefore.get(node);

            if (stepTown == null)
                return new ArrayList<>();

            Road stepRoad = getEdge(stepTown, node);

            arrayList.add(stepTown.name + " via " + stepRoad.name + " to " + node.name + " " + stepRoad.distance + " mi");
            node = stepTown;

            if (stepTown.equals(sourceVertex)) {
                break;
            }

        }



        Collections.reverse(arrayList);
        return arrayList;
    }


    @Override
    public void dijkstraShortestPath(Town sourceVertex) {

        HashSet<Town> unsettled = new HashSet<>();
        HashSet<Town> settled = new HashSet<>();


        distances.put(sourceVertex, 0);
        unsettled.add(sourceVertex);

        while (!(unsettled.isEmpty())) {

            Town evaluationNode = getLowestDistanceNode(unsettled);
            unsettled.remove(evaluationNode);
            settled.add(evaluationNode);
            evaluateNeighbors(evaluationNode, distances, nodesBefore, unsettled);
        }

    }

    /**
     * Support method
     * @param unsettledNodes
     * @return
     */
    public Town getLowestDistanceNode(HashSet<Town> unsettledNodes) {

        int shortestDistance = Integer.MAX_VALUE;
        Town shortestTown = null;

        for (Town town : unsettledNodes) {
            if (distances.get(town) < shortestDistance) {
                shortestDistance = distances.get(town);
                shortestTown = town;
            }
        }

        return shortestTown;

    }


    /**
     * Support method
     * @param evaluationNode
     * @param distances
     * @param nodesBefore
     * @param unsettled
     */
    public void evaluateNeighbors(Town evaluationNode, HashMap<Town, Integer> distances, HashMap<Town, Town> nodesBefore, HashSet<Town> unsettled) {

        for (Town town : towns) {
            if (town.equals(evaluationNode)) {
                for (Town town2 : town.getAdjacentTowns()) {
                    for (Road road : roads) {

                        if ((road.contains(evaluationNode)) && road.contains(town2)) {


                            if (distances.containsKey(town2)) {
                                if (distances.get(town2) > (distances.get(evaluationNode) + road.distance)) {
                                    distances.put(town2, distances.get(evaluationNode) + road.distance);
                                    nodesBefore.put(town2, evaluationNode);
                                    unsettled.add(town2);
                                }
                            } else {
                                distances.put(town2, distances.get(evaluationNode) + road.distance);
                                nodesBefore.put(town2, evaluationNode);
                                unsettled.add(town2);
                            }

                        }
                    }
                }
            }
        }
    }


}