package homework7;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionApp {

    public static void start(Class clazz){

        Method[] methods = clazz.getDeclaredMethods();

        List<Method> methodsWithBeforeSuiteAnnotation = Arrays.stream(methods)
                .filter(m -> m.getDeclaredAnnotation(BeforeSuite.class) != null)
                .collect(Collectors.toList());
        if(methodsWithBeforeSuiteAnnotation.size() > 1){
            throw new RuntimeException(String.format("Class %s has more than one method with @BeforeSuite annotation", clazz.getName()));
        }

        if(!methodsWithBeforeSuiteAnnotation.isEmpty()){
            try {
                methodsWithBeforeSuiteAnnotation.get(0).invoke(clazz);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        Arrays.stream(methods)
                .filter(m -> m.getDeclaredAnnotation(Test.class) != null)
                .sorted(Comparator.comparingInt(o -> o.getDeclaredAnnotation(Priority.class) != null ?
                        o.getDeclaredAnnotation(Priority.class).value() :
                        Constants.MAX_PRIORITY
                ))
                .forEach(m -> {
                    try {
                        m.invoke(clazz);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

        List<Method> methodsWithAfterSuiteAnnotation = Arrays.stream(methods)
                .filter(m -> m.getDeclaredAnnotation(AfterSuite.class) != null)
                .collect(Collectors.toList());
        if(methodsWithAfterSuiteAnnotation.size() > 1){
            throw new RuntimeException(String.format("Class %s has more than one method with @AfterSuite annotation", clazz.getName()));
        }
        if(!methodsWithAfterSuiteAnnotation.isEmpty()){
            try {
                methodsWithAfterSuiteAnnotation.get(0).invoke(clazz);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void start(String clazz){
        try {
            start(Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        start(ReflectionTest1App.class);
        start(ReflectionTest2App.class);
    }


}
