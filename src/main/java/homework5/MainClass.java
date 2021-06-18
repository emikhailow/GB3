package homework5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {

    private static final int CARS_COUNT = 4;
    private volatile static CountDownLatch startLatch = new CountDownLatch(CARS_COUNT);
    private volatile static CountDownLatch finishLatch = new CountDownLatch(CARS_COUNT);
    private volatile static Car winner;
    private static Lock lock = new ReentrantLock();

    public static Car getWinner() {
        return winner;
    }

    public static int getCarsCount() {
        return CARS_COUNT;
    }
    public static void setWinner(Car winner) {
        MainClass.winner = winner;
    }

    public static CountDownLatch getStartLatch() {
        return startLatch;
    }

    public static CountDownLatch getFinishLatch() {
        return finishLatch;
    }

    public static Lock getLock() {
        return lock;
    }

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            startLatch.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            finishLatch.await();
            System.out.println(String.format("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!! Победитель: %s", winner.getName()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}