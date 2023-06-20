from queueing.services import queue_worker
from multiprocessing import Process, JoinableQueue, cpu_count

if __name__ == '__main__':
    processes = []
    for i in range(0, cpu_count()):
        p = Process(target=queue_worker.main)
        processes.append(p)

    for p in processes:
        p.start()

    for p in processes:
        p.join()