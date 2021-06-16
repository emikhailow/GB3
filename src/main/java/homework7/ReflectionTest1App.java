package homework7;

import org.junit.jupiter.api.Test;

public class ReflectionTest1App {

    @BeforeSuite
    public static void beforeSuite1(){
        System.out.println("ReflectionTest1App" + " " + "beforeSuite1");
    }

    @Test
    public static void test1(){
       System.out.println("ReflectionTest1App" + " " + "test1");
    }

    @Test
    @Priority(5)
    public static void test2(){
        System.out.println("ReflectionTest1App" + " " + "test2");
    }

    @AfterSuite
    public static void afterSuite1(){
        System.out.println("ReflectionTest1App" + " " + "afterSuite1");
    }
    
    


}
