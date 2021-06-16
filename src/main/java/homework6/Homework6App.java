package homework6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Homework6App {

    public static ArrayList<Integer> ExtractSubArrayAfterLastOccurrence(List<Integer> arrayList, int occurence){
        int index = arrayList.lastIndexOf(occurence);
        if(index == -1){
            throw new RuntimeException(String.format("No occurrence of %d", occurence));
        } else
        {
            return arrayList.stream()
                    .skip(index + 1)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static boolean CheckArrayForContainingOnlyFollowingNumbers(List<Integer> list, Integer... ints){
       return Arrays.asList(ints).containsAll(list) && list.containsAll(Arrays.asList(ints));
    }

    public static void main(String[] args) {
        System.out.println(CheckArrayForContainingOnlyFollowingNumbers(List.of(1, 2, 1, 2, 1, 3), 1, 2, 3));
    }

}
