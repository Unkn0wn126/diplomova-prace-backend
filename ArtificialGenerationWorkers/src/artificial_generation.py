import os

from queueing import workers, config
from multiprocessing import Process, cpu_count
from dotenv import load_dotenv


def launch_workers(num_model_workers: int, num_scn_workers: int, model_req_queue: str, model_res_queue: str,
                   scn_req_queue: str, scn_res_queue: str):
    processes = []
    for i in range(0, num_model_workers):
        p = Process(target=workers.launch_model_parsing, args=(rabbit_config, model_req_queue, model_res_queue))
        processes.append(p)

    for i in range(0, num_scn_workers):
        p = Process(target=workers.launch_scene_generation, args=(rabbit_config, scn_req_queue, scn_res_queue))
        processes.append(p)

    for p in processes:
        p.start()

    for p in processes:
        p.join()


if __name__ == '__main__':
    load_dotenv()

    rabbit_config = config.RabbitConfig(host=os.getenv("RABBIT_HOST"), port=int(os.getenv("RABBIT_PORT")),
                                        user_name=os.getenv("RABBIT_USERNAME"), password=os.getenv("RABBIT_PASSWORD"),
                                        exchange=os.getenv("QUEUES_EXCHANGE"))

    model_parsing_min_workers_count = int(os.getenv("MODEL_PARSING_MIN_WORKERS_COUNT"))

    scene_generation_workers_count = min(int(os.getenv("SCENE_GENERATION_WORKERS_COUNT")),
                                         max(cpu_count() - model_parsing_min_workers_count, 0))

    model_parsing_workers_count = min(int(os.getenv("MODEL_PARSING_WORKERS_COUNT")),
                                      cpu_count() - scene_generation_workers_count)

    model_request_queue = os.getenv("QUEUES_MODEL-PARSING_REQUEST")
    model_response_queue = os.getenv("QUEUES_MODEL-PARSING_RESPONSE")
    scn_request_queue = os.getenv("QUEUES_SCENE-GENERATION_REQUEST")
    scn_response_queue = os.getenv("QUEUES_SCENE-GENERATION_RESPONSE")

    print(f"Launching {scene_generation_workers_count} workers for scene generation")
    print(f"Launching {model_parsing_workers_count} workers for parsing of models")

    launch_workers(model_parsing_workers_count, scene_generation_workers_count, model_request_queue,
                   model_response_queue, scn_request_queue, scn_response_queue)
