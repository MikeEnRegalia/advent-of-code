function findBestSpots(map) {
    return map.map((row, rowIndex) => row.map((spot, colIndex) => {
        if (spot === '.') return 0

        const p1 = Point(colIndex, rowIndex)
        let count = 0
        map.forEach((otherRow, otherRowIndex) => otherRow.forEach((otherSpot, otherColIndex) => {
            if (otherSpot === '.') return
            const p2 = Point(otherColIndex, otherRowIndex)
            //console.info('checking', p1.toString(), p2.toString())
            if (isBlocked(map, p1, p2)) {
                return
            }
            count++
        }))
        return count
    }))
}

const bestSpot = findBestSpots(createMap())
    .map(row => row.reduce((acc, spot, i) => spot > acc.count ? {x: i, count: spot} : acc, {count: 0}))
    .reduce((acc, spot, i) => spot.count > acc.count ? {x: spot.x, y: i, count: spot.count} : acc, {count: 0})

console.info('bestSpot', bestSpot)

function blast(map, base) {
    const angles = map
        .reduce((totalAcc, row, rowI) => {
            return [...totalAcc, ...row.reduce((acc, spot, colI) => {
                const p = Point(colI, rowI)
                if (!p.equals(base))
                    acc.push(p)
                return acc
            }, [])]
        }, [])
        .filter(p => p.isAsteroid(map))
        .reduce((acc, p) => {
            const angle = p.polar(base).angle
            let o = acc.find(e => e.angle === angle)
            if (o === undefined) {
                acc.push(o = {angle, points: []})
            }
            o.points.push(p)
            return acc
        }, [])
        .sort((a, b) => b.angle - a.angle)

    angles.forEach(o => o.points = o.points.sort((a, b) => a.polar(base).distance - b.polar(base).distance))

    const total = angles.reduce((acc, o) => acc + o.points.reduce((acc, p) => acc + 1, 0), 0)

    let destroyed = 0
    let lastDestroyed
    let i = 0
    while (destroyed < Math.min(total, 200)) {

        const {points} = angles[i]
        i = (i + 1) % angles.length
        lastDestroyed = points.find(p => p.isAsteroid(map))
        if (lastDestroyed) {
            lastDestroyed.mark(map, 'x')
            destroyed++
        }
    }
    console.info('lastDestroyed', lastDestroyed.toString(), lastDestroyed.x * 100 + lastDestroyed.y)
}

blast(createMap(), bestSpot)

function isBlocked(map, p1, p2) {
    if (p1.equals(p2)) {
        return true
    }

    const dx = Math.abs(p1.x - p2.x)
    const dy = Math.abs(p1.y - p2.y)
    if (dx <= 1 && dy <= 1) {
        return false
    }

    const d = divisor(dx, dy)

    //console.info('is-blocked', p1.toString(), p2.toString(), d, dx, dy)

    let x = p1.x
    let y = p1.y
    while (true) {
        x += p2.x > p1.x ? dx / d : -dx / d
        y += p2.y > p1.y ? dy / d : -dy / d
        if (x === p2.x && y === p2.y) {
            break
        }

        const intermediate = Point(x, y)
        //console.info('intermediate', intermediate.toString())
        if (intermediate.isAsteroid(map)) {
            return true
        }
    }
    return false
}

function divisor(n, m, d = Math.min(n === 0 ? m : n, m === 0 ? n : m)) {
    if (n === 0 && m === 0) return 0
    if (n % d === 0 && m % d === 0) return d
    return divisor(n, m, d - 1)
}

function Point(x, y) {
    return Object.freeze({
        x,
        y,
        equals: other => other.x === x && other.y === y,
        polar: other => {
            const dx = x - other.x
            const dy = y - other.y
            return {distance: Math.sqrt(dx * dx + dy * dy), angle: Math.atan2(dx, dy) * (180 / Math.PI)}
        },
        isAsteroid: map => map && map[y][x] === '#',
        mark: (map, symbol) => {
            map[y][x] = symbol
        },
        toString: () => `(${x},${y})`
    })
}

function between(x1, x2) {
    const min = Math.min(x1, x2)
    const max = Math.max(x1, x2)
    return min + Math.round((max - min) / 2)
}

function createMap(s) {
    const input = s || `.###.#...#.#.##.#.####..
    .#....#####...#.######..
    #.#.###.###.#.....#.####
    ##.###..##..####.#.####.
    ###########.#######.##.#
    ##########.#########.##.
    .#.##.########.##...###.
    ###.#.##.#####.#.###.###
    ##.#####.##..###.#.##.#.
    .#.#.#####.####.#..#####
    .###.#####.#..#..##.#.##
    ########.##.#...########
    .####..##..#.###.###.#.#
    ....######.##.#.######.#
    ###.####.######.#....###
    ############.#.#.##.####
    ##...##..####.####.#..##
    .###.#########.###..#.##
    #.##.#.#...##...#####..#
    ##.#..###############.##
    ##.###.#####.##.######..
    ##.#####.#.#.##..#######
    ...#######.######...####
    #....#.#.#.####.#.#.#.##`
    return input.split('\n').map(row => row.trim().split(''))
}