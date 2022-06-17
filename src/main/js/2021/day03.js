const fs = require("fs")
const input = fs.readFileSync(0).toString()
const data = input.split("\n")

function part1(data, value) {
    return parseInt(data[0].split("").reduce((acc, _, bit) => {
        const moreOnes = data
            .map(s => s[bit])
            .filter(c => c === '1')
            .length >= data.length / 2.0

        return acc + (moreOnes ^ !value ? '1' : '0')
    }, ""), 2)
}

console.log(part1(data, true) * part1(data, false))
