# Test data

Every case for Pair the Numbers lives here as a plain text file, grouped
into three difficulty tiers. Both `python/run_tests.py` and
`java/src/TestRunner.java` load directly from this directory — nothing is
duplicated in code.

## File format

```
name=<case name>
target=<int>
expected_i=<int>
expected_j=<int>
numbers=
<value 0>
<value 1>
...
```

Everything from the line after `numbers=` to the end of the file is the
number list, one value per line. Filenames are numbered (`01_...`,
`02_...`) purely so both loaders sort them into a stable, predictable
order when they list a directory — the number carries no other meaning.

`expected_i`/`expected_j` were never computed by hand — they come from a
working reference implementation kept outside this repository, checked
once and then treated as ground truth. Take them as correct.

## Simple — `simple/`

Tiny lists. Each one isolates a single mechanic rather than combining
several, so a failure here points at a specific piece of missing logic
rather than "something is wrong somewhere."

| Case | Numbers | Target | Expected | Tests |
|---|---|---|---|---|
| `01_trivial_pair` | `3, 5` | 8 | `(0, 1)` | The simplest possible case: exactly two numbers, and they happen to work. |
| `02_no_valid_pair` | `1, 2, 3` | 100 | `(-1, -1)` | No pair comes close. Checks the "impossible" path is reported honestly instead of returning something plausible-looking but wrong. |
| `03_distractor_numbers` | `10, 20, 3, 5, 30` | 8 | `(2, 3)` | Three numbers (`10, 20, 30`) don't participate in the answer at all — they're just noise the solution has to see past. |
| `04_negative_numbers` | `-3, 4, 3, 90` | 0 | `(0, 2)` | Checks negative numbers and a target of exactly zero are handled like any other value, not as a special case. |
| `05_repeated_value_distinct_indices` | `5, 5, 1` | 10 | `(0, 1)` | Two *different* positions happen to hold the *same* number, and pairing them is completely legitimate — contrast with the next case. |
| `06_cannot_reuse_same_element` | `4, 1, 2` | 8 | `(-1, -1)` | **The first trap.** `4 + 4 = 8`, and `4` does appear in the list — but only once, at position 0. A solution that doesn't check `i != j` will find that a number is its own complement and incorrectly return `(0, 0)`. |
| `07_earliest_completing_pair` | `3, 3, 4, 4` | 7 | `(0, 2)` | Four different pairs all sum to 7. Checks the tie-break rule (smallest second position, then smallest first) is actually implemented, not just "return any valid pair." |
| `08_negative_target` | `-5, -3, -1, 4` | -8 | `(0, 1)` | A negative target, satisfied by two negative numbers. Nothing about the arithmetic should care about the sign. |

## Medium — `medium/`

Still small enough to work out with pencil and paper, but big enough that
the interesting behaviour is a genuine decision, not a one-glance
inspection.

| Case | Numbers | Target | Expected | Tests |
|---|---|---|---|---|
| `01_trap_wrong_order_scan` | `1, 4, 6, 9` | 10 | `(1, 2)` | **The second trap.** Two valid pairs exist: `(0,3)` and `(1,2)`. A solution that fixes the first number and scans forward from there finds `(0,3)` first and stops — but `(1,2)` finishes *earlier* in the list, so it's the correct answer, not `(0,3)`. |
| `02_trap_doubled` | `4, 6, 9, 11` | 15 | `(1, 2)` | The same trap shape as `01`, different numbers, to rule out a fluke pass. |
| `03_trap_disguised_at_scale` | `100, 4, 200, 6, 9, 300, 11, 400` | 15 | `(3, 4)` | The same trap again, now buried among distractor numbers so it's no longer obvious by inspection which two positions are even worth checking. |
| `04_near_miss_no_pair` | `6, 9, 14` | 16 | `(-1, -1)` | The closest sum available (`6 + 9 = 15`) is off by exactly one. Checks that "close" doesn't get accepted as "equal." |
| `05_self_pair_trap_disguised` | `3, 15, 8, 22, 4, 9, 1` | 8 | `(-1, -1)` | The same self-pairing trap as simple `06`, buried among distractor numbers. `4` appears once and `4 + 4 = 8`, but no genuine pair sums to 8 — a solution that builds a value-to-index map up front and doesn't exclude the current position will find `4` "paired with itself" and wrongly report a match. |

## Hard — `hard/`

Two different kinds of case, both too large to work out by hand.

**Adversarial, no-pair cases (`01`, `02`).** A moderate number of values —
thousands, not tens of thousands — with a target chosen so that *no* pair
sums to it. There's no way to short-circuit a search like this early: an
approach that checks pairs one at a time has to rule out (almost) all of
them before it can honestly report "impossible," which is exactly what
makes an O(n²) nested loop pay for the full search space even at a size
that looks harmless.

**Realistic-scale cases (`03`–`06`).** Tens to hundreds of thousands of
numbers. Mostly a straightforward performance and correctness check —
nothing adversarial about the shape of the data, just a lot of it — with
one exception (`05`) sized so that no pair sums to the target *at all*,
which is a different animal at this scale: not just slow for a pairwise
search, but genuinely never going to finish. `06` uses numbers large
enough that adding two of them together overflows a standard 32-bit
integer, worth keeping in mind for how you store and compare sums,
especially in Java.

| Case | Numbers | Expected | Tests |
|---|---|---|---|
| `01_adversarial_no_pair_n6000` | 6,000 | `(-1, -1)` | Smallest of the adversarial no-pair cases — the first proof that "small" doesn't mean "fast" for an approach that checks every pair. |
| `02_adversarial_no_pair_n12000` | 12,000 | `(-1, -1)` | Double the size of `01`. An O(n²) approach should take roughly 4× as long here; an O(n) approach shouldn't notice the difference. |
| `03_large_n_100000_with_pair` | 100,000 | `(2236, 14568)` | Realistic scale, nothing adversarial — mostly a performance check. |
| `04_large_n_300000_with_pair` | 300,000 | `(7377, 31602)` | Roughly triple the scale of `03`. |
| `05_large_n_no_pair_n200000` | 200,000 | `(-1, -1)` | No pair sums to the target, at a scale where an O(n²) search wouldn't finish in any reasonable amount of time — genuinely infeasible to brute-force, not just slow. |
| `06_int_overflow_pair` | 2,000 | `(137, 1861)` | Two of the numbers (at positions 137 and 1861) are large enough — around 1.3–1.4 billion each — that their sum exceeds what fits in a 32-bit `int`, even though neither number by itself does. Checks that sums are computed with enough headroom, not just the individual values. |
