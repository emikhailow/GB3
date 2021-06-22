package homework6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
        assertThrows(RuntimeException.class, () -> Homework6App.extractSubArrayAfterLastOccurrence(arrayList, 4));
    }

    public static Stream<Arguments> dataForTestAsExpectedExtractSubArrayAfterLastOccurrence(){
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(List.of(7, 0, 4, 1, 4, 3, 9, 1, 5, 9), List.of(3, 9, 1, 5, 9)));
        out.add(Arguments.arguments(List.of(6, 8, 4, 9, 0, 1, 1, 1, 6, 6), List.of(9, 0, 1, 1, 1, 6, 6)));
        out.add(Arguments.arguments(List.of(4, 1, 7, 0, 2, 3, 8, 5, 9, 5), List.of(1, 7, 0, 2, 3, 8, 5, 9, 5)));
        out.add(Arguments.arguments(List.of(2, 1, 0, 2, 9, 4, 1, 3, 2, 6), List.of(1, 3, 2, 6)));
        return out.stream();
    }

    @DisplayName("ExtractSubArrayAfterLastOccurrence: as expected")
    @ParameterizedTest
    @MethodSource("dataForTestAsExpectedExtractSubArrayAfterLastOccurrence")
    void testAsExpectedExtractSubArrayAfterLastOccurrence(List<Integer> list, List<Integer> result){
        assertEquals(Homework6App.extractSubArrayAfterLastOccurrence(list, 4), result);
    }

    public static Stream<Arguments> dataForTestAsExpectedCheckArrayForContainingOnlyFollowingNumbers(){
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(List.of(1, 1, 1, 4, 4, 1, 4, 4), true));
        out.add(Arguments.arguments(List.of(1, 1, 1, 1, 1, 1), false));
        out.add(Arguments.arguments(List.of(4, 4, 4, 4), false));
        out.add(Arguments.arguments(List.of(1, 4, 4, 1, 1, 4, 3), false));
        return out.stream();
    }

    @DisplayName("CheckArrayForContainingOnlyFollowingNumbers: as expected")
    @ParameterizedTest
    @MethodSource("dataForTestAsExpectedCheckArrayForContainingOnlyFollowingNumbers")
    void testAsExpectedCheckArrayForContainingOnlyFollowingNumbers(List<Integer> list, Boolean result){
        assertEquals(Homework6App.checkArrayForContainingOnlyFollowingNumbers(list, 1, 4), result);
    }

}
