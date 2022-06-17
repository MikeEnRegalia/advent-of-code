const program = createProgram()

const run = (program, inputs, output, i = 0) => {

    const parseInstruction = instruction => {
        const opCode = parseInt(instruction.substr(instruction.length - 2))
        const modes = instruction.substr(0, instruction.length - 2).split('')
            .map(x => parseInt(x))
            .reverse()
        return [opCode, modes]
    }

    let input = 0
    while (i < program.length) {
        const [code, modes] = parseInstruction(program[i])
        //console.info(code, modes)

        const mode = pos => (modes.length > pos - 1 ? modes[pos - 1] : 0)

        const read = pos => {
            const value = parseInt(program[i + pos])
            const result = mode(pos) === 1 ? value : parseInt(program[value])
            //console.info('read', result, 'from', value)
            return result
        }

        const store = (pos, value) => {
            const location = parseInt(program[i + pos])
            //console.info('writing', value, 'to', location)
            program[location] = '' + value
            return location
        }

        switch (code) {
            case 1:
            case 2: {
                const location = store(3, code === 1 ? read(1) + read(2) : read(1) * read(2))
                i = location === i ? i : i + 4;
                continue
            }
            case 3: {
                const inputValue = inputs[input++]
                //console.log('input', input - 1, inputValue)
                const location = store(1, inputValue)
                i = location === i ? i : i + 2
                continue
            }
            case 4:
                output = read(1)
                i += 2
                return [output, i]
            case 5:
            case 6: {
                const value = read(1)
                const jump = code === 5 ? value !== 0 : value === 0
                i = jump ? read(2) : i + 3
                continue
            }
            case 7:
            case 8: {
                const value1 = read(1)
                const value2 = read(2)
                const ok = code === 7 ? value1 < value2 : value1 === value2
                const location = store(3, ok ? 1 : 0)
                i = location === i ? i : i + 4
                continue
            }
            case 99:
                return [output, -1]
            default:
                return [output = 'invalid opcode: ' + code + ' from ' + program[i], -1]
        }
    }
}

const calculateOutput = (phaseSettings) => phaseSettings.reduce((output, phaseSetting) => {
    return run([...program], [phaseSetting, output], output)[0]
}, '0')

function createCombinations(alphabet) {
    return alphabet.length === 1 ? alphabet : alphabet
        .map(letter => createCombinations(alphabet.filter(l => l !== letter)).map(combination => [letter, ...combination]))
        .reduce((acc, combination) => [...acc, ...combination], [])
}

console.info(createCombinations(['0', '1', '2', '3', '4'])
    .map(calculateOutput)
    .reduce((acc, output) => output > acc ? output : acc, 0))

function createAmplifier(program, phaseSetting) {
    return {
        program: [...program],
        inputs: [phaseSetting],
        output: undefined,
        i: 0
    }
}

const calculateOutputWithFeedback = (program) => (phaseSettings) => {
    const amplifiers = phaseSettings.map(n => createAmplifier(program, n))

    let result
    while (result === undefined) {
        amplifiers.forEach((amp, i, amps) => {
            const prevOutput = amps[i == 0 ? amps.length - 1 : (i - 1)].output
            amp.inputs.push(prevOutput === undefined ? 0 : prevOutput)

            const [output, programI] = run(amp.program, amp.inputs, amp.output, amp.i)

            if (programI === -1 && i == amps.length - 1) {
                result = output === undefined ? amp.output : output
            }
            amp.output = output
            amp.inputs = []
            amp.i = programI
        })
    }
    return result
}

console.info(createCombinations(['5', '6', '7', '8', '9'])
    .map(calculateOutputWithFeedback(createProgram(`3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5`)))
    .reduce((acc, output) => output > acc ? output : acc, 0))

console.info(createCombinations(['5', '6', '7', '8', '9'])
    .map(calculateOutputWithFeedback(createProgram(`3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,
    -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,
    53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10`)))
    .reduce((acc, output) => output > acc ? output : acc, 0))

console.info(createCombinations(['5', '6', '7', '8', '9'])
    .map(calculateOutputWithFeedback(createProgram()))
    .reduce((acc, output) => output > acc ? output : acc, 0))

function createProgram(s) {
    return Object.freeze((s !== undefined ? s : `3,8,1001,8,10,8,105,1,0,0,21,34,51,76,101,114,195,276,357,438,
    99999,3,9,1001,9,3,9,1002,9,3,9,4,9,99,3,9,101,4,9,9,102,4,9,9,1001,9,5,9,4,9,99,3,9,1002,9,4,9,
    101,3,9,9,102,5,9,9,1001,9,2,9,1002,9,2,9,4,9,99,3,9,1001,9,3,9,102,2,9,9,101,4,9,9,102,3,9,9,101,
    2,9,9,4,9,99,3,9,102,2,9,9,101,4,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,
    9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,
    1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,
    9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,
    1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,
    2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,
    3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,
    9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,
    102,2,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,
    9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,99`)
        .split(','))
}
