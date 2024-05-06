
abstract class AbstractShape {
    private static int numOfInstances;

    public static int getNumOfInstances(){
        return numOfInstances;
    }
    public AbstractShape() {
        numOfInstances++; 
    }

}
