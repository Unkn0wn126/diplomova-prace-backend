import pika, json, sys, os, tempfile
import src.models.parsing as model_parsing
import dataclasses
import base64

# basic skeleton taken from: https://www.rabbitmq.com/tutorials/tutorial-one-python.html
docker_host = 'artificial-generation-rabbitmq'
local_host = 'localhost'


def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=local_host, credentials=pika.credentials.PlainCredentials(username='myuser', password='mypassword')))
    channel = connection.channel()

    channel.queue_declare(queue='spring-boot')

    channel2 = connection.channel()

    channel2.queue_declare(queue='python')

    def callback(ch, method, properties, body):
        print("Got message!")
        parsed = body.decode('utf8')
        loaded = json.loads(parsed)
        client_id = loaded.get('clientID')
        operation_id = loaded.get('operationID')
        operation_type = loaded.get('operationType')
        content = loaded.get('content')

        if operation_type == 'PARSE_OBJECTS':
            file_suffix = content.get('fileSuffix')
            file_data = base64.b64decode(content.get('fileData'))

            f = tempfile.NamedTemporaryFile(mode='wb', delete=False, suffix=file_suffix)
            f.write(file_data)
            f.close()

            resp_data = model_parsing.import_model_from_file(f.name)
            resp_dict = dataclasses.asdict(resp_data)

            resp_json = json.dumps(resp_dict)

            channel2.basic_publish(exchange='',
                                  routing_key='python',
                                  body=bytes(resp_json, 'UTF-8'),
                                  properties=pika.BasicProperties(
                                      headers={'operationType': operation_type, 'operationID': operation_id,
                                               'clientID': client_id}
                                  ))

        elif operation_type == 'GENERATE_POSITIONS':
            resp_json = "test"
            model_dict = content.get('model')
            scene_definition_dict = content.get('sceneDefinition')
            grab_points_dict = content.get('grabPoints')

            channel2.basic_publish(exchange='',
                                  routing_key='python',
                                  body=bytes(resp_json, 'UTF-8'),
                                  properties=pika.BasicProperties(
                                      headers={'operationType': operation_type, 'operationID': operation_id,
                                               'clientID': client_id}
                                  ))

    channel.basic_consume(queue='spring-boot',
                          auto_ack=True,
                          on_message_callback=callback)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
