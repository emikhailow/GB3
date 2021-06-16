package homework6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework6TestApp {

    Random random = new Random();
    int arrayLength = 10;

    @Test
    @DisplayName("ExtractSubArrayAfterLastOccurrence: runtime exception")
    void testExceptionExtractSubArrayAfterLastOccurrence(){
        List<Integer> arrayList = new ArrayList<>();
        for(int j = 0; j < arrayLength; j++){
            arrayList.add(random.nextInt(4));
        }
        assertThrows(RuntimeException.class, () -> Homework6App.ExtractSubArrayAfterLastOccurrence(arrayList, 4));
    }

    @Test
    @DisplayName("ExtractSubArrayAfterLastOccurrence: as expected")
    void testAsExpectedExtractSubArrayAfterLastOccurrence(){
        HashMap<List<Integer>, List<Integer>> hashMap = new HashMap<>();
        hashMap.put(List.of(7, 0, 4, 1, 4, 3, 9, 1, 5, 9), List.of(3, 9, 1, 5, 9));
        hashMap.put(List.of(6, 8, 4, 9, 0, 1, 1, 1, 6, 6), List.of(9, 0, 1, 1, 1, 6, 6));
        hashMap.put(List.of(4, 1, 7, 0, 2, 3, 8, 5, 9, 5), List.of(1, 7, 0, 2, 3, 8, 5, 9, 5));
        hashMap.put(List.of(2, 1, 0, 2, 9, 4, 1, 3, 2, 6), List.of(1, 3, 2, 6));

        for (Map.Entry<List<Integer>, List<Integer>> entry: hashMap.entrySet()){
            assertEquals(Homework6App.ExtractSubArrayAfterLastOccurrence(entry.getKey(), 4), entry.getValue());
        }
    }

    @Test
    @DisplayName("CheckArrayForContainingOnlyFollowingNumbers: as expected")
    void testAsExpectedCheckArrayForContainingOnlyFollowingNumbers(){
        HashMap<List<Integer>, Boolean> hashMap = new HashMap<>();
        hashMap.put(List.of(1, 1, 1, 4, 4, 1, 4, 4), true);
        hashMap.put(List.of(1, 1, 1, 1, 1, 1), true);
        hashMap.put(List.of(4, 4, 4, 4), false);
        hashMap.put(List.of(1, 4, 4, 1, 1, 4, 3), false);

        for (Map.Entry<List<Integer>, Boolean> entry: hashMap.entrySet()){
            assertEquals(Homework6App.CheckArrayForContainingOnlyFollowingNumbers(entry.getKey(), 1, 4), entry.getValue());
        }
    }

}
