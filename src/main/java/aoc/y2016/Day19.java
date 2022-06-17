package aoc.y2016;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Day19 {
    public static void main(String... args) throws IOException {
        part1();
        try {
            part2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void part1() {
        var elves = new int[3001330];
        Arrays.fill(elves, 1);

        var curr = 0;
        while (true) {
            if (elves[curr] != 0) {
                var right = curr;
                while (true) {
                    right = right == elves.length - 1 ? 0 : right + 1;
                    if (right == curr) {
                        System.out.println(curr + 1);
                        return;
                    }
                    var toSteal = elves[right];
                    if (toSteal > 0) {
                        elves[curr] += toSteal;
                        elves[right] = 0;
                        break;
                    }
                }
            }
            curr = (curr + 1) % elves.length;
        }
    }

    // n .. left .. 0 curr 0 .. right .. n
    private static void part2() {
        var left = new LinkedList<Integer>();
        var right = new LinkedList<Integer>();
        for (int i = 0; i < 5 / 2; ++i) {
            left.add(1);
            right.add(1);
        }
        var curr = 1;
        while (true) {
            curr += right.pop();
            left.addFirst(curr);
            if (right.isEmpty()) {
                break;
            }
            curr = right.removeFirst();
            right.add(left.pop());
        }
        System.out.println(curr);
    }
}