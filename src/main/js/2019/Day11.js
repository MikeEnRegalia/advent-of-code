function createProgram(s) {
    const code = s || `3,8,1005,8,336,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,101,0,8,28,1006,0,36,1,2,5,10,1006,0,57,1006,0,68,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,63,2,6,20,10,1,106,7,10,2,9,0,10,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,97,1006,0,71,3,8,1002,8,-1,10,101,1,10,10,4,10,108,1,8,10,4,10,1002,8,1,122,2,105,20,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,148,2,1101,12,10,1006,0,65,2,1001,19,10,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,101,0,8,181,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,0,10,4,10,1002,8,1,204,2,7,14,10,2,1005,20,10,1006,0,19,3,8,102,-1,8,10,101,1,10,10,4,10,108,1,8,10,4,10,102,1,8,236,1006,0,76,1006,0,28,1,1003,10,10,1006,0,72,3,8,1002,8,-1,10,101,1,10,10,4,10,108,0,8,10,4,10,102,1,8,271,1006,0,70,2,107,20,10,1006,0,81,3,8,1002,8,-1,10,1001,10,1,10,4,10,108,1,8,10,4,10,1002,8,1,303,2,3,11,10,2,9,1,10,2,1107,1,10,101,1,9,9,1007,9,913,10,1005,10,15,99,109,658,104,0,104,1,21101,0,387508441896,1,21102,1,353,0,1106,0,457,21101,0,937151013780,1,21101,0,364,0,1105,1,457,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21102,179490040923,1,1,21102,411,1,0,1105,1,457,21101,46211964123,0,1,21102,422,1,0,1106,0,457,3,10,104,0,104,0,3,10,104,0,104,0,21101,838324716308,0,1,21101,0,445,0,1106,0,457,21102,1,868410610452,1,21102,1,456,0,1106,0,457,99,109,2,22101,0,-1,1,21101,40,0,2,21101,0,488,3,21101,478,0,0,1106,0,521,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,483,484,499,4,0,1001,483,1,483,108,4,483,10,1006,10,515,1101,0,0,483,109,-2,2105,1,0,0,109,4,2101,0,-1,520,1207,-3,0,10,1006,10,538,21101,0,0,-3,22102,1,-3,1,21202,-2,1,2,21101,0,1,3,21101,557,0,0,1105,1,562,109,-4,2105,1,0,109,5,1207,-3,1,10,1006,10,585,2207,-4,-2,10,1006,10,585,22101,0,-4,-4,1106,0,653,21201,-4,0,1,21201,-3,-1,2,21202,-2,2,3,21102,604,1,0,1106,0,562,21202,1,1,-4,21101,0,1,-1,2207,-4,-2,10,1006,10,623,21102,0,1,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,645,21202,-1,1,1,21101,0,645,0,106,0,520,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0`
    return code.split(',').reduce((acc, n, i) => {
        acc[BigInt(i)] = BigInt(n)
        return acc
    }, [])
}

const parseInstruction = instruction => {
    const instructionString = '' + instruction
    const opCode = parseInt(instructionString.substr(instructionString.length - 2))
    const modes = instructionString.substr(0, instructionString.length - 2).split('')
        .map(x => parseInt(x))
        .reverse()
    return [BigInt(opCode), modes]
}

