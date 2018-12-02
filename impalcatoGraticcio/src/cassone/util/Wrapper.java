package cassone.util;

/**
 * Created by IntelliJ IDEA.
 * User: Andrea
 * Date: 6-apr-2004
 * Time: 23.08.29
 * To change this template use Options | File Templates.
 */
public abstract class Wrapper {

    protected String name;
    protected double value;

    public Wrapper ( String name, double value ) {
        this.name = name;
        this.value = value;
    }

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public double getValue () {
        return value;
    }

    public void setValue ( double value ) {
        this.value = value;
    }
}
