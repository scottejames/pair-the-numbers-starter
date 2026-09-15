# Worked example, step by step

This walks through the problem by hand, using two real cases (the same
cases as `test_data/simple/07_earliest_completing_pair.txt` and
`test_data/medium/01_trap_wrong_order_scan.txt`, so you can cross-check
both final answers against those files). The goal here is to make sure
the *rules* of the problem are completely clear before you write any
code — it deliberately stops short of showing you an efficient way to
search a large list. See [README.md](README.md) for the full problem
statement and constraints.

## The setup

```
numbers: 3, 3, 4, 4
target:  7
```

A **pair** is two different positions in the list whose numbers add up
to `target`. If more than one pair qualifies, "the" answer is the one
that *completes first* reading the list left to right — the smallest
second position, and if that's tied, the smallest first position.

## Stage 1 — list every valid pair

With only 4 numbers, there are 6 possible position-pairs. Check every
one by hand:

| Positions | Numbers | Sum | Valid? |
|---|---|---|---|
| (0,1) | 3, 3 | 6 | no |
| (0,2) | 3, 4 | 7 | **yes** |
| (0,3) | 3, 4 | 7 | **yes** |
| (1,2) | 3, 4 | 7 | **yes** |
| (1,3) | 3, 4 | 7 | **yes** |
| (2,3) | 4, 4 | 8 | no |

Four different pairs all sum to 7. Whichever one gets returned needs to
be picked by a rule, not a coin flip — that's exactly why the problem
defines "the" answer the way it does.

## Stage 2 — apply the tie-break rule

Group the valid pairs by their second position: `(0,2)` and `(1,2)` both
finish at position 2; `(0,3)` and `(1,3)` both finish at position 3.
Position 2 comes before position 3, so the answer has to be one of the
two pairs finishing at position 2. Between those, `(0,2)` has the
smaller first position.

**Answer: `find_pair([3,3,4,4], 7)` = `(0, 2)`.**

This matters because a solution that just returns the *first* pair it
happens to stumble across — say, by checking `(0,1)`, `(0,2)`, `(0,3)`,
`(1,2)`, ... in that order and stopping at the first hit — would also
land on `(0,2)` here purely by luck, since it's checking positions in an
order that happens to agree with the rule. The next example shows a case
where that luck runs out.

---

## A second wrinkle: the obvious scanning order can pick the wrong pair

Different example:

```
numbers: 1, 4, 6, 9
target:  10
```

## Stage 1 — try the obvious approach, and watch it go wrong

A natural way to search: fix the *first* number, then check every number
after it for a match, in order, and stop as soon as something works.

Starting at position 0 (`1`): check position 1 (`1+4=5`, no), position 2
(`1+6=7`, no), position 3 (`1+9=10` — **match!**). That's a hit, so this
approach stops here and reports `(0, 3)`.

## Stage 2 — check whether that's actually correct

Before trusting it, list out every valid pair, the same way as before:

| Positions | Numbers | Sum | Valid? |
|---|---|---|---|
| (0,1) | 1, 4 | 5 | no |
| (0,2) | 1, 6 | 7 | no |
| (0,3) | 1, 9 | 10 | **yes** |
| (1,2) | 4, 6 | 10 | **yes** |
| (1,3) | 4, 9 | 13 | no |
| (2,3) | 6, 9 | 15 | no |

Two valid pairs exist: `(0,3)` and `(1,2)`. Per the tie-break rule, the
one that completes first is whichever has the smaller *second* position
— that's `(1,2)`, finishing at position 2, ahead of `(0,3)`, which
doesn't finish until position 3.

## Stage 3 — see exactly where the obvious approach went wrong

The "fix the first number, scan forward" approach committed to position
0 before ever checking whether some *later* starting position might
complete a pair sooner. By the time it found `(0,3)`'s match, it had
already skipped past position 1 — which, paired with position 2, would
have completed first. Fixing the first number and scanning forward from
there is a perfectly good way to check *whether a pair exists at all*,
but it isn't the same thing as finding the one that finishes earliest in
the list.

**Answer: `find_pair([1,4,6,9], 10)` = `(1, 2)`**, not `(0, 3)`.
