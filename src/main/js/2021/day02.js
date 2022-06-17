const fs = require("fs")
const input = fs.readFileSync(0).toString()
const data = input.split("\n").map(s => s.split(" "))

function dive(pos) {
    return data.reduce((acc, [cmd, x]) => { acc[cmd](parseInt(x)); return acc }, pos).calc()
}

function Part1Pos() {
    let horizontal = 0
    let depth = 0

    return {
        "forward": x => horizontal += x,
        "up": x => depth -= x,
        "down": x => depth += x,
        "calc": () => horizontal * depth
    }
}

function Part2Pos() {
    let horizontal = 0
    let depth = 0
    let aim = 0

    return {
        "forward": x => { horizontal += x; depth += aim * x },
        "up": x => aim -= x,
        "down": x => aim += x,
        "calc": () => horizontal * depth
    }
}

console.log(dive(Part1Pos()))
console.log(dive(Part2Pos()))
