// noinspection JSUnusedGlobalSymbols
export default {}

function parseTile(line: string) {
    return line
        .replace(/ne/g, 'a')
        .replace(/nw/g, 'b')
        .replace(/se/g, 'c')
        .replace(/sw/g, 'd')
        .split('').map(c => {
            if (c === 'a') return 'ne'
            if (c === 'b') return 'nw'
            if (c === 'c') return 'se'
            if (c === 'd') return 'sw'
            if (c === 'e') return 'e'
            if (c === 'w') return 'w'
            return c
        })
}

function getPosition(tile: string[]) {
    return tile.reduce((acc, c) => {
        switch (c) {
            case 'se':
                acc.x++
                acc.y++
                break
            case 'ne':
                acc.x++
                acc.y--
                break
            case 'e':
                acc.x += 2
                break
            case 'w':
                acc.x -= 2
                break
            case 'nw':
                acc.x--
                acc.y--
                break
            case 'sw':
                acc.x--
                acc.y++
                break
        }
        return acc
    }, {x: 0, y: 0})
}

function getState(space: Map<number, Map<number, boolean>>, pos: { x: number; y: number }) {
    const yMap = space.get(pos.x)
    return !yMap ? false : yMap.get(pos.y)
}

function flipTile(space: Map<number, Map<number, boolean>>, pos: { x: number; y: number }) {
    let yMap = space.get(pos.x)
    if (!yMap) {
        yMap = new Map<number, boolean>()
        space.set(pos.x, yMap)
    }
    yMap.set(pos.y, !yMap.get(pos.y))
}

function flip(input: string) {
    const tiles = input.split('\n').map(line => parseTile(line))

    const space = tiles.reduce((space, tile) => {
        flipTile(space, getPosition(tile))
        return space
    }, new Map<number, Map<number, boolean>>())

    function count() {
        return Array.from(space.values()).reduce((acc, map) => acc +
            Array.from(map.values()).filter(b => b).length, 0)
    }

    const nBlack = count()

    flipLiving(space)

    const nBlack2 = count()

    return {nBlack, nBlack2}
}

console.info(flip(input()))

function neighbors(pos: { x: number, y: number }) {
    return [
        [2, 0], [-2, 0], [1, 1], [1, -1], [-1, 1], [-1, -1]
    ].map(delta => ({x: pos.x + delta[0], y: pos.y + delta[1]}))
}


function flipLiving(space: Map<number, Map<number, boolean>>) {
    for (let i = 0; i < 100; ++i) {
        const candidates = []
        for (const [x, yMap] of space) {
            for (const [y,] of yMap) {
                [{x, y}, ...neighbors({x, y})]
                    .filter(pos => !candidates.find(o => o.x === pos.x && o.y === pos.y))
                    .forEach(pos => candidates.push(pos))
            }
        }

        const toFlip = []
        candidates.forEach(pos => {
            const state = getState(space, pos)
            const nBlackNeighbors = neighbors(pos).filter(pos => getState(space, pos)).length
            if (state && (nBlackNeighbors === 0 || nBlackNeighbors > 2)) {
                toFlip.push(pos)
            } else if (!state && nBlackNeighbors === 2) {
                toFlip.push(pos)
            }
        })

        toFlip.forEach(pos => flipTile(space, pos))
    }
}

