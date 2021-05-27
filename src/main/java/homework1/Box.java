package homework1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Box <T extends Fruit> {

    private List<T> fruitsList;
    private static int currentNumber = 0;
    private int number;

    public Box() {

        this.fruitsList = new ArrayList<>();
        this.number = ++currentNumber;

    }

    public int getNumber() {
        return number;
    }

    public void addFruit(T fruit){

        this.fruitsList.add(fruit);

    }

    public double getWeight(){

        return fruitsList.stream()
                .mapToDouble(Fruit::getWeightPerPiece)
                .sum();

    }

    public boolean compare(Box <? extends Fruit> anotherBox){

        return this.getWeight() == anotherBox.getWeight();

    }

    public void moveFruitsTo(Box <T> anotherBox){

        if(this == anotherBox){
            return;
        }
        for (T fruit : fruitsList) {
            anotherBox.addFruit(fruit);
        }
        fruitsList.clear();

    }

}
