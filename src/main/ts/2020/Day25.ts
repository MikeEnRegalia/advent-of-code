export default {}

function getLoopSize(pubKey: number, subjectNumber = 7) {
    let loopSize = 0
    let transformed = 1
    while (pubKey !== transformed) {
        transformed = transformStep(transformed, subjectNumber)
        loopSize++
    }
    return loopSize
}

function hack(cardPubKey: number, doorPubKey: number) {
    let cardLoopSize = getLoopSize(cardPubKey)
    let doorLoopSize = getLoopSize(doorPubKey)

    return {cardLoopSize, doorLoopSize, encryptionKey: transform(cardPubKey, doorLoopSize)}
}

function transformStep(result: number, subjectNumber: number) {
    return (result * subjectNumber) % 20201227
}

function transform(subjectNumber: number, cycles: number) {
    let result = 1
    for (let i = 0; i < cycles; ++i) {
        result = transformStep(result, subjectNumber)
    }
    return result
}

console.info(hack(5764801, 17807724))
console.info(hack(3418282, 8719412))