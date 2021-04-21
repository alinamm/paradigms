"use strict";

"use strict";

function Const(value) {
    this.value = value;
    this.toString = function () {
        return this.value + "";
    }
    this.evaluate = function () {
        return this.value;
    }
    this.prefix = function () {
        return this.value + "";
    }
}


function parsePrefix(string) {
    let i = 0;

    if (string.length === 0) {
        throw new ParserError("empty input");
    }
    let res = test();
    skipWhiteSpaces()
    if (i !== string.length) {
        throw new ParserError("wrong format")
    }

    return res;

    function test() {
        skipWhiteSpaces();
        if (string[i] === "(") {
            i++;
            skipWhiteSpaces();
            if (string[i] === "+") {
                return parseAdd();
            } else if (string[i] === "-" && !(string[i + 1] <= '9' && string[i + 1] >= '0')) {
                return parseSubtract();
            } else if (string[i] === "*") {
                return parseMultiply();
            } else if (string[i] === "/") {
                return parseDivide();
            } else if (string.slice(i, i + 6) === "negate") {
                return parseNegate();
            } else if (string.slice(i, i + 10) === "arith-mean") {
                return parseArithMean();
            } else if (string.slice(i, i + 9) === "geom-mean") {
                return parseGeomMean();
            } else if (string.slice(i, i + 9) === "harm-mean") {
                return parseHarmMean();
            } else {
                throw new ParserError("unknown symbol");
            }
        } else if (isConst() || string[i] === "-" && string[i + 1] !== " ") {
            return parseConst();
        } else if (isLetter()) {
            return parseVariable();
        } else {
            throw new ParserError("unknown symbol");
        }
    }

    function parseArithMean() {
        i += 10;
        skipWhiteSpaces();
        let arr = [];
        let j = 0;
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            //console.log(a)
            arr[j] = a;
            //console.log(arr[j])
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new ArithMean(...arr);
    }

    function parseGeomMean() {
        i += 9;
        skipWhiteSpaces();
        let j = 0;
        let arr = [];
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            //console.log(a)
            arr[j] = a;
            //console.log(arr[j])
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new GeomMean(...arr);
    }

    function parseHarmMean() {
        i += 9;
        let j = 0;
        let arr = [];
        skipWhiteSpaces();
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            arr[j] = a;
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new HarmMean(...arr);
    }

    function parseAdd() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces()
        if ((string[i] !== ")")) {
            throw new ParserError("expected )");
        }
        i++;
        return new Add(left, right);
    }

    function parseSubtract() {
        skipWhiteSpaces();
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Subtract(left, right);
    }

    function parseMultiply() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Multiply(left, right);
    }

    function parseDivide() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")" || i === string.length) {
            throw new ParserError("expected )");
        }
        i++;
        return new Divide(left, right);
    }

    function parseConst() {
        let a = "";
        skipWhiteSpaces();
        if (string[i] === "-") {
            a += "-";
            i++;
            if (string[i] === " ") {
                throw new ParserError("extra symbol")
            }
        }
        while (i < string.length && isConst()) {
            a += string[i];
            i++;
        }
        skipWhiteSpaces();
        return new Const(Number(a));
    }

    function isConst() {
        if (/\d/.test(string[i])) {
            return true;
        }
    }

    function parseNegate() {
        skipWhiteSpaces();
        i += 6;
        skipWhiteSpaces();
        let a = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Negate(a);
    }

    function parseVariable() {
        let v = string[i];
        if (v === "x" || v === "y" || v === "z") {
            i++;
            return new Variable(v);
        } else {
            throw new ParserError("unknown variable");
        }
    }

    function isLetter() {
        if (string[i].match(/[a-z]/i)) {
            return true;
        }
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

function Avg5(a, b, c, d, e) {
    this.evaluate = function (x, y, z) {
        return (a.evaluate(x, y, z) + b.evaluate(x, y, z) + c.evaluate(x, y, z) + d.evaluate(x, y, z) + e.evaluate(x, y, z)) / 5;
    }
    this.toString = function () {
        return a + " " + b + " " + c + " " + d + " " + e + " " + "avg5";
    }
}

function Med3(a, b, c) {
    let arr;
    this.evaluate = function (x, y, z) {
        arr = [a.evaluate(x, y, z), b.evaluate(x, y, z), c.evaluate(x, y, z)];
        arr.sort(function (e, r) {
            return e - r;
        });
        return arr[1];
    }
    this.toString = function () {
        return a + " " + b + " " + c + " " + "med3";
    }
}

function ArithMean(...args) {
    this.prefix = function () {
        let str = "(" + "arith-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "arith-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 0;
        for (let i = 0; i < args.length; i++) {
            sum += args[i].evaluate(x, y, z);
        }
        return sum / args.length;
    }
}

function GeomMean(...args) {
    this.prefix = function () {
        let str = "(" + "geom-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "geom-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 1;
        for (let i = 0; i < args.length; i++) {
            sum *= args[i].evaluate(x, y, z);
        }
        return Math.pow(Math.abs(sum), 1 / args.length);
    }
}

function HarmMean(...args) {
    this.prefix = function () {
        let str = "(" + "harm-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "harm-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 0;
        for (let i = 0; i < args.length; i++) {
            sum += 1 / args[i].evaluate(x, y, z);
        }
        return args.length / sum;
    }
}

function Variable(element) {
    this.element = element;
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

function Add(a, b) {
    this.a = a;
    this.b = b;
    this.prefix = function () {
        return "(" + "+" + " " + a.prefix() + " " + b.prefix() + ")";
    }
    this.toString = function () {
        return a + " " + b + " " + "+";
    }
    this.evaluate = function (x, y, z) {
        return a.evaluate(x, y, z) + b.evaluate(x, y, z);
    }
}

function Subtract(a, b) {
    this.a = a;
    this.b = b;
    this.toString = function () {
        return a + " " + b + " " + "-";
    }
    this.evaluate = function (x, y, z) {
        return a.evaluate(x, y, z) - b.evaluate(x, y, z);
    }
    this.prefix = function () {
        return "(" + "-" + " " + a.prefix() + " " + b.prefix() + ")";
    }
}

function Divide(a, b) {
    this.a = a;
    this.b = b;
    this.toString = function () {
        return a + " " + b + " " + "/";
    }
    this.evaluate = function (x, y, z) {
        return a.evaluate(x, y, z) / b.evaluate(x, y, z);
    }
    this.prefix = function () {
        return "(" + "/" + " " + a.prefix() + " " + b.prefix() + ")";
    }
}

function Multiply(a, b) {
    this.a = a;
    this.b = b;
    this.toString = function () {
        return a + " " + b + " " + "*";
    }
    this.evaluate = function (x, y, z) {
        return a.evaluate(x, y, z) * b.evaluate(x, y, z);
    }
    this.prefix = function () {
        return "(" + "*" + " " + a.prefix() + " " + b.prefix() + ")";
    }
}


function Negate(value) {
    this.value = value;
    this.toString = function () {
        return value + " " + "negate";
    }
    this.evaluate = function (x, y, z) {
        return -value.evaluate(x, y, z);
    }
    this.prefix = function () {
        return "(" + "negate" + " " + value.prefix() + ")";
    }
}




/*function General(f, sign, args) {
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
            str += " " + this.args[i];
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

function Negate(a) {
    General.call(this, (a) => -a, "negate", [a]);
}

Negate.prototype = Object.create(General.prototype);

function Add(a, b) {
    General.call(this, (a, b) => a + b, "+", [a, b]);
}

Add.prototype = Object.create(General.prototype);

function Divide(a, b) {
    General.call(this, (a, b) => a / b, "/", [a, b]);
}

Divide.prototype = Object.create(General.prototype);

function Subtract(a, b) {
    General.call(this, (a, b) => a - b, "-", [a, b]);
}

Subtract.prototype = Object.create(General.prototype);

function Multiply(a, b) {
    General.call(this, (a, b) => a * b, "*", [a, b]);
}

Multiply.prototype = Object.create(General.prototype);

function Avg5(a, b, c, d, e) {
    General.call(this, (a, b, c, d, e) => (a + b + c + d + e) / 5, "avg5", [a, b, c, d, e]);
}

Avg5.prototype = Object.create(General.prototype);

function Med3(a, b, c) {
    General.call(this, (a, b, c) => [a, b, c].sort(function (e, r) {
        return e - r;
    })[1], "med3", [a, b, c]);
}

Med3.prototype = Object.create(General.prototype);


function parsePrefix(string) {
    let i = 0;

    if (string.length === 0) {

        throw new ParserError("empty input");
    }
    let res = test();
    skipWhiteSpaces()
    if (i !== string.length) {
        throw new ParserError("wrong format")
    }

    return res;

    function test() {
        skipWhiteSpaces();
        if (string[i] === "(") {
            i++;
            skipWhiteSpaces();
            if (string[i] === "+") {
                return parseAdd();
            } else if (string[i] === "-" && !(string[i + 1] <= '9' && string[i + 1] >= '0')) {
                return parseSubtract();
            } else if (string[i] === "*") {
                return parseMultiply();
            } else if (string[i] === "/") {
                return parseDivide();
            } else if (string.slice(i, i + 6) === "negate") {
                return parseNegate();
            } else if (string.slice(i, i + 10) === "arith-mean") {
                return parseArithMean();
            } else if (string.slice(i, i + 9) === "geom-mean") {
                return parseGeomMean();
            } else if (string.slice(i, i + 9) === "harm-mean") {
                return parseHarmMean();
            } else {
                throw new ParserError("unknown symbol");
            }
        } else if (isConst() || string[i] === "-" && string[i + 1] !== " ") {
            return parseConst();
        } else if (isLetter()) {
            return parseVariable();
        } else {
            throw new ParserError("unknown symbol");
        }
    }

    function parseArithMean() {
        i += 10;
        skipWhiteSpaces();
        let arr = [];
        let j = 0;
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            //console.log(a)
            arr[j] = a;
            //console.log(arr[j])
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new ArithMean(...arr);
    }

    function parseGeomMean() {
        i += 9;
        skipWhiteSpaces();
        let j = 0;
        let arr = [];
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            arr[j] = a;
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new GeomMean(...arr);
    }

    function parseHarmMean() {
        i += 9;
        let j = 0;
        let arr = [];
        skipWhiteSpaces();
        while (string[i] !== ")") {
            skipWhiteSpaces();
            let a = test();
            arr[j] = a;
            j++;
            skipWhiteSpaces();
        }
        i++;
        return new HarmMean(...arr);
    }

    function parseAdd() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces()
        if ((string[i] !== ")")) {
            throw new ParserError("expected )");
        }
        i++;
        return new Add(left, right);
    }

    function parseSubtract() {
        skipWhiteSpaces();
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Subtract(left, right);
    }

    function parseMultiply() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Multiply(left, right);
    }

    function parseDivide() {
        i++;
        skipWhiteSpaces();
        let left = test();
        skipWhiteSpaces();
        let right = test();
        skipWhiteSpaces();
        if (string[i] !== ")" || i === string.length) {
            throw new ParserError("expected )");
        }
        i++;
        return new Divide(left, right);
    }

    function parseConst() {
        let a = "";
        skipWhiteSpaces();
        if (string[i] === "-") {
            a += "-";
            i++;
            if (string[i] === " ") {
                throw new ParserError("extra symbol")
            }
        }
        while (i < string.length && isConst()) {
            a += string[i];
            i++;
        }
        skipWhiteSpaces();
        return new Const(Number(a));
    }

    function isConst() {
        if (/\d/.test(string[i])) {
            return true;
        }
    }

    function parseNegate() {
        skipWhiteSpaces();
        i += 6;
        skipWhiteSpaces();
        let a = test();
        skipWhiteSpaces();
        if (string[i] !== ")") {
            throw new ParserError("expected )");
        }
        i++;
        return new Negate(a);
    }

    function parseVariable() {
        let v = string[i];
        if (v === "x" || v === "y" || v === "z") {
            i++;
            return new Variable(v);
        } else {
            throw new ParserError("unknown variable");
        }
    }

    function isLetter() {
        if (string[i].match(/[a-z]/i)) {
            return true;
        }
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


function ArithMean(...args) {
    this.prefix = function () {
        let str = "(" + "arith-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "arith-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 0;
        for (let i = 0; i < args.length; i++) {
            sum += args[i].evaluate(x, y, z);
        }
        return sum / args.length;
    }
}

function GeomMean(...args) {
    this.prefix = function () {
        let str = "(" + "geom-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "geom-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 1;
        for (let i = 0; i < args.length; i++) {
            sum *= args[i].evaluate(x, y, z);
        }
        return Math.pow(Math.abs(sum), 1 / args.length);
    }
}

function HarmMean(...args) {
    this.prefix = function () {
        let str = "(" + "harm-mean";
        for (let i = 0; i < args.length; i++) {
            str += " " + args[i].prefix();
        }
        str += ")";
        return str;
    }
    this.toString = function () {
        let str = "(";
        for (let i = 0; i < args.length; i++) {
            str += args[i] + " ";
        }
        str += "harm-mean" + ")";
        return str;
    }
    this.evaluate = function (x, y, z) {
        let sum = 0;
        for (let i = 0; i < args.length; i++) {
            sum += 1 / args[i].evaluate(x, y, z);
        }
        return args.length / sum;
    }
}*/
