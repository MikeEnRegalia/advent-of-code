export default {}

function play(hands: number[][]) {

    const isDone = (hands: number[][]) => hands.filter(h => h.length === 0).length === hands.length-1

    while (!isDone(hands)) {
        const turn = []
        hands.forEach(hand => turn.push(hand.length === 0 ? -1 : hand.splice(0, 1)[0]))
        const sortedTurn = turn.map(a => a).sort((a, b) => b - a)
        const winnerCard = sortedTurn[0]

        const winnerPlayer = turn.indexOf(winnerCard)
        hands[winnerPlayer].push(...sortedTurn)
        //console.info(turn, winnerCard, winnerPlayer)
    }
    return hands.filter(h => h.length > 0)[0]
        .reverse()
        .reduce((acc, card, i) => acc + card * (i + 1), 0)
}


console.info(play([[9, 2, 6, 3, 1], [5, 8, 4, 7, 10]]))
console.info(play(input()))

function input() {
    const parse = (s: string) => s.split('\n').map(n => parseInt(n))

    return [parse(`25
37
35
16
9
26
17
5
47
32
11
43
40
15
7
19
36
20
50
3
21
34
44
18
22`), parse(`12
1
27
41
4
39
13
29
38
2
33
28
10
6
24
31
42
8
23
45
46
48
49
30
14`)]
}