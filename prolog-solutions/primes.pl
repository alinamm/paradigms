edit(I, N, TEMP) :- I < N,
    assertz(composite(I)),
    J is I + TEMP,
    edit(J, N, TEMP).
check(I, N) :- \+ composite(I),
    J is I * I,
    edit(J, N, I).
check(I, N) :- I * I < N,
    J is I + 1,
    check(J, N).

prime(X) :-  X > 1, \+ composite(X).
init(MAX_N) :- check(2, MAX_N).

get_divisor(N, I, R) :- prime(I),
    0 is N mod I, R is I, !.
get_divisor(N, I, R) :- I < N + 1,
    J is I + 1,
    get_divisor(N, J, R).

prime_divisors(1, []).
prime_divisors(N, [N]) :- prime(N), !.
prime_divisors(N, [H | T]) :- get_divisor(N, 2, H),
    N1 is N / H, prime_divisors(N1, T).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

addit(0, _, []) :- !.
addit(I, E, R) :- I > 0, I1 is I - 1, addit(I1, E, R1),concat(R1, [E], R).

func(N, I, R) :-  N > 1, divisor(N, 2, R1),
     addit(R, I, R1, R2),
     R is R2,
     N1 is N / R1, func(N1, I, R).

power_divisors(_, 0, []) :- !.
power_divisors(1, _, []) :- !.
power_divisors(N, I, D) :-  get_divisor(N, 2, H),
    addit(I, H, R1),
    N1 is N / H, power_divisors(N1,I, R2), concat(R1, R2, D).