function input() {
    return `neswsewswseswseenwseneswseswswswseswse
nwnenwwnwnwneswnenweswnwnwnwnenwswnese
wnewwwwwwwwswwwsewnwwswnee
wseeeeeeesesenesenweseseeseesw
nenenenwneenwnenenwnwswnenwseswsenwenw
sesenwsesesenweseeseseesesesesenwsesesw
sesesewseswsewesenwneseswswwsesesesenese
ewnenenenenewwneeneneneneseneenew
eswswswwseswswsesese
ewnwnwnwnwnwnwwswwwnwnwew
swswswswswesewswswsweeswswswneswwsww
nwwwesesewnwwwnewwwwwwenwsew
wnesenwneeneneswnesenewnenwnesenwnenw
swseswweseswswswswswseewse
nenewnwsenwnwnenwnwnwnwnwnwnwnw
neneneswnwnwnenenwwsenenenenenenenwsenenw
eeseeeeneneseenwseeweseeeeswe
seneswesesesesewsw
swswwsweswseneswswswseswnweswsesenwwswsw
swnwnwsenwwnwnwnwwenwnwwnwwwnwnenw
sesewseseseseseesesenwse
neneneneenenenwneswneenene
swswswewswnwsewswswseswseswswseswswswne
nenwnwnewneeenenwswneneneseneswneesw
neswwseswnwswswsesewesenwnwswsenewe
neeneneenesenwneswnenenwnwwnwnenesenesw
seseswswswneswswswswnwswneswsewseseesw
wnwwseswnwenewenwnweewswswswsenw
eswwenweseewesenw
swsewneenewnwseseweeswneeseneew
eeeeeseenweeee
eseswenwwseseseese
sesesesenesesesesewseesewseneeweesese
wwnwnwnwnwesenwnwwnwwsw
eeswnwnweesweeenwsewswswenewnesw
swswswswswswnweswswswsweswnwswswswneswsw
wwnwwnwnwwnwnwenwsenwsewwnwswnwnenw
seseeseseseseswneswsenwsenwsesesesesw
enwnwwnwnwsweneseswsenwwenwnwnwenw
seeenenwewweseseseswseewenesesee
weswswwswwswwswwswwnwswseneswwwnw
eeeeneseeneeeswneenweneneeewnesw
nwwswswnweswswwwesewswswswwsw
seeseneswneswswnwenwenwnwwneseswneee
neneneseneneswneenenenenwnenene
enenenewneswenesenwneneseneenenewne
senwseseseesweseesenwnesesesesesenesesew
seesesesewsewnweseeeeneeseseeese
wneseswenwswnenwnwwswseseswneesenew
nesenwswwnwwseewnewwwsewwnwwsw
seseeswswneswneesewnwnwenesewse
wswnwswswwwweswswswwsw
eeeeweeseeneee
swnwswnwseeeswenwnweswnwswswsewswswswne
nenesenwnweenwneeneswneneeneeneeswnesw
senenenwwnweneswnwnwwnwnwnwenwnwnenenwne
swewseeneswneweneeneeeswnwneneeee
nenwwwswwnesweewwseswswswswewwew
sesesewnwseneseswsenesesesenesewesesesese
nwnenenwwnwwswsewswewswnwseeeew
nwwweeseneeesweeeeeewew
wwnwwwewwwwnwsewwwsew
neewnwnwnwswswnwnwnwswnwnwnwnwnwnenwnw
eeeeenweeseeswseeeseesweneew
wwwseswswswnewwwswwseneswswswsww
nenesweneweeswswwnewnwnenweenee
nwwwswwwwsenwwwnwwwwwenwsenww
nwnwnwewnwnwwseenwnwseneenwwnwnwnwse
wnwnwsewnwnwsenenw
wwesewnwswenwswseenwneswswseswnwwww
nwnwsewwwwwswwenwnwwnewsewwewnw
wnewsenwsewseneseseeseseseseseneesee
neneneneneeneswneswswnenenenenenenenwnw
enwnwnwsenewnenenwnwnwnwewnwnwwnenw
wwwseneseswseeneesenesenewseswenwsw
newneeneneneswnenwneneeseneneenenee
neeswnwswswnewwnwewwswnwseewseswe
seseesesewsesenwweweswesese
wsewnwswswswswswswswnwseswswseeneneswwsw
senewneseswseseswswenesenwswswswswswswsesw
wswneswwwswswwwwswswneswseewwe
newwwwswwwwwwnwsewewwww
sesewsenewseneswwnewswswswseswnenene
nwneneneewnwswnenwnenwneseneenwneneswsenw
eweeesweseseneneseeeeeenwsee
wswenwenwswswseswsw
sweenweeseneenweeeene
nenwseneeeeseswnweeeeeswwneenee
wewnweswwwwswwsewswnewwwswwswsw
swseneeswwswwswwwswswswseswswswswne
wneneneneneswnwwneeenwneneneenwnenw
swnwnenwnwnwenwnenwnenwnwswnenwswnwenwnenw
nenwnenwwnwnwweswneseneseneneneswnwnwsw
weenwswnwnwwwnwnenwsenwnenw
seseseeneeswseseeseesewswswsewsenwnwne
nwwswwswwswwseswnewweseseswswwne
newwenwewnwnwnwswnwswwenwnwsewnwnw
seswneswswswwswswswnwswswesweeswswswnwsw
swswswseswswseneewsewwwneswwswneseswne
eenwseeeeneeeeeseewswseewe
wwwwwwwwwnesww
senwewenwseseeeeseswseseseeesee
neswnwnwnwwenwnwwwsewnenwnesesewswne
ewesweseneneeeseesewsewewsenwnenw
newwswwweswsweswwwswwwwwwwe
nweeenesweseneweenesewene
enenweseseneneswwneswewseswseeew
seneswsenwnenwseseswsenwseseseswsesesew
seswnenwwnwswnweeeweseswnwnweesw
wwnwwwwenwenwwnwwewnwnwnwnwnwsw
swswnwswnewswswswswwwswsweeswswnwswswsw
nenenenenenewenenenenenenesene
swseeseswneseswneswswseswswswwswnwneswsw
sewseneseseeeeseneew
nwenenwsweeswwnwnweenwswswswenwnww
eeeseseswsenwwweeswnesenwswswneww
nwnenenenenenenenenwneseneneswneneenwsene
seneseesweeeesee
eneswseeeseseswseeeewnweesewenee
wwsewswswswneswswswnew
neseeswneneswneneneenwnenwnew
eseseewsewewneseeenwenwesesese
nenenenenenewneneeneneenee
sewewsesenwwwseeneneswnenesesesenw
nenenwneswneswwneseswneenwnwneeswnwnwe
seeeenwseeseeseeeswneeneweewsee
nwenwswnwnwenwswnenwnwnwswnenwnewneenw
eswwnwneswswsenwwenenwseswseseesene
eesewswseseeenwwnweseewwnese
neneseneneswneneenewnwneneneswnwneneene
esesenwswesesewwnesenwewswswsenwswse
sesewnwnwwnenewnwwwswsewne
wnenwnwnwneenwnweswwnwswsenweswsesene
wnwneneseeesewsesenwnesw
esesenwnwswsewswnwseneeseseseesesenwse
swnwnweseeeeeeeeesenwsewnwsese
nwswnwswsweswseseseseswswswswswwneseeswsw
wneenwseewsenwsesesewneswsesweesee
wnewswwsenenwnwsewnwnwnwnenwnwsww
swewswnewwneswwnweeenweenenesene
sewneswseneseseswswswseseseseswneseswse
eeeenweeeneeseseewneeesweese
wnwneswswnwnwnenwsenenwsenwnenwnwnenesw
weneswnweneeneeesesweeneenenww
seswneseseseswsesesesese
neeeneneswneneweenenwnenwswneesene
swswswsewnwnwseseeswswswnwswswsweswnene
seseswseswwnenesese
nwneswwseneneneeewwseneneenwesww
wnwnenenenwsenwnwe
nwwenenwenwswsenwwwsewswwnenwnwesw
nwnesenwnwswsenwnwwnwwnenwwnewnwww
sweeenweeeeenwsweeeeeeswneee
eeseseswnwnweeeeneeseeesewnwee
seswsesewweneswseswnweswneswsewneswsw
wwwwsenewnwwwwwnesewswwneww
swwnwnwseseswewsenwswswewnw
wenwwnenwsweswnewsewswwsewnwweesw
seeseeneeeeswwenwseeeswseeeee
nenwneswnewseswnenenwsenewnenenenwsene
neneneneswenenenwwneeswneenenenenenene
enwnwnwnenwwsenenewnwnene
nenenwneswnwseeswnenwneseeneneeswnene
nenenesenwnewnwnwnesenwnenwnwnwwnwnene
wwwseseeneswseswnwsewswswseneeene
wwwseesesenwwwwwnwwnewwswsw
wwswswwnwnwesewseswwnweswnwseeww
neswnwswnwswenesewnew
wewsewnwnwnwnwnwnenwnwwnwnwnw
swsesweswneswnwneneeswswwwswswswsesenw
wwweswwwswwwswswwswnw
seseswseswsweswnwseswswsweseswseseswnw
eswwneswswswneeswseswwwswswewene
swsewnwneswnwswswseeswsewnwseswnwseese
neweswswswswswsweseswnwnwnesenwswneswsew
nenenenenewneneneneneeneswnwwnenenee
newnwwsesenwseeseseseeeesweneseee
eneeeeeseeeeeswee
wswweneneseseswswseseneswswseswswwsw
ewnenwneeseneeneneneseeneneweneee
swwwswwenwnewswseneswswswswwwwsw
eeneesweenweeeeeneweeeesew
nwwsenwnwnenweseeweewsewsenewswnesw
nwwnwnwnwnwnwnwwenwnwnwnwnwnewswsenw
nwnenwnenwnenenweswnwnenwnwne
sesenenwenwnenweseneneeneneseenenew
seseeeeseeswenwse
seseseeenwseseeeewsweseesenwee
seseseseeseswnwsesweneseswsenwsesesesese
swwseswswseswswwwneswswwwswnesw
nwnwswwneeswnwnwwnwnwwsewnwwnwew
newneswseswwwswwwwnewwsenwwswswswse
nwwnwnwswenwnwwwnwnweswnwwewnwswnw
wwsewwwwwneewwwwswswnwweww
nwnwnwswnwnwnwnwnenwnwnwnwnwnw
nwnwnwnwwnwswswwewwnewwne
swwneeswwnwnenenenenenenenenenenenwnese
eeneeneeeweeeewnesenweeeesw
eseewwswwwnewwwnewwsewwwew
eswwsenenwsenewesesesewesesesenenwse
swswswswswswwwnwswseswsw
eeseenewswwnesewweneeeenweseese
wneseswswneswnwnewnewsenewnesewwsww
swsenwseneswneswswswswnwswseswwwswswwsw
nwnwnwsenwnwnwnwnwnwnwnwnwnwnwseswswnene
nwsweseeseseswsenwenwseenwsw
seswseneseswseswnwnwsewswe
swsweeenwnweneswe
enwwnwwnenwnwwewnwwwswsewnwnww
nwewwsweewswsweswnwewneswnw
wnenenwnenwnwwseneneneeneswnenesenene
enwwswesewesene
nenenwneneswnewenwnenenwswseenwwnwwsesw
senwnwseswswswswswseseswswsenwwswswswseee
eeeseeseeeewee
neeswnweenweeswnwswwenenwneswneeene
neseenenwseeeenwesweswwwswenenene
nwnwwnwnwswseneneswwnenwsenwnwnwewnwswne
sesesenwseseseseesenwseseswsesenwsesese
nwesewseswsesesese
wwswseswnwswwswnwsenesewsenwwwnene
swnwwnenenwnwnwnenwnwnenwnwsenwnwne
nenesenesenenenwneenenenwsesenwnesewnw
senwwwswwnwwnwwnwwnwwnewsewsew
eswnwswswwswswswwswsww
wneneseneewnenene
sewesweeeeeeseneweseseseneese
eeeseenwesweweewseenweee
sesesenesenwsesewseswsesweseswsenenwsw
eenwnwwswswneewswnwnwswwswenewne
senwnwenenewwnwnwwwesenwnwnwnwnwew
seewwseneswenweeeseseseseeeseenw
swwswswsenwswswwswneswwswwenwswnesesw
esenenwseeneenenenwenenewneeewnene
wwswseswsenwwwswnwswenewnewswsw
neeeeeeswenwseeneeneseeeenew
nwswswnwnwswsesewswswswneswnweseseseese
swseseewseseneeseseseeseneswsesenwe
swwswwnwneseneeswswseswswwnwswnenwew
seswsewseneneseseswwsesewseswseesenesw
swseswswseswswnweswswswesewwswswenw
wswnwnwsweneswenwnwsenewsenwwswnew
swewwwnwnwwwwwnwww
seeseseenesewseeeeeenesewneswesw
sewseseswseswswseswsesesesweswwne
nwnwwnwesewseswneswnwnwswnewwwneww
nenenewneenenenenenenewenenene
eeeesewswneeeseeeeeswwenwnene
esweenweeeeenee
swesesewswsenewsesese
seseswseswseswswseswswwnenweswseswswsw
swswswseswwswswneswswnweswswewwwww
sweeeeswneeeeeenweee
swnenwnwenwnenwnwneswnwswnwneneenenwe
sweseswswnwswseswwswswswswswswswswnww
wwswswwswwneneswwwwwwnewseww
seseesenweseswnwseseseswswseenwesese
nenenenenenenwnewneseenenenenenene
neeswnenwnwnewnwsweneeswnwnenwnwnenwnw
eewneeeeeeeneeenenwneneeswswne
wseswswesesesenwsenewseseeseswswsesese
nwewnwwwseswnwnenewswneseseneenwswsww
newwwnwnwwnenwwwwwsenenwwnwsesese
newwwwenwnwnwwwwwwewwwwsw
wneseneswenwswneswneswwnesewswswswew
esenwswsewsesesewsenesewseeneesese
nwswwnwnwwnwewnwwwwnwwewwse
eeswnwnwnwnenwneneseswseswewsewseseswsw
eseenenwseeswswseseweneswswnewenene
nenewsenwnenenenwneneswnweeeseneneswsw
nweswswnwesewnewsewwwwwnwwnenwnww
neeseenweneneneneeneesenewswwnee
swesenwseneeesesesesesewsewsenwese
wnwnewwwneewwwwwwwswwwwse
nwwnwnwnwnwswnwnwnwnwenwnwnwseesewnwnw
eneswswenwneneseswwseneeenwewnenw
neswnenwneneenenwnenenenwswneswnesesew
sesesesesesenwswnwseseswsewseseswsesene
wseswnwswseseenwnwswe
wsenwsenwnwnwneewwwswenewnwwnwsw
nwnwneswnenewnweswnwsenewneenwnwsene
newneewewnewneswwseeenewnwsesesew
eeseeswneseseswnwseswesenwswwenenwne
esenwswswsweswswseneseseswsewsenwsese
eswneneneswnenenenwswnesweeeneenenw
sweseseenwsewswseseseeseenesenwseesee
ewnwwwwwwswsewwwswswnewwnesw
neeweenwnesweeswneeswneneenenee
wsewwnwswwnwwseesewwswnenewwwe
sweswenwswnwwnenwnwnwneswnwenwnw
swnenewenwswsesewswnwwswwswwwww
senenwewsesesenwsesewnewnesw
wnenwseneswwnenenwswnwnenenesesenewneene
swnenwewswswswwwswseswneswswswwswswsw
wneeswnewwsewnwwwnwsenwwwwsenwnw
seseneenewwnenwenwnwsewwswsenewsene
eeeswseseseewneseesenwewswsenenesw
nesenenenewnwnwnenwnenw
neswewnwsewwewswsewwwwnwwwwne
nwnenenenenenesewneneneneneswne
swseswsesenwnenewnwwwwswswwwnenesesesw
swseseesesewsenwswseswsenwesenewwnew
seseneenweenwseeneenweneeeewnene
wneenwseseeenewnweneneesewsenewee
eewesenweeewneeeeeseeeeee
seswneseenwsesesenwsesesesesesesewswswse
seswnweswnwseswswswseswswseeenwswswswsese
eneswenenenewnenenenenwneneswneneswnwse
wsenewwwsewnwwwwwswneseswwww
nenenwswenwsenwswneweweswnewsenewe
esweeneeeswneneenenwneseneenwe
neseseswesewswsenwswseneswseswswswseswsesw
nwnenwseneneeneseenee
nenewneeeneeswewswwsenenenweenesw
nwnwseswnenwnwenwwseeneswwwnwnwwswse
seswswswseseseswnenwsesesesesenwswsweswsw
ewneswwwswnwwnwnwswwwesewenww
nwsenwnwnwnwwnenenenenenwnwne
wnwsenwnwwnwenwsesenenewnewwnwwnw
wsenwnwswsenwnwsewsenwnwwnenwenwsenw
esewsewenweswenesesenweseeseseswese
nwenwseenenwneneneewneneswnwnewnwwne
neenenwswneswneenenenenwnenwswwnenenenw
esenesweneesweswwenwneswnweswnene
ewwwwewswwnewwwwnwwwsw
esenesenwnewneseneneneenenenenwweene
swneeswseswwsewwwwewenenwswwnwe
sesesenweeseenwseswsesesewene
wswsenwseseswseeseseswnwwswsesenesesese
enweeeeeeeeeewsweenweswe
swswseswswneswnewswswswswneswswseswsesw
eseswenweneeseswswnwwweneeeee
sweswswswwswneswswswswswnesewswwswsw
swnenwnwnwnenwneenenenwnwnweswnenwnwnesw
eseweneneenenwnewswnwnenwswswswenw
newnenesenwneenenenew
ewswswswneenwnenenenenwnwnenenenenwsw
neneneswswenwenenenenenesenewnenesenenene
wnwwwsenwwwwwwwwnesewnwwsewnw
neeneeenwewneenweeswswesenene
seseneswswsenesesesenwswseswsesesesesewse
nwnenwswseeeswnenwnwnwnwwnwnwnesenwne
seswswswsweneswseswswswswswseswnwsw
swwnwnwwnwnwnwnwnwnwsesenwnwnwnwenwnww
enwnwnwneswnenwnwnwswnwnwnwenenwnwnwnwsesw
nwswswswswswseswneswswswswseswswswswsenw
wnweswneeeneeswswneeneewseneenee
seneesenwnwnesewnwwnwnwnenw
nwenwnenenwnwswnwnwnwnenenwswsenwnwenw
nenwnwnwnenwnwnesweseneneswnenenwnwswswne
wnwwwwnwwnwsenwnese
nwwswwwnwwwnwwswenenwwwsenwww
eswswnenenenwswenewneeesweeenenene
sweseneesesewsewweneeeeseewsew
eeneeeneeeeeeweseeeneeswsw
senwnwswnwenwnwwnwswnwenwnwnwnwnwnwwnw
eeswnenenweswneswwnwenwneeenwswsw
eeswswweeenweeeenweeneeee
nenwnwnwnwnwnwnwneswnenenenenw
seeseeenenweseeewsweewesesee
sweseswseseswneswswswnwsenwnwswsweswesese
nwwesewwwwwnwwnwwwwnenwswwnw
wseswwswwwwneswswwweewwswswwe
sewnwnwweseswnwswewswwswwnewsww
swwwwswwswnwswswwwswe
nwewneneswnwswswneswsewseeseewswse
swswnwswswseswswswsenwnwsweneneneseswwsw
eenenenwseeewseneenwseneneneenee
swswswseswswwneswnwswwswsweswswswswnew
sewnesweewnenewneneneseswneweneese
senwsewnwsenwnenwenwsenwnwnwnwnwwnwnwnw
senwneswnenwnwnwwnwnwsewnwnwsewwnwnw
swneneeneenwneeneeee
swwswwswnwswswswswwswswswsesw
nenwswnwnenwnwnwnenwenwnesweeswnenenene
sweeenwnewenwsenwneeeneswneneeseee
swenwenwsweswnweeeee
nwwnwnwnwwenwnwnwnwnwnwseswnwnwnwwneenw
nwnewnenwnwnwnwnwnwnwsenenwsenwnwnwnwnwse
swseweswswswesewnwwwnwwwseewnew
senwswnewnwnesenwwsewesesewnwseneswnw
eneeneeneneeenwneesweenwesewsee
sweenwswwwwwwwswswswswnw
eeseeseneweesewseseeneseswnwnew
wswnwseewnwwsenwwnewwwwsweseswne
nenwnwswnwnwneneneneenenenewnwnwsenene
wwswseswseswseswsewneswseseeswneswseswse
nwnwswwnwenwnwsweenwewswnwswnwnwne
senenenwwnenenenenenwsenewneneneswenwne
eeeseeneweneweenweneeeeesee
swneswswseswswwswswswswsenewswswswswwne
swneswswnwnwnwnenwenesweesewneenwswnenw
wwnenwnewsenwsesesenwwnwneswwewne
newenwwnwsewseneewseneeeneeneesw
enesewwnwnwnwswneenwnwswnwwwwnwwe
senwwwwwswwnewsweswww
nwswswwnewwswwwwwswwwsesew
nesweneewenwsweneseeneeenwnesww
seseswwseseswseseseswsesewneseswswnese
wseswnenwnewnwswneewnwwseenwenwene
nwnwwwwweseswwwnwwwwsenwnwneew
nwnwswnwnwnwswenenwnenwnenwnwnwne
seeeeeswwsewewnwwwenwwsenwne
neswnenwnwwwnenwenwnwnwneseneenwnenwnene
swseswseseswenwneenwswswswsewewswsw
nenwnwnenenwswnwnwnwnwnenwnw
seneneseswseswsesesesesesesenwwseseswsese
swwewewsewwswnwwneswswwwswwwsw
neeewseseeswesesenwnweewnwseswsesesw
ewsweneesenwnesewneseeneeneenee
nwnwnwnwnenwnwnenewnwenenwnw`
}