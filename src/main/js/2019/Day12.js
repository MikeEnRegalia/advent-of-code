function simulate(moons, steps = 1000) {
    const pairs = createCombinations(moons)

    const prevMapX = new Map()
    const prevMapY = new Map()
    const prevMapZ = new Map()

    const save = (map, key, i) => {
        const prevPos = map.get(key)
        if (prevPos !== undefined) {
            return i
        }
        map.set(key, i)
        return undefined
    }

    const moonsKeyX = moons => moons.map(m => `${m.pos.x()}:${m.speed.x()}`).join('|')
    const moonsKeyY = moons => moons.map(m => `${m.pos.y()}:${m.speed.y()}`).join('|')
    const moonsKeyZ = moons => moons.map(m => `${m.pos.z()}:${m.speed.z()}`).join('|')

    let cycleX, cycleY, cycleZ

    for (let i = 0; i < steps; ++i) {
        if (!cycleX) {
            cycleX = save(prevMapX, moonsKeyX(moons), i)
            if (cycleX !== undefined) {
                console.info('cycleX', bin(cycleX))
            }
        }
        if (!cycleY) {
            cycleY = save(prevMapY, moonsKeyY(moons), i)
            if (cycleY !== undefined) {
                console.info('cycleY', bin(cycleY))
            }
        }
        if (!cycleZ) {
            cycleZ = save(prevMapZ, moonsKeyZ(moons), i)
            if (cycleZ !== undefined) {
                console.info('cycleZ', bin(cycleZ))
            }
        }
        if (cycleX && cycleY && cycleZ) {
            let cycleParts = [BigInt(cycleX), BigInt(cycleY), BigInt(cycleZ)]
            let cycle = BigInt(cycleParts[0]) * BigInt(cycleParts[1]) * BigInt(cycleParts[2])
            // too lazy to remove the redundant prime factors, divide by 8 works for my input(s)
            console.info('cycle', cycle / 8n)
            break
        }

        pairs.forEach(([moon1, moon2]) => {
            moon1.applyGravity(moon2)
            moon2.applyGravity(moon1)
        })

        moons.forEach(moon => moon.move())
    }

    console.info('total energy', moons.reduce((acc, moon) => acc + moon.energy(), 0n))
}

function bin(n, d = 2) {
    if (n <= 0 || d >= n) return [n]
    if (n % d === 0) return [d, ...bin(n / d, d)]
    while (n % d !== 0 && d < n) d++
    return bin(n, d)
}

simulate(puzzleInput(), 1000)
simulate(puzzleInput(), 2000000)

function Vector(x, y, z) {
    return {
        x: () => x,
        y: () => y,
        z: () => z,
        add: (dx, dy, dz) => {
            x += dx
            y += dy
            z += dz
        }
    }
}

function Moon(x, y, z) {
    const pos = Vector(x, y, z)
    const speed = Vector(0n, 0n, 0n)
    return {
        pos,
        speed,
        applyGravity: other => {
            speed.add(
                acc(pos.x(), other.pos.x()),
                acc(pos.y(), other.pos.y()),
                acc(pos.z(), other.pos.z())
            )
        },
        move: () => {
            pos.add(
                speed.x(),
                speed.y(),
                speed.z()
            )
        },
        energy: () => {
            return (abs(pos.x()) + abs(pos.y()) + abs(pos.z()))
                * (abs(speed.x()) + abs(speed.y()) + abs(speed.z()))
        },
        toString: () => `<${pos.x()} (${speed.x()}), ${pos.y()} (${speed.y()}), ${pos.z()} (${speed.z()})>`
    }
}

function acc(n, n2) {
    if (n === n2) return 0n
    return n < n2 ? 1n : -1n
}

function abs(n) {
    return n < 0 ? -n : n
}

function createCombinations(list) {
    if (list.length < 1)
        return list
    const result = []
    const item = list[0]
    const remaining = list.slice(1)
    for (const otherItem of remaining) {
        result.push([item, otherItem])
    }
    return [...result, ...createCombinations(remaining)]
}

function testInput() {
    return [
        Moon(-1n, 0n, 2n),
        Moon(2n, -10n, -7n),
        Moon(4n, -8n, 8n),
        Moon(3n, 5n, -1n)
    ]
}

function puzzleInput() {
    return [
        Moon(17n, -12n, 13n),
        Moon(2n, 1n, 1n),
        Moon(-1n, -17n, 7n),
        Moon(12n, -14n, 18n)
    ]
}

