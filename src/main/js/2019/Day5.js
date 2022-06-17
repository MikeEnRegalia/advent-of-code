function createProgram() {
  const program = `3,225,1,225,6,6,1100,1,238,225,104,0,1002,43,69,224,101,-483,224,224,4,224,1002,223,8,223,1001,224,5,224,1,224,223,223,1101,67,60,225,1102,5,59,225,1101,7,16,225,1102,49,72,225,101,93,39,224,101,-98,224,224,4,224,102,8,223,223,1001,224,6,224,1,224,223,223,1102,35,82,225,2,166,36,224,101,-4260,224,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,102,66,48,224,1001,224,-4752,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1001,73,20,224,1001,224,-55,224,4,224,102,8,223,223,101,7,224,224,1,223,224,223,1102,18,41,224,1001,224,-738,224,4,224,102,8,223,223,101,6,224,224,1,224,223,223,1101,68,71,225,1102,5,66,225,1101,27,5,225,1101,54,63,224,1001,224,-117,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1,170,174,224,101,-71,224,224,4,224,1002,223,8,223,1001,224,4,224,1,223,224,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1007,226,226,224,1002,223,2,223,1006,224,329,1001,223,1,223,1007,226,677,224,102,2,223,223,1006,224,344,1001,223,1,223,108,677,677,224,102,2,223,223,1005,224,359,1001,223,1,223,1007,677,677,224,1002,223,2,223,1006,224,374,101,1,223,223,8,677,226,224,1002,223,2,223,1006,224,389,101,1,223,223,7,226,226,224,1002,223,2,223,1005,224,404,101,1,223,223,7,677,226,224,102,2,223,223,1005,224,419,1001,223,1,223,8,226,677,224,1002,223,2,223,1005,224,434,101,1,223,223,1008,226,677,224,102,2,223,223,1006,224,449,1001,223,1,223,7,226,677,224,1002,223,2,223,1006,224,464,1001,223,1,223,108,677,226,224,102,2,223,223,1005,224,479,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,494,101,1,223,223,8,226,226,224,1002,223,2,223,1005,224,509,1001,223,1,223,1107,677,226,224,102,2,223,223,1005,224,524,1001,223,1,223,1107,226,226,224,102,2,223,223,1005,224,539,1001,223,1,223,1108,677,677,224,1002,223,2,223,1006,224,554,101,1,223,223,107,226,677,224,102,2,223,223,1005,224,569,1001,223,1,223,1108,226,677,224,1002,223,2,223,1005,224,584,1001,223,1,223,1107,226,677,224,1002,223,2,223,1005,224,599,1001,223,1,223,1008,226,226,224,1002,223,2,223,1005,224,614,101,1,223,223,107,226,226,224,102,2,223,223,1006,224,629,1001,223,1,223,1008,677,677,224,1002,223,2,223,1006,224,644,101,1,223,223,107,677,677,224,1002,223,2,223,1005,224,659,101,1,223,223,1108,677,226,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226`
    .split(',')
  return Object.freeze(program)
}

const program = createProgram()

const parseInstruction = instruction => {
  const opCode = parseInt(instruction.substr(instruction.length - 2))
  const modes = instruction.substr(0, instruction.length - 2).split('')
    .map(x => parseInt(x))
    .reverse()
  return [opCode, modes]
}

const run = (program, input) => {
  let i = 0
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
        console.log('input', input)
        const location = store(1, input)
        i = location === i ? i : i + 2
        continue
      }
      case 4:
        console.log('output', read(1))
        i += 2
        continue
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
        return program[0]
      default:
        return 'invalid opcode: ' + code + ' from ' + instruction
    }
  }
}

console.info(run([...program], 1))
console.info(run([...program], 5))
