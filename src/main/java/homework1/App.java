package homework1;

import java.util.*;

public class App {

    private static final int MAX_FRUITS_QUANTITY = 20;
    private static final int MAX_BOXES_QUANTITY = 5;
    public static void main(String[] args) {

        Random random = new Random();

        //Creating two arrays for storing boxes
        List<Box> listBoxesWithOranges = new ArrayList<>();
        List<Box> listBoxesWithApples = new ArrayList<>();

        //Filling the arrays
        System.out.println("Creating boxes with oranges: ");

        for(int i = 0; i < Math.max(random.nextInt(MAX_BOXES_QUANTITY + 1), 2); i++){

            Box<Orange> boxWithOranges = new Box<>();
            for(int j = 0; j < random.nextInt(MAX_FRUITS_QUANTITY + 1); j++){
                boxWithOranges.addFruit(new Orange());
            }
            listBoxesWithOranges.add(boxWithOranges);
            System.out.printf("Weight of box %d is %.1f\n", boxWithOranges.getNumber(), boxWithOranges.getWeight());

        }
        System.out.println();

        System.out.println("Creating boxes with apples: ");

        for(int i = 0; i < Math.max(random.nextInt(MAX_BOXES_QUANTITY + 1), 2); i++){

            Box<Apple> boxWithApples = new Box<>();
            for(int j = 0; j < random.nextInt(MAX_FRUITS_QUANTITY + 1); j++){
                boxWithApples.addFruit(new Apple());
            }
            listBoxesWithApples.add(boxWithApples);
            System.out.printf("Weight of box %d is %.1f\n", boxWithApples.getNumber(), boxWithApples.getWeight());

        }
        System.out.println();

        //Merging two arrays
        List<Box> listBoxes = new ArrayList<>(listBoxesWithOranges);
        listBoxes.addAll(listBoxesWithApples);

        //Comparing the weight by pairs of boxes
        for (Box box1 : listBoxes) {
            for (Box box2 : listBoxes) {
                if(box1.getNumber() < box2.getNumber()){
                    System.out.printf("Box %d weight (%.1f) is%s equal to box %d weight (%.1f)\n",
                            box1.getNumber(), box1.getWeight(), box1.compare(box2) ? "" : " not", box2.getNumber(), box2.getWeight());
                }
            }
        }
        System.out.println();

        //Moving items to another box
        ListIterator<Box> listIterator;

        listIterator = listBoxesWithOranges.listIterator();
        while(listIterator.hasNext()){

            Box currentBox = listIterator.next();
            if(listIterator.hasNext()){
                Box anotherBox = listIterator.next();
                currentBox.moveFruitsTo(anotherBox);
                System.out.printf("Fruits from box %d have been moved to box %d\n", currentBox.getNumber(), anotherBox.getNumber());
                System.out.printf("Current weight of box %d is %.1f, current weight of box %d is %.1f\n",
                        currentBox.getNumber(), currentBox.getWeight(), anotherBox.getNumber(), anotherBox.getWeight());
                listIterator.previous();
            }

        }
        System.out.println();

        listIterator = listBoxesWithApples.listIterator();
        while(listIterator.hasNext()){

            Box currentBox = listIterator.next();
            if(listIterator.hasNext()){
                Box anotherBox = listIterator.next();
                currentBox.moveFruitsTo(anotherBox);
                System.out.printf("Fruits from box %d have been moved to box %d\n", currentBox.getNumber(), anotherBox.getNumber());
                System.out.printf("Current weight of box %d is %.1f, current weight of box %d is %.1f\n",
                        currentBox.getNumber(), currentBox.getWeight(), anotherBox.getNumber(), anotherBox.getWeight());
                listIterator.previous();
            }

        }
    }

}
