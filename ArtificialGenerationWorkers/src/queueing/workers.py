import base64
import dataclasses

import json
import os
import pika
import sys
import tempfile

import src.models.parsing as model_parsing
import src.scenes.generation as scene_gen
from .config import RabbitConfig
from .constants import OPERATION_RESULT_SUCCESS, OPERATION_RESULT_ERROR


# basic skeleton taken from: https://www.rabbitmq.com/tutorials/tutorial-one-python.html


def launch_model_parsing(config: RabbitConfig, request_queue: str, response_queue: str):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=config.host,
                                                                   port=config.port,
                                                                   credentials=pika.credentials
                                                                   .PlainCredentials(
                                                                       username=config.user_name,
                                                                       password=config.password)))
    channel = connection.channel()

    channel.queue_declare(queue=request_queue)

    channel2 = connection.channel()

    channel2.queue_declare(queue=response_queue)

    def callback(ch, method, properties, body):
        print("Got message!")
        parsed = body.decode('utf8')
        loaded = json.loads(parsed)
        client_id = loaded.get('clientID')
        operation_id = loaded.get('operationID')
        operation_type = loaded.get('operationType')
        content = loaded.get('content')

        try:
            if operation_type == 'PARSE_OBJECTS':
                file_suffix = content.get('fileSuffix')
                file_data = base64.b64decode(content.get('fileData'))

                f = tempfile.NamedTemporaryFile(mode='wb', delete=False, suffix=file_suffix)
                f.write(file_data)
                f.close()

                model_data = model_parsing.import_model_from_file(f.name)
                model_dict = dataclasses.asdict(model_data)

                resp_json = json.dumps({"message": "Model parsed successfully", "content": model_dict})

                channel2.basic_publish(exchange='',
                                       routing_key=response_queue,
                                       body=bytes(resp_json, 'UTF-8'),
                                       properties=pika.BasicProperties(
                                           headers={'operationType': operation_type, 'operationID': operation_id,
                                                    'clientID': client_id, 'operationResult': OPERATION_RESULT_SUCCESS}
                                       ))
            else:
                raise AttributeError("Wrong operation type")
        except Exception as e:
            model_data = {"message": str(e), "content": None}
            channel2.basic_publish(exchange='',
                                   routing_key=response_queue,
                                   body=bytes(json.dumps(model_data), 'UTF-8'),
                                   properties=pika.BasicProperties(
                                       headers={'operationType': operation_type, 'operationID': operation_id,
                                                'clientID': client_id, 'operationResult': OPERATION_RESULT_ERROR}
                                   ))

    channel.basic_consume(queue=request_queue,
                          auto_ack=True,
                          on_message_callback=callback)

    print(' [*] Waiting for model parsing request messages. To exit press CTRL+C')
    channel.start_consuming()


def launch_scene_generation(config: RabbitConfig, request_queue: str, response_queue: str):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=config.host,
                                                                   port=config.port,
                                                                   credentials=pika.credentials
                                                                   .PlainCredentials(
                                                                       username=config.user_name,
                                                                       password=config.password)))
    channel = connection.channel()

    channel.queue_declare(queue=request_queue)

    channel2 = connection.channel()

    channel2.queue_declare(queue=response_queue)

    def callback(ch, method, properties, body):
        print("Got message!")
        parsed = body.decode('utf8')
        loaded = json.loads(parsed)
        client_id = loaded.get('clientID')
        operation_id = loaded.get('operationID')
        operation_type = loaded.get('operationType')
        content = loaded.get('content')

        try:
            if operation_type == 'GENERATE_SCENES':
                result = scene_gen.generate_scene(content)

                result_dict = dataclasses.asdict(result)

                resp_json = json.dumps({"message": "Scene generated successfully", "content": result_dict})

                channel2.basic_publish(exchange='',
                                       routing_key=response_queue,
                                       body=bytes(resp_json, 'UTF-8'),
                                       properties=pika.BasicProperties(
                                           headers={'operationType': operation_type, 'operationID': operation_id,
                                                    'clientID': client_id, 'operationResult': OPERATION_RESULT_SUCCESS}
                                       ))
            else:
                raise AttributeError("Wrong operation type")
        except Exception as e:
            model_data = {"message": str(e), "content": None}
            channel2.basic_publish(exchange='',
                                   routing_key=response_queue,
                                   body=bytes(json.dumps(model_data), 'UTF-8'),
                                   properties=pika.BasicProperties(
                                       headers={'operationType': operation_type, 'operationID': operation_id,
                                                'clientID': client_id, 'operationResult': OPERATION_RESULT_ERROR}
                                   ))

    channel.basic_consume(queue=request_queue,
                          auto_ack=True,
                          on_message_callback=callback)

    print(' [*] Waiting for scene generation request messages. To exit press CTRL+C')
    channel.start_consuming()


if __name__ == '__main__':
    try:
        launch_model_parsing()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
