package aoc2015.day19

fun main() {
    val (molecule, replacements) = input()
    replacements
        .fold(mutableSetOf<String>()) { acc, (from, to) ->
            acc.apply {
                addAll(molecule.replaceAll(from, to))
            }
        }
        .size
        .also { println(it) }
    println(replacements.deconstruct(molecule))
}

private fun String.replaceAll(from: String, to: String) =
    sequence {
        var i = 0
        while (true) {
            val (at, e) = replaceFirst(from, to, i) ?: break
            yield(e)
            i = at + 1
        }
    }.toCollection(mutableSetOf()) as Set<String>

fun String.replaceFirst(from: String, to: String, i: Int = 0) =
    indexOf(from, i)
        .takeIf { it > -1 }
        ?.let { IndexedValue(it, substring(0, it) + to + substring(it + from.length)) }

fun List<Pair<String, String>>.deconstruct(molecule: String): Int? {
    var dest = molecule
    var steps = 0
    while (dest != "e") {
        val oldDest = dest
        forEach { (from, to) -> dest.replaceFirst(to, from)?.let { dest = it.value; steps++ } }
        if (oldDest == dest) return null
    }
    return steps
}

fun input() = """Al => ThF
Al => ThRnFAr
B => BCa
B => TiB
B => TiRnFAr
Ca => CaCa
Ca => PB
Ca => PRnFAr
Ca => SiRnFYFAr
Ca => SiRnMgAr
Ca => SiTh
F => CaF
F => PMg
F => SiAl
H => CRnAlAr
H => CRnFYFYFAr
H => CRnFYMgAr
H => CRnMgYFAr
H => HCa
H => NRnFYFAr
H => NRnMgAr
H => NTh
H => OB
H => ORnFAr
Mg => BF
Mg => TiMg
N => CRnFAr
N => HSi
O => CRnFYFAr
O => CRnMgAr
O => HP
O => NRnFAr
O => OTi
P => CaP
P => PTi
P => SiRnFAr
Si => CaSi
Th => ThCa
Ti => BP
Ti => TiTi
e => HF
e => NAl
e => OMg

CRnCaSiRnBSiRnFArTiBPTiTiBFArPBCaSiThSiRnTiBPBPMgArCaSiRnTiMgArCaSiThCaSiRnFArRnSiRnFArTiTiBFArCaCaSiRnSiThCaCaSiRnMgArFYSiRnFYCaFArSiThCaSiThPBPTiMgArCaPRnSiAlArPBCaCaSiRnFYSiThCaRnFArArCaCaSiRnPBSiRnFArMgYCaCaCaCaSiThCaCaSiAlArCaCaSiRnPBSiAlArBCaCaCaCaSiThCaPBSiThPBPBCaSiRnFYFArSiThCaSiRnFArBCaCaSiRnFYFArSiThCaPBSiThCaSiRnPMgArRnFArPTiBCaPRnFArCaCaCaCaSiRnCaCaSiRnFYFArFArBCaSiThFArThSiThSiRnTiRnPMgArFArCaSiThCaPBCaSiRnBFArCaCaPRnCaCaPMgArSiRnFYFArCaSiThRnPBPMgAr"""
    .run {
        val parts = split("\n\n")
        Pair(parts[1], parts[0].split("\n").map { it.split(" => ") }.map { Pair(it[0], it[1]) })
    }