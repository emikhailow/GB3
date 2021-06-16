package homework7;

import org.junit.jupiter.api.Test;

public class ReflectionTest2App {

    @BeforeSuite
    public static void beforeSuite1(){
        System.out.println("ReflectionTest2App" + " " + "beforeSuite1");
    }

    @BeforeSuite
    public static void beforeSuite2(){
        System.out.println("ReflectionTest2App" + " " + "beforeSuite2");
    }

    @Test
    @Priority()
    public static void test1(){
        System.out.println("ReflectionTest2App" + " " + "test1");
    }

    @Test
    @Priority(1)
    public static void test2(){
        System.out.println("ReflectionTest2App" + " " + "test2");
    }

    @AfterSuite
    public static void afterSuite1(){
        System.out.println("ReflectionTest2App" + " " + "afterSuite1");
    }




}
