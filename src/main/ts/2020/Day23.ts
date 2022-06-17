export default {}

function play(input: string) {

    let circle = input.split('').map(s => parseInt(s))

    for (let i = 0; i < 100; ++i) {
        const pickedUp = circle.splice(1, 3)
        let destinationCup = circle[0] - 1
        while (circle.indexOf(destinationCup) === -1) {
            destinationCup--
            if (destinationCup < 1) destinationCup = 9
        }
        circle.splice(circle.indexOf(destinationCup) + 1, 0, ...pickedUp)
        circle = [...circle.slice(1), circle[0]]
    }

    const one = circle.indexOf(1)
    console.info('x', circle.join(''))
    return [...circle.slice(one+1), ...circle.slice(0, one)].join('')
}

console.info(play('389125467'))
console.info(play('925176834'))