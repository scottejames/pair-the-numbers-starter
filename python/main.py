"""Demo runner -- runs find_pair on one example case and prints the result."""

import os

from pair_numbers import find_pair
from test_data import TEST_DATA_DIR, load_case


def main() -> None:
    path = os.path.join(TEST_DATA_DIR, "medium", "01_trap_wrong_order_scan.txt")
    case = load_case(path)

    print(f"Case: {case.name}")
    print(f"Numbers: {case.numbers}")
    print(f"Target: {case.target}")

    result = find_pair(case.numbers, case.target)
    print(f"Pair of indices: {result}")


if __name__ == "__main__":
    main()
