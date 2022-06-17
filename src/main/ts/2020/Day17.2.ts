export default {}
type Space = Map<number, Map<number, Map<number, Map<number, boolean>>>>

const keys = (m: Map<number, any>) => Array.from(m.keys()).sort((a, b) => a - b)

console.info(simulate(parse(`.#.
..#
###`)))

console.info(simulate(parse(input())))

function simulate(space: Space) {
    let countActive = 0
    for (let i = 0; i < 6; ++i) {
        const oldSpace = space
        space = copy(oldSpace)

        const candidates = []
        for (const w of keys(oldSpace)) {
            const zMap = oldSpace.get(w)
            for (const z of keys(zMap)) {
                const yMap = zMap.get(z)
                for (const y of keys(yMap)) {
                    const xMap = yMap.get(y)
                    for (const x of keys(xMap)) {
                        neighbors(oldSpace, w, z, y, x).forEach(n => {
                            if (!candidates.find(o => o.x === n.x && o.y === n.y && o.z === n.z && o.w === n.w)) {
                                candidates.push(n)
                            }
                        })
                    }
                }
            }

        }
        countActive = 0

        candidates.forEach(({w, z, y, x}) => {
            const activeNeighbors = neighbors(oldSpace, w, z, y, x)
                .filter(n => getState(oldSpace, n.w, n.z, n.y, n.x))
                .reduce(acc => acc + 1, 0)

            const active = getState(oldSpace, w, z, y, x)
            if (active) {
                if (activeNeighbors !== 2 && activeNeighbors !== 3) {
                    setState(space, w, z, y, x, false)
                }
            } else {
                if (activeNeighbors === 3) {
                    setState(space, w, z, y, x, true)
                }
            }
            countActive += getState(space, w, z, y, x) ? 1 : 0
        })
    }

    return countActive
}

function neighbors(space: Space, aroundW: number, aroundZ: number, aroundY: number, aroundX: number) {
    const result = []
    for (const w of [-1, 0, 1]) {
        for (const z of [-1, 0, 1]) {
            for (const y of [-1, 0, 1]) {
                for (const x of [-1, 0, 1]) {
                    if (![w, z, y, x].every(c => c === 0)) {
                        result.push({w: aroundW + w, z: aroundZ + z, y: aroundY + y, x: aroundX + x})
                    }
                }
            }
        }
    }
    return result
}

function getState(space: Space, w: number, z: number, y: number, x: number) {
    const zMap = space.get(w)
    if (!zMap) {
        return false
    }
    const yMap = zMap.get(z)
    if (!yMap) {
        return false
    }
    const xMap = yMap.get(y)
    if (!xMap) {
        return false
    }
    return !xMap || xMap.get(x)
}

function setState(space: Space, w: number, z: number, y: number, x: number, active: boolean) {
    if (!space.has(w)) {
        space.set(w, new Map<number, Map<number, Map<number, boolean>>>())
    }
    const resultW = space.get(w)
    if (!resultW.has(z)) {
        resultW.set(z, new Map<number, Map<number, boolean>>())
    }
    const resultY = resultW.get(z)
    if (!resultY.has(y)) {
        resultY.set(y, new Map<number, boolean>())
    }
    const resultX = resultY.get(y)
    resultX.set(x, active)
}

function copy(space: Space) {
    const result = new Map<number, Map<number, Map<number, Map<number, boolean>>>>()
    for (const w of keys(space)) {
        const zMap = space.get(w)
        for (const z of keys(zMap)) {
            const yMap = zMap.get(z)
            for (const y of keys(yMap)) {
                const xMap = yMap.get(y)
                for (const x of keys(xMap)) {
                    setState(result, w, z, y, x, xMap.get(x))
                }
            }
        }
    }
    return result
}

function parse(input: string) {
    const space = new Map<number, Map<number, Map<number, Map<number, boolean>>>>()
    input.split('\n').forEach((s, y) =>
        s.split('').forEach((s, x) => {
            setState(space, 0, 0, y, x, s === '#')
        }))
    return space
}

function input() {
    return `..#..#.#
##.#..#.
#....#..
.#..####
.....#..
...##...
.#.##..#
.#.#.#.#`
}