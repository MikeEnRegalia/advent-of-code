export default {}
const input = `1005526
37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,587,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,19,x,x,x,23,x,x,x,x,x,29,x,733,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,17`

function solve(input: string) {
    const waitingTime = parseInt(input.split('\n')[0])
    const departingBuses = input.split('\n')[1].split(',').filter(x => x !== 'x').map(s => parseInt(s))
    const solution = departingBuses.map(bus => [bus, bus - (waitingTime % bus)]).sort((a, b) => a[1] - b[1])[0]
    console.info(solution[0] * solution[1])
}

solve(`939
7,13,x,x,59,x,31,19`)
solve(input)
