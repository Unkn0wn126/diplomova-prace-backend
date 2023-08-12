from queueing import worker
from multiprocessing import Process, cpu_count

if __name__ == '__main__':
    processes = []
    for i in range(0, cpu_count()):
        p = Process(target=worker.main)
        processes.append(p)

    for p in processes:
        p.start()

    for p in processes:
        p.join()