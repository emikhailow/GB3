package homework7;

import org.junit.jupiter.api.Test;

public class ReflectionTest1App {

    @BeforeSuite
    public void beforeSuite1(){
        System.out.println("ReflectionTest1App" + " " + "beforeSuite1");
    }

    @homework7.Test(priority = 2)
    public void test1(){
       System.out.println("ReflectionTest1App" + " " + "test1");
    }

    @homework7.Test(priority = 1)
    public void test2(){
        System.out.println("ReflectionTest1App" + " " + "test2");
    }

    @AfterSuite
    public void afterSuite1(){
        System.out.println("ReflectionTest1App" + " " + "afterSuite1");
    }
    
    


}
