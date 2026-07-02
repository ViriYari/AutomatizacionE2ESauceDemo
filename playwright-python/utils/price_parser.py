def parse_price(price_text: str) -> float:
    return float(price_text.strip().replace("$", ""))


def is_sorted_ascending(prices: list[float]) -> bool:
    return all(prices[index] <= prices[index + 1] for index in range(len(prices) - 1))
