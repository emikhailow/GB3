package homework6;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Homework6App {

    public static List<Integer> extractSubArrayAfterLastOccurrence(List<Integer> arrayList, int occurence){
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

    public static boolean checkArrayForContainingOnlyFollowingNumbers(List<Integer> list, Integer... ints){
        Map<Integer, Boolean> map = List.of(ints)
                .stream()
                .collect(Collectors.toMap(Function.identity(), x -> false));

        for (Integer integer : list) {
            if(map.get(integer) == null){
                return false;
            }
            map.put(integer, true);
        }
        return map.entrySet().stream().allMatch(Map.Entry::getValue);
    }

    public static void main(String[] args) {
        System.out.println(checkArrayForContainingOnlyFollowingNumbers(List.of(), 1, 4));
    }

}
