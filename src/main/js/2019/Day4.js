Array.prototype.last = function() {
  return this.length === 0 ? undefined : this[this.length-1]
}

const [min, max] = [246540, 787419]

const filtered = new Array(max - min).fill(0)
  .map((_, i) => `${min + i}`.split("").map(x => parseInt(x)))
  .filter(s => s.join() === s.sort().join())
  .filter(s => s.sort().some((digit, i, arr) => i > 0 && digit === arr[i - 1]))

console.info(filtered.length)

const filteredMore = filtered.filter(s => s.sort().reduce((acc, digit) => {
  if (acc.length === 0 || acc.last()[0] !== digit) acc.push([digit]);
  else acc.last().push(digit);
  return acc;
}, []).some(group => group.length === 2))

console.info(filteredMore.length)
