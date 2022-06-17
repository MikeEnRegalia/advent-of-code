const layers = createInput()

function groupByDigit(layer) {
    return layer.reduce((acc, pixel) => {
        acc.set(pixel, (acc.get(pixel) || 0) + 1)
        return acc
    }, new Map())
}

const lowestZeroLayer = layers
    .map(groupByDigit)
    .sort((a, b) => a.get(0) - b.get(0))[0]

console.info(lowestZeroLayer.get(1) * lowestZeroLayer.get(2))

console.info(layers
    .reduce((acc, layer) => acc.map((pixel, i) => pixel === 2 ? layer[i] : pixel), new Array(layers[0].length).fill(2))
    .reduce((acc, pixel, i) => {
        const content = pixel === 1 ? 'O' : ' '
        if (i % 25 === 0)
            acc.push(content)
        else
            acc[acc.length - 1] = `${acc[acc.length - 1]}${content}`

        return acc
    }, []))


function createInput(width = 25, height = 6) {
    const s = `222122222222222221222222020220220222201212222222121120222212202222222222222222202222222222222222210222202222222201222222122022222022022222222222222212222122222222222221222222022222220222211202222222020221222222222222222222222222222222222222222222210222222222222211222222122022222022022222222222222222222022222222222222222222021222221222222202222122121122222202222222222222222222202222222222222222220222212222222202222222222222222222222222222222222212212022222222222221222222222221220222200202222222120220222212222222222222222222212222222222222222200222202222222210222222022022222122122222222222222222222022222222222221222222022221222222220212222122021022222212222222222222222222212222222222222222202222202222222200222222022122222222022222222222222212212022222222222221222222022222221222212222222222220020222222102222222222222222202222222222222222220222212222222212222222122122222122022222222222222222212222222222222222222222120220222222220222222222221122222222022222222222222222222222222222222222212222222222222221222212222122222122222222222222222212212022222222222220222222121220222222202202222022220221222212202222222222222222212222222222222222202222222222222220220202222022222022122222222222222212222122122222222221222222121221222222201202222022121120222212222222222222222222222222220222222222221222212222222210222212022122222022222222222222222212222022122222222220222222022222220222202222222022122022222212112122222222222222202222221222222222220222222222222201221222022022222022222222222222222212211022222222222220222222022220221222222212222222122010222212102122222222222222212222220222222222211222222222222222222222222022222122222222222222222202210122022222222221222222120222222222211212222022121200222222022222222222222222222222220222212222202222212222222221221212122222222022022222222222222212202122122222222220222222020222220222202222222122222121222202112122222222222222222222220222202222222222202222222211221222222102222022122222222222222222202222022222222221222222222220222222202202222222021120222222112122222222222222212222221222202222202222212222222212222202122012222022122222222222222202222122022222212222222222221220220222221212222222020011222222122122222222222222222222220222212222211222202222222211222212021202222222122222222222222202202222022222212221222222021222221222220202222022120012222212012122222222222222202222221222202222222222202222222221222212120002222022222222222222222202222222022222202222222222020221221222212222222122022000222222102122222222222222222222222222202222210222222222222201220222021122222222122222222222222222210222022222212222222222122220222222220012222222122201222202002022222222222222202222222222222222201222202222222211220202120122222222222222222222222212202122022222212220222222220222221222212112222222022021222202202022202222222222012222222222222222210222212222222222222012020022222022122222222222222202222121222222222221222222022220220222210112222122221122222202222222202222222222212222221222222222210222212222222212220202120222222222222222222222222222210120022222202222222222022220222222220002222202022211222222012122202222222222002222221222202222221222212222222221221212122122222022022222222222222222221222222222212220022222121220222222221102222112121101222202202222212222222222112222020212222122220222202222222200220022022112222222022222222222222222222120122222202220222222021221220222202222222022122121222212122222212222212222102222220202202222211222222222222211222022020112222222222222222222222212201121022222202220022222222220220222210122222122122020222222112122212222202222202222220222222022200222202222222210220102121022222022222222222222222222212021222222202222222222022221222222222122222212220201222212202222212222222202012222221222222122200222202222222211222122122212222022022222202222222202202121222222202222021222120220221222201002222212120221222202122222212222212202112222221222222002212222212222222220222022220212222222022222222222222222211122122222202222222222121222220222221212222202221020222212112122202222222212112222122222202002211222212222222220221102121012222222122222202222222202222122122222212222222222122222220222211012222122022022222212102022202022202222202222122222222112210222222222222202222102122212222222022222212222222222212222022222220221021222120221220222221000222112121000222222202222222022222212102222222202222122212222222222222201220212221122222222122222222222222212201022022222220221122222221221220222221211222022222020222212222122212222222112202222120222202112211222202222222221222112020112222022022222202222222222211122022222221220220222221220220222222121222102222210222212002022212222222222212222121212212022222222202222222202221012020212222022222222212222222222212221022222212221220222221220221222222000222122020011222222202222222122212202002222221222222022102222212222222202220202021002222122122222212222222222220221122222202221220222221222222222222212222122121010222222102222212022222122022222120202202012110222222222222220221022122222222222122222202222222212200222222222202222020222221221222222202200222022121200222202112022212022212222222222021220202002111222222222222221220122121202222222222222202202222202212121122222222222120222221220200222220210222102121221222212212221222022212212222222221221202012112222222222222220220002021012222222022221222202222212202120122222200222221222221222221222210011222212021222222222212222202222222022022222021220202122022222202222222220220112221222222122122221222212222222222121022222200221221220020220201222222002222212122200222212122122202122022212112222221221202202212222212222222211222222220222222022122222002222222222201120222222220222220220120221200222211212222112221210222212212220212022012122022222220221212102001222202222222222220112020002222022222220102222222202212020022222201221020222122220222222211111022012220122222222012122202222202222222222122222212222002220202222222221221212021102222122022220002212222202210020122222202221120221121220202222221001022202220111222212112021212222222102112222022202202022020221222222222200220102220222222022022220122202222212222021022222210222120221021222210222222120222212120020222222212022202022112102122222020222212022222222202222222220220202222102202222022222002212222212211222122222200221220222220221221222222001022022021112222202102021212122122122222222220222212202201220202222222212220212021212212222020020022212222212201220222220212222221120010220202222201220222202122002222212112222202122112112212222121220222212002221002222222201222002221002222222220121022212222202211121222222212222221222001222202222210010222112021202222212102120202222222122002222120221222122020222122222222211222222222222210122221122022202222222212021122221202220120222000222221222212002022112022012222202222022222022212012002222122220212022122220112222222200220002022212202122220120022212222202221220222221201220021122121222220222211212122212222220222202022122222022102002222222121210222202022221112222222220221022020012210222221221222212222212220120022222211222221120022220212222210012222012122012222222202222222222202112222222021200212202200221202222222201222012222002211222221022112222222202202220222221220222021020021221211222221111222102020000222222202220202122002102022222221211122102120211002222222212222002120022222212222220022202222212202221222222210221120020201221212222221100222002022121222022202221222222002022002222121212212111211212122222222222222202122002200112220120202222222212211221222222021221222120210221222222220022222022022012222122112020212022022122012222221210102202021200202222222200221202121202210122222122012202222202201122222221120220022222112222221222201211122002121020222012222021202222020212112222122200122121102211002222222200220212220202220012221122012212222202202220122210000222220022010221211222212022022112022212222102002122222222111212202222220222022211202211112202222010221122222202222102122020102212222222221121222201021221221022210221221222201022022012021220222112022121202022222012202222120200002002110200222200222000221022101022222002021021222202222222200220222200101220122122020222201222210112022102021112222002202022202222022212122222122222222020220221102220222020222222212012201012020222222012220222101222122202100222120021120222202222221122022202122212222012002220212022120122002222220200122002201220002200222020220012011022220122120022012012221212121222122221000221021020221221220222211201122012020121222212122121212022000002122222220221102222012221122210222211212212122022210002221121122012221222022122122220012222120021002222201222200222022102022222222022022120212122121122022222020220202011001221222210222200201002122222200122122120102202220202212020222200010221021021120220222222222121222002122201222022002220202022210012002222022210212220020200022211222121221102120212212002221122022202210222110121122210101220020222002220200222212111122112221120222002122221202022120222002222122220212120101201202201222222221102200212212212121121022212210202002221022221022220120020100222201222201121122002121021222022012020222022201022122222122202102001022200022220222121222012021122212212221221222222212222010220122201012221022012002220212222222111222212122112222012202222212122010122022222121210022112010202202201222122210202112012200012120020201122220212020122022221012220222111020220221222201222022002121022222202102220222122212002202222020202112121222200122221222101211202022111210122122220020202210212021122222222012222120221201222220222211222122012220121222202012020212122202122202222120210212210202220122210222020220222101212200002220120220112211202000122022201100222022201111221202222202022122112122001222002202022222122221102102222021211102220220221002200222112221022220200200022222022212122212212111222022221121221021201202220221222202211122122122222222022212021212022021012202222021211012110221201212220222121211212022012220022021121011012202222211020122210122221122110120222200222212100022102021111022222112122212222021102022222022221222121101222212210202210221012220112201222122022121112202012210221222202221221020022100221210222220121222102020201122102212020222122121212122222120201122110121222212222202112222212220111221002120220101212220122012022120200021222120021010221212222220100122112121221222102202120212122012122102220222202202000120211222222202102220122002122222202020221011212222212010220220222122221022211010220220222220010122002020021122002112122212022112122012221122201002222022212012222212110212022201000211112222021121012222222210121020202200220121210100220221222220001122022020222222212212222212122021002102221222211122000202212112210222001201102001102210112120221102112201012102221121201202221021110012222210222222012022112221200022022022122202212000002012222121212012220010200022201212101211202222122201102122020211102220212010221020222102221020022200220201222210211022202022210122022002221222012010212122211221221102201022211102220212110222022110121200222122122002212210122211220220221222221222201220221120222210011222022121111222102022222222012000022002200220222102012110221012212202201211122220021200012121122202122211022221121021212100220120012002222120222202210022022220020122012222222202212100022222211020210102220202211222211202220221122001011211222122122211212221112202122120200011220120212201221110222220202022012021012222012002220212022220222002221120220202110000211012202212020221122211110221222220220100112212222221021222220011222021001120220211222200121022222120101022202202020222002212222102222022212122011220202022220202020211022022220212222220222111112201212010120221210212221122120212221101222202212122212222120222102122122222202101022112202220212202220000200122210222012211122022100200022220222001002220202122121220201021221000002101221122222200112122102122012022112212121222112210212012210022221212002102220212211222100200012001202201012122020020012211022200121122222102220000002210221211222221122222022020201122022102222222102200002022212222000012211121201122211212121222012100121212122222020122102211002022021222210012222221001020220111222222011222022022210122112202022202222201012022212120221202202222221012211202021210022221011221022122020201212201012220122022222112221102011211222102222210010222012122110122222212121212222221012122202222201222202120220022210222110221212002122220222220120221102201202111021222200122222212010012220210222212021022112022011022012202221202112021002122210121002202100101200122210212021202112122000220222221020202012201002201021221201111222101021011222222222210020022002020200122002112122222222202102002210220102122211121201102212212012212202222201222022020122020212211212210221021221011222100002221221012222210100022202121010022222112021202102011102012201220211022022000201122220222020222012022021202102020122010012222102001022022202011220012112201221201222220101022102021210022212122020212122000102102222220202222100200222122201202110220012001122222012020220102112220212100121121202102220221201122221010222210002022202221000122212012222202102201212022210010111002211211211202212202200202222200001212112121222002122221012002121022210222221222120020222212222212121022112222011022222202220222102020012122220020012022111112211102222212202220012020212201022222122002002222002222222122012102221021121121221221222210122222212220110022212222222222202220022212220122120112002121220212212222101200202112000220112001120020112220222211122020120210222120202210220222222210120022122120021022222012220202102200112212220002211002201102212212212222121220212122221212002021122101222202222022021220122222222210101000222202222201100022122222101222102222200202022212022012221201202112211212210022202222011220202221010200112001221222122212012122120120022021221222111222221211222221121222202122121122122012002222002222210112210102002202202011210202210222011222122212222200012211020200202220002111002121001020222022222121222020222222000022222021200122012112210212012001000122200000201022102010200112111222220211112120010201102020222221122212012121121022022120222200101102022210222200111022012120111022012022100222212012022002201221111102200102220212101202220221122111010201112212121020112201222201112020120201221001121120220220222212012222222120021022202102112012102012212222212022120202220000202120211222222222122202022211112201020100202222102020120222101120221101110100221221222210000122120121110222112122002122222101121222201000222122201122202202220222020202012222000220212100020021122201122022212222110022221211101210021021222220200022201022001112022122112222002202121122200111210122021221210210102212211221212222120212202200021212122211202202200121101001221021021221021022222202122022210122012212122212012202102022221222111020111112120221210210020222110200202112111202002020121210012210012102002020000221222200002210020220202221110122121020222102002102100022222121101222102120001112101222202001022222101221102021112200102200222022002220102210111121221220221202020011222102222210020222021022211002202002120112202120111222121002002022112112200200221212000201122110100200212020122012202201122122000212211222011122220200201100111000022010102201221120010110121011120100100011202001101011012210020022201001212022210200111020120211112200111000100`
    return s.split('')
        .map(x => parseInt(x))
        .reduce((acc, pixel) => {
            if (acc.length === 0 || acc[acc.length - 1].length === width * height) {
                acc.push([pixel])
                return acc
            }
            acc[acc.length - 1].push(pixel)
            return acc
        }, [])
}