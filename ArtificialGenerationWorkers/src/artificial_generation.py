import os, logging
import sys

from queueing import worker, config
from multiprocessing import Process, cpu_count
from dotenv import load_dotenv

if __name__ == '__main__':
    logging.getLogger().setLevel(logging.DEBUG)
    logging.getLogger().addHandler(logging.StreamHandler())

    load_dotenv()

    rabbit_config = config.RabbitConfig(host=os.getenv("RABBIT_HOST"), port=int(os.getenv("RABBIT_PORT")),
                        user_name=os.getenv("RABBIT_USERNAME"), password=os.getenv("RABBIT_PASSWORD"))

    model_parsing_min_workers_count = int(os.getenv("MODEL_PARSING_MIN_WORKERS_COUNT"))

    scene_generation_workers_count = min(int(os.getenv("SCENE_GENERATION_WORKERS_COUNT")),
                                         max(cpu_count() - model_parsing_min_workers_count, 0))

    model_parsing_workers_count = min(int(os.getenv("MODEL_PARSING_WORKERS_COUNT")),
                                      cpu_count() - scene_generation_workers_count)

    logging.info(f"Launching {scene_generation_workers_count} workers for scene generation")
    logging.info(f"Launching {model_parsing_workers_count} workers for parsing of models")

    processes = []
    for i in range(0, scene_generation_workers_count):
        p = Process(target=worker.main, args=(rabbit_config,))
        processes.append(p)

    for i in range(0, model_parsing_workers_count):
        p = Process(target=worker.main, args=(rabbit_config,))
        processes.append(p)

    for p in processes:
        p.start()

    for p in processes:
        p.join()
