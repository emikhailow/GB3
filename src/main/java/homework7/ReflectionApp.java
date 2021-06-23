package homework7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionApp {

    public static void start(Class clazz){

        Method[] methods = clazz.getDeclaredMethods();
        Object instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
        Object instanceEF = instance;

        List<Method> methodsWithBeforeSuiteAnnotation = Arrays.stream(methods)
                .filter(m -> m.getDeclaredAnnotation(BeforeSuite.class) != null)
                .collect(Collectors.toList());
        if(methodsWithBeforeSuiteAnnotation.size() > 1){
            throw new RuntimeException(String.format("Class %s has more than one method with @BeforeSuite annotation", clazz.getName()));
        }

        if(!methodsWithBeforeSuiteAnnotation.isEmpty()){
            try {
                methodsWithBeforeSuiteAnnotation.get(0).invoke(instanceEF);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        Arrays.stream(methods)
                .filter(m -> m.getDeclaredAnnotation(homework7.Test.class) != null)
                .sorted(Comparator.comparingInt(o -> o.getDeclaredAnnotation(homework7.Test.class).priority()))
                .forEach(m -> {
                    try {
                        m.invoke(instanceEF);
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
                methodsWithAfterSuiteAnnotation.get(0).invoke(instanceEF);
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
