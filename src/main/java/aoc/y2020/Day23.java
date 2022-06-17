import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;

import static java.util.stream.Collectors.toCollection;

public class Day23 {

    public static void main(String... args) {

        var input = "925176834";

        var circle = Arrays.stream(input.split(""))
                .map(Integer::parseInt)
                .collect(toCollection(LinkedList::new));
        while (circle.size() < 1_000_000) {
            circle.add(circle.size());
        }

        var start = Instant.now();

        for (var i = 0; i < 10_000_000; ++i) {
            if (i % 100 == 0 && i > 0) {
                var soFar = Duration.between(start, Instant.now());

                System.out.printf("%s     \r",
                        soFar.multipliedBy((10_000_000 - i)/i));
            }
            var pickedUp = new LinkedList<Integer>();
            pickedUp.add(circle.remove(1));
            pickedUp.add(circle.remove(1));
            pickedUp.add(circle.remove(1));

            var destinationCup = circle.get(0) - 1;

            while (pickedUp.contains(destinationCup) || destinationCup < 1) {
                destinationCup = destinationCup <= 1 ? 999_999 : destinationCup - 1;
            }
            System.out.printf("%s ", destinationCup);
            circle.addAll(circle.indexOf(destinationCup) + 1, pickedUp);
            circle.addLast(circle.remove(0));
        }

        var one = circle.indexOf(1);
        var result = new LinkedList<>(circle.subList(one + 1, circle.size()));
        result.addAll(circle.subList(0, one));

        System.out.println(result.get(0) * result.get(1));
    }
}
