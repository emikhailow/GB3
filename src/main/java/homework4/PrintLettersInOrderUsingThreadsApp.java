package homework4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrintLettersInOrderUsingThreadsApp {

    private static final int NUMBER_OF_REPETITIONS = 3;

    private final Object locker = new Object();
    private static final String stringToRepeat = "ABC";
    private static volatile char currentLetter;

    public static void main(String[] args) {
        PrintLettersInOrderUsingThreadsApp app = new PrintLettersInOrderUsingThreadsApp();
        ExecutorService executorService = Executors.newFixedThreadPool(stringToRepeat.length());
        currentLetter = stringToRepeat.charAt(0);
        for(int i = 0; i < stringToRepeat.length(); i++){
            int index = i;
            executorService.execute(() -> app.printLetter(stringToRepeat, index));
        }
        executorService.shutdown();
    }

    private void printLetter(String stringToRepeat, int index){
        synchronized (locker){
            for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
                char letterToBePrinted = stringToRepeat.charAt(index);
                while (currentLetter != letterToBePrinted) {
                    try {
                       locker.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(letterToBePrinted);
                currentLetter = index < stringToRepeat.length() - 1 ?
                        stringToRepeat.charAt(index + 1) :
                        stringToRepeat.charAt(0);
                locker.notifyAll();
            }
        }
    }
}
