"use strict";

const cnst = value => () => value;
const variable = element => (x, y, z) => {
    if (element === 'x') {
        return x;
    } else if (element === 'y') {
        return y;
    } else {
        return z;
    }
}

let one = cnst(1);
let two = cnst(2);

const funct = (f, a, b) => (...vars) => f(a(...vars), b(...vars));

const negate = value => (...vars) => -value(...vars);

const add = (a, b) => funct((a, b) => a + b, a, b);

const divide = (a, b) => funct((a, b) => a / b, a, b);

const multiply = (a, b) => funct((a, b) => a * b, a, b);

const subtract = (a, b) => funct((a, b) => a - b, a, b);

