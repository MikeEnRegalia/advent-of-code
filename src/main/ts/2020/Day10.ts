export default {}
const input = `2
49
78
116
143
42
142
87
132
86
67
44
136
82
125
1
108
123
46
37
137
148
106
121
10
64
165
17
102
156
22
117
31
38
24
69
131
144
162
63
171
153
90
9
107
79
7
55
138
34
52
77
152
3
158
100
45
129
130
135
23
93
96
103
124
95
8
62
39
118
164
172
75
122
20
145
14
112
61
43
141
30
85
101
151
29
113
94
68
58
76
97
28
111
128
21
11
163
161
4
168
157
27
72`

function process(s: string) {
    return s.split('\n')
        .map(s => parseInt(s))
        .sort((a, b) => a - b)
}

const diffs = process(input)
    .reduce((agg, n, i, l) => {
        const prev = i === 0 ? 0 : l[i - 1]
        agg.set(n - prev, !agg.has(n - prev) ? 1 : agg.get(n - prev) + 1)
        return agg
    }, new Map<number, number>())

console.info(diffs.get(1) * (diffs.get(3) + 1))


// 0 1 2 3 4 7
// 0 1 2 3 6
// 0 1 2 5
// 0 1 4


function count(list: number[]) {
    return [0, ...list, list[list.length - 1] + 3]
        .reduce((agg, adapter, i, l) => {
            if (i > 0 && l[i-1] === adapter -1 && i+1 < l.length && l[i+1] === adapter + 1) {
                return agg * BigInt(2)
            }
            return agg
        }, BigInt(1))
}

console.info(count(process(`16
10
15
5
1
11
7
19
6
12
4`)))

console.info(count(process(`28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3`)))

console.info(count(process(input)))