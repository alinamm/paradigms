"use strict";

function General(f, sign, args) {
    this.f = f;
    this.sign = sign;
    this.args = args;
}

General.prototype = {
    toString: function () {
        let str = "";
        for (let i = 0; i < this.args.length; i++) {
            str += this.args[i] + " ";
        }
        return str + this.sign;
    },
    evaluate: function (x, y, z) {

        return this.f(...this.args.map(l => l.evaluate(x, y, z)));
    },
    prefix: function () {
        let str = "";
        for (let i = 0; i < this.args.length; i++) {
            str += " " + this.args[i].prefix();
        }
        return "(" + this.sign + str + ")";
    },
    constructor: General
}

function Const(value) {
    this.toString = function () {
        return value + "";
    }
    this.evaluate = function () {
        return value;
    }
    this.prefix = function () {
        return value + "";
    }
}

function Variable(element) {
    this.toString = function () {
        return element + "";
    }
    this.evaluate = function (x, y, z) {
        if (element === 'x') {
            return x;
        } else if (element === 'y') {
            return y;
        } else {
            return z;
        }
    }
    this.prefix = function () {
        return element + "";
    }
}

function generator(f, sign) {
    function func(...ops) {
        General.call(this, f, sign, ops)
    }

    func.prototype = Object.create(General.prototype)
    return func;
}

let Negate = generator((a) => -a, "negate");
let HarmMean = generator((...args) => args.length / args.reduce((a, c) => (a + 1 / c), 0), "harm-mean");
let GeomMean = generator((...args) => Math.pow(Math.abs(args.reduce((a, c) => (a * c), 1)), 1 / args.length), "geom-mean");
let ArithMean = generator((...args) => args.reduce((a, c) => (a + c), 0) / args.length, "arith-mean");
let Add = generator((a, b) => a + b, "+");
let Divide = generator((a, b) => a / b, "/");
let Subtract = generator((a, b) => a - b, "-");
let Multiply = generator((a, b) => a * b, "*");
let Avg5 = generator((a, b, c, d, e) => (a + b + c + d + e) / 5, "avg5");
let Med3 = generator((a, b, c) => [a, b, c].sort(function (e, r) {
    return e - r;
})[1], "med3");


function parsePrefix(string) {
    let i = 0;
    let operation = {
        "negate": Negate,
        "+": Add,
        "-": Subtract,
        '*': Multiply,
        "/": Divide,
        "arith-mean": ArithMean,
        "geom-mean": GeomMean,
        "harm-mean": HarmMean
    }
    let kol = {
        "negate": 1,
        "+": 2,
        "-": 2,
        '*': 2,
        "/": 2,
        "arith-mean": Infinity,
        "geom-mean": Infinity,
        "harm-mean": Infinity
    }
    let set = new Set();
    set.add("x");
    set.add("y");
    set.add("z");

    if (string.length === 0) {
        throw new ParserError("empty input in position: " +  i);
    }
    let res = test();
    skipWhiteSpaces()
    if (i !== string.length) {
        throw new ParserError("invalid expression in position: " +  i);
    }

    return res;

    function test() {
        skipWhiteSpaces();
        if (string[i] === "(") {
            i++;
            skipWhiteSpaces();
            let a = getToken();
            skipWhiteSpaces();
            let k = 0;
            if (a in operation) {
                k = kol[a];
                let p = 0;
                let arr = [];
                while (string[i] !== ")") {
                    skipWhiteSpaces();
                    arr[p] = test();
                    skipWhiteSpaces();
                    p++;
                }
                if (p !== k && k !== Infinity) {
                    throw new ParserError("invalid number of arguments in position: " +  i);
                }
                skipWhiteSpaces();
                if (string[i] === ")") {
                    i++;
                }
                if (k === 1) {
                    return new operation[a](arr[0]);
                } else if (k === 2) {
                    return new operation[a](arr[0], arr[1]);
                } else {
                    return new operation[a](...arr);
                }
            } else {
                throw new ParserError("invalid format of operation in position: " +  i);
            }
        } else if (set.has(string[i])) {
            let v = string[i];
            i++;
            return new Variable(v);
        } else {
            let num = getToken();
            if (isNaN(+num)) {
                throw new ParserError("invalid expression in position: " +  i);
            } else {
                return new Const(+num);
            }
        }
    }

    function getToken() {
        let a = string[i];
        i++;
        while (i < string.length && string[i] !== " " && string[i] !== "(" && string[i] !== ")") {
            a += string[i];
            i++;
        }
        return a;
    }

    function skipWhiteSpaces() {
        while (string[i] === " ") {
            i++;
        }
    }

}

function ParserError(message) {
    Error.call(this, message);
    this.message = message;
}

ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.name = "ParserError";
ParserError.prototype.constructor = ParserError;
