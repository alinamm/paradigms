map_build([], null) :- !.
map_build(ListMap, TreeMap) :- count(ListMap, R), build(ListMap, TreeMap, 0, R, R), !.
build(ListMap, (K, V, null, null), Le, Ri, _) :- Le is Ri - 1, func(ListMap, Le,(K, V)), !.
build(ListMap, (K, V, L, null), Le, Ri, C) :- S is div(Le + Ri, 2), Ri is S + 1, func(ListMap, S, (K, V)),
        build(ListMap, L, Le, S, C), !.
build(ListMap, (K, V, L, R), Le, Ri, C) :- S is div(Le + Ri, 2), func(ListMap, S, (K, V)),
        build(ListMap, L, Le, S, C), S1 is S + 1, build(ListMap, R, S1, Ri, C), !.
func([(K, V) | _], 0, (K, V)) :- !.
func([_ | T], I, N) :- I > 0, I1 is I - 1, func(T, I1, N), !.
count([], 0).
count([_ | T], R) :- count(T, TR), R is TR + 1.

map_get((K, V, _, _), K, V) :- !.
map_get((K, _, _, R), K1, V1) :- K1 > K, map_get(R, K1, V1).
map_get((K, _, L, _), K1, V1) :- K1 < K, map_get(L, K1, V1).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

map_keys(null, []) :- !.
map_keys((K, _, null, null), [K]) :- !.
map_keys((K, _, L, null), Keys) :- map_keys(L, R1), concat(R1, [K], Keys), !.
map_keys((K, _, L, R), Keys) :- map_keys(L, R1), concat(R1, [K], R3), map_keys(R, R2) , concat(R3, R2, Keys), !.

map_values(null, []) :- !.
map_values((_, V, null, null), [V]) :- !.
map_values((_, V, L, null), Values) :- map_values(L, R1), concat(R1, [V], Values), !.
map_values((_, V, L, R), Values) :- map_values(L, R1),  concat(R1, [V], R3), map_values(R, R2), concat(R3, R2, Values), !.
