package aoc.y2021;

import static java.lang.System.in;
import static java.util.Arrays.stream;

public class Day01 {
    public static void main(String...args) throws Exception {
        var data = stream(new String(in.readAllBytes()).split("\n"))
                .mapToInt(Integer::parseInt)
                .toArray();

        var part1 = 0;
        for (int i = 1; i < data.length; ++i) {
            if (data[i] > data[i-1]) {
                part1++;
            }
        }

        System.out.println(part1);

        var part2 = 0;
        for (int i = 3; i < data.length; ++i) {
            if (data[i] > data[i - 3]) {
                part2++;
            }
        }

        System.out.println(part2);
    }
}
