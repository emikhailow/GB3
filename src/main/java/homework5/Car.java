package homework5;

public class Car implements Runnable {

    private static int carsCount;
    private Race race;
    private int speed;
    private String name;

    static {
        carsCount = 0;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        carsCount++;
        this.name = "Участник #" + carsCount;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            MainClass.getStartLatch().countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MainClass.getStartLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(MainClass.getLock().tryLock()){
            MainClass.setWinner(this);
        }
        MainClass.getFinishLatch().countDown();
    }
}