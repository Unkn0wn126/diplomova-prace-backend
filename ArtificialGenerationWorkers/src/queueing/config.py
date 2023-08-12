from dataclasses import dataclass


@dataclass
class RabbitConfig:
    host: str
    port: int
    user_name: str
    password: str
    exchange: str
