"""
Loads Pair the Numbers test cases from ../test_data/{simple,medium,hard}/*.txt.

File format:

    name=<case name>
    target=<int>
    expected_i=<int>
    expected_j=<int>
    numbers=
    <value 0>
    <value 1>
    ...
"""

import os
from dataclasses import dataclass
from typing import List, Tuple

TEST_DATA_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "test_data")


@dataclass
class TestCase:
    name: str
    numbers: List[int]
    target: int
    expected: Tuple[int, int]


def load_case(path: str) -> TestCase:
    with open(path) as f:
        lines = f.read().splitlines()

    name = None
    target = None
    expected_i = None
    expected_j = None
    numbers_start = None

    for i, line in enumerate(lines):
        if line == "numbers=":
            numbers_start = i + 1
            break
        key, _, value = line.partition("=")
        if key == "name":
            name = value
        elif key == "target":
            target = int(value)
        elif key == "expected_i":
            expected_i = int(value)
        elif key == "expected_j":
            expected_j = int(value)

    numbers = [int(line) for line in lines[numbers_start:]]
    return TestCase(name=name, numbers=numbers, target=target, expected=(expected_i, expected_j))


def load_tier(tier: str) -> List[TestCase]:
    tier_dir = os.path.join(TEST_DATA_DIR, tier)
    cases = []
    for filename in sorted(os.listdir(tier_dir)):
        if filename.endswith(".txt"):
            cases.append(load_case(os.path.join(tier_dir, filename)))
    return cases
