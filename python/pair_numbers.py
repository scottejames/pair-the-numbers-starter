"""
Pair the Numbers.

Implement find_pair below. See ../README.md for the full problem
statement, constraints, and worked examples.

The helper function is optional scaffolding -- use it, change its
signature, or delete it and structure your solution however you like.
"""

from typing import List, Tuple


def index_of_first_occurrence(numbers: List[int], value: int, before: int) -> int:
    """Return the smallest index < before where numbers[index] == value,
    or -1 if it doesn't occur there."""
    # TODO: implement
    raise NotImplementedError


def find_pair(numbers: List[int], target: int) -> Tuple[int, int]:
    """
    Find two DIFFERENT indices i, j into numbers such that
    numbers[i] + numbers[j] == target. A single number can't be paired
    with itself -- i and j must refer to different positions in the
    list, even if the values there happen to be equal.

    More than one valid pair might exist. Return the one that completes
    first scanning the list left to right: the pair with the smallest
    second index j, and if more than one pair shares that j, the
    smallest first index i.

    Return that pair as (i, j) with i < j. If no valid pair exists at
    all, return (-1, -1).
    """
    # TODO: implement
    raise NotImplementedError
