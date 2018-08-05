package gos;

public interface ClassInfo  
{  
    // This is how we can implement a method within an interface,
    // by using the default keyword.
    default public String className() {
        return getClass().getSimpleName();
    }
}  