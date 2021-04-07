"use strict";


function Const (value) {
    this.value = value;
    this.toString = function() {
        return this.value + "";
    }
    this.evaluate = function() {
        return this.value;
    }
}
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
    this.evaluate = function(x, y, z) {
        arr = [a.evaluate(x, y, z), b.evaluate(x, y, z), c.evaluate(x, y, z)];
        arr.sort(function(e, r) {
            return e - r;
        });
        return arr[1];
    }
    this.toString = function() {
        return a + " " + b + " " + c + " " + "med3";
    }
}

function Variable (element) {
    this.element = element;
    this.toString = function() {
        return element + "";
    }
    this.evaluate = function(x, y, z) {
        if (element === 'x') {
            return x;
        } else if (element === 'y') {
            return y;
        } else {
            return z;
        }
    }

}

function Add (a, b) {
    this.a = a;
    this.b = b;
    this.toString = function() {
        return a + " " + b + " " + "+";
    }
    this.evaluate = function(x, y, z) {
        return a.evaluate(x, y, z) + b.evaluate(x, y, z);
    }
}

function Subtract (a, b) {
    this.a = a;
    this.b = b;
    this.toString = function() {
        return a + " " + b + " " + "-";
    }
    this.evaluate = function(x, y, z) {
        return a.evaluate(x, y, z) - b.evaluate(x, y, z);
    }
}

function Divide (a, b) {
    this.a = a;
    this.b = b;
    this.toString = function() {
        return a + " " + b + " " + "/";
    }
    this.evaluate = function(x, y, z) {
        return a.evaluate(x, y, z) / b.evaluate(x, y, z);
    }
}

function Multiply (a, b) {
    this.a = a;
    this.b = b;
    this.toString = function() {
        return a + " " + b + " " + "*";
    }
    this.evaluate = function(x, y, z) {
        return a.evaluate(x, y, z) * b.evaluate(x, y, z);
    }
}

function Negate (value) {
    this.value = value;
    this.toString = function() {
        return value + " " + "negate";
    }
    this.evaluate = function(x, y, z) {
        return -value.evaluate(x, y, z);
    }
}

const funct = (f, a, b) => (x, y, z) => f(a(x, y, z), b(x, y, z));
const func = (value) => (x, y, z) => (value(x, y, z));




/*let one = cnst(1);
let two = cnst(2);
const funct = (f, a, b) => (x, y, z) => f(a(x, y, z), b(x, y, z));

const negate = value => (x, y, z) => -value(x, y, z);

const add = (a, b) => funct((a, b) => a + b, a, b);

const divide = (a, b) => funct((a, b) => a / b, a, b);

const multiply = (a, b) => funct((a, b) => a * b, a, b);

const subtract = (a, b) => funct((a, b) => a - b, a, b);*/