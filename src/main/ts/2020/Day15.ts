export default {}

function play(input: string, turns = 2020) {
    const moves = new Map<number, number>()
    const Prev = (n: number) => ({n, last: moves.get(n)})

    const seeds = input.split(',').map(n => parseInt(n))
    seeds.forEach((move, turn) => moves.set(move, turn))

    let turn = seeds.length - 1
    let prev = Prev(seeds[turn])

    while (turn < turns - 1) {
        const n = prev.last === undefined ? 0 : turn - prev.last

        prev = Prev(n)
        moves.set(n, ++turn)
    }
    return prev.n
}

[`0,20,7,16,1,18,15`].forEach(input => {
    [play(input), play(input, 30000000)].forEach(s => console.info(s))
})