const run = (program, input, i, base) => {
    const output = []

    while (true) {
        const [code, modes] = parseInstruction(program[i])
        //console.info(code, modes)

        const mode = pos => (modes.length > pos - 1 ? modes[pos - 1] : 0)

        const read = pos => {
            const value = program[i + BigInt(pos)]
            const m = mode(pos)
            if (m === 1) {
                //console.info('read', value, 'directly from', i + BigInt(pos))
                return value === undefined ? 0n : value
            }
            const index = (m === 2 ? base : 0n) + (value === undefined ? 0n : value)
            const result = program[index]
            //console.info('read', result, 'from', index)
            return result === undefined ? 0n : result
        }

        const store = (pos, value) => {
            const location = program[i + BigInt(pos)]
            const m = mode(pos)
            //console.info('writing', value, 'to', location)
            program[(m === 2 ? base : 0n) + location] = value
            return location
        }

        switch (code) {
            case 1n:
            case 2n: {
                const value1 = read(1)
                const value2 = read(2)
                const result = code === 1n ? value1 + value2 : value1 * value2
                const location = store(3, result)
                i = location === i ? i : i + 4n;
                continue
            }
            case 3n: {
                //console.log('input', input)
                const location = store(1, BigInt(input))
                i = location === i ? i : i + 2n
                continue
            }
            case 4n:
                output.push(read(1))
                i += 2n
                if (output.length == 2) {
                    return [i, base, output.map(n => parseInt(n))]
                }
                continue
            case 5n:
            case 6n: {
                const value = read(1)
                const jump = code === 5n ? value !== 0n : value === 0n
                i = jump ? read(2) : i + 3n
                continue
            }
            case 7n:
            case 8n: {
                const value1 = read(1)
                const value2 = read(2)
                const ok = code === 7n ? value1 < value2 : value1 === value2
                const location = store(3, ok ? 1n : 0n)
                i = location === i ? i : i + 4n
                continue
            }
            case 9n:
                base += read(1)
                //console.info('base', base)
                i += 2n
                continue
            case 99n:
                return null
            default:
                console.info('invalid opcode: ', code)
                return null
        }
    }
}

function Point(x, y, color = 0, painted = false) {
    return {
        x,
        y,
        color: () => color,
        painted: () => painted,
        paint: c => {
            color = c
            painted = true
        },
        at: direction => {
            switch (direction) {
                case 'up': return Point(x, y - 1)
                case 'right': return Point(x + 1, y)
                case 'down': return Point(x, y + 1)
                case 'left': return Point(x - 1, y)
            }
        },
        toString: () => `(${x},${y}: ${color === 0 ? 'black' : 'white'}${painted ? ', painted' : ''})`
    }
}

const directions = ['up', 'right', 'down', 'left']

function turn(facing, dir) {
    const max = directions.length
    const delta = dir === 0 ? -1 : 1
    return directions[(directions.indexOf(facing) + max + (delta)) % max]
}

function paint(startColor, showCanvas = false) {
    const start = Point(0, 0, startColor)

    const hull = [start]
    let pos = start
    let dir = directions[0]

    const program = createProgram()
    let i = 0n
    let base = 0n

    while (true) {
        const result = run(program, pos.color(), i, base)
        if (result === null) {
            if (!showCanvas) {
                console.info('painted', hull.filter(p => p.painted()).length)
            } else {
                const minX = hull.reduce((acc, p) => acc === null || acc > p.x ? p.x : acc, null)
                const minY = hull.reduce((acc, p) => acc === null || acc > p.y ? p.y : acc, null)
                const maxX = hull.reduce((acc, p) => acc === null || acc < p.x ? p.x : acc, null)
                const maxY = hull.reduce((acc, p) => acc === null || acc < p.y ? p.y : acc, null)
                const canvas = []
                for (let x = minX; x <= maxX; ++x) {
                    for (let y = minY; y <= maxY; ++y) {
                        let row = canvas[y]
                        if (!row) {
                            canvas[y] = row = []
                        }
                        const p = hull.find(p => p.x === x && p.y === y)
                        row[x] = p ? p.color() : 0
                    }
                }
                console.info(canvas.map(row => row.map(s => s === 0 ? ' ' : 'O').join('')).join('\n'))
            }
            break
        }
        const [newI, newBase, output] = result
        i = newI
        base = newBase

        const [color, newDirection] = output
        pos.paint(color)

        dir = turn(dir, newDirection)
        const createdPoint = pos.at(dir)
        const alreadySeen = hull.find(p => createdPoint.x === p.x && createdPoint.y === p.y)
        const prevPos = pos
        if (alreadySeen) {
            pos = alreadySeen
        }
        else {
            pos = createdPoint
            hull.push(pos)
        }
    }
}

paint(0)
paint(1, true)