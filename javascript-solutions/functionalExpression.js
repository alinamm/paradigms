"use strict";

const cnst = value => (x, y, z) => value;
const variable = ch => (x, y, z) => {
    if (ch === 'x') {
        return x;
    } else if (ch === 'y') {
        return y;
    } else {
        return z;
    }
}

let one = cnst(1);
let two = cnst(2);

const negate = value => (x, y, z) => -value(x, y, z);

const add = (a, b) => ((x, y, z) => a(x, y, z) + b(x, y, z));

const divide = (a, b) => ((x, y, z) => a(x, y, z) / b(x, y, z));

const multiply = (a, b) => ((x, y, z) => a(x, y, z) * b(x, y, z));

const subtract = (a, b) => ((x, y, z) => a(x, y, z) - b(x, y, z));

// let expr = divide(negate(variable('x')), cnst(2));
// console.log(expr(0.00000000000000000000,0.00000000000000000000,0.00000000000000000000));