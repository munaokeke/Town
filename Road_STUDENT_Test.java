package cmsc204Assign6;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Road_STUDENT_Test {

    private Town town1;
    private Town town2;
    private Town town3;


    @Before
    public void setUp() throws Exception{
        town1 = new Town("Gaithersburg");
        town2 = new Town("Germantown");
        town3 = new Town("Poolesville");

    }

    @After
    public void tearDown() throws Exception{
        town1 = null;
        town2 = null;
    }

    @Test
    public void testRoad(){
        Road theRoad = new Road(town1,town2,3, "355");
        Road theRoad2 = new Road(town1,town3,6,"28");
        assertEquals(town1,theRoad.getSourceTown());
        assertEquals(town2,theRoad.getDestinationTown());
        assertEquals(false,town2.equals(theRoad.getSourceTown()));
        assertEquals(false,town1.equals(theRoad.getDestinationTown()));
        assertEquals("355", theRoad.getName());
          assertEquals(false,100== theRoad.getWeight());
        assertEquals(false, theRoad.hashCode()==theRoad2.hashCode());
        assertEquals(false,theRoad2.contains(town2));

    }


}