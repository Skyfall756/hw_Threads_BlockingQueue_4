import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        Thread fillQueues = new Thread(() -> {
            for(int i = 0; i < 10000; i++ ) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    return;
                }

            }
        });
        fillQueues.start();

        Thread searchA = new Thread(()-> {
            int max;
            try {
                max = findMax(queue1, 'a');
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Максимальная частота \"а\" в строке: " + max);
        });
        searchA.start();

        Thread searchB = new Thread(()-> {
            int max;
            try {
                max = findMax(queue2, 'b');
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Максимальная частота \"b\" в строке: " + max);
        });
        searchB.start();

        Thread searchC = new Thread(()-> {
            int max;
            try {
                max = findMax(queue3, 'c');
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Максимальная частота \"c\" в строке: " + max);
        });
        searchC.start();

        searchA.join();
        searchB.join();
        searchC.join();



    }

    public static int findMax (BlockingQueue<String> queue, char c) throws InterruptedException {
        int count;
        int max = 0;
        String text;
        for (int i = 0; i < 10000; i++) {
            count = 0;
            text = queue.take();
            for (int j = 0; j < text.length(); j++) {
                if (text.charAt(j) == c) count++;
            }
            if (count > max) max = count;

        }
        return max;
    }
}